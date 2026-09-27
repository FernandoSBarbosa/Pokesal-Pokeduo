public class Treinador {
    private String nome;
    private Pokemon pokemon;
    private int itensUsados;

    public Treinador(String nome, Pokemon pokemon) {
        this.itensUsados = 0;
        this.nome = nome;
        this.pokemon = pokemon;
    }
    public String getNome() {
        return nome;
    }
    public Pokemon getPokemon() {
        return pokemon;
    }

    public int getItensUsados() {
        return itensUsados;
    }

    public boolean podeUsarItem(){
        return itensUsados < 2;
    }

    public void usarItem(){
        if(podeUsarItem()){
            itensUsados++;
        }
    }

    public void usarItem(Item item) {

        if (!podeUsarItem()) {
            throw new IllegalStateException("Você excedeu o limite de uso de itens");
        }

        switch (item) {

            case POTION:
                pokemon.setHp(pokemon.getHp() + 10);
                break;

            case SUPER_POTION:
                pokemon.setHp(pokemon.getHp() + 20);
                break;

            case ANTIDOTO:
                if (pokemon.getStatus() == Status.ENVENENADO) {
                    pokemon.setStatus(Status.NORMAL);
                }
                break;
        }

        itensUsados++;
    }
}
