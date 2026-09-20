import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeradorBots {
    List<Pokemon> pokemons;
    private static final double FATOR_DIFICULDADE = 0.15;
    public GeradorBots() {
        pokemons = new ArrayList<>();
        pokemons.add(new Pokemon("CharSal", 52, 43, Tipos.FOGO, 65, 39, Status.NORMAL));
        pokemons.add(new Pokemon("SquirtSal", 48, 65, Tipos.AGUA, 43, 44, Status.NORMAL));
        pokemons.add(new Pokemon("Bulbasal", 49, 49, Tipos.PLANTA, 45, 45, Status.NORMAL));
        pokemons.add(new Pokemon("ChikoSal", 49, 65, Tipos.PLANTA, 45, 45, Status.NORMAL));
        pokemons.add(new Pokemon("CyndaSal", 55, 40, Tipos.FOGO, 65, 39, Status.NORMAL));
        pokemons.add(new Pokemon("Totosal", 65, 64, Tipos.AGUA, 43, 50, Status.NORMAL));

    }
    public Pokemon sortearBase(){
        Random rand = new Random();
        int numeropokemon = rand.nextInt(pokemons.size());
        return pokemons.get(numeropokemon);
    }
    public Pokemon escalarPokemon(Pokemon base, int nivel){
        double multiplicador = 1 + (FATOR_DIFICULDADE * nivel);
        int novoAtk = (int) (base.getAtk() * multiplicador);
        int novoHp = (int) (base.getHp() * multiplicador);
        int novoDef = (int) (base.getDef() * multiplicador);
        int novoVel = (int) (base.getVel() * multiplicador);
        return new Pokemon(base.getNome(), novoAtk, novoDef, base.getTipo(), novoVel, novoHp, base.getStatus());
    }
    public Treinador gerarBot(int nivel){
        Pokemon pokemonSorteado = sortearBase();
        Pokemon pokemonEscalado = escalarPokemon(pokemonSorteado, nivel);
        return new Treinador("Bot nível " + nivel, pokemonEscalado);

    }

}
