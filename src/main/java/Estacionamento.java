public class Estacionamento {
    private TiposTerreno terrenoAtual;
    private final ServicoTerreno servicoTerreno;

    public Estacionamento(TiposTerreno terrenoInicial, ServicoTerreno servicoTerreno) {
        this.terrenoAtual = terrenoInicial;
        this.servicoTerreno = servicoTerreno;
    }

    public Estacionamento(TiposTerreno terrenoInicial) {
        this(terrenoInicial, null);
    }

    public double multiplicadorTerreno(Tipos tipoAtacante) {
        if (servicoTerreno != null) {
            return servicoTerreno.obterMultiplicador(tipoAtacante, terrenoAtual);
        }
        if (terrenoAtual == TiposTerreno.DIA && tipoAtacante == Tipos.FOGO) return 1.15;
        if (terrenoAtual == TiposTerreno.CHUVA && tipoAtacante == Tipos.AGUA) return 1.10;
        return 1.0;
    }

    public void curaCanteiro(Pokemon pokemon) {
        if (terrenoAtual == TiposTerreno.CANTEIRO && pokemon.getTipo() == Tipos.PLANTA) {
            int cura = (int) (pokemon.getMaxHP() * 0.05);
            pokemon.setHp(pokemon.getHp() + cura);
        }
    }

    public TiposTerreno getTerrenoAtual() { return terrenoAtual; }
    public void setTerrenoAtual(TiposTerreno terrenoAtual) { this.terrenoAtual = terrenoAtual; }
}