import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EstacionamentoTest {
    @Test
    void delegaMultiplicadorAoServicoInjetado() {
        ServicoTerreno servico = mock(ServicoTerreno.class);
        when(servico.obterMultiplicador(Tipos.AGUA, TiposTerreno.CHUVA)).thenReturn(1.10);
        Estacionamento estacionamento = new Estacionamento(TiposTerreno.CHUVA, servico);

        assertEquals(1.10, estacionamento.multiplicadorTerreno(Tipos.AGUA), 0.001);
        verify(servico).obterMultiplicador(Tipos.AGUA, TiposTerreno.CHUVA);
    }

    @Test
    void compatibilidadeSemServicoAplicaSomenteRegrasFixasConhecidas() {
        assertEquals(1.15, new Estacionamento(TiposTerreno.DIA).multiplicadorTerreno(Tipos.FOGO));
        assertEquals(1.10, new Estacionamento(TiposTerreno.CHUVA).multiplicadorTerreno(Tipos.AGUA));
        assertEquals(1.0, new Estacionamento(TiposTerreno.NORMAL).multiplicadorTerreno(Tipos.FOGO));
        assertEquals(1.0, new Estacionamento(TiposTerreno.CANTEIRO).multiplicadorTerreno(Tipos.PLANTA));
    }

    @Test
    void curaCanteiroLimitaHpMaximoENaoCuraOutrosTipos() {
        Estacionamento estacionamento = new Estacionamento(TiposTerreno.CANTEIRO,
                new ServicoTerreno(java.time.Clock.systemUTC()));
        Pokemon plantaQuaseCheia = new Pokemon("Planta", 1, 1, Tipos.PLANTA, 1, 100);
        Pokemon agua = new Pokemon("Agua", 1, 1, Tipos.AGUA, 1, 100);
        plantaQuaseCheia.setHp(99);
        agua.setHp(50);

        estacionamento.curaCanteiro(plantaQuaseCheia);
        estacionamento.curaCanteiro(agua);
        assertEquals(100, plantaQuaseCheia.getHp());
        assertEquals(50, agua.getHp());
        assertThrows(IllegalArgumentException.class, () -> estacionamento.curaCanteiro(null));
    }

    @Test
    void rejeitaTerrenoOuTipoNulo() {
        ServicoTerreno servico = new ServicoTerreno(java.time.Clock.systemUTC());
        assertThrows(IllegalArgumentException.class, () -> new Estacionamento(null, servico));
        assertThrows(IllegalArgumentException.class, () -> new Estacionamento(TiposTerreno.NORMAL, null));
        Estacionamento estacionamento = new Estacionamento(TiposTerreno.NORMAL, servico);
        assertThrows(IllegalArgumentException.class, () -> estacionamento.setTerrenoAtual(null));
        assertThrows(IllegalArgumentException.class, () -> estacionamento.multiplicadorTerreno(null));
    }
}
