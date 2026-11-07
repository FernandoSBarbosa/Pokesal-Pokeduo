import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClassesBaseTest {
    @Test
    void pokemonClampaHpEAtributosNaoNegativos() {
        Pokemon pokemon = new Pokemon("P", 10, 10, Tipos.FOGO, 10, 100);
        pokemon.setHp(-5);
        assertEquals(0, pokemon.getHp());
        pokemon.setHp(500);
        assertEquals(100, pokemon.getHp());
        pokemon.setAtk(-1);
        assertEquals(0, pokemon.getAtk());
    }

    @Test
    void terrenoCuraApenasPlantaECuraNoMaximoCincoPorCento() {
        Estacionamento estacionamento = new Estacionamento(TiposTerreno.CANTEIRO,
                new ServicoTerreno(java.time.Clock.systemUTC()));
        Pokemon planta = new Pokemon("Planta", 10, 10, Tipos.PLANTA, 10, 100);
        Pokemon fogo = new Pokemon("Fogo", 10, 10, Tipos.FOGO, 10, 100);
        planta.setHp(50);
        fogo.setHp(50);
        estacionamento.curaCanteiro(planta);
        estacionamento.curaCanteiro(fogo);
        assertEquals(55, planta.getHp());
        assertEquals(50, fogo.getHp());
    }

    @Test
    void itemNaoConsumidoSeNaoTiverEfeito() {
        Treinador treinador = new Treinador("T", new Pokemon("P", 10, 10, Tipos.FOGO, 10, 100));
        assertThrows(IllegalStateException.class, () -> treinador.usarItem(Item.POTION));
        assertEquals(0, treinador.getItensUsados());
    }
}



