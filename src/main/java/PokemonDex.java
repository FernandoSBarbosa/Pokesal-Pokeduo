import java.util.ArrayList;
import java.util.List;

public class PokemonDex {
    private static final Golpe GOLPE_FOLHA_PRECISO = new Golpe("Golpe de Folha", 95);
    private static final Golpe GOLPE_FOLHA = new Golpe("Golpe de Folha", 90);
    private static final Golpe GOLPE_CHAMA_PRECISO = new Golpe("Golpe de Chama", 95);
    private static final Golpe GOLPE_CHAMA = new Golpe("Golpe de Chama", 90);
    private static final Golpe GOLPE_AGUA_PRECISO = new Golpe("Golpe de Água", 100);
    private static final Golpe GOLPE_AGUA = new Golpe("Golpe de Água", 90);

    private PokemonDex() {
    }

    /**
     * Cria a lista com os seis Pokésal iniciais (objetos novos a cada chamada).
     *
     * @return lista com os seis Pokésal base
     */
    public static List<Pokemon> getTodos() {
        List<Pokemon> lista = new ArrayList<>();
        lista.add(new Pokemon("Bulbasal", 49, 49, Tipos.PLANTA, 45, 45, Status.NORMAL, GOLPE_FOLHA_PRECISO));
        lista.add(new Pokemon("CharSal", 52, 43, Tipos.FOGO, 65, 39, Status.NORMAL, GOLPE_CHAMA));
        lista.add(new Pokemon("SquirtSal", 48, 65, Tipos.AGUA, 43, 44, Status.NORMAL, GOLPE_AGUA_PRECISO));
        lista.add(new Pokemon("ChikoSal", 49, 65, Tipos.PLANTA, 45, 45, Status.NORMAL, GOLPE_FOLHA));
        lista.add(new Pokemon("CyndaSal", 55, 40, Tipos.FOGO, 65, 39, Status.NORMAL, GOLPE_CHAMA_PRECISO));
        lista.add(new Pokemon("Totosal", 65, 64, Tipos.AGUA, 43, 50, Status.NORMAL, GOLPE_AGUA));
        return lista;
    }
}
