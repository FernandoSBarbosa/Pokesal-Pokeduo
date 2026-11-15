public class Estacionamento {
    private static final double BONUS_FOGO = 1.15;
    private static final double BONUS_AGUA = 1.10;
    private static final double SEM_BONUS = 1.0;
    private static final double CURA = 0.05;

    private final ServicoTerreno servicoTerreno;

    public Estacionamento(ServicoTerreno servicoTerreno) {
        this.servicoTerreno = servicoTerreno;
    }

    public TiposTerreno getTerrenoAtual() {
        return servicoTerreno.obterTerreno();
    }

    /**
     * Multiplicador de dano que o terreno atual dá a um golpe do tipo informado.
     *
     * @param tipoAtacante tipo do Pokémon que ataca
     * @return 1.15 (fogo no asfalto quente), 1.10 (água na chuva) ou 1.0
     */
    public double multiplicadorTerreno(Tipos tipoAtacante) {
        TiposTerreno terreno = getTerrenoAtual();
        if (terreno == TiposTerreno.DIA && tipoAtacante == Tipos.FOGO) {
            return BONUS_FOGO;
        }
        if (terreno == TiposTerreno.CHUVA && tipoAtacante == Tipos.AGUA) {
            return BONUS_AGUA;
        }
        return SEM_BONUS;
    }

    /**
     * No canteiro central, Pokémon de Planta recuperam 5% do HP máximo.
     *
     * @param pokemon Pokémon que pode ser curado
     * @return true se o Pokémon recuperou HP
     */
    public boolean curaCanteiro(Pokemon pokemon) {
        if (getTerrenoAtual() != TiposTerreno.CANTEIRO || pokemon.getTipo() != Tipos.PLANTA) {
            return false;
        }
        int hpAntes = pokemon.getHp();
        pokemon.setHp(hpAntes + (int) (pokemon.getMaxHP() * CURA));
        return pokemon.getHp() > hpAntes;
    }
}
