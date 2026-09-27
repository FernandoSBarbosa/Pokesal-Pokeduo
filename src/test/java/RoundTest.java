import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RoundTest {
    @Test
    public void testVantagemElemental() {
        Estacionamento estacionamento = new Estacionamento(TiposTerreno.NORMAL);
        Round round = new Round(estacionamento);

        Pokemon p1 = new Pokemon("CharSal", 50, 40, Tipos.FOGO, 65, 39, Status.NORMAL);
        Pokemon p2 = new Pokemon("Bulbasal", 33, 50, Tipos.PLANTA, 45, 45, Status.NORMAL);
        Pokemon p3 = new Pokemon("SquirtSal", 48, 50, Tipos.AGUA, 43, 44, Status.NORMAL);

        double danoFogoPlanta = round.calcularDano(p1, p2);
        assertEquals(16.0, danoFogoPlanta, 0.01);

        double danoFogoAgua = round.calcularDano(p1, p3);
        assertEquals(4.0, danoFogoAgua, 0.01);

        Pokemon aguaAtacante = new Pokemon("Totosal", 50, 40, Tipos.AGUA, 43, 50, Status.NORMAL);
        Pokemon fogoDefensor = new Pokemon("CyndaSal", 30, 50, Tipos.FOGO, 65, 39, Status.NORMAL);

        double danoAguaFogo = round.calcularDano(aguaAtacante, fogoDefensor);
        assertEquals(16.0, danoAguaFogo, 0.01);

        double danoAguaPlanta = round.calcularDano(aguaAtacante, p2);
        assertEquals(4.0, danoAguaPlanta, 0.01);

        Pokemon plantaAtacante = new Pokemon("ChikoSal", 50, 65, Tipos.PLANTA, 45, 45, Status.NORMAL);

        double danoPlantaAgua = round.calcularDano(plantaAtacante, p3);
        assertEquals(16.0, danoPlantaAgua, 0.01);

        double danoPlantaFogo = round.calcularDano(plantaAtacante, fogoDefensor);
        assertEquals(4.0, danoPlantaFogo, 0.01);
    }
    @Test
    public void testEfeitoTerrenoEstacionamentoUCSal() {
        Estacionamento estacionamentoDia = new Estacionamento(TiposTerreno.DIA);
        double multiplicadorFogo = estacionamentoDia.multiplicadorTerreno(Tipos.FOGO);
        assertEquals(1.15, multiplicadorFogo, 0.01);

        Estacionamento estacionamentoChuva = new Estacionamento(TiposTerreno.CHUVA);
        double multiplicadorAgua = estacionamentoChuva.multiplicadorTerreno(Tipos.AGUA);
        assertEquals(1.10, multiplicadorAgua, 0.01);

        Estacionamento estacionamentoCanteiro = new Estacionamento(TiposTerreno.CANTEIRO);
        Pokemon pokemonPlanta = new Pokemon("Bulbasal", 49, 49, Tipos.PLANTA, 45, 100, Status.NORMAL);
        pokemonPlanta.setHp(50);
        estacionamentoCanteiro.curaCanteiro(pokemonPlanta);
        assertEquals(55, pokemonPlanta.getHp());
    }
    @Test
    public void testOrdemDeAtaquePorVelocidade(){
        Pokemon p1 = new Pokemon("CharSal", 50, 40, Tipos.FOGO, 65, 39, Status.NORMAL);
        Pokemon p2 = new Pokemon("Bulbasal", 33, 50, Tipos.PLANTA, 45, 45, Status.NORMAL);
        Estacionamento estacionamento = new Estacionamento(TiposTerreno.NORMAL);
        Round round = new Round(estacionamento);
        Pokemon pokemonRapido = round.ordem(p1, p2);
        assertSame(p1, pokemonRapido);
    }
    @Test
    public void testCalculoDanoBoundaryValues(){
        Round round = new Round(new Estacionamento(TiposTerreno.NORMAL));

        Pokemon p1 = new Pokemon("CharSal", 50, 40, Tipos.FOGO, 65, 39, Status.NORMAL);
        Pokemon p2 = new Pokemon("Bulbasal", 33, 0, Tipos.PLANTA, 45, 45, Status.NORMAL);
        double resultadoDefZero = round.calcularDano(p1, p2);
        assertEquals(Double.POSITIVE_INFINITY, resultadoDefZero, 0.01);

        Pokemon p3 = new Pokemon("SquirtSal", 0, 50, Tipos.AGUA, 43, 44, Status.NORMAL);
        Pokemon p4 = new Pokemon("Totosal", 50, 40, Tipos.AGUA, 43, 50, Status.NORMAL);
        double resultadoAtkZero = round.calcularDano(p3, p4);
        assertEquals(0.0, resultadoAtkZero, 0.01);
    }
}