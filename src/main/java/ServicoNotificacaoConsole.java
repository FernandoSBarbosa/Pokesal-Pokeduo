/** Mostra as conquistas no console. */
public class ServicoNotificacaoConsole implements ServicoNotificacao {
    @Override
    public void notificarConquista(String treinador, String conquista) {
        System.out.println("*** CONQUISTA de " + treinador + ": " + conquista + " ***");
    }
}
