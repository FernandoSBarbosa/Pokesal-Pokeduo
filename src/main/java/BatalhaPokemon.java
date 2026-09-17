import java.util.Random;
import java.util.Scanner;

public class BatalhaPokemon {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random random = new Random();

        System.out.println("Bem vindo treinador ao Pokeduo!\nInforme seu nome: ");
        String nome1 = sc.next();

        Pokemon poke1 = null;
        while (poke1 == null) {
            System.out.println(nome1 + " escolha seu pokesal para batalhar!");
            System.out.println("1 - Bulbasal\n2 - Charsal\n3 - Squirtsal\n4 - Chikosal\n5 - Cyndasal\n6 - Totosal");
            int escolha1 = sc.nextInt();

            if (escolha1 == 1) poke1 = new Pokemon("Bulbasal", 49, 49, Tipos.PLANTA, 45, 45, Status.NORMAL);
            else if (escolha1 == 2) poke1 = new Pokemon("CharSal", 52, 43, Tipos.FOGO, 65, 39, Status.NORMAL);
            else if (escolha1 == 3) poke1 = new Pokemon("SquirtSal", 48, 65, Tipos.AGUA, 43, 44, Status.NORMAL);
            else if (escolha1 == 4) poke1 = new Pokemon("ChikoSal", 49, 65, Tipos.PLANTA, 45, 45, Status.NORMAL);
            else if (escolha1 == 5) poke1 = new Pokemon("CyndaSal", 55, 40, Tipos.FOGO, 65, 39, Status.NORMAL);
            else if (escolha1 == 6) poke1 = new Pokemon("Totosal", 65, 64, Tipos.AGUA, 43, 50, Status.NORMAL);
            else System.out.println("Opção inválida! Tente novamente.\n");
        }

        Treinador t1 = new Treinador(nome1, poke1);

        System.out.println("\nAgora é sua vez segundo treinador \nInforme seu nome: ");
        String nome2 = sc.next();

        Pokemon poke2 = null;
        while (poke2 == null) {
            System.out.println(nome2 + " escolha seu pokesal para batalhar!");
            System.out.println("1 - Bulbasal\n2 - Charsal\n3 - Squirtsal\n4 - Chikosal\n5 - Cyndasal\n6 - Totosal");
            int escolha2 = sc.nextInt();

            if (escolha2 == 1) poke2 = new Pokemon("Bulbasal", 49, 49, Tipos.PLANTA, 45, 45, Status.NORMAL);
            else if (escolha2 == 2) poke2 = new Pokemon("CharSal", 52, 43, Tipos.FOGO, 65, 39, Status.NORMAL);
            else if (escolha2 == 3) poke2 = new Pokemon("SquirtSal", 48, 65, Tipos.AGUA, 43, 44, Status.NORMAL);
            else if (escolha2 == 4) poke2 = new Pokemon("ChikoSal", 49, 65, Tipos.PLANTA, 45, 45, Status.NORMAL);
            else if (escolha2 == 5) poke2 = new Pokemon("CyndaSal", 55, 40, Tipos.FOGO, 65, 39, Status.NORMAL);
            else if (escolha2 == 6) poke2 = new Pokemon("Totosal", 65, 64, Tipos.AGUA, 43, 50, Status.NORMAL);
            else System.out.println("Opção inválida! Tente novamente.\n");
        }

        Treinador t2 = new Treinador(nome2, poke2);

        int mapa = random.nextInt(4) + 1;
        TiposTerreno tipoEscolhido = TiposTerreno.NORMAL;
        if (mapa == 1) tipoEscolhido = TiposTerreno.DIA;
        else if (mapa == 2) tipoEscolhido = TiposTerreno.CHUVA;
        else if (mapa == 3) tipoEscolhido = TiposTerreno.CANTEIRO;

        Estacionamento estacionamento = new Estacionamento(tipoEscolhido);
        Round gerenciadorRound = new Round(estacionamento);

        System.out.println("\n--- INICIO DA BATALHA ---");
        System.out.println("MAPA SORTEADO: " + estacionamento.getTerrenoAtual());
        int x = 1;

        while (poke1.getHp() > 0 && poke2.getHp() > 0) {
            System.out.println("\n==================== ROUND " + x + " ====================");
            System.out.println(poke1.getNome() + " HP: " + poke1.getHp());
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
                    System.out.println(t2.getNome() + " usou um item!"); // Corrigido de t1 para t2
                } else {
                    System.out.println("Você já usou seus itens! Vai atacar.");
                }
            }

            if (t1UsouItem && t2UsouItem) {
                System.out.println("Ambos os treinadores usaram itens!");
            } else if (t1UsouItem) {
                gerenciadorRound.executarAtaque(poke2, poke1);
            } else if (t2UsouItem) {
                gerenciadorRound.executarAtaque(poke1, poke2);
            } else {
                gerenciadorRound.simularRound(poke1, poke2);
            }

            estacionamento.curaCanteiro(poke1);
            estacionamento.curaCanteiro(poke2);
            gerenciadorRound.aplicarStatus(poke1);
            gerenciadorRound.aplicarStatus(poke2);

            x++;
        }


        System.out.println("\n=== FIM DA BATALHA ===");
        if (poke1.getHp() > 0) {
            System.out.println("Vencedor: " + t1.getNome());
        } else if (poke2.getHp() > 0) {
            System.out.println("Vencedor: " + t2.getNome());
        }

        sc.close();
    }
}