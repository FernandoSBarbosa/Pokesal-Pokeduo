public class GeradorBots {
    private final GeradorAleatorio gerador;

    public GeradorBots(GeradorAleatorio gerador) {
        if (gerador == null) {
            throw new IllegalArgumentException("GeradorAleatorio é obrigatório.");
        }
        this.gerador = gerador;
    }

    public Pokemon escalarPokemon(Pokemon base, int nivel) {
        double multiplicador = 1.0 + (nivel - 1) * 0.15;
        int atk = (int) (base.getAtk() * multiplicador);
        int def = (int) (base.getDef() * multiplicador);
        int vel = (int) (base.getVel() * multiplicador);
        int hp = (int) (base.getHp() * multiplicador);

        return new Pokemon("Bot-" + base.getNome(), atk, def, base.getTipo(), vel, hp, Status.NORMAL);
    }

    public Treinador criarBot(String nome) {
        int atk = gerador.gerarNumero(30, 60);
        int def = gerador.gerarNumero(20, 50);
        int vel = gerador.gerarNumero(40, 70);
        Tipos tipo = Tipos.values()[gerador.proximoInt(Tipos.values().length)];
        Pokemon p = new Pokemon("Bot-" + nome, atk, def, tipo, vel, 100);
        return new Treinador(nome, p);
    }
}