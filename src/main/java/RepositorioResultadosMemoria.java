import java.util.ArrayList;
import java.util.List;

/** Repositório simples que guarda os resultados em memória. */
public class RepositorioResultadosMemoria implements RepositorioResultados {
    private final List<ResultadoBatalha> resultados = new ArrayList<>();

    @Override
    public void salvarResultado(ResultadoBatalha resultado) {
        resultados.add(resultado);
    }

    public List<ResultadoBatalha> listar() {
        return List.copyOf(resultados);
    }
}
