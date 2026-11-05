public class ResultadoBatalha {
    private final String vencedor;
    private final String perdedor;
    private final int turnos;
    private final String pokesalUtilizado;

    public ResultadoBatalha(String vencedor, String perdedor, int turnos, String pokesalUtilizado) {
        this.vencedor = vencedor;
        this.perdedor = perdedor;
        this.turnos = turnos;
        this.pokesalUtilizado = pokesalUtilizado;
    }

    public String getVencedor() { return vencedor; }
    public String getPerdedor() { return perdedor; }
    public int getTurnos() { return turnos; }
    public String getPokesalUtilizado() { return pokesalUtilizado; }
}