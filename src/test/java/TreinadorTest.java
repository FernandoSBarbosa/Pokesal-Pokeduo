import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TreinadorTest {

    @Test
    public void testUsoLimiteDeItensExcedido() {
        Pokemon pokemon = new Pokemon("Bulbasal", 49, 49, Tipos.PLANTA, 45, 45, Status.NORMAL);
        Treinador treinador = new Treinador("Treinador", pokemon);
        treinador.usarItem(Item.POTION);
        treinador.usarItem(Item.POTION);
        assertThrows(IllegalStateException.class, () -> treinador.usarItem(Item.POTION));
    }
}