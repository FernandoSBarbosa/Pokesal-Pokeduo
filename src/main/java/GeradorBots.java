import java.util.List;

public class GeradorBots {
    private static final double FATOR_DIFICULDADE = 0.15;
    private final GeradorAleatorio gerador;

    // Construtor sem parâmetros (Compatibilidade com testes antigos)
    public GeradorBots() {
        this(new GeradorAleatorioReal());
    }

    // Construtor com Injeção de Dependência (Fase 03)
    public GeradorBots(GeradorAleatorio gerador) {
        this.gerador = gerador;
    }

    public Pokemon sortearBase() {
        List<Pokemon> pokemons = PokemonDex.getTodos();
        int index = gerador.proximoInt(pokemons.size());
        return pokemons.get(index);
    }

    public Pokemon escalarPokemon(Pokemon base, int nivel) {
        double mult = 1 + (FATOR_DIFICULDADE * (nivel - 1));
        return new Pokemon(
                base.getNome(),
                (int) (base.getAtk() * mult),
                (int) (base.getDef() * mult),
                base.getTipo(),
                (int) (base.getVel() * mult),
                (int) (base.getHp() * mult),
                base.getStatus()
        );
    }

    public Treinador gerarBot(int nivel) {
        Pokemon base = sortearBase();
        Pokemon escalado = escalarPokemon(base, nivel);
        return new Treinador("Bot nível " + nivel, escalado);
    }
}