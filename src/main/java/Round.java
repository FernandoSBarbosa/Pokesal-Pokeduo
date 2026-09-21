public class Round {
    private Estacionamento estacionamento;
    private static final double FATOR_DANO = 8;

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
        double multTerreno = estacionamento.multiplicadorTerreno(p1.getTipo());

        return ((double) p1.getAtk() / p2.getDef())
                *FATOR_DANO
                * multElemental
                * multTerreno;
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
        Pokemon segundo = (primeiro == p1) ? p2 : p1;
        executarAtaque(primeiro, segundo);
        if (segundo.getHp() > 0) {
            executarAtaque(segundo, primeiro);
        }
        if (estacionamento != null) {
            estacionamento.curaCanteiro(p1);
            estacionamento.curaCanteiro(p2);
            aplicarStatus(p1);
            aplicarStatus(p2);
            System.out.println("Fim do round! HP do " + p1.getNome() + ": " + p1.getHp()
                    + " | HP do " + p2.getNome() + ": " + p2.getHp());

        }
    }

    public void aplicarStatus(Pokemon pokemon){
        if (pokemon.getStatus() == Status.QUEIMADO) {

            int dano = 5;

            pokemon.setHp(pokemon.getHp() - dano);

            pokemon.setAtk(pokemon.getAtk() - 5);
        }

        if (pokemon.getStatus() == Status.ENVENENADO) {

            int dano = 5;

            pokemon.setHp(pokemon.getHp() - dano);
        }

        if (pokemon.getStatus() == Status.PARALISADO) {

            pokemon.setVel(pokemon.getVel() - 5);
        }
    }
}
