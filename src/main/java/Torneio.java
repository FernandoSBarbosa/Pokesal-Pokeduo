import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        if (round == null) throw new IllegalArgumentException("Round é obrigatório.");
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
        if (t1 == null || t2 == null) throw new IllegalArgumentException("Dois treinadores são obrigatórios.");
        int turnos = round.executarBatalhaComTurnos(t1, t2);
        boolean vivo1 = t1.getPokemon().isVivo();
        boolean vivo2 = t2.getPokemon().isVivo();
        if (vivo1 == vivo2) return null; // Empate por limite de turnos ou nocaute simultâneo.

        Treinador vencedor = vivo1 ? t1 : t2;
        Treinador perdedor = vivo1 ? t2 : t1;
        if (repositorio != null) {
            try {
                repositorio.salvarResultado(new ResultadoBatalha(vencedor.getNome(), perdedor.getNome(), turnos,
                        vencedor.getPokemon().getNome() + " / " + perdedor.getPokemon().getNome()));
            } catch (Exception e) {
                System.err.println("Falha ao salvar resultado da batalha: " + e.getMessage());
            }
        }
        if (vencedor == jogador && vencedor.getItensUsados() == 0 && notificacao != null)
            notificacao.notificarConquista(vencedor.getNome(), "Vitória Perfeita sem Itens");
        if (vencedor == jogador && calculadoraBonus != null) {
            int bonus = calculadoraBonus.calcularBonusXp(1, turnos);
            vencedor.getPokemon().setAtk(vencedor.getPokemon().getAtk() + Math.max(0, bonus));
        }
        return vencedor;
    }

    public Treinador executarTorneio4Treinadores(List<Treinador> inscritos) { return executarTorneio(inscritos); }

    public Treinador executarTorneio(List<Treinador> inscritos) {
        if (inscritos == null || (inscritos.size() != 4 && inscritos.size() != 8))
            throw new IllegalArgumentException("O torneio deve conter 4 ou 8 treinadores.");
        Set<Treinador> unicos = new HashSet<>(inscritos);
        if (unicos.size() != inscritos.size() || inscritos.stream().anyMatch(t -> t == null || t.getPokemon() == null))
            throw new IllegalArgumentException("Cada treinador deve ser único e possuir um Pokémon.");

        List<Treinador> rodadaAtual = new ArrayList<>(inscritos);
        int numeroRodada = 1;
        while (rodadaAtual.size() > 1) {
            System.out.println("\n--- RODADA " + numeroRodada + " (" + rodadaAtual.size() + " participantes) ---");
            List<Treinador> vencedoresRodada = new ArrayList<>();
            for (int i = 0; i < rodadaAtual.size(); i += 2) {
                Treinador t1 = rodadaAtual.get(i);
                Treinador t2 = rodadaAtual.get(i + 1);
                System.out.println("Confronto: " + t1.getNome() + " vs " + t2.getNome());
                Treinador vencedor = executarBatalha(t1, t2);
                if (vencedor == null) {
                    throw new IllegalStateException("A batalha terminou empatada; é necessário desempatar para continuar o torneio.");
                }
                System.out.println("Vencedor: " + vencedor.getNome());
                vencedoresRodada.add(vencedor);
            }
            rodadaAtual = vencedoresRodada;
            numeroRodada++;
        }
        return rodadaAtual.get(0);
    }
}
