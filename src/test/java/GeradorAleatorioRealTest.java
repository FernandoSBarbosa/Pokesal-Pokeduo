import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeradorAleatorioRealTest {
    @Test
    void geraValoresDentroDosLimitesERespeitaPrecisao() {
        GeradorAleatorioReal gerador = new GeradorAleatorioReal();

        assertTrue(gerador.proximoInt(5) >= 0);
        assertTrue(gerador.proximoInt(5) < 5);
        assertTrue(gerador.proximoDouble() >= 0.0);
        assertTrue(gerador.proximoDouble() < 1.0);
        assertFalse(gerador.checarAcerto(0));
        assertTrue(gerador.checarAcerto(100));

        for (int i = 0; i < 100; i++) {
            int numero = gerador.gerarNumero(3, 7);
            assertTrue(numero >= 3 && numero <= 7);
        }
        assertEquals(4, gerador.gerarNumero(4, 4));
    }

    @Test
    void chanceCriticaRetornaBooleano() {
        GeradorAleatorioReal gerador = new GeradorAleatorioReal();
        for (int i = 0; i < 100; i++) assertNotNull(Boolean.valueOf(gerador.checarCritico()));
    }

    @Test
    void rejeitaIntervalosInvalidos() {
        GeradorAleatorioReal gerador = new GeradorAleatorioReal();
        assertThrows(IllegalArgumentException.class, () -> gerador.gerarNumero(8, 2));
        assertThrows(IllegalArgumentException.class, () -> gerador.checarAcerto(-1));
        assertThrows(IllegalArgumentException.class, () -> gerador.checarAcerto(101));
        assertThrows(IllegalArgumentException.class, () -> gerador.proximoInt(0));
    }
}
