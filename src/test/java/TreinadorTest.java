import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TreinadorTest {

    private Treinador criarTreinador() {
        Pokemon pokemon = new Pokemon("Bulbasal", 49, 49, Tipos.PLANTA, 45, 100, Status.NORMAL);
        return new Treinador("Treinador", pokemon);
    }

    @Test
    public void testUsoLimiteDeItensExcedido() {
        Treinador treinador = criarTreinador();
        treinador.usarItem(Item.POTION);
        treinador.usarItem(Item.POTION);

        assertThrows(IllegalStateException.class, () -> treinador.usarItem(Item.POTION));
        assertEquals(2, treinador.getItensUsados());
    }

    @Test
    public void testItensCuramOuRemovemVeneno() {
        Treinador treinador = criarTreinador();
        Pokemon pokemon = treinador.getPokemon();
        pokemon.setHp(30);

        treinador.usarItem(Item.POTION);
        assertEquals(40, pokemon.getHp());

        pokemon.setStatus(Status.ENVENENADO);
        treinador.usarItem(Item.ANTIDOTO);
        assertEquals(Status.NORMAL, pokemon.getStatus());
    }

    @Test
    public void testSuperPotionCuraVinteDeHp() {
        Treinador treinador = criarTreinador();
        treinador.getPokemon().setHp(30);

        treinador.usarItem(Item.SUPER_POTION);

        assertEquals(50, treinador.getPokemon().getHp());
    }

    @Test
    public void testPokemonDerrotadoNaoUsaItem() {
        Treinador treinador = criarTreinador();
        treinador.getPokemon().setHp(0);

        assertThrows(IllegalStateException.class, () -> treinador.usarItem(Item.POTION));
        assertEquals(0, treinador.getItensUsados());
    }
}
