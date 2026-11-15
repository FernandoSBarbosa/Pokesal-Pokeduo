import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class PokemonDexTest {

    @Test
    public void testPokedexTemSeisPokesalComGolpesValidos() {
        List<Pokemon> todos = PokemonDex.getTodos();
        Set<String> nomes = new HashSet<>();

        for (Pokemon p : todos) {
            nomes.add(p.getNome());
            assertTrue(p.getGolpe().precisao() >= 0 && p.getGolpe().precisao() <= 100);
        }
        assertEquals(6, todos.size());
        assertEquals(6, nomes.size());
    }

    @Test
    public void testCadaChamadaCriaPokemonsNovos() {
        // Os bots e os jogadores alteram seus Pokémon; não podem compartilhar objetos.
        Pokemon primeiro = PokemonDex.getTodos().get(0);
        Pokemon segundo = PokemonDex.getTodos().get(0);

        assertNotSame(primeiro, segundo);
    }
}
