import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** O relógio (Clock) é mockado para simular dia e noite. */
@ExtendWith(MockitoExtension.class)
public class ServicoTerrenoPadraoTest {

    private static final Instant MANHA = Instant.parse("2026-11-05T10:00:00Z");
    private static final Instant NOITE = Instant.parse("2026-11-05T22:00:00Z");

    @Mock
    private Clock relogio;

    @Test
    public void testAsfaltoQuenteApenasDuranteODia() {
        when(relogio.getZone()).thenReturn(ZoneOffset.UTC);
        when(relogio.instant()).thenReturn(MANHA, NOITE);   // 1ª consulta: dia; 2ª: noite
        Estacionamento estacionamento = new Estacionamento(new ServicoTerrenoPadrao(relogio, false));

        assertEquals(1.15, estacionamento.multiplicadorTerreno(Tipos.FOGO), 0.001);   // de dia
        assertEquals(1.0, estacionamento.multiplicadorTerreno(Tipos.FOGO), 0.001);    // à noite
        verify(relogio, times(2)).instant();
        verify(relogio, atLeastOnce()).getZone();
    }

    @Test
    public void testTerrenoNosLimitesDoHorarioDeDia() {
        when(relogio.getZone()).thenReturn(ZoneOffset.UTC);
        when(relogio.instant()).thenReturn(
                Instant.parse("2026-11-05T06:00:00Z"),    // início do dia
                Instant.parse("2026-11-05T17:59:00Z"),    // último minuto do dia
                Instant.parse("2026-11-05T18:00:00Z"),    // início da noite
                Instant.parse("2026-11-05T05:59:00Z"));   // ainda é noite
        ServicoTerreno servico = new ServicoTerrenoPadrao(relogio, false);

        assertEquals(TiposTerreno.DIA, servico.obterTerreno());
        assertEquals(TiposTerreno.DIA, servico.obterTerreno());
        assertEquals(TiposTerreno.CANTEIRO, servico.obterTerreno());
        assertEquals(TiposTerreno.CANTEIRO, servico.obterTerreno());
    }

    @Test
    public void testChuvaTemPrioridadeSobreOHorario() {
        ServicoTerreno servico = new ServicoTerrenoPadrao(relogio, true);

        assertEquals(TiposTerreno.CHUVA, servico.obterTerreno());
        verify(relogio, never()).instant();
    }
}
