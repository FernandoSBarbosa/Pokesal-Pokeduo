/** Calcula o bônus de ataque que o jogador ganha ao vencer (requisito autoral 2). */
public interface CalculadoraBonus {
    /**
     * Calcula o bônus de ataque de uma vitória.
     *
     * @param turnos quantos turnos a batalha durou
     * @return bônus de ataque (0 ou mais)
     */
    int calcularBonusAtaque(int turnos);
}
