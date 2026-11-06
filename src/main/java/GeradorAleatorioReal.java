import java.util.Random;

public class GeradorAleatorioReal implements GeradorAleatorio {
    private final Random random = new Random();

    @Override
    public int proximoInt(int bound) { return random.nextInt(bound); }

    @Override
    public double proximoDouble() { return random.nextDouble(); }

    @Override
    public boolean checarCritico() { return random.nextDouble() < 0.0625; }

    @Override
    public boolean checarAcerto(int precisao) { return (random.nextInt(100) + 1) <= precisao; }

    @Override
    public int gerarNumero(int min, int max) { return random.nextInt((max - min) + 1) + min; }
}