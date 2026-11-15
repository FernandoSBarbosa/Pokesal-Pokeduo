/**
 * Toda aleatoriedade do jogo passa por esta interface (regra 2.1 do PDF).
 * As regras de negócio decidem o que fazer com os números sorteados.
 */
public interface GeradorAleatorio {
    /**
     * Sorteia um inteiro de 0 (inclusive) até o limite (exclusivo).
     *
     * @param limite limite superior, exclusivo
     * @return inteiro sorteado
     */
    int proximoInt(int limite);

    /**
     * Sorteia um número real de 0.0 (inclusive) até 1.0 (exclusivo).
     *
     * @return número sorteado
     */
    double proximoDouble();
}
