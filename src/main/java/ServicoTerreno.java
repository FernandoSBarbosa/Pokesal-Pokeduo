import java.time.Clock;

public class ServicoTerreno {
    private final Clock clock;

    public ServicoTerreno(Clock clock) {
        this.clock = clock;
    }

    public boolean isDia() {
        if (clock == null) return true;
        int hora = clock.instant().atZone(clock.getZone()).getHour();
        return hora >= 6 && hora < 18;
    }

    public double obterMultiplicador(Tipos tipoAtacante, TiposTerreno terreno) {
        if (terreno == TiposTerreno.DIA && tipoAtacante == Tipos.FOGO) {
            return isDia() ? 1.15 : 1.0;
        }
        if (terreno == TiposTerreno.CHUVA && tipoAtacante == Tipos.AGUA) {
            return 1.10;
        }
        return 1.0;
    }
}