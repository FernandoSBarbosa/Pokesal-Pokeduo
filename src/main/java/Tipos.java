public enum Tipos {
    FOGO,
    AGUA,
    PLANTA;
    private static final double SUPER_EFETIVO = 2.0;
    private  static final double POUCO_EFETIVO = 0.5;
    private  static final double DANO_NORMAL = 1.0;
    public double multiplicadorContra(Tipos defensor){
        if (this == FOGO){
            if (defensor == PLANTA) return SUPER_EFETIVO;
            if (defensor == AGUA) return POUCO_EFETIVO;
        }
        if (this == AGUA){
            if (defensor == FOGO) return SUPER_EFETIVO;
            if (defensor == PLANTA) return POUCO_EFETIVO;
        }
        if (this == PLANTA){
            if (defensor == FOGO) return POUCO_EFETIVO;
            if (defensor == AGUA) return SUPER_EFETIVO;
        }
        return DANO_NORMAL;
    }
}
