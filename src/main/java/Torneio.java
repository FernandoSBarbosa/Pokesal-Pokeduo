import java.util.ArrayList;
import java.util.List;

public class Torneio {
    private final Treinador jogador;
    private final Estacionamento estacionamento;
    private final Round round;
    private final GeradorBots geradorBots;
    private final RepositorioResultados repositorio;
    private final ServicoNotificacao notificacao;
    private final CalculadoraBonus calculadoraBonus;

    public Torneio(Treinador jogador, Estacionamento estacionamento, Round round,
                   GeradorBots geradorBots, RepositorioResultados repositorio,
                   ServicoNotificacao notificacao, CalculadoraBonus calculadoraBonus) {
        this.jogador = jogador;
        this.estacionamento = estacionamento;
        this.round = round;
        this.geradorBots = geradorBots;
        this.repositorio = repositorio;
        this.notificacao = notificacao;
        this.calculadoraBonus = calculadoraBonus;
    }

    public Torneio(Round round, RepositorioResultados repositorio) {
        this(null, null, round, null, repositorio, null, null);
    }

    public Treinador executarBatalha(Treinador t1, Treinador t2) {
        int turnos = round.executarBatalhaComTurnos(t1, t2);
        Treinador vencedor = t1.getPokemon().isVivo() ? t1 : t2;
        Treinador perdedor = (vencedor == t1) ? t2 : t1;

        if (repositorio != null) {
            try {
                ResultadoBatalha resultado = new ResultadoBatalha(
                        vencedor.getNome(),
                        perdedor.getNome(),
                        turnos,
                        vencedor.getPokemon().getNome()
                );
                repositorio.salvarResultado(resultado);
            } catch (Exception e) {
                System.err.println("⚠️ Falha ao salvar no repositório: " + e.getMessage());
            }
        }

        if (vencedor == jogador && t1.getItensUsados() == 0 && notificacao != null) {
            notificacao.notificarConquista(vencedor.getNome(), "Vitória Perfeita sem Itens");
        }

        if (vencedor == jogador && calculadoraBonus != null) {
            int bonus = calculadoraBonus.calcularBonusXp(1, turnos);
            vencedor.getPokemon().setAtk(vencedor.getPokemon().getAtk() + bonus);
        }

        return vencedor;
    }

    public Treinador executarTorneio4Treinadores(List<Treinador> inscritos) {
        return executarTorneio(inscritos);
    }

    public Treinador executarTorneio(List<Treinador> inscritos) {
        if (inscritos == null || (inscritos.size() != 4 && inscritos.size() != 8)) {
            throw new IllegalArgumentException("O torneio deve conter 4 ou 8 treinadores.");
        }

        List<Treinador> rodadaAtual = new ArrayList<>(inscritos);
        int numeroRodada = 1;

        while (rodadaAtual.size() > 1) {
            System.out.println("\n--- ROADA " + numeroRodada + " (" + rodadaAtual.size() + " Participantes) ---");
            List<Treinador> vencedoresRodada = new ArrayList<>();

            for (int i = 0; i < rodadaAtual.size(); i += 2) {
                Treinador t1 = rodadaAtual.get(i);
                Treinador t2 = rodadaAtual.get(i + 1);

                System.out.println("Confronto: " + t1.getNome() + " vs " + t2.getNome());
                Treinador vencedor = executarBatalha(t1, t2);
                System.out.println("Vencedor: " + vencedor.getNome());
                vencedoresRodada.add(vencedor);
            }
            rodadaAtual = vencedoresRodada;
            numeroRodada++;
        }

        return rodadaAtual.get(0);
    }
}