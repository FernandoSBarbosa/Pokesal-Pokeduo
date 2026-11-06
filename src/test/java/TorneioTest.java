import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TorneioTest {

    @Mock private GeradorAleatorio geradorMock;
    @Mock private RegistroBatalha registroMock;
    @Mock private RepositorioResultados repositorioMock;
    @Mock private ServicoNotificacao notificacaoMock;
    @Mock private CalculadoraBonus calculadoraBonusMock;

    @Test
    public void testAcertoCriticoComGeradorMockado() {
        when(geradorMock.checarAcerto(anyInt())).thenReturn(true);
        when(geradorMock.checarCritico()).thenReturn(true);

        Round round = new Round(null, geradorMock, registroMock);
        Pokemon p1 = new Pokemon("CharSal", 50, 40, Tipos.FOGO, 50, 100);
        Pokemon p2 = new Pokemon("BulbaSal", 40, 50, Tipos.PLANTA, 40, 100);

        double dano = round.calcularDano(p1, p2, 100);

        // Sem crítico: (50/50)*10*2.0 = 20.0 | Com crítico (1.5x): 30.0
        assertEquals(30.0, dano, 0.01);
        verify(geradorMock, times(1)).checarCritico();
    }

    @Test
    public void testGolpeErraQuandoPrecisaoFalha() {
        when(geradorMock.checarAcerto(anyInt())).thenReturn(false);

        Round round = new Round(null, geradorMock, registroMock);
        Pokemon p1 = new Pokemon("CharSal", 50, 40, Tipos.FOGO, 50, 100);
        Pokemon p2 = new Pokemon("BulbaSal", 40, 50, Tipos.PLANTA, 40, 100);

        int hpInicial = p2.getHp();
        double dano = round.calcularDano(p1, p2, 70);

        assertEquals(0.0, dano, 0.001);
        assertEquals(hpInicial, p2.getHp());
        verify(geradorMock, never()).checarCritico();
    }

    @Test
    public void testAsfaltoQuenteApenasDuranteODia() {
        Clock clockDia = Clock.fixed(Instant.parse("2026-11-05T10:00:00Z"), ZoneId.of("UTC"));
        ServicoTerreno servicoDia = new ServicoTerreno(clockDia);

        Clock clockNoite = Clock.fixed(Instant.parse("2026-11-05T22:00:00Z"), ZoneId.of("UTC"));
        ServicoTerreno servicoNoite = new ServicoTerreno(clockNoite);

        assertEquals(1.15, servicoDia.obterMultiplicador(Tipos.FOGO, TiposTerreno.ASFALTO_QUENTE));
        assertEquals(1.00, servicoNoite.obterMultiplicador(Tipos.FOGO, TiposTerreno.ASFALTO_QUENTE));
    }

    @Test
    public void testSequenciaDeEventosNoTurno() {
        when(geradorMock.checarAcerto(anyInt())).thenReturn(true);
        when(geradorMock.checarCritico()).thenReturn(false);

        Round round = new Round(null, geradorMock, registroMock);
        Pokemon p1 = new Pokemon("CharSal", 50, 40, Tipos.FOGO, 50, 100);
        Pokemon p2 = new Pokemon("BulbaSal", 40, 50, Tipos.PLANTA, 40, 100);

        round.calcularDano(p1, p2, 100);

        InOrder inOrder = inOrder(registroMock);
        inOrder.verify(registroMock).registrarAtaque(p1.getNome(), p2.getNome());
        inOrder.verify(registroMock).registrarDano(eq(p2.getNome()), anyDouble(), eq(false));
    }

    @Test
    public void testResultadoSalvoAoFimDaBatalha() {
        Treinador t1 = new Treinador("Jogador", new Pokemon("CharSal", 100, 40, Tipos.FOGO, 50, 100));
        Treinador t2 = new Treinador("Bot", new Pokemon("BulbaSal", 10, 50, Tipos.PLANTA, 10, 10));

        Round roundReal = new Round(null, geradorMock, null);
        Torneio torneio = new Torneio(t1, null, roundReal, null, repositorioMock, null, null);

        torneio.executarBatalha(t1, t2);

        ArgumentCaptor<ResultadoBatalha> captor = ArgumentCaptor.forClass(ResultadoBatalha.class);
        verify(repositorioMock, times(1)).salvarResultado(captor.capture());

        assertEquals("Jogador", captor.getValue().getVencedor());
        assertEquals("Bot", captor.getValue().getPerdedor());
        assertTrue(captor.getValue().getTurnos() > 0);
    }

    @Test
    public void testFalhaNoRepositorioNaoInvalidaBatalha() {
        doThrow(new RuntimeException("Banco Offline")).when(repositorioMock).salvarResultado(any());

        Treinador t1 = new Treinador("Jogador", new Pokemon("CharSal", 100, 40, Tipos.FOGO, 50, 100));
        Treinador t2 = new Treinador("Bot", new Pokemon("BulbaSal", 10, 50, Tipos.PLANTA, 10, 10));

        Round roundReal = new Round(null, geradorMock, null);
        Torneio torneio = new Torneio(t1, null, roundReal, null, repositorioMock, null, null);

        assertDoesNotThrow(() -> {
            Treinador vencedor = torneio.executarBatalha(t1, t2);
            assertEquals(t1, vencedor);
        });
    }

    @Test
    public void testRepositorioNaoChamadoSeBatalhaNaoTerminou() {
        verify(repositorioMock, never()).salvarResultado(any());
    }

    @Test
    public void testChaveamentoTorneioAvancaVencedores() {
        Treinador t1 = new Treinador("T1", new Pokemon("P1", 100, 50, Tipos.FOGO, 50, 100));
        Treinador t2 = new Treinador("T2", new Pokemon("P2", 10, 10, Tipos.AGUA, 10, 10));
        Treinador t3 = new Treinador("T3", new Pokemon("P3", 100, 50, Tipos.FOGO, 50, 100));
        Treinador t4 = new Treinador("T4", new Pokemon("P4", 10, 10, Tipos.AGUA, 10, 10));

        List<Treinador> lista = Arrays.asList(t1, t2, t3, t4);
        Round roundReal = new Round(null, geradorMock, null);
        Torneio torneio = new Torneio(null, null, roundReal, null, repositorioMock, null, null);

        Treinador campeao = torneio.executarTorneio(lista);
        assertNotNull(campeao);
        verify(repositorioMock, atLeastOnce()).salvarResultado(any());
    }

    @Test
    public void testNotificacaoEnviadaAoVencerSemItens() {
        Treinador t1 = new Treinador("Jogador", new Pokemon("CharSal", 100, 40, Tipos.FOGO, 50, 100));
        Treinador t2 = new Treinador("Bot", new Pokemon("BulbaSal", 10, 50, Tipos.PLANTA, 10, 10));

        Round roundReal = new Round(null, geradorMock, null);
        Torneio torneio = new Torneio(t1, null, roundReal, null, null, notificacaoMock, null);
        torneio.executarBatalha(t1, t2);

        verify(notificacaoMock, times(1)).notificarConquista("Jogador", "Vitória Perfeita sem Itens");
    }

    @Test
    public void testCalculadoraBonusInvocadaAposVitoria() {
        when(calculadoraBonusMock.calcularBonusXp(anyInt(), anyInt())).thenReturn(5);

        Treinador t1 = new Treinador("Jogador", new Pokemon("CharSal", 100, 40, Tipos.FOGO, 50, 100));
        Treinador t2 = new Treinador("Bot", new Pokemon("BulbaSal", 10, 50, Tipos.PLANTA, 10, 10));

        Round roundReal = new Round(null, geradorMock, null);
        Torneio torneio = new Torneio(t1, null, roundReal, null, null, null, calculadoraBonusMock);
        torneio.executarBatalha(t1, t2);

        verify(calculadoraBonusMock, times(1)).calcularBonusXp(eq(1), anyInt());
        assertEquals(105, t1.getPokemon().getAtk());
    }
}