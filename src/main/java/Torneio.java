public class Torneio {
    private Treinador jogador;
    private Estacionamento estacionamento;
    private Round gerenciar;
    private GeradorBots geradorBots;

    public Torneio(Treinador jogador, Estacionamento estacionamento,  Round gerenciar) {
        this.jogador = jogador;
        this.estacionamento = estacionamento;
        this.gerenciar = gerenciar;
        this.geradorBots = new GeradorBots();
    }
    public void executarTorneio(){
        int vitorias = 0;
        for (int nivel = 1; nivel <= 5; nivel++){
            Treinador bot = geradorBots.gerarBot(nivel);
            while (jogador.getPokemon().getHp() > 0 && bot.getPokemon().getHp() > 0) {
                gerenciar.simularRound(jogador.getPokemon(), bot.getPokemon());
            }
            if (jogador.getPokemon().getHp() <= 0) {
                System.out.println("Seu pokemon morreu " + jogador.getPokemon().getHp() + " Você perdeu no nível: " + nivel);
                break;
            } else {
                System.out.println("Você venceu o  nível " + nivel + "!");
                jogador.getPokemon().fortalecer();
                vitorias++;
                if (vitorias == 5){
                    System.out.println("Você venceu o torneio, parabéns!!! ");
                    break;
                }
            }
        }
    }

}