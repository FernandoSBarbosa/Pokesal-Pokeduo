import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoundCoverageTest {
    @Mock GeradorAleatorio gerador;
    @Mock RegistroBatalha registro;

    private Pokemon pokemon(String nome, int atk, int def, Tipos tipo, int vel, int hp, Status status) {
        return new Pokemon(nome, atk, def, tipo, vel, hp, status);
    }

    @Test
    void empateDeVelocidadeUsaGeradorParaDesempatarNosDoisSentidos() {
        when(gerador.proximoDouble()).thenReturn(0.25, 0.75);
        Round round = new Round(null, gerador, registro);
        Pokemon a = pokemon("A", 1, 1, Tipos.FOGO, 10, 10, Status.NORMAL);
        Pokemon b = pokemon("B", 1, 1, Tipos.AGUA, 10, 10, Status.NORMAL);

        assertSame(a, round.ordem(a, b));
        assertSame(b, round.ordem(a, b));
    }

    @Test
    void aplicaDanoContinuoAQueimadoEEnvenenadoESkipParaStatusNormalOuNocauteado() {
        Round round = new Round(null, gerador, registro);
        Pokemon queimado = pokemon("Queimado", 10, 10, Tipos.FOGO, 10, 20, Status.QUEIMADO);
        Pokemon envenenado = pokemon("Envenenado", 10, 10, Tipos.AGUA, 10, 20, Status.ENVENENADO);
        Pokemon normal = pokemon("Normal", 10, 10, Tipos.PLANTA, 10, 20, Status.NORMAL);
        Pokemon nocauteado = pokemon("KO", 10, 10, Tipos.PLANTA, 10, 20, Status.ENVENENADO);
        nocauteado.setHp(0);

        round.aplicarStatus(queimado);
        round.aplicarStatus(envenenado);
        round.aplicarStatus(normal);
        round.aplicarStatus(nocauteado);

        assertEquals(15, queimado.getHp());
        assertEquals(15, envenenado.getHp());
        assertEquals(20, normal.getHp());
        assertEquals(0, nocauteado.getHp());
        verify(registro, times(2)).registrarEfeitoStatus(anyString(), any());
    }

    @Test
    void batalhaEncerraComVencedorOuEmpateNoLimiteDeTurnos() {
        when(gerador.checarAcerto(anyInt())).thenReturn(true, false);
        when(gerador.checarCritico()).thenReturn(false);
        Round round = new Round(null, gerador, registro);
        Pokemon forte = pokemon("Forte", 100, 10, Tipos.FOGO, 20, 100, Status.NORMAL);
        Pokemon fraco = pokemon("Fraco", 1, 10, Tipos.AGUA, 10, 10, Status.NORMAL);
        assertSame(forte, round.executarBatalha(forte, fraco));

        Pokemon a = pokemon("A", 1, 100, Tipos.FOGO, 10, 100, Status.NORMAL);
        Pokemon b = pokemon("B", 1, 100, Tipos.AGUA, 10, 100, Status.NORMAL);
        when(gerador.checarAcerto(anyInt())).thenReturn(false);
        assertNull(round.executarBatalha(a, b));
        assertEquals(100, a.getHp());
        assertEquals(100, b.getHp());
    }

    @Test
    void rejeitaParametrosNulosEOprecisaoForaDoIntervalo() {
        Round round = new Round(null, gerador, registro);
        Pokemon p = pokemon("P", 1, 1, Tipos.FOGO, 1, 10, Status.NORMAL);
        assertThrows(IllegalArgumentException.class, () -> round.simularRound(null, p));
        assertThrows(IllegalArgumentException.class, () -> round.calcularDano(null, p, 100));
        assertThrows(IllegalArgumentException.class, () -> round.calcularDano(p, p, -1));
        assertThrows(IllegalArgumentException.class, () -> round.calcularDano(p, p, 101));
    }
}
