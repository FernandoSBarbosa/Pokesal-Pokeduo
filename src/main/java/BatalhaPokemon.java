import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class BatalhaPokemon {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random random = new Random();

        System.out.println("Bem vindo treinador ao Pokeduo!\nInforme seu nome: ");
        String nome1 = sc.next();

        Pokemon poke1 = escolherPokemon(sc, nome1);
        Treinador t1 = new Treinador(nome1, poke1);

        int mapa = random.nextInt(4) + 1;
        TiposTerreno tipoEscolhido = TiposTerreno.NORMAL;
        if (mapa == 1) tipoEscolhido = TiposTerreno.DIA;
        else if (mapa == 2) tipoEscolhido = TiposTerreno.CHUVA;
        else if (mapa == 3) tipoEscolhido = TiposTerreno.CANTEIRO;

        Estacionamento estacionamento = new Estacionamento(tipoEscolhido);
        Round gerenciadorRound = new Round(estacionamento);

        System.out.println("Qual modo de jogo você quer jogar? ");
        System.out.println("1- Batalha contra outro jogador\n 2- Torneio contra bots");
        int modoDeJogo = sc.nextInt();
        if (modoDeJogo == 1) {
            jogarBatalhaSimples(t1, sc, estacionamento, gerenciadorRound);
        } else if (modoDeJogo == 2) {
            jogarTorneio(t1, estacionamento, gerenciadorRound);
        } else {
            System.out.println("Escolha de modo de jogo inválida, escolha entre 1 e 2");
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

    public static void jogarBatalhaSimples(Treinador t1, Scanner sc, Estacionamento estacionamento, Round gerenciadorRound) {
        System.out.println("\nAgora é sua vez segundo treinador \nInforme seu nome: ");
        String nome2 = sc.next();

        Pokemon poke2 = escolherPokemon(sc, nome2);
        Treinador t2 = new Treinador(nome2, poke2);

        System.out.println("\n--- INICIO DA BATALHA ---");
        System.out.println("MAPA SORTEADO: " + estacionamento.getTerrenoAtual());
        int x = 1;

        while (t1.getPokemon().getHp() > 0 && poke2.getHp() > 0) {
            System.out.println("\n==================== ROUND " + x + " ====================");
            System.out.println(t1.getPokemon().getNome() + " HP: " + t1.getPokemon().getHp());
            System.out.println(poke2.getNome() + " HP: " + poke2.getHp());

            boolean t1UsouItem = false;
            boolean t2UsouItem = false;

            System.out.println("\nAção do " + t1.getNome() + ":");
            System.out.println("1 - Atacar\n2 - Usar Item");
            System.out.print("Opção: ");
            int acao1 = sc.nextInt();

            if (acao1 == 2) {
                if (t1.podeUsarItem()) {
                    System.out.println("Escolha o item: \n1- Porção \n2- Super-porção\n3- Antídoto");
                    int item = sc.nextInt();
                    if (item == 1) t1.usarItem(Item.POTION);
                    else if (item == 2) t1.usarItem(Item.SUPER_POTION);
                    else if (item == 3) t1.usarItem(Item.ANTIDOTO);
                    t1UsouItem = true;
                    System.out.println(t1.getNome() + " usou um item!");
                } else {
                    System.out.println("Você já usou seus itens! Vai atacar.");
                }
            }

            System.out.println("\nAção do " + t2.getNome() + ":");
            System.out.println("1 - Atacar\n2 - Usar Item");
            System.out.print("Opção: ");
            int acao2 = sc.nextInt();

            if (acao2 == 2) {
                if (t2.podeUsarItem()) {
                    System.out.println("Escolha o item: \n1- Porção \n2- Super-porção\n3- Antídoto");
                    int item = sc.nextInt();
                    if (item == 1) t2.usarItem(Item.POTION);
                    else if (item == 2) t2.usarItem(Item.SUPER_POTION);
                    else if (item == 3) t2.usarItem(Item.ANTIDOTO);
                    t2UsouItem = true;
                    System.out.println(t2.getNome() + " usou um item!");
                } else {
                    System.out.println("Você já usou seus itens! Vai atacar.");
                }
            }

            if (t1UsouItem && t2UsouItem) {
                System.out.println("Ambos os treinadores usaram itens!");
            } else if (t1UsouItem) {
                gerenciadorRound.executarAtaque(poke2, t1.getPokemon());
            } else if (t2UsouItem) {
                gerenciadorRound.executarAtaque(t1.getPokemon(), poke2);
            } else {
                gerenciadorRound.simularRound(t1.getPokemon(), poke2);
            }

            estacionamento.curaCanteiro(t1.getPokemon());
            estacionamento.curaCanteiro(poke2);
            gerenciadorRound.aplicarStatus(t1.getPokemon());
            gerenciadorRound.aplicarStatus(poke2);

            x++;
        }

        System.out.println("\n=== FIM DA BATALHA ===");
        if (t1.getPokemon().getHp() > 0) {
            System.out.println("Vencedor: " + t1.getNome());
        } else if (poke2.getHp() > 0) {
            System.out.println("Vencedor: " + t2.getNome());
        }
    }

    public static void jogarTorneio(Treinador t1, Estacionamento estacionamento, Round gerenciar) {
        Torneio torneio = new Torneio(t1, estacionamento, gerenciar);
        torneio.executarTorneio();
    }
}