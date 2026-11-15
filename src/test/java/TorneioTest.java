import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * O Torneio é testado isolado: Round, repositório, notificação e calculadora são mocks.
 * Não mockamos Treinador nem Pokemon (dados simples que o Torneio precisa ler e alterar).
 *
 * Matchers: any() quando o argumento não importa para a verificação (ex.: "alguma batalha");
 * valores exatos quando o argumento é o objetivo (ex.: o nome do jogador na conquista).
 */
@ExtendWith(MockitoExtension.class)
public class TorneioTest {

    @Mock
    private Round round;
    @Mock
    private RepositorioResultados repositorio;
    @Mock
    private ServicoNotificacao notificacao;
    @Mock
    private CalculadoraBonus calculadoraBonus;
    @InjectMocks
    private Torneio torneio;   // sem jogador (null): só bots

    private Treinador criarTreinador(String nome) {
        return new Treinador(nome, new Pokemon("Pokesal de " + nome, 50, 40, Tipos.FOGO, 60, 40, Status.NORMAL));
    }

    private Torneio criarTorneioComJogador(Treinador jogador) {
        return new Torneio(jogador, round, repositorio, notificacao, calculadoraBonus);
    }

    // Faz o primeiro treinador vencer, derrubando o segundo, em 'turnos' turnos.
    private void primeiroVence(Treinador t1, Treinador t2, int turnos) {
        when(round.executarBatalha(t1, t2)).thenAnswer(invocacao -> {
            t2.getPokemon().setHp(0);
            return turnos;
        });
    }

    // ---------------- Testes obrigatórios da Fase 03 ----------------

    @Test
    public void testResultadoSalvoAoFimDaBatalha() {
        Treinador ana = criarTreinador("Ana");
        Treinador bruno = criarTreinador("Bruno");
        primeiroVence(ana, bruno, 3);

        Treinador vencedor = torneio.executarBatalha(ana, bruno);

        ArgumentCaptor<ResultadoBatalha> captor = ArgumentCaptor.forClass(ResultadoBatalha.class);
        verify(repositorio, times(1)).salvarResultado(captor.capture());
        ResultadoBatalha salvo = captor.getValue();
        assertSame(ana, vencedor);
        assertEquals("Ana", salvo.getVencedor());
        assertEquals("Bruno", salvo.getPerdedor());
        assertEquals(3, salvo.getTurnos());
        assertEquals("Pokesal de Ana", salvo.getPokesalVencedor());
        assertEquals("Pokesal de Bruno", salvo.getPokesalPerdedor());
    }

    @Test
    public void testFalhaNoRepositorioNaoInvalidaBatalha() {
        Treinador ana = criarTreinador("Ana");
        Treinador bruno = criarTreinador("Bruno");
        primeiroVence(ana, bruno, 3);
        doThrow(new IllegalStateException("Banco offline"))
                .when(repositorio).salvarResultado(any(ResultadoBatalha.class));

        Treinador vencedor = torneio.executarBatalha(ana, bruno);

        assertSame(ana, vencedor);   // o vencedor continua definido
        verify(repositorio, atLeastOnce()).salvarResultado(any(ResultadoBatalha.class));
    }

    @Test
    public void testRepositorioNaoChamadoSeBatalhaNaoTerminou() {
        Treinador ana = criarTreinador("Ana");
        Treinador bruno = criarTreinador("Bruno");
        when(round.executarBatalha(ana, bruno)).thenThrow(new IllegalStateException("Batalha interrompida"));

        assertThrows(IllegalStateException.class, () -> torneio.executarBatalha(ana, bruno));

        verify(repositorio, never()).salvarResultado(any(ResultadoBatalha.class));
    }

    @Test
    public void testChaveamentoTorneioAvancaVencedores() {
        Treinador t1 = criarTreinador("T1");
        Treinador t2 = criarTreinador("T2");
        Treinador t3 = criarTreinador("T3");
        Treinador t4 = criarTreinador("T4");
        // Resultado mockado: em qualquer batalha, o primeiro da dupla vence.
        when(round.executarBatalha(any(Treinador.class), any(Treinador.class))).thenAnswer(invocacao -> {
            Treinador perdedor = invocacao.getArgument(1);
            perdedor.getPokemon().setHp(0);
            return 2;
        });

        Treinador campeao = torneio.executarTorneio(List.of(t1, t2, t3, t4));

        assertSame(t1, campeao);
        InOrder ordem = inOrder(round);
        ordem.verify(round).executarBatalha(t1, t2);   // semifinal 1
        ordem.verify(round).executarBatalha(t3, t4);   // semifinal 2
        ordem.verify(round).executarBatalha(t1, t3);   // final: os dois vencedores
        verify(round, times(3)).executarBatalha(any(Treinador.class), any(Treinador.class));
        verify(repositorio, times(3)).salvarResultado(any(ResultadoBatalha.class));
    }

    // ---------------- Outros testes do torneio ----------------

    @Test
    public void testTorneioDeOitoTreinadoresTemSeteBatalhas() {
        List<Treinador> inscritos = List.of(
                criarTreinador("T1"), criarTreinador("T2"), criarTreinador("T3"), criarTreinador("T4"),
                criarTreinador("T5"), criarTreinador("T6"), criarTreinador("T7"), criarTreinador("T8"));
        when(round.executarBatalha(any(Treinador.class), any(Treinador.class))).thenAnswer(invocacao -> {
            Treinador perdedor = invocacao.getArgument(1);
            perdedor.getPokemon().setHp(0);
            return 2;
        });

        Treinador campeao = torneio.executarTorneio(inscritos);

        assertSame(inscritos.get(0), campeao);
        verify(round, times(7)).executarBatalha(any(Treinador.class), any(Treinador.class));
    }

    @Test
    public void testTorneioRejeitaQuantidadeInvalidaDeTreinadores() {
        List<Treinador> tresTreinadores = List.of(criarTreinador("A"), criarTreinador("B"), criarTreinador("C"));

        assertThrows(IllegalArgumentException.class, () -> torneio.executarTorneio(tresTreinadores));

        verify(round, never()).executarBatalha(any(Treinador.class), any(Treinador.class));
    }

    @Test
    public void testNoLimiteDeTurnosVenceQuemTemMaisHp() {
        Treinador ana = criarTreinador("Ana");
        Treinador bruno = criarTreinador("Bruno");
        bruno.getPokemon().setHp(30);
        when(round.executarBatalha(ana, bruno)).thenReturn(50);   // ninguém caiu

        assertSame(ana, torneio.executarBatalha(ana, bruno));
        verify(repositorio).salvarResultado(any(ResultadoBatalha.class));
    }

    @Test
    public void testEmpateNoLimiteDeTurnosVenceOPrimeiroTreinador() {
        Treinador ana = criarTreinador("Ana");
        Treinador bruno = criarTreinador("Bruno");
        when(round.executarBatalha(ana, bruno)).thenReturn(50);

        assertSame(ana, torneio.executarBatalha(ana, bruno));
    }

    // ---------------- Requisito autoral 1: conquista "vitória perfeita" ----------------
    // Colaborador mockado: ServicoNotificacao

    @Test
    public void testNotificaConquistaQuandoJogadorVenceSemUsarItens() {
        Treinador jogador = criarTreinador("Ana");
        Treinador bot = criarTreinador("Bot");
        primeiroVence(jogador, bot, 4);

        criarTorneioComJogador(jogador).executarBatalha(jogador, bot);

        verify(notificacao, times(1)).notificarConquista("Ana", Torneio.CONQUISTA_VITORIA_PERFEITA);
    }

    @Test
    public void testNaoNotificaConquistaQuandoJogadorUsouItem() {
        Treinador jogador = criarTreinador("Ana");
        Treinador bot = criarTreinador("Bot");
        jogador.usarItem(Item.POTION);
        primeiroVence(jogador, bot, 4);

        criarTorneioComJogador(jogador).executarBatalha(jogador, bot);

        verify(notificacao, never()).notificarConquista(anyString(), anyString());
    }

    @Test
    public void testNaoNotificaConquistaQuandoOBotVence() {
        Treinador jogador = criarTreinador("Ana");
        Treinador bot = criarTreinador("Bot");
        primeiroVence(bot, jogador, 4);

        criarTorneioComJogador(jogador).executarBatalha(bot, jogador);

        verify(notificacao, never()).notificarConquista(anyString(), anyString());
    }

    // ---------------- Requisito autoral 2: bônus de ataque por vitória ----------------
    // Colaborador mockado: CalculadoraBonus

    @Test
    public void testBonusDeAtaqueAplicadoAoJogadorVencedor() {
        Treinador jogador = criarTreinador("Ana");
        Treinador bot = criarTreinador("Bot");
        jogador.getPokemon().setHp(10);
        primeiroVence(jogador, bot, 4);
        when(calculadoraBonus.calcularBonusAtaque(4)).thenReturn(5);

        criarTorneioComJogador(jogador).executarBatalha(jogador, bot);

        assertEquals(60, jogador.getPokemon().getAtk());   // 50 * 1.10 (fortalecer) + 5 de bônus
        assertEquals(40, jogador.getPokemon().getHp());    // fortalecer() cura todo o HP
    }

    @Test
    public void testBonusRecebeOsTurnosDaBatalha() {
        Treinador jogador = criarTreinador("Ana");
        Treinador bot = criarTreinador("Bot");
        primeiroVence(jogador, bot, 7);

        criarTorneioComJogador(jogador).executarBatalha(jogador, bot);

        ArgumentCaptor<Integer> turnos = ArgumentCaptor.forClass(Integer.class);
        verify(calculadoraBonus).calcularBonusAtaque(turnos.capture());
        assertEquals(7, turnos.getValue());
    }

    @Test
    public void testBonusNaoEAplicadoQuandoJogadorPerde() {
        Treinador jogador = criarTreinador("Ana");
        Treinador bot = criarTreinador("Bot");
        primeiroVence(bot, jogador, 4);

        criarTorneioComJogador(jogador).executarBatalha(bot, jogador);

        verify(calculadoraBonus, never()).calcularBonusAtaque(anyInt());
        assertEquals(50, jogador.getPokemon().getAtk());
    }
}
