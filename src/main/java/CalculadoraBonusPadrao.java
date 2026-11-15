/** Quanto mais rápida a vitória, maior o bônus de ataque. */
public class CalculadoraBonusPadrao implements CalculadoraBonus {
    private static final int BONUS_MAXIMO = 10;

    @Override
    public int calcularBonusAtaque(int turnos) {
        return Math.max(0, BONUS_MAXIMO - turnos);
    }
}
