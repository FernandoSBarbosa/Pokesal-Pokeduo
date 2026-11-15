import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InfraestruturaTest {

    @Test
    public void testBonusDiminuiComOsTurnosENuncaFicaNegativo() {
        CalculadoraBonus calculadora = new CalculadoraBonusPadrao();

        assertEquals(7, calculadora.calcularBonusAtaque(3));
        assertEquals(0, calculadora.calcularBonusAtaque(10));
        assertEquals(0, calculadora.calcularBonusAtaque(25));
    }

    @Test
    public void testRepositorioEmMemoriaGuardaNaOrdemEProtegeALista() {
        RepositorioResultadosMemoria repositorio = new RepositorioResultadosMemoria();
        ResultadoBatalha primeiro = new ResultadoBatalha("Ana", "Bruno", 4, "CharSal", "Totosal");
        ResultadoBatalha segundo = new ResultadoBatalha("Bruno", "Carla", 6, "Totosal", "Bulbasal");

        repositorio.salvarResultado(primeiro);
        repositorio.salvarResultado(segundo);

        assertEquals(2, repositorio.listar().size());
        assertSame(primeiro, repositorio.listar().get(0));
        assertSame(segundo, repositorio.listar().get(1));
        assertThrows(UnsupportedOperationException.class, () -> repositorio.listar().clear());
    }
}
