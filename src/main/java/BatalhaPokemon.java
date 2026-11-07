import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BatalhaPokemon {

    // Implementação utilitária para exibição de eventos no console
    private static class ConsoleRegistroBatalha implements RegistroBatalha {
        @Override
        public void registrarAtaque(String atacante, String defensor) {
            System.out.println("⚔️ " + atacante + " atacou " + defensor + "!");
        }
        @Override
        public void registrarDano(String defensor, double dano, boolean eCritico) {
            if (dano == 0) System.out.println("🛡️ O ataque errou!");
            else System.out.printf("💥 Dano causado a %s: %.1f %s\n", defensor, dano, (eCritico ? "(CRÍTICO!)" : ""));
        }
        @Override
        public void registrarEfeitoStatus(String pokemon, Status status) {
            System.out.println("⚡ Efeito de Status de " + pokemon + ": " + status);
        }
        @Override
        public void registrarEfeitoTerreno(String pokemon, TiposTerreno terreno) {
            System.out.println("🌱 Terreno " + terreno + " afetando " + pokemon);
        }
        @Override
        public void registrarUsoItem(String treinador, Item item) {
            System.out.println("🧪 " + treinador + " usou o item " + item);
        }
    }

    private static class RepositorioEmMemoria implements RepositorioResultados {
        @Override
        public void salvarResultado(ResultadoBatalha resultado) {
            System.out.println("💾 [BD] Resultado Gravado: " + resultado.getVencedor() + " venceu " +
                    resultado.getPerdedor() + " em " + resultado.getTurnos() + " turnos.");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Clock clock = Clock.systemDefaultZone();
        ServicoTerreno servicoTerreno = new ServicoTerreno(clock);
        Estacionamento estacionamento = new Estacionamento(TiposTerreno.ASFALTO_QUENTE, servicoTerreno);
        GeradorAleatorio gerador = new GeradorAleatorioReal();
        RegistroBatalha registro = new ConsoleRegistroBatalha();
        RepositorioResultados repositorio = new RepositorioEmMemoria();

        Round round = new Round(estacionamento, gerador, registro);
        GeradorBots geradorBots = new GeradorBots(gerador);

        System.out.println("=== POKESAL - FASE 03 ===");
        System.out.println("1 - Batalha 1v1 (Jogador vs Jogador)");
        System.out.println("2 - Torneio Eliminatório (4 ou 8 Treinadores)");
        System.out.print("Escolha uma opção: ");

        int opcao = lerInteiro(scanner, 1, 2, "Escolha 1 ou 2: ");

        if (opcao == 1) {
            Pokemon p1 = new Pokemon("CharSal", 50, 40, Tipos.FOGO, 60, 100);
            Pokemon p2 = new Pokemon("BulbaSal", 40, 50, Tipos.PLANTA, 40, 100);

            Treinador t1 = new Treinador("Treinador 1", p1);
            Treinador t2 = new Treinador("Treinador 2", p2);
            t1.setRegistroBatalha(registro);
            t2.setRegistroBatalha(registro);

            System.out.println("\n--- MODO 1: BATALHA INTERATIVA ---");
            t1.usarItem(Item.POTION);

            Pokemon vencedor = round.executarBatalha(p1, p2);
            if (vencedor == null) System.out.println("\nA batalha terminou empatada após o limite de turnos.");
            else System.out.println("\nVencedor: " + vencedor.getNome());

        } else if (opcao == 2) {
            System.out.print("Quantidade de treinadores (4 ou 8): ");
            int quantidade = lerQuantidadeTorneio(scanner);
            List<Treinador> inscritos = new ArrayList<>();
            for (int i = 1; i <= quantidade; i++) {
                inscritos.add(geradorBots.criarBot("Bot_" + i));
            }

            Torneio torneio = new Torneio(null, estacionamento, round, geradorBots, repositorio, null, null);
            Treinador campeao = torneio.executarTorneio(inscritos);

            System.out.println("\n==========================================");
            System.out.println(" 🏆 CAMPEÃO DO TORNEIO: " + campeao.getNome());
            System.out.println("==========================================");
        } else {
            System.out.println("Opção inválida.");
        }

        scanner.close();
    }

    private static int lerQuantidadeTorneio(Scanner scanner) {
        while (true) {
            String entrada = scanner.nextLine().trim();
            if (entrada.equals("4") || entrada.equals("8")) return Integer.parseInt(entrada);
            System.out.print("Informe 4 ou 8: ");
        }
    }

    private static int lerInteiro(Scanner scanner, int minimo, int maximo, String mensagemErro) {
        while (true) {
            String entrada = scanner.nextLine().trim();
            try {
                int valor = Integer.parseInt(entrada);
                if (valor >= minimo && valor <= maximo) return valor;
            } catch (NumberFormatException ignored) { }
            System.out.print(mensagemErro);
        }
    }
}
