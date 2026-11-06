import java.util.ArrayList;
import java.util.List;

public class Torneio {
    private final Round round;
    private final RepositorioResultados repositorio;

    public Torneio(Round round, RepositorioResultados repositorio) {
        if (round == null || repositorio == null) {
            throw new IllegalArgumentException("Dependências não podem ser nulas.");
        }
        this.round = round;
        this.repositorio = repositorio;
    }

    public Treinador executarTorneio(List<Treinador> inscritos) {
        if (inscritos == null || (inscritos.size() != 4 && inscritos.size() != 8)) {
            throw new IllegalArgumentException("O torneio deve ter exatamente 4 ou 8 treinadores.");
        }

        List<Treinador> participantesAtuais = new ArrayList<>(inscritos);

        while (participantesAtuais.size() > 1) {
            List<Treinador> vencedoresDaRodada = new ArrayList<>();

            // Iteração correta em pares para 4 ou 8 participantes
            for (int i = 0; i < participantesAtuais.size(); i += 2) {
                Treinador t1 = participantesAtuais.get(i);
                Treinador t2 = participantesAtuais.get(i + 1);

                Pokemon vencedorPokemon = round.executarBatalha(t1.getPokemon(), t2.getPokemon());
                Treinador vencedor = (vencedorPokemon == t1.getPokemon()) ? t1 : t2;
                vencedoresDaRodada.add(vencedor);
            }

            participantesAtuais = vencedoresDaRodada;
        }

        Treinador campeao = participantesAtuais.get(0);
        repositorio.salvarResultado("Campeão do Torneio: " + campeao.getNome());
        return campeao;
    }
}