import java.time.Clock;
import java.util.List;
import java.util.Scanner;

public class BatalhaPokemon {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        GeradorAleatorio gerador = new GeradorAleatorioReal();
        Clock clock = Clock.systemDefaultZone();
        ServicoTerreno servicoTerreno = new ServicoTerreno(clock);
        RegistroBatalha registro = null;
        RepositorioResultados repositorio = null;

        System.out.println("Bem vindo treinador ao Pokeduo!\nInforme seu nome: ");
        String nome1 = sc.next();

        Pokemon poke1 = escolherPokemon(sc, nome1);
        Treinador t1 = new Treinador(nome1, poke1);

        int mapa = gerador.proximoInt(4) + 1;
        TiposTerreno tipoEscolhido = TiposTerreno.NORMAL;
        if (mapa == 1) tipoEscolhido = TiposTerreno.DIA;
        else if (mapa == 2) tipoEscolhido = TiposTerreno.CHUVA;
        else if (mapa == 3) tipoEscolhido = TiposTerreno.CANTEIRO;

        Estacionamento estacionamento = new Estacionamento(tipoEscolhido, servicoTerreno);
        Round gerenciadorRound = new Round(estacionamento, gerador, registro);
        GeradorBots geradorBots = new GeradorBots(gerador);

        System.out.println("Qual modo de jogo você quer jogar? ");
        System.out.println("1- Batalha contra outro jogador\n 2- Torneio contra bots");
        int modoDeJogo = sc.nextInt();

        if (modoDeJogo == 2) {
            Torneio torneio = new Torneio(t1, estacionamento, gerenciadorRound, geradorBots, repositorio, null, null);
            Treinador bot1 = geradorBots.gerarBot(1);
            Treinador bot2 = geradorBots.gerarBot(2);
            Treinador bot3 = geradorBots.gerarBot(3);

            torneio.executarTorneio4Treinadores(List.of(t1, bot1, bot2, bot3));
        }

        sc.close();
    }

    public static Pokemon escolherPokemon(Scanner sc, String nomeTreinador) {
        List<Pokemon> pokesal = PokemonDex.getTodos();
        Pokemon escolhido = null;
        while (escolhido == null) {
            System.out.println(nomeTreinador + " escolha seu pokesal para batalhar!");
            System.out.println("1 - Bulbasal\n2 - Charsal\n3 - Squirtsal\n4 - Chikosal\n5 - Cyndasal\n6 - Totosal");
            int escolha = sc.nextInt();

            if (escolha >= 1 && escolha <= pokesal.size()) {
                escolhido = pokesal.get(escolha - 1);
            } else {
                System.out.println("Opção inválida! Tente novamente.\n");
            }
        }
        return escolhido;
    }
}