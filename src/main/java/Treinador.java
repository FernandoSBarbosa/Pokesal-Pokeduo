public class Treinador {
    private String nome;
    private Pokemon pokemon;

    public Treinador(String nome, Pokemon pokemon) {
        this.nome = nome;
        this.pokemon = pokemon;
    }
    public String getNome() {
        return nome;
    }
    public Pokemon getPokemon() {
        return pokemon;
    }

}