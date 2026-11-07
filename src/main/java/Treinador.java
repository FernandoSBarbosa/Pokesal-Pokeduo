public class Treinador {
    private final String nome;
    private final Pokemon pokemon;
    private int itensUsados;
    private RegistroBatalha registro;

    public Treinador(String nome, Pokemon pokemon) {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Nome do treinador é obrigatório.");
        if (pokemon == null) throw new IllegalArgumentException("Pokémon é obrigatório.");
        this.nome = nome;
        this.pokemon = pokemon;
    }

    public String getNome() { return nome; }
    public Pokemon getPokemon() { return pokemon; }
    public int getItensUsados() { return itensUsados; }
    public void setRegistroBatalha(RegistroBatalha registro) { this.registro = registro; }
    public boolean podeUsarItem() { return itensUsados < 2; }

    public void usarItem(Item item) {
        if (item == null) throw new IllegalArgumentException("Item é obrigatório.");
        if (!pokemon.isVivo()) throw new IllegalStateException("Pokémon derrotado não pode usar item.");
        if (!podeUsarItem()) throw new IllegalStateException("Você excedeu o limite de uso de itens.");
        switch (item) {
            case POTION -> {
                if (pokemon.getHp() >= pokemon.getMaxHP()) throw new IllegalStateException("O Pokémon já está com HP máximo.");
                pokemon.setHp(pokemon.getHp() + 10);
            }
            case SUPER_POTION -> {
                if (pokemon.getHp() >= pokemon.getMaxHP()) throw new IllegalStateException("O Pokémon já está com HP máximo.");
                pokemon.setHp(pokemon.getHp() + 20);
            }
            case ANTIDOTO -> {
                if (pokemon.getStatus() != Status.ENVENENADO) throw new IllegalStateException("O Pokémon não está envenenado.");
                pokemon.setStatus(Status.NORMAL);
            }
        }
        itensUsados++;
        if (registro != null) registro.registrarUsoItem(nome, item);
    }
}
