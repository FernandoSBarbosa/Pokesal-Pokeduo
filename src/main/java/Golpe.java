/**
 * Golpe de um Pokémon. A precisão vai de 0 (nunca acerta) a 100 (sempre acerta).
 *
 * @param nome     nome do golpe
 * @param precisao chance de acerto, de 0 a 100
 */
public record Golpe(String nome, int precisao) {
    private static final int PRECISAO_MINIMA = 0;
    private static final int PRECISAO_MAXIMA = 100;

    public Golpe {
        if (precisao < PRECISAO_MINIMA || precisao > PRECISAO_MAXIMA) {
            throw new IllegalArgumentException("A precisão deve estar entre 0 e 100");
        }
    }
}
