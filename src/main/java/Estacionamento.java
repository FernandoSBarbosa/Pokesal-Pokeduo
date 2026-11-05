public class Estacionamento {
    private TiposTerreno terrenoAtual;
    private final ServicoTerreno servicoTerreno;
    private static final double BONUS_FOGO = 1.15;
    private static final double BONUS_AGUA = 1.10;
    private static final double CURA = 0.05;

    // Construtor antigo (Compatibilidade com testes da Fase 02)
    public Estacionamento(TiposTerreno terrenoInicial) {
        this(terrenoInicial, null);
    }

    // Construtor novo com Injeção de Dependência (Fase 03)
    public Estacionamento(TiposTerreno terrenoInicial, ServicoTerreno servicoTerreno) {
        this.terrenoAtual = terrenoInicial;
        this.servicoTerreno = servicoTerreno;
    }

    public double multiplicadorTerreno(Tipos tipoAtacante) {
        // Se o ServicoTerreno foi injetado (Fase 03), utiliza a regra de horário com Clock
        if (servicoTerreno != null) {
            return servicoTerreno.obterMultiplicador(tipoAtacante, terrenoAtual);
        }

        // Regra legado para testes da Fase 02 (sem passar o relógio/serviço)
        if (terrenoAtual == TiposTerreno.DIA && tipoAtacante == Tipos.FOGO) {
            return BONUS_FOGO;
        }
        if (terrenoAtual == TiposTerreno.CHUVA && tipoAtacante == Tipos.AGUA) {
            return BONUS_AGUA;
        }
        return 1.0;
    }

    public void curaCanteiro(Pokemon pokemon) {
        if (terrenoAtual == TiposTerreno.CANTEIRO && pokemon.getTipo() == Tipos.PLANTA) {
            int cura = (int) (pokemon.getMaxHP() * CURA);
            pokemon.setHp(Math.min(pokemon.getHp() + cura, pokemon.getMaxHP()));
        }
    }

    public TiposTerreno getTerrenoAtual() { return terrenoAtual; }
    public void setTerrenoAtual(TiposTerreno terrenoAtual) { this.terrenoAtual = terrenoAtual; }
}