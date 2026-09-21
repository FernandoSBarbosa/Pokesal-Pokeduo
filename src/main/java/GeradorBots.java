import java.util.List;
import java.util.Random;
/**
 * Classe que gera os bots do torneio e faz as regras
*/
public class GeradorBots {
    private static final double FATOR_DIFICULDADE = 0.15;
/**
 * Método que sorteia o pokemon para cada nível do bot
 *
 * @return retorna um pokemon sorteado
 */
    public Pokemon sortearBase() {
        List<Pokemon> pokemons = PokemonDex.getTodos();
        Random rand = new Random();
        int numeropokemon = rand.nextInt(pokemons.size());
        return pokemons.get(numeropokemon);
    }
/**
* Método que faz o escalonamento dos bots conforme o jogador passa os níveis
 *
 * @param base é o pokemon original de fábrica
 * @param nivel é o nível do bot
 * @return retorna o pokemon buffado
 */
    public Pokemon escalarPokemon(Pokemon base, int nivel) {
        double multiplicador = 1 + (FATOR_DIFICULDADE * nivel);
        int novoAtk = (int) (base.getAtk() * multiplicador);
        int novoHp = (int) (base.getHp() * multiplicador);
        int novoDef = (int) (base.getDef() * multiplicador);
        int novoVel = (int) (base.getVel() * multiplicador);
        return new Pokemon(base.getNome(), novoAtk, novoDef, base.getTipo(), novoVel, novoHp, base.getStatus());
    }
/**
 * Método que gera os bots usando os outros dois métodos
 *
 * @param nivel é o nivel do torneio
 * @return retorna o novo bot com o o pokemon novo
 */
    public Treinador gerarBot(int nivel) {
        Pokemon pokemonSorteado = sortearBase();
        Pokemon pokemonEscalado = escalarPokemon(pokemonSorteado, nivel);
        return new Treinador("Bot nível " + nivel, pokemonEscalado);
    }
}