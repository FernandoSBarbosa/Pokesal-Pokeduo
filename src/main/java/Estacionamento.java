public class Estacionamento {
    private TiposTerreno terrenoAtual;
    private final ServicoTerreno servicoTerreno;

    public Estacionamento(TiposTerreno terrenoInicial, ServicoTerreno servicoTerreno) {
        if (terrenoInicial == null) throw new IllegalArgumentException("Terreno inicial é obrigatório.");
        if (servicoTerreno == null) throw new IllegalArgumentException("Serviço de terreno é obrigatório.");
        this.terrenoAtual = terrenoInicial;
        this.servicoTerreno = servicoTerreno;
    }

    /** Construtor de compatibilidade para regras sem integração temporal. */
    public Estacionamento(TiposTerreno terrenoInicial) {
        if (terrenoInicial == null) throw new IllegalArgumentException("Terreno inicial é obrigatório.");
        this.terrenoAtual = terrenoInicial;
        this.servicoTerreno = null;
    }

    public double multiplicadorTerreno(Tipos tipoAtacante) {
        if (tipoAtacante == null) throw new IllegalArgumentException("Tipo atacante é obrigatório.");
        if (servicoTerreno != null) return servicoTerreno.obterMultiplicador(tipoAtacante, terrenoAtual);
        if (terrenoAtual == TiposTerreno.DIA && tipoAtacante == Tipos.FOGO) return 1.15;
        if (terrenoAtual == TiposTerreno.CHUVA && tipoAtacante == Tipos.AGUA) return 1.10;
        return 1.0;
    }

    public void curaCanteiro(Pokemon pokemon) {
        if (pokemon == null) throw new IllegalArgumentException("Pokémon é obrigatório.");
        if (terrenoAtual == TiposTerreno.CANTEIRO && pokemon.getTipo() == Tipos.PLANTA) {
            int cura = (int) (pokemon.getMaxHP() * 0.05);
            pokemon.setHp(pokemon.getHp() + cura);
        }
    }

    public TiposTerreno getTerrenoAtual() { return terrenoAtual; }
    public void setTerrenoAtual(TiposTerreno terrenoAtual) {
        if (terrenoAtual == null) throw new IllegalArgumentException("Terreno não pode ser nulo.");
        this.terrenoAtual = terrenoAtual;
    }
}
