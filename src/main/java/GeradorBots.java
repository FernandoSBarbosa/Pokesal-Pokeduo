import java.util.List;
import java.util.Random;

public class GeradorBots {
    private static final double FATOR_DIFICULDADE = 0.15;

    public Pokemon sortearBase() {
        List<Pokemon> pokemons = PokemonDex.getTodos();
        Random rand = new Random();
        int numeropokemon = rand.nextInt(pokemons.size());
        return pokemons.get(numeropokemon);
    }

    public Pokemon escalarPokemon(Pokemon base, int nivel) {
        double multiplicador = 1 + (FATOR_DIFICULDADE * nivel);
        int novoAtk = (int) (base.getAtk() * multiplicador);
        int novoHp = (int) (base.getHp() * multiplicador);
        int novoDef = (int) (base.getDef() * multiplicador);
        int novoVel = (int) (base.getVel() * multiplicador);
        return new Pokemon(base.getNome(), novoAtk, novoDef, base.getTipo(), novoVel, novoHp, base.getStatus());
    }

    public Treinador gerarBot(int nivel) {
        Pokemon pokemonSorteado = sortearBase();
        Pokemon pokemonEscalado = escalarPokemon(pokemonSorteado, nivel);
        return new Treinador("Bot nível " + nivel, pokemonEscalado);
    }
}