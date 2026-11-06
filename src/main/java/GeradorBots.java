public class GeradorBots {
    private final GeradorAleatorio gerador;

    // Injeção de dependência via construtor
    public GeradorBots(GeradorAleatorio gerador) {
        if (gerador == null) {
            throw new IllegalArgumentException("GeradorAleatorio é obrigatório.");
        }
        this.gerador = gerador;
    }

    public Treinador criarBot(String nome) {
        int atk = gerador.gerarNumero(30, 60);
        int def = gerador.gerarNumero(20, 50);
        int vel = gerador.gerarNumero(40, 70);
        Pokemon p = new Pokemon("Bot-" + nome, atk, def, vel, 100);
        return new Treinador(nome, p);
    }
}