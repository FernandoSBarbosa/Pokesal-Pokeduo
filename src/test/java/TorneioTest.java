import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TorneioTest {
    @Mock GeradorAleatorio gerador;
    @Mock RegistroBatalha registro;
    @Mock RepositorioResultados repositorio;
    @Mock ServicoNotificacao notificacao;
    @Mock CalculadoraBonus calculadoraBonus;

    @InjectMocks Round round;

    private Pokemon pokemon(String nome, int atk, int def, Tipos tipo, int vel, int hp) {
        return new Pokemon(nome, atk, def, tipo, vel, hp);
    }

    @Test
    void testAcertoCriticoComGeradorMockado() {
        when(gerador.checarAcerto(anyInt())).thenReturn(true);
        when(gerador.checarCritico()).thenReturn(true);
        Round batalha = new Round(null, gerador, registro);
        double dano = batalha.calcularDano(pokemon("F", 50, 40, Tipos.FOGO, 50, 100),
                pokemon("P", 40, 50, Tipos.PLANTA, 40, 100), 100);
        assertEquals(30.0, dano, 0.01);
        verify(gerador).checarCritico();
    }

    @Test
    void testGolpeErraQuandoPrecisaoFalha() {
        when(gerador.checarAcerto(70)).thenReturn(false);
        Pokemon defensor = pokemon("P", 40, 50, Tipos.PLANTA, 40, 100);
        Round batalha = new Round(null, gerador, registro);
        assertEquals(0.0, batalha.calcularDano(pokemon("F", 50, 40, Tipos.FOGO, 50, 100), defensor, 70));
        assertEquals(100, defensor.getHp());
        verify(gerador, never()).checarCritico();
    }

    @Test
    void testAsfaltoQuenteApenasDuranteDia() {
        Clock dia = Clock.fixed(Instant.parse("2026-11-05T10:00:00Z"), ZoneId.of("UTC"));
        Clock noite = Clock.fixed(Instant.parse("2026-11-05T22:00:00Z"), ZoneId.of("UTC"));
        ServicoTerreno diaMock = mock(ServicoTerreno.class);
        ServicoTerreno noiteMock = mock(ServicoTerreno.class);
        when(diaMock.obterMultiplicador(Tipos.FOGO, TiposTerreno.ASFALTO_QUENTE)).thenReturn(1.15);
        when(noiteMock.obterMultiplicador(Tipos.FOGO, TiposTerreno.ASFALTO_QUENTE)).thenReturn(1.0);
        assertEquals(1.15, new Estacionamento(TiposTerreno.ASFALTO_QUENTE, diaMock).multiplicadorTerreno(Tipos.FOGO));
        assertEquals(1.0, new Estacionamento(TiposTerreno.ASFALTO_QUENTE, noiteMock).multiplicadorTerreno(Tipos.FOGO));
        assertTrue(new ServicoTerreno(dia).isDia());
        assertFalse(new ServicoTerreno(noite).isDia());
    }

    @Test
    void testSequenciaDeEventosNoTurno() {
        when(gerador.checarAcerto(anyInt())).thenReturn(true, true);
        when(gerador.checarCritico()).thenReturn(false, false);
        Round batalha = new Round(new Estacionamento(TiposTerreno.NORMAL,
                new ServicoTerreno(Clock.systemUTC())), gerador, registro);
        batalha.simularRound(pokemon("Rapido", 10, 10, Tipos.FOGO, 20, 100),
                pokemon("Lento", 10, 10, Tipos.AGUA, 10, 100));
        InOrder ordem = inOrder(registro);
        ordem.verify(registro).registrarAtaque("Rapido", "Lento");
        ordem.verify(registro).registrarDano(eq("Lento"), anyDouble(), eq(false));
        ordem.verify(registro).registrarAtaque("Lento", "Rapido");
        ordem.verify(registro).registrarDano(eq("Rapido"), anyDouble(), eq(false));
        verify(registro, atLeastOnce()).registrarEfeitoTerreno(anyString(), any());
    }

    @Test
    void testResultadoSalvoAoFimDaBatalha() {
        when(gerador.checarAcerto(anyInt())).thenReturn(true);
        when(gerador.checarCritico()).thenReturn(false);
        Treinador jogador = new Treinador("Jogador", pokemon("F", 100, 40, Tipos.FOGO, 50, 100));
        Treinador bot = new Treinador("Bot", pokemon("P", 10, 50, Tipos.PLANTA, 10, 10));
        Torneio torneio = new Torneio(jogador, null, new Round(null, gerador, null), null, repositorio, null, null);
        assertSame(jogador, torneio.executarBatalha(jogador, bot));
        ArgumentCaptor<ResultadoBatalha> captor = ArgumentCaptor.forClass(ResultadoBatalha.class);
        verify(repositorio).salvarResultado(captor.capture());
        assertEquals("Jogador", captor.getValue().getVencedor());
        assertEquals("Bot", captor.getValue().getPerdedor());
        assertTrue(captor.getValue().getTurnos() > 0);
        assertTrue(captor.getValue().getPokesalUtilizado().contains("F"));
    }

    @Test
    void testFalhaNoRepositorioNaoInvalidaBatalha() {
        when(gerador.checarAcerto(anyInt())).thenReturn(true);
        when(gerador.checarCritico()).thenReturn(false);
        doThrow(new RuntimeException("offline")).when(repositorio).salvarResultado(any(ResultadoBatalha.class));
        Treinador jogador = new Treinador("J", pokemon("F", 100, 40, Tipos.FOGO, 50, 100));
        Treinador bot = new Treinador("B", pokemon("P", 10, 50, Tipos.PLANTA, 10, 10));
        Torneio torneio = new Torneio(jogador, null, new Round(null, gerador, null), null, repositorio, null, null);
        assertSame(jogador, torneio.executarBatalha(jogador, bot));
        verify(repositorio).salvarResultado(any(ResultadoBatalha.class));
    }

    @Test
    void testRepositorioNaoChamadoSeBatalhaNaoTerminou() {
        Round roundMock = mock(Round.class);
        when(roundMock.executarBatalhaComTurnos(any(Treinador.class), any(Treinador.class))).thenReturn(50);
        Treinador t1 = new Treinador("A", pokemon("A", 10, 100, Tipos.FOGO, 10, 100));
        Treinador t2 = new Treinador("B", pokemon("B", 10, 100, Tipos.AGUA, 10, 100));
        Torneio torneio = new Torneio(null, null, roundMock, null, repositorio, null, null);
        assertNull(torneio.executarBatalha(t1, t2));
        verify(repositorio, never()).salvarResultado(any(ResultadoBatalha.class));
    }

    @Test
    void testChaveamentoTorneioAvancaVencedores() {
        Round roundMock = mock(Round.class);
        when(roundMock.executarBatalhaComTurnos(any(Treinador.class), any(Treinador.class))).thenAnswer(inv -> {
            Treinador a = inv.getArgument(0);
            Treinador b = inv.getArgument(1);
            b.getPokemon().setHp(0);
            return 1;
        });
        List<Treinador> inscritos = List.of(
                new Treinador("T1", pokemon("P1", 10, 10, Tipos.FOGO, 10, 100)),
                new Treinador("T2", pokemon("P2", 10, 10, Tipos.AGUA, 10, 100)),
                new Treinador("T3", pokemon("P3", 10, 10, Tipos.FOGO, 10, 100)),
                new Treinador("T4", pokemon("P4", 10, 10, Tipos.AGUA, 10, 100)));
        Torneio torneio = new Torneio(null, null, roundMock, null, repositorio, null, null);
        assertEquals("T1", torneio.executarTorneio(inscritos).getNome());
        verify(roundMock, times(3)).executarBatalhaComTurnos(any(Treinador.class), any(Treinador.class));
        verify(repositorio, atLeastOnce()).salvarResultado(any(ResultadoBatalha.class));
    }

    @Test
    void testNotificacaoSemItensEAumentoDeAtributo() {
        when(gerador.checarAcerto(anyInt())).thenReturn(true);
        when(gerador.checarCritico()).thenReturn(false);
        when(calculadoraBonus.calcularBonusXp(1, 1)).thenReturn(5);
        Treinador jogador = new Treinador("J", pokemon("F", 100, 40, Tipos.FOGO, 50, 100));
        Treinador bot = new Treinador("B", pokemon("P", 10, 50, Tipos.PLANTA, 10, 10));
        Torneio torneio = new Torneio(jogador, null, new Round(null, gerador, null), null,
                repositorio, notificacao, calculadoraBonus);
        torneio.executarBatalha(jogador, bot);
        verify(notificacao).notificarConquista("J", "Vitória Perfeita sem Itens");
        verify(calculadoraBonus).calcularBonusXp(1, 1);
        assertEquals(105, jogador.getPokemon().getAtk());
    }

    @Test
    void requisitoAutoralNotificaConquistaApenasQuandoJogadorVenceSemItens() {
        when(gerador.checarAcerto(anyInt())).thenReturn(true);
        when(gerador.checarCritico()).thenReturn(false);
        Treinador jogador = new Treinador("J", pokemon("F", 100, 40, Tipos.FOGO, 50, 100));
        Treinador bot = new Treinador("B", pokemon("P", 10, 50, Tipos.PLANTA, 10, 10));
        Torneio torneio = new Torneio(jogador, null, new Round(null, gerador, null), null,
                repositorio, notificacao, null);

        assertSame(jogador, torneio.executarBatalha(jogador, bot));
        verify(notificacao, times(1)).notificarConquista("J", "Vitória Perfeita sem Itens");

        Treinador jogador2 = new Treinador("J2", pokemon("F2", 100, 40, Tipos.FOGO, 50, 100));
        Treinador bot2 = new Treinador("B2", pokemon("P2", 10, 50, Tipos.PLANTA, 10, 10));
        jogador2.getPokemon().setHp(90);
        jogador2.usarItem(Item.POTION);
        Torneio torneioComItem = new Torneio(jogador2, null, new Round(null, gerador, null), null,
                repositorio, notificacao, null);
        torneioComItem.executarBatalha(jogador2, bot2);
        verify(notificacao, times(1)).notificarConquista(anyString(), anyString());
    }

    @Test
    void requisitoAutoralBonusCalculadoPorColaboradorEValorNegativoNaoReduzAtaque() {
        when(gerador.checarAcerto(anyInt())).thenReturn(true);
        when(gerador.checarCritico()).thenReturn(false);
        when(calculadoraBonus.calcularBonusXp(1, 1)).thenReturn(5, -3);
        Treinador jogador1 = new Treinador("J1", pokemon("F1", 100, 40, Tipos.FOGO, 50, 100));
        Treinador bot1 = new Treinador("B1", pokemon("P1", 10, 50, Tipos.PLANTA, 10, 10));
        Torneio torneio1 = new Torneio(jogador1, null, new Round(null, gerador, null), null,
                null, null, calculadoraBonus);
        torneio1.executarBatalha(jogador1, bot1);
        assertEquals(105, jogador1.getPokemon().getAtk());

        Treinador jogador2 = new Treinador("J2", pokemon("F2", 100, 40, Tipos.FOGO, 50, 100));
        Treinador bot2 = new Treinador("B2", pokemon("P2", 10, 50, Tipos.PLANTA, 10, 10));
        Torneio torneio2 = new Torneio(jogador2, null, new Round(null, gerador, null), null,
                null, null, calculadoraBonus);
        torneio2.executarBatalha(jogador2, bot2);
        assertEquals(100, jogador2.getPokemon().getAtk());
        verify(calculadoraBonus, times(2)).calcularBonusXp(1, 1);
    }
}


