import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Torneio eliminatório de 4 ou 8 treinadores. */
public class Torneio {
    public static final String CONQUISTA_VITORIA_PERFEITA = "Vitória perfeita (sem usar itens)";

    private static final Logger LOGGER = Logger.getLogger(Torneio.class.getName());
    private static final int MINIMO_PARTICIPANTES = 4;
    private static final int MAXIMO_PARTICIPANTES = 8;

    private final Treinador jogador;
    private final Round round;
    private final RepositorioResultados repositorio;
    private final ServicoNotificacao notificacao;
    private final CalculadoraBonus calculadoraBonus;

    /**
     * Cria o torneio.
     *
     * @param jogador          treinador controlado pelo jogador (pode ser null se só houver bots)
     * @param round            quem executa as batalhas
     * @param repositorio      onde os resultados são salvos
     * @param notificacao      avisa as conquistas do jogador
     * @param calculadoraBonus calcula o bônus de ataque do jogador
     */
    public Torneio(Treinador jogador, Round round, RepositorioResultados repositorio,
                   ServicoNotificacao notificacao, CalculadoraBonus calculadoraBonus) {
        this.jogador = jogador;
        this.round = round;
        this.repositorio = repositorio;
        this.notificacao = notificacao;
        this.calculadoraBonus = calculadoraBonus;
    }

    /**
     * Disputa o torneio: os vencedores de cada par avançam até sobrar o campeão.
     *
     * @param inscritos 4 ou 8 treinadores (os pares seguem a ordem da lista)
     * @return campeão do torneio
     */
    public Treinador executarTorneio(List<Treinador> inscritos) {
        int total = inscritos.size();
        if (total != MINIMO_PARTICIPANTES && total != MAXIMO_PARTICIPANTES) {
            throw new IllegalArgumentException("O torneio deve ter 4 ou 8 treinadores");
        }
        List<Treinador> fase = new ArrayList<>(inscritos);
        while (fase.size() > 1) {
            List<Treinador> vencedores = new ArrayList<>();
            for (int i = 0; i < fase.size(); i += 2) {
                vencedores.add(executarBatalha(fase.get(i), fase.get(i + 1)));
            }
            fase = vencedores;
        }
        return fase.get(0);
    }

    /**
     * Luta uma batalha, salva o resultado e premia o jogador se ele venceu.
     * Se o repositório falhar, o erro é registrado e a batalha continua válida.
     *
     * @param t1 primeiro treinador
     * @param t2 segundo treinador
     * @return vencedor da batalha
     */
    public Treinador executarBatalha(Treinador t1, Treinador t2) {
        int turnos = round.executarBatalha(t1, t2);
        Treinador vencedor = definirVencedor(t1, t2);
        Treinador perdedor = vencedor == t1 ? t2 : t1;

        salvar(new ResultadoBatalha(vencedor.getNome(), perdedor.getNome(), turnos,
                vencedor.getPokemon().getNome(), perdedor.getPokemon().getNome()));

        if (vencedor == jogador) {
            premiarJogador(turnos);
        }
        return vencedor;
    }

    // Quem ficou de pé vence; se acabou o limite de turnos, vence quem tem mais HP
    // (empate: o primeiro treinador).
    private Treinador definirVencedor(Treinador t1, Treinador t2) {
        return t2.getPokemon().getHp() > t1.getPokemon().getHp() ? t2 : t1;
    }

    private void salvar(ResultadoBatalha resultado) {
        try {
            repositorio.salvarResultado(resultado);
        } catch (RuntimeException e) {
            LOGGER.log(Level.WARNING, "Falha ao salvar o resultado da batalha", e);
        }
    }

    // Requisito autoral 1: conquista por vitória sem itens (ServicoNotificacao).
    // Requisito autoral 2: bônus de ataque por vitória (CalculadoraBonus).
    private void premiarJogador(int turnos) {
        if (jogador.getItensUsados() == 0) {
            notificacao.notificarConquista(jogador.getNome(), CONQUISTA_VITORIA_PERFEITA);
        }
        int bonus = calculadoraBonus.calcularBonusAtaque(turnos);
        Pokemon pokemon = jogador.getPokemon();
        pokemon.fortalecer();
        pokemon.setAtk(pokemon.getAtk() + bonus);
    }
}
