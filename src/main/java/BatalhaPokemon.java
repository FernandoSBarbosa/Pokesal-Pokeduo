import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Inicialização e injeção manual das dependências
        GeradorAleatorio gerador = new GeradorAleatorioReal();
        ServicoTerreno servicoTerreno = new ServicoTerrenoImpl(true, TipoTerreno.ASFALTO_QUENTE);
        RegistroBatalha registro = new RegistroBatalhaConsole();
        RepositorioResultados repositorio = new RepositorioMemoria();

        Round round = new Round(servicoTerreno, gerador, registro);
        Torneio torneio = new Torneio(round, repositorio);
        GeradorBots geradorBots = new GeradorBots(gerador);

        Scanner scanner = new Scanner(System.in);
        System.out.println("=== POKESAL - FASE 03 ===");
        System.out.println("1 - Batalha 1v1 (Jogador vs Jogador com Uso de Itens)");
        System.out.println("2 - Torneio de Treinadores (8 Participantes)");
        System.out.print("Escolha uma opção: ");

        int opcao = scanner.nextInt();

        if (opcao == 1) {
            // Modo 1 restaurado
            Pokemon p1 = new Pokemon("Pikachu", 50, 30, 60, 100);
            Pokemon p2 = new Pokemon("Charmander", 55, 25, 55, 100);

            System.out.println("\n--- Modo 1: Batalha 1v1 ---");
            registro.registrarUsoItem(p1, "Poção de Vida");
            p1.setHp(p1.getHp() + 20);

            System.out.println("Fortalecendo " + p2.getNome() + " (+10 ATK)...");
            p2.setAtaque(p2.getAtaque() + 10);

            Pokemon vencedor = round.executarBatalha(p1, p2);
            System.out.println("\n🏆 VENCEDOR DA BATALHA: " + vencedor.getNome());

        } else if (opcao == 2) {
            // Modo 2 integrado
            List<Treinador> inscritos = new ArrayList<>();
            for (int i = 1; i <= 8; i++) {
                inscritos.add(geradorBots.criarBot("Bot_" + i));
            }

            System.out.println("\n--- Modo 2: Torneio de 8 Treinadores ---");
            Treinador campeao = torneio.executarTorneio(inscritos);

            System.out.println("\n==========================================");
            System.out.println(" 🏆 CAMPEÃO DO TORNEIO: " + campeao.getNome());
            System.out.println("==========================================");
        } else {
            System.out.println("Opção inválida.");
        }

        scanner.close();
    }
}