import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class GeradorBotsTest {
    @Test
    public void testEscalarPokemonNivel1(){
        Pokemon base = new Pokemon("CharSal", 50, 40, Tipos.FOGO, 60, 40, Status.NORMAL);
        GeradorBots geradorBots = new GeradorBots();
        Pokemon resultado = geradorBots.escalarPokemon(base, 1);
        int vida = resultado.getHp();
        assertEquals(40, vida);
        int dano = resultado.getAtk();
        assertEquals(50, dano);
        int vel = resultado.getVel();
        assertEquals(60, vel);
        int def = resultado.getDef();
        assertEquals(40, def);
    }
    @Test
    public void testEscalarPokemonNivel3(){
        Pokemon base = new Pokemon("CharSal", 50, 40, Tipos.FOGO, 60, 40, Status.NORMAL);
        GeradorBots geradorBots = new GeradorBots();
        Pokemon resultado = geradorBots.escalarPokemon(base, 3);

        assertEquals(65, resultado.getAtk());
        assertEquals(52, resultado.getHp());
        assertEquals(52, resultado.getDef());
        assertEquals(78, resultado.getVel());
    }
}
