import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class PokemonTest {
    @Test
    public void testFortalecer(){
        Pokemon p1 = new Pokemon("CharSal", 50, 40, Tipos.FOGO, 65, 39, Status.NORMAL);
        p1.setHp(30);
        p1.fortalecer();
        int vida = p1.getHp();
        assertEquals(39, vida);
        int dano = p1.getAtk();
        assertEquals(55, dano);
        int def = p1.getDef();
        assertEquals(44, def);
        int vel = p1.getVel();
        assertEquals(71, vel);


    }

}
