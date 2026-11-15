import java.util.Random;

/** Implementação real do gerador: é a única classe do projeto que usa Random. */
public class GeradorAleatorioReal implements GeradorAleatorio {
    private final Random random = new Random();

    @Override
    public int proximoInt(int limite) {
        return random.nextInt(limite);
    }

    @Override
    public double proximoDouble() {
        return random.nextDouble();
    }
}
