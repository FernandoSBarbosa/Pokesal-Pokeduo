import java.util.List;

/** Gera os bots do torneio: sorteia um Pokésal e fortalece conforme o nível do bot. */
public class GeradorBots {
    private static final double FATOR_DIFICULDADE = 0.15;

    private final GeradorAleatorio gerador;

    public GeradorBots(GeradorAleatorio gerador) {
        this.gerador = gerador;
    }

    /**
     * Sorteia um dos Pokésal da Pokédex.
     *
     * @return Pokémon sorteado
     */
    public Pokemon sortearBase() {
        List<Pokemon> pokemons = PokemonDex.getTodos();
        return pokemons.get(gerador.proximoInt(pokemons.size()));
    }

    /**
     * Cria uma cópia do Pokémon com os atributos aumentados em 15% por nível acima do 1.
     *
     * @param base  Pokémon original
     * @param nivel nível do bot (1 mantém os atributos)
     * @return Pokémon fortalecido
     */
    public Pokemon escalarPokemon(Pokemon base, int nivel) {
        double multiplicador = 1 + (FATOR_DIFICULDADE * (nivel - 1));
        return new Pokemon(
                base.getNome(),
                (int) (base.getAtk() * multiplicador),
                (int) (base.getDef() * multiplicador),
                base.getTipo(),
                (int) (base.getVel() * multiplicador),
                (int) (base.getHp() * multiplicador),
                base.getStatus(),
                base.getGolpe());
    }

    /**
     * Gera um bot: Pokémon sorteado e fortalecido pelo nível.
     *
     * @param nivel nível do bot
     * @return treinador bot
     */
    public Treinador gerarBot(int nivel) {
        return new Treinador("Bot nível " + nivel, escalarPokemon(sortearBase(), nivel));
    }
}
