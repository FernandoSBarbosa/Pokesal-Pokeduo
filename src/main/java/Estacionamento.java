public class Estacionamento {
    private TiposTerreno terrenoAtual;
    private static final double BONUS_FOGO = 1.15;
    private static final double BONUS_AGUA = 1.10;
    private static final double CURA = 0.05;

    public Estacionamento(TiposTerreno terrenoInicial){
        this.terrenoAtual = terrenoInicial;
    }

    public double MultiplicadorTerreno(Tipos tipoAtacante) {
        if (terrenoAtual == TiposTerreno.DIA && tipoAtacante == Tipos.FOGO) {
            return BONUS_FOGO;
        }
        if (terrenoAtual == TiposTerreno.CHUVA && tipoAtacante == Tipos.AGUA) {
            return BONUS_AGUA;
        }
        return 1.0;
    }
    public void curaCanteiro(Pokemon pokemon) {
        if (terrenoAtual == TiposTerreno.CANTEIRO
                && pokemon.getTipo() == Tipos.PLANTA) {

            int cura = (int) (pokemon.getMaxHP() * CURA);

            int novoHp = Math.min(
                    pokemon.getHp() + cura,
                    pokemon.getMaxHP()
            );

            pokemon.setHp(novoHp);
        }
    }

    public TiposTerreno getTerrenoAtual() {
        return terrenoAtual;
    }

    public void setTerrenoAtual(TiposTerreno terrenoAtual) {
        this.terrenoAtual = terrenoAtual;
    }
}

