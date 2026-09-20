import java.util.ArrayList;
import java.util.List;
public class PokemonDex {
    public static List<Pokemon> getTodos(){
        List<Pokemon> lista = new ArrayList<>();
        lista.add(new Pokemon("CharSal", 52, 43, Tipos.FOGO, 65, 39, Status.NORMAL));
        lista.add(new Pokemon("SquirtSal", 48, 65, Tipos.AGUA, 43, 44, Status.NORMAL));
        lista.add(new Pokemon("Bulbasal", 49, 49, Tipos.PLANTA, 45, 45, Status.NORMAL));
        lista.add(new Pokemon("ChikoSal", 49, 65, Tipos.PLANTA, 45, 45, Status.NORMAL));
        lista.add(new Pokemon("CyndaSal", 55, 40, Tipos.FOGO, 65, 39, Status.NORMAL));
        lista.add(new Pokemon("Totosal", 65, 64, Tipos.AGUA, 43, 50, Status.NORMAL));
        return lista;
    }
}
