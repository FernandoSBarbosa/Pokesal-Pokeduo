public enum Tipos {
    FOGO,
    AGUA,
    PLANTA;

    private static final double SUPER_EFETIVO = 2.0;
    private static final double POUCO_EFETIVO = 0.5;
    private static final double DANO_NORMAL = 1.0;

    /**
     * Multiplicador de dano deste tipo (atacante) contra o tipo defensor.
     *
     * @param defensor tipo do Pokémon que recebe o golpe
     * @return 2.0 (super efetivo), 0.5 (pouco efetivo) ou 1.0 (normal)
     */
    public double multiplicadorContra(Tipos defensor) {
        if (this == FOGO) {
            if (defensor == PLANTA) return SUPER_EFETIVO;
            if (defensor == AGUA) return POUCO_EFETIVO;
        }
        if (this == AGUA) {
            if (defensor == FOGO) return SUPER_EFETIVO;
            if (defensor == PLANTA) return POUCO_EFETIVO;
        }
        if (this == PLANTA) {
            if (defensor == FOGO) return POUCO_EFETIVO;
            if (defensor == AGUA) return SUPER_EFETIVO;
        }
        return DANO_NORMAL;
    }
}
