import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PokemonTest {

    @Test
    public void testFortalecer() {
        Pokemon p1 = new Pokemon("CharSal", 50, 40, Tipos.FOGO, 65, 39, Status.NORMAL);
        p1.setHp(30);
        p1.fortalecer();

        assertEquals(39, p1.getHp());   // cura total
        assertEquals(55, p1.getAtk());  // 50 * 1.10
        assertEquals(44, p1.getDef());  // 40 * 1.10
        assertEquals(71, p1.getVel());  // 65 * 1.10 = 71.5, o cast para int trunca
    }

    @Test
    public void testHpNuncaPassaDosLimites() {
        Pokemon p = new Pokemon("Bulbasal", 49, 49, Tipos.PLANTA, 45, 100, Status.NORMAL);

        p.setHp(150);
        assertEquals(100, p.getHp());

        p.setHp(-5);
        assertEquals(0, p.getHp());
        assertFalse(p.isVivo());
    }

    @Test
    public void testGolpePadraoTemPrecisaoMaxima() {
        Pokemon p = new Pokemon("CharSal", 52, 43, Tipos.FOGO, 65, 39, Status.NORMAL);

        assertEquals(100, p.getGolpe().precisao());
    }

    @Test
    public void testGolpeComPrecisaoInvalidaLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> new Golpe("Golpe", 101));
        assertThrows(IllegalArgumentException.class, () -> new Golpe("Golpe", -1));
    }

    @Test
    public void testStatusNuloViraNormal() {
        Pokemon p = new Pokemon("CharSal", 52, 43, Tipos.FOGO, 65, 39, null);

        assertEquals(Status.NORMAL, p.getStatus());
    }
}
