import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BatalhaPokemon {
    private static final int MODO_BATALHA = 1;
    private static final int MODO_TORNEIO = 2;
    private static final int ACAO_ATACAR = 1;
    private static final int ACAO_ITEM = 2;
    private static final int TORNEIO_PEQUENO = 4;
    private static final int TORNEIO_GRANDE = 8;
    private static final int CHANCE_CHUVA = 4;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Único lugar onde os colaboradores são criados (injeção de dependências).
        GeradorAleatorio gerador = new GeradorAleatorioReal();
        boolean chovendo = gerador.proximoInt(CHANCE_CHUVA) == 0;
        ServicoTerreno servicoTerreno = new ServicoTerrenoPadrao(Clock.systemDefaultZone(), chovendo);
        Estacionamento estacionamento = new Estacionamento(servicoTerreno);
        Round round = new Round(estacionamento, gerador, new RegistroBatalhaConsole());

        System.out.println("=== POKESAL - FASE 03 ===");
        System.out.println("Terreno de hoje: " + estacionamento.getTerrenoAtual());
        System.out.println("1 - Batalha entre dois jogadores");
        System.out.println("2 - Torneio eliminatório (4 ou 8 treinadores)");
        int modo = lerInteiro(sc, "Escolha o modo: ", MODO_BATALHA, MODO_TORNEIO);

        if (modo == MODO_BATALHA) {
            batalhaEntreJogadores(sc, round);
        } else {
            torneio(sc, round, new GeradorBots(gerador));
        }
        sc.close();
    }

    private static void batalhaEntreJogadores(Scanner sc, Round round) {
        Treinador t1 = criarTreinador(sc, "Primeiro treinador");
        Treinador t2 = criarTreinador(sc, "Segundo treinador");

        int turno = 1;
        while (t1.getPokemon().isVivo() && t2.getPokemon().isVivo()) {
            System.out.println("\n===== TURNO " + turno + " =====");
            mostrarHp(t1);
            mostrarHp(t2);
            Item item1 = escolherAcao(sc, t1);
            Item item2 = escolherAcao(sc, t2);
            round.executarTurno(t1, item1, t2, item2);
            turno++;
        }

        System.out.println("\n=== FIM DA BATALHA ===");
        if (t1.getPokemon().isVivo()) {
            System.out.println("Vencedor: " + t1.getNome());
        } else if (t2.getPokemon().isVivo()) {
            System.out.println("Vencedor: " + t2.getNome());
        } else {
            System.out.println("Empate: os dois Pokésal caíram no mesmo turno.");
        }
    }

    private static void torneio(Scanner sc, Round round, GeradorBots geradorBots) {
        Treinador jogador = criarTreinador(sc, "Treinador");
        int total = 0;
        while (total != TORNEIO_PEQUENO && total != TORNEIO_GRANDE) {
            total = lerInteiro(sc, "Quantidade de treinadores (4 ou 8): ", TORNEIO_PEQUENO, TORNEIO_GRANDE);
        }

        List<Treinador> inscritos = new ArrayList<>();
        inscritos.add(jogador);
        for (int nivel = 1; nivel < total; nivel++) {
            inscritos.add(geradorBots.gerarBot(nivel));
        }

        RepositorioResultadosMemoria repositorio = new RepositorioResultadosMemoria();
        Torneio torneio = new Torneio(jogador, round, repositorio,
                new ServicoNotificacaoConsole(), new CalculadoraBonusPadrao());
        Treinador campeao = torneio.executarTorneio(inscritos);

        System.out.println("\n=== RESULTADOS DAS BATALHAS ===");
        for (ResultadoBatalha r : repositorio.listar()) {
            System.out.println(r.getVencedor() + " (" + r.getPokesalVencedor() + ") venceu "
                    + r.getPerdedor() + " (" + r.getPokesalPerdedor() + ") em " + r.getTurnos() + " turnos");
        }
        System.out.println("\nCAMPEÃO DO TORNEIO: " + campeao.getNome());
    }

    private static Treinador criarTreinador(Scanner sc, String rotulo) {
        System.out.print(rotulo + ", informe seu nome: ");
        String nome = sc.nextLine().trim();
        return new Treinador(nome, escolherPokemon(sc, nome));
    }

    private static Pokemon escolherPokemon(Scanner sc, String nome) {
        List<Pokemon> pokesal = PokemonDex.getTodos();
        System.out.println(nome + ", escolha seu pokesal:");
        for (int i = 0; i < pokesal.size(); i++) {
            System.out.println((i + 1) + " - " + pokesal.get(i).getNome() + " (" + pokesal.get(i).getTipo() + ")");
        }
        int escolha = lerInteiro(sc, "Opção: ", 1, pokesal.size());
        return pokesal.get(escolha - 1);
    }

    // Retorna o item escolhido, ou null se o treinador decidiu atacar.
    private static Item escolherAcao(Scanner sc, Treinador treinador) {
        System.out.println("\nAção de " + treinador.getNome() + ": 1 - Atacar | 2 - Usar item");
        int acao = lerInteiro(sc, "Opção: ", ACAO_ATACAR, ACAO_ITEM);
        if (acao == ACAO_ATACAR) {
            return null;
        }
        if (!treinador.podeUsarItem()) {
            System.out.println("Você já usou os 2 itens permitidos. Vai atacar!");
            return null;
        }
        System.out.println("1 - Poção | 2 - Super poção | 3 - Antídoto");
        int item = lerInteiro(sc, "Item: ", 1, Item.values().length);
        return Item.values()[item - 1];
    }

    private static void mostrarHp(Treinador t) {
        System.out.println(t.getNome() + " - " + t.getPokemon().getNome()
                + " HP: " + t.getPokemon().getHp() + "/" + t.getPokemon().getMaxHP());
    }

    private static int lerInteiro(Scanner sc, String mensagem, int minimo, int maximo) {
        while (true) {
            System.out.print(mensagem);
            String entrada = sc.nextLine().trim();
            try {
                int valor = Integer.parseInt(entrada);
                if (valor >= minimo && valor <= maximo) {
                    return valor;
                }
            } catch (NumberFormatException e) {
                // cai na mensagem abaixo
            }
            System.out.println("Valor inválido. Digite um número de " + minimo + " a " + maximo + ".");
        }
    }
}
