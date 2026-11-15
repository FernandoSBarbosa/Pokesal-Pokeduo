import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Testes da Fase 02 (regras de dano, terreno, velocidade e valores limite).
 * Aqui o Estacionamento é real; o gerador e o registro são mocks sem comportamento,
 * porque calcularDano() não sorteia nem registra nada.
 */
public class RoundTest {

    private Estacionamento criarEstacionamento(TiposTerreno terreno) {
        return new Estacionamento(() -> terreno);
    }

    private Round criarRound(TiposTerreno terreno) {
        return new Round(criarEstacionamento(terreno), mock(GeradorAleatorio.class), mock(RegistroBatalha.class));
    }

    @Test
    public void testVantagemElemental() {
        Round round = criarRound(TiposTerreno.NORMAL);

        Pokemon fogo = new Pokemon("CharSal", 50, 40, Tipos.FOGO, 65, 39, Status.NORMAL);
        Pokemon agua = new Pokemon("SquirtSal", 48, 50, Tipos.AGUA, 43, 44, Status.NORMAL);
        Pokemon planta = new Pokemon("Bulbasal", 33, 50, Tipos.PLANTA, 45, 45, Status.NORMAL);
        Pokemon aguaAtacante = new Pokemon("Totosal", 50, 40, Tipos.AGUA, 43, 50, Status.NORMAL);
        Pokemon fogoDefensor = new Pokemon("CyndaSal", 30, 50, Tipos.FOGO, 65, 39, Status.NORMAL);
        Pokemon plantaAtacante = new Pokemon("ChikoSal", 50, 65, Tipos.PLANTA, 45, 45, Status.NORMAL);

        // atk 50 / def 50 = 1.0; dano = 1.0 * 8 * multiplicador elemental
        assertEquals(16.0, round.calcularDano(fogo, planta), 0.01);          // fogo -> planta x2.0
        assertEquals(4.0, round.calcularDano(fogo, agua), 0.01);             // fogo -> água x0.5
        assertEquals(16.0, round.calcularDano(aguaAtacante, fogoDefensor), 0.01); // água -> fogo x2.0
        assertEquals(4.0, round.calcularDano(aguaAtacante, planta), 0.01);   // água -> planta x0.5
        assertEquals(16.0, round.calcularDano(plantaAtacante, agua), 0.01);  // planta -> água x2.0
        assertEquals(4.0, round.calcularDano(plantaAtacante, fogoDefensor), 0.01); // planta -> fogo x0.5
    }

    @Test
    public void testEfeitoTerrenoEstacionamentoUCSal() {
        Estacionamento dia = criarEstacionamento(TiposTerreno.DIA);
        assertEquals(1.15, dia.multiplicadorTerreno(Tipos.FOGO), 0.01);
        assertEquals(1.0, dia.multiplicadorTerreno(Tipos.AGUA), 0.01);

        Estacionamento chuva = criarEstacionamento(TiposTerreno.CHUVA);
        assertEquals(1.10, chuva.multiplicadorTerreno(Tipos.AGUA), 0.01);
        assertEquals(1.0, chuva.multiplicadorTerreno(Tipos.FOGO), 0.01);

        Estacionamento canteiro = criarEstacionamento(TiposTerreno.CANTEIRO);
        Pokemon planta = new Pokemon("Bulbasal", 49, 49, Tipos.PLANTA, 45, 100, Status.NORMAL);
        planta.setHp(50);
        assertTrue(canteiro.curaCanteiro(planta));
        assertEquals(55, planta.getHp()); // 5% de 100
    }

    @Test
    public void testOrdemDeAtaquePorVelocidade() {
        Round round = criarRound(TiposTerreno.NORMAL);
        Pokemon rapido = new Pokemon("CharSal", 50, 40, Tipos.FOGO, 65, 39, Status.NORMAL);
        Pokemon lento = new Pokemon("Bulbasal", 33, 50, Tipos.PLANTA, 45, 45, Status.NORMAL);
        Pokemon empatado = new Pokemon("Outro", 10, 10, Tipos.AGUA, 65, 10, Status.NORMAL);

        assertSame(rapido, round.ordem(rapido, lento));
        assertSame(rapido, round.ordem(lento, rapido));
        assertSame(rapido, round.ordem(rapido, empatado)); // empate: o primeiro informado
    }

    @Test
    public void testCalculoDanoBoundaryValues() {
        Round round = criarRound(TiposTerreno.NORMAL);
        Pokemon atacante = new Pokemon("CharSal", 50, 40, Tipos.FOGO, 65, 39, Status.NORMAL);
        Pokemon defesaZero = new Pokemon("Bulbasal", 33, 0, Tipos.PLANTA, 45, 45, Status.NORMAL);
        Pokemon ataqueZero = new Pokemon("SquirtSal", 0, 50, Tipos.AGUA, 43, 44, Status.NORMAL);
        Pokemon alvo = new Pokemon("Totosal", 50, 40, Tipos.AGUA, 43, 50, Status.NORMAL);

        assertEquals(Double.POSITIVE_INFINITY, round.calcularDano(atacante, defesaZero), 0.01);
        assertEquals(0.0, round.calcularDano(ataqueZero, alvo), 0.01);
    }
}
