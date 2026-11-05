public class Round {
    private final Estacionamento estacionamento;
    private final GeradorAleatorio gerador;
    private final RegistroBatalha registro;
    private static final double FATOR_DANO = 8;

    // Construtor antigo (Passa null no gerador para evitar acertos críticos aleatórios nos testes legados)
    public Round(Estacionamento estacionamento) {
        this(estacionamento, null, null);
    }

    // Construtor novo com Injeção de Dependências (Fase 03)
    public Round(Estacionamento estacionamento, GeradorAleatorio gerador, RegistroBatalha registro) {
        this.estacionamento = estacionamento;
        this.gerador = gerador;
        this.registro = registro;
    }

    public Pokemon ordem(Pokemon p1, Pokemon p2) {
        return (p1.getVel() >= p2.getVel()) ? p1 : p2;
    }

    public double calcularDano(Pokemon atacante, Pokemon defensor) {
        return calcularDano(atacante, defensor, 100);
    }

    public double calcularDano(Pokemon atacante, Pokemon defensor, int precisao) {
        if (gerador != null && !gerador.checarAcerto(precisao)) {
            return 0.0;
        }

        double multElemental = atacante.getTipo().multiplicadorContra(defensor.getTipo());
        double multTerreno = (estacionamento != null) ? estacionamento.multiplicadorTerreno(atacante.getTipo()) : 1.0;
        double baseDano = ((double) atacante.getAtk() / defensor.getDef()) * FATOR_DANO * multElemental * multTerreno;

        boolean eCritico = (gerador != null) && gerador.checarCritico();
        if (eCritico) {
            baseDano *= 1.5;
        }

        if (registro != null) {
            registro.registrarAtaque(atacante.getNome(), defensor.getNome());
            registro.registrarDano(defensor.getNome(), baseDano, eCritico);
        }

        return baseDano;
    }

    public void executarAtaque(Pokemon atacante, Pokemon defensor) {
        double dano = calcularDano(atacante, defensor, 100);
        defensor.setHp((int) (defensor.getHp() - dano));
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
        }
    }

    public void aplicarStatus(Pokemon pokemon) {
        if (pokemon.getStatus() == Status.QUEIMADO) {
            pokemon.setHp(pokemon.getHp() - 5);
            if (registro != null) registro.registrarEfeitoStatus(pokemon.getNome(), Status.QUEIMADO);
        }
        if (pokemon.getStatus() == Status.ENVENENADO) {
            pokemon.setHp(pokemon.getHp() - 5);
            if (registro != null) registro.registrarEfeitoStatus(pokemon.getNome(), Status.ENVENENADO);
        }
    }
}