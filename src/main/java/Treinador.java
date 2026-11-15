public class Treinador {
    private static final int LIMITE_ITENS = 2;
    private static final int CURA_POTION = 10;
    private static final int CURA_SUPER_POTION = 20;

    private final String nome;
    private final Pokemon pokemon;
    private int itensUsados;

    public Treinador(String nome, Pokemon pokemon) {
        this.nome = nome;
        this.pokemon = pokemon;
        this.itensUsados = 0;
    }

    public String getNome() { return nome; }
    public Pokemon getPokemon() { return pokemon; }
    public int getItensUsados() { return itensUsados; }

    public boolean podeUsarItem() {
        return itensUsados < LIMITE_ITENS;
    }

    /**
     * Usa um item no Pokémon do treinador (máximo de 2 por batalha).
     *
     * @param item item escolhido
     * @throws IllegalStateException se o limite de itens foi atingido
     *                               ou se o Pokémon já foi derrotado
     */
    public void usarItem(Item item) {
        if (!podeUsarItem()) {
            throw new IllegalStateException("Você excedeu o limite de uso de itens");
        }
        if (!pokemon.isVivo()) {
            throw new IllegalStateException("Pokémon derrotado não pode receber itens");
        }

        switch (item) {
            case POTION -> pokemon.setHp(pokemon.getHp() + CURA_POTION);
            case SUPER_POTION -> pokemon.setHp(pokemon.getHp() + CURA_SUPER_POTION);
            case ANTIDOTO -> {
                if (pokemon.getStatus() == Status.ENVENENADO) {
                    pokemon.setStatus(Status.NORMAL);
                }
            }
        }
        itensUsados++;
    }
}
