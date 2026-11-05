import java.util.List;

public class Torneio {
    private final Treinador jogador;
    private final Estacionamento estacionamento;
    private final Round gerenciar;
    private final GeradorBots geradorBots;
    private final RepositorioResultados repositorio;
    private final ServicoNotificacao servicoNotificacao;
    private final CalculadoraBonus calculadoraBonus;

    // Construtor antigo (Compatibilidade)
    public Torneio(Treinador jogador, Estacionamento estacionamento, Round gerenciar) {
        this(jogador, estacionamento, gerenciar, new GeradorBots(), null, null, null);
    }

    // Construtor novo com Injeção de Dependências (Fase 03)
    public Torneio(Treinador jogador, Estacionamento estacionamento, Round gerenciar,
                   GeradorBots geradorBots, RepositorioResultados repositorio,
                   ServicoNotificacao servicoNotificacao, CalculadoraBonus calculadoraBonus) {
        this.jogador = jogador;
        this.estacionamento = estacionamento;
        this.gerenciar = gerenciar;
        this.geradorBots = geradorBots;
        this.repositorio = repositorio;
        this.servicoNotificacao = servicoNotificacao;
        this.calculadoraBonus = calculadoraBonus;
    }

    public Treinador executarBatalha(Treinador t1, Treinador t2) {
        int turnos = 0;
        while (t1.getPokemon().getHp() > 0 && t2.getPokemon().getHp() > 0) {
            gerenciar.simularRound(t1.getPokemon(), t2.getPokemon());
            turnos++;
        }

        Treinador vencedor = (t1.getPokemon().getHp() > 0) ? t1 : t2;
        Treinador perdedor = (vencedor == t1) ? t2 : t1;

        if (repositorio != null) {
            try {
                repositorio.salvarResultado(new ResultadoBatalha(
                        vencedor.getNome(), perdedor.getNome(), turnos, vencedor.getPokemon().getNome()
                ));
            } catch (Exception e) {
                System.err.println("Erro ao salvar resultado: " + e.getMessage());
            }
        }

        if (vencedor == jogador && jogador.getItensUsados() == 0 && servicoNotificacao != null) {
            servicoNotificacao.notificarConquista(jogador.getNome(), "Vitória Perfeita sem Itens");
        }

        if (vencedor == jogador && calculadoraBonus != null) {
            int bonus = calculadoraBonus.calcularBonusXp(1, turnos);
            jogador.getPokemon().setAtk(jogador.getPokemon().getAtk() + bonus);
        }

        return vencedor;
    }

    public Treinador executarTorneio4Treinadores(List<Treinador> participantes) {
        if (participantes == null || participantes.size() < 4) {
            throw new IllegalArgumentException("Necessário no mínimo 4 treinadores para o torneio");
        }

        Treinador v1 = executarBatalha(participantes.get(0), participantes.get(1));
        Treinador v2 = executarBatalha(participantes.get(2), participantes.get(3));

        return executarBatalha(v1, v2);
    }
}