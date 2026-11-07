import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PokemonDexTest {
    @Test
    void retornaSeisPokemonComTiposENomesEsperados() {
        List<Pokemon> pokemons = PokemonDex.getTodos();

        assertEquals(6, pokemons.size());
        assertEquals(List.of("Bulbasal", "CharSal", "SquirtSal", "ChikoSal", "CyndaSal", "Totosal"),
                pokemons.stream().map(Pokemon::getNome).toList());
        assertTrue(pokemons.stream().allMatch(Pokemon::isVivo));
        assertTrue(pokemons.stream().allMatch(p -> p.getStatus() == Status.NORMAL));
    }
}
