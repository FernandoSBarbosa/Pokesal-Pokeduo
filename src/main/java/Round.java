public class Round {
    private Estacionamento estacionamento;

    public Round(Estacionamento estacionamento) {
        this.estacionamento = estacionamento;
    }

    public Pokemon ordem(Pokemon p1, Pokemon p2) {
        if (p1.getVel() > p2.getVel()) {
            return p1;
        } else if (p2.getVel() > p1.getVel()) {
            return p2;
        } else  {
            return p1;
        }
    }
    public double calcularDano(Pokemon p1, Pokemon p2) {
        double multElemental = p1.getTipo().multiplicadorContra(p2.getTipo());
        return ((double) p1.getAtk() / p2.getDef()) * multElemental;
    }
    public void aplicarDano(Pokemon defensor, double dano) {
        defensor.setHp((int) (defensor.getHp() - dano));
    }
    public void executarAtaque(Pokemon p1, Pokemon p2) {
        double dano = calcularDano(p1, p2);
        aplicarDano(p2, dano);
    }
    public void simularRound(Pokemon p1, Pokemon p2) {
        Pokemon primeiro = ordem(p1, p2);
        Pokemon segundo = primeiro = (primeiro == p1) ?  p2 : p1;
        executarAtaque(primeiro, segundo);
        if (segundo.getHp() > 0) {
            executarAtaque(segundo, primeiro);
        }
        if (estacionamento != null) {
            estacionamento.curaCanteiro(p1);
            estacionamento.curaCanteiro(p2);
            System.out.println("Fim do round! HP do " + p1.getNome() + ": " + p1.getHp()
                    + " | HP do " + p2.getNome() + ": " + p2.getHp());
        }
    }
}