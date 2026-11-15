import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GeradorAleatorioRealTest {

    @Test
    public void testProximoIntFicaDentroDoLimite() {
        GeradorAleatorio gerador = new GeradorAleatorioReal();

        for (int i = 0; i < 1000; i++) {
            int valor = gerador.proximoInt(10);
            assertTrue(valor >= 0 && valor < 10);
        }
    }

    @Test
    public void testProximoDoubleFicaEntreZeroEUm() {
        GeradorAleatorio gerador = new GeradorAleatorioReal();

        for (int i = 0; i < 1000; i++) {
            double valor = gerador.proximoDouble();
            assertTrue(valor >= 0.0 && valor < 1.0);
        }
    }
}
