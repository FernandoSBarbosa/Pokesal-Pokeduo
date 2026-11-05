import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ClassesBaseTest {

    @Test
    public void testPokemonAtributosELimitesHp() {
        Pokemon p = new Pokemon("Bulbasal", 50, 40, Tipos.PLANTA, 45, 100, Status.NORMAL);

        assertEquals("Bulbasal", p.getNome());
        assertEquals(50, p.getAtk());
        assertEquals(40, p.getDef());
        assertEquals(Tipos.PLANTA, p.getTipo());
        assertEquals(45, p.getVel());
        assertEquals(100, p.getHp());
        assertEquals(100, p.getMaxHP());
        assertEquals(Status.NORMAL, p.getStatus());

        // Teste de setters e mutadores
        p.setNome("Bulba");
        assertEquals("Bulba", p.getNome());
        p.setTipo(Tipos.FOGO);
        assertEquals(Tipos.FOGO, p.getTipo());
        p.setVel(50);
        assertEquals(50, p.getVel());
        p.setDef(45);
        assertEquals(45, p.getDef());
        p.setMaxHP(120);
        assertEquals(120, p.getMaxHP());
        p.setStatus(Status.QUEIMADO);
        assertEquals(Status.QUEIMADO, p.getStatus());

        // Limites de HP
        p.setHp(150);
        assertEquals(120, p.getHp());

        p.setHp(-20);
        assertEquals(0, p.getHp());

        p.fortalecer();
        assertEquals(120, p.getHp());
        assertTrue(p.getAtk() > 50);
    }

    @Test
    public void testPokemonDexETreinador() {
        List<Pokemon> todos = PokemonDex.getTodos();
        assertFalse(todos.isEmpty());

        Treinador t = new Treinador("Ash", todos.get(0));
        assertEquals("Ash", t.getNome());
        assertEquals(todos.get(0), t.getPokemon());
        assertEquals(0, t.getItensUsados());
        assertTrue(t.podeUsarItem());

        t.usarItem(Item.POTION);
        t.usarItem(Item.SUPER_POTION);
        assertEquals(2, t.getItensUsados());
        assertFalse(t.podeUsarItem());

        assertThrows(IllegalStateException.class, () -> t.usarItem(Item.ANTIDOTO));
    }

    @Test
    public void testTreinadorAntidoto() {
        Pokemon p = new Pokemon("Char", 50, 40, Tipos.FOGO, 50, 50, Status.ENVENENADO);
        Treinador t = new Treinador("Red", p);
        t.usarItem(Item.ANTIDOTO);
        assertEquals(Status.NORMAL, p.getStatus());
    }

    @Test
    public void testResultadoBatalhaDTO() {
        ResultadoBatalha res = new ResultadoBatalha("Jogador", "Bot", 5, "CharSal");
        assertEquals("Jogador", res.getVencedor());
        assertEquals("Bot", res.getPerdedor());
        assertEquals(5, res.getTurnos());
        assertEquals("CharSal", res.getPokesalUtilizado());
    }

    @Test
    public void testGeradorAleatorioReal() {
        GeradorAleatorioReal gerador = new GeradorAleatorioReal();
        assertTrue(gerador.proximoInt(10) >= 0);
        assertTrue(gerador.proximoDouble() >= 0.0);
        assertNotNull(gerador.checarCritico());
        assertTrue(gerador.checarAcerto(100));
        assertFalse(gerador.checarAcerto(0));
    }

    @Test
    public void testServicoTerrenoEStatus() {
        ServicoTerreno stNull = new ServicoTerreno(null);
        assertTrue(stNull.isDia());
        assertEquals(1.0, stNull.obterMultiplicador(Tipos.PLANTA, TiposTerreno.NORMAL));

        Round r = new Round(null);
        Pokemon p1 = new Pokemon("P1", 50, 40, Tipos.FOGO, 50, 100, Status.QUEIMADO);
        Pokemon p2 = new Pokemon("P2", 50, 40, Tipos.FOGO, 50, 100, Status.ENVENENADO);
        r.aplicarStatus(p1);
        r.aplicarStatus(p2);
        assertEquals(95, p1.getHp());
        assertEquals(95, p2.getHp());
    }

    @Test
    public void testEnumsValores() {
        assertNotNull(Tipos.valueOf("FOGO"));
        assertNotNull(Status.valueOf("QUEIMADO"));
        assertNotNull(TiposTerreno.valueOf("CHUVA"));
        assertNotNull(Item.valueOf("POTION"));
    }
}