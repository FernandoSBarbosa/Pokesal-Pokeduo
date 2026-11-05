public interface GeradorAleatorio {
    int proximoInt(int bound);
    double proximoDouble();
    boolean checarCritico();
    boolean checarAcerto(int precisao);
}