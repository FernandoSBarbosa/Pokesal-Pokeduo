import java.time.Clock;
import java.time.LocalTime;

public class ServicoTerreno {
    private final Clock clock;

    public ServicoTerreno(Clock clock) {
        if (clock == null) throw new IllegalArgumentException("Clock é obrigatório.");
        this.clock = clock;
    }

    public boolean isDia() {
        LocalTime agora = LocalTime.now(clock);
        return !agora.isBefore(LocalTime.of(6, 0)) && agora.isBefore(LocalTime.of(18, 0));
    }

    public double obterMultiplicador(Tipos tipoAtacante, TiposTerreno terreno) {
        if (tipoAtacante == null || terreno == null) throw new IllegalArgumentException("Tipo e terreno são obrigatórios.");
        if ((terreno == TiposTerreno.DIA || terreno == TiposTerreno.ASFALTO_QUENTE) && tipoAtacante == Tipos.FOGO) {
            return isDia() ? 1.15 : 1.0;
        }
        if (terreno == TiposTerreno.CHUVA && tipoAtacante == Tipos.AGUA) {
            return 1.10;
        }
        return 1.0;
    }
}
