import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/** O Estacionamento é testado isolado: o ServicoTerreno é mockado. */
@ExtendWith(MockitoExtension.class)
public class EstacionamentoTest {

    @Mock
    private ServicoTerreno servicoTerreno;
    @InjectMocks
    private Estacionamento estacionamento;

    @Test
    public void testBonusDeFogoSoNoAsfaltoQuente() {
        when(servicoTerreno.obterTerreno()).thenReturn(TiposTerreno.DIA);

        assertEquals(1.15, estacionamento.multiplicadorTerreno(Tipos.FOGO), 0.001);
        assertEquals(1.0, estacionamento.multiplicadorTerreno(Tipos.AGUA), 0.001);
    }

    @Test
    public void testBonusDeAguaSoNaChuva() {
        when(servicoTerreno.obterTerreno()).thenReturn(TiposTerreno.CHUVA);

        assertEquals(1.10, estacionamento.multiplicadorTerreno(Tipos.AGUA), 0.001);
        assertEquals(1.0, estacionamento.multiplicadorTerreno(Tipos.FOGO), 0.001);
    }

    @Test
    public void testCanteiroCuraPlantaEmCincoPorCentoDoHpMaximo() {
        when(servicoTerreno.obterTerreno()).thenReturn(TiposTerreno.CANTEIRO);
        Pokemon planta = new Pokemon("Bulbasal", 49, 49, Tipos.PLANTA, 45, 100, Status.NORMAL);
        planta.setHp(50);

        assertTrue(estacionamento.curaCanteiro(planta));
        assertEquals(55, planta.getHp());
    }

    @Test
    public void testCanteiroNaoCuraOutrosTiposNemPlantaComHpCheio() {
        when(servicoTerreno.obterTerreno()).thenReturn(TiposTerreno.CANTEIRO);
        Pokemon fogo = new Pokemon("CharSal", 52, 43, Tipos.FOGO, 65, 100, Status.NORMAL);
        fogo.setHp(50);
        Pokemon plantaCheia = new Pokemon("Bulbasal", 49, 49, Tipos.PLANTA, 45, 100, Status.NORMAL);

        assertFalse(estacionamento.curaCanteiro(fogo));
        assertEquals(50, fogo.getHp());
        assertFalse(estacionamento.curaCanteiro(plantaCheia));
        assertEquals(100, plantaCheia.getHp());
    }

    @Test
    public void testTerrenoNormalNaoDaEfeito() {
        when(servicoTerreno.obterTerreno()).thenReturn(TiposTerreno.NORMAL);
        Pokemon planta = new Pokemon("Bulbasal", 49, 49, Tipos.PLANTA, 45, 100, Status.NORMAL);
        planta.setHp(50);

        assertEquals(1.0, estacionamento.multiplicadorTerreno(Tipos.FOGO), 0.001);
        assertFalse(estacionamento.curaCanteiro(planta));
    }
}
