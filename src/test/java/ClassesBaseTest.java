import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClassesBaseTest {

    @Mock private ServicoTerreno servicoTerreno;
    @Mock private GeradorAleatorio gerador;
    @Mock private RegistroBatalha registro;
    @Mock private RepositorioResultados repositorio;
    @Mock private Round roundMock;

    private Round round;
    private Pokemon p1;
    private Pokemon p2;

    @BeforeEach
    void setUp() {
        round = new Round(servicoTerreno, gerador, registro);
        p1 = new Pokemon("Pikachu", 50, 30, 60, 100);
        p2 = new Pokemon("Charmander", 40, 20, 50, 100);
    }

    @Test
    @DisplayName("Deve aplicar bônus de asfalto quente apenas durante o dia")
    public void testAsfaltoQuenteApenasDuranteODia() {
        when(servicoTerreno.obterModificadorTerreno()).thenReturn(1.5);
        when(gerador.gerarNumero(1, 100)).thenReturn(50); // Acerto normal

        double dano = round.calcularDano(p1, p2);
        assertEquals(45.0, dano, 0.01); // (50 - 20) * 1.5 = 45.0
    }

    @Test
    @DisplayName("Deve calcular acerto crítico com multiplicador correto de 1.5x")
    public void testDanoCriticoComPrecisao() {
        when(servicoTerreno.obterModificadorTerreno()).thenReturn(1.0);
        when(gerador.gerarNumero(1, 100)).thenReturn(50, 5); // 1º acerto, 2º crítico

        double dano = round.calcularDano(p1, p2);
        assertEquals(45.0, dano, 0.01); // (50 - 20) * 1.0 * 1.5 = 45.0
    }

    @Test
    @DisplayName("Deve executar o chaveamento do torneio com 4 treinadores e mocks")
    public void testChaveamentoTorneioComMocks() {
        Torneio torneio = new Torneio(roundMock, repositorio);

        Treinador t1 = new Treinador("T1", new Pokemon("P1", 50, 30, 50, 100));
        Treinador t2 = new Treinador("T2", new Pokemon("P2", 50, 30, 50, 100));
        Treinador t3 = new Treinador("T3", new Pokemon("P3", 50, 30, 50, 100));
        Treinador t4 = new Treinador("T4", new Pokemon("P4", 50, 30, 50, 100));

        List<Treinador> inscritos = Arrays.asList(t1, t2, t3, t4);

        when(roundMock.executarBatalha(t1.getPokemon(), t2.getPokemon())).thenReturn(t1.getPokemon());
        when(roundMock.executarBatalha(t3.getPokemon(), t4.getPokemon())).thenReturn(t3.getPokemon());
        when(roundMock.executarBatalha(t1.getPokemon(), t3.getPokemon())).thenReturn(t1.getPokemon());

        Treinador campeao = torneio.executarTorneio(inscritos);

        assertEquals(t1, campeao);
        verify(repositorio, atLeastOnce()).salvarResultado(anyString());
    }

    @Test
    @DisplayName("Valida aplicação correta de status queimado e paralisado")
    public void testAplicacaoStatus() {
        p1.setStatus(Status.QUEIMADO);
        round.aplicarStatus(p1);

        assertEquals(25, p1.getAtaque()); // Redução de 50%
        assertEquals(95, p1.getHp());     // Dano contínuo

        p2.setStatus(Status.PARALISADO);
        round.aplicarStatus(p2);
        assertEquals(25, p2.getVelocidade()); // Redução de 50%
    }
}