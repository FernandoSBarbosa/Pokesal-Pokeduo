import java.time.Clock;
import java.time.LocalTime;

/**
 * Escolhe o terreno pelo clima e pelo horário do relógio recebido no construtor:
 * chovendo vira poça de chuva; de dia vira asfalto quente; à noite vira canteiro central.
 */
public class ServicoTerrenoPadrao implements ServicoTerreno {
    private static final LocalTime INICIO_DIA = LocalTime.of(6, 0);
    private static final LocalTime FIM_DIA = LocalTime.of(18, 0);

    private final Clock relogio;
    private final boolean chovendo;

    public ServicoTerrenoPadrao(Clock relogio, boolean chovendo) {
        this.relogio = relogio;
        this.chovendo = chovendo;
    }

    @Override
    public TiposTerreno obterTerreno() {
        if (chovendo) {
            return TiposTerreno.CHUVA;
        }
        LocalTime agora = LocalTime.ofInstant(relogio.instant(), relogio.getZone());
        boolean eDia = !agora.isBefore(INICIO_DIA) && agora.isBefore(FIM_DIA);
        return eDia ? TiposTerreno.DIA : TiposTerreno.CANTEIRO;
    }
}
