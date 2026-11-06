public class Round {
    private final Estacionamento estacionamento;
    private final GeradorAleatorio gerador;
    private final RegistroBatalha registro;

    public Round(Estacionamento estacionamento, GeradorAleatorio gerador, RegistroBatalha registro) {
        this.estacionamento = estacionamento;
        this.gerador = gerador;
        this.registro = registro;
    }

    public Round(Estacionamento estacionamento) {
        this(estacionamento, null, null);
    }

    public Pokemon ordem(Pokemon p1, Pokemon p2) {
        if (p1.getVel() > p2.getVel()) return p1;
        if (p2.getVel() > p1.getVel()) return p2;
        return (gerador != null && gerador.proximoDouble() < 0.5) ? p1 : p2;
    }

    public void aplicarStatus(Pokemon p) {
        if (p == null || p.getStatus() == Status.NORMAL) return;

        switch (p.getStatus()) {
            case QUEIMADO:
                p.setAtk((int) (p.getAtk() * 0.5)); // Redução de 50% no ATK
                p.setHp(p.getHp() - 5);
                break;
            case ENVENENADO:
                p.setHp(p.getHp() - 5); // Dano contínuo
                break;
            case PARALISADO:
                p.setVel((int) (p.getVel() * 0.5)); // Redução de 50% na VEL
                break;
        }

        if (registro != null) {
            registro.registrarEfeitoStatus(p.getNome(), p.getStatus());
        }
    }

    public double calcularDano(Pokemon atacante, Pokemon defensor, int precisao) {
        if (registro != null) {
            registro.registrarAtaque(atacante.getNome(), defensor.getNome());
        }

        boolean acertou = (gerador == null) || gerador.checarAcerto(precisao);

        if (!acertou) {
            if (registro != null) {
                registro.registrarDano(defensor.getNome(), 0.0, false);
            }
            return 0.0;
        }

        if (defensor.getDef() == 0) {
            if (registro != null) {
                registro.registrarDano(defensor.getNome(), Double.POSITIVE_INFINITY, false);
            }
            return Double.POSITIVE_INFINITY;
        }

        double modVantagem = atacante.getTipo().multiplicadorContra(defensor.getTipo());
        double modTerreno = (estacionamento != null) ? estacionamento.multiplicadorTerreno(atacante.getTipo()) : 1.0;
        boolean critico = (gerador != null) && gerador.checarCritico();
        double modCritico = critico ? 1.5 : 1.0;

        double dano = ((double) atacante.getAtk() / defensor.getDef()) * 10.0 * modVantagem * modTerreno * modCritico;

        if (registro != null) {
            registro.registrarDano(defensor.getNome(), dano, critico);
        }

        return dano;
    }

    public double calcularDano(Pokemon atacante, Pokemon defensor) {
        return calcularDano(atacante, defensor, atacante.getPrecisao());
    }

    public void simularRound(Pokemon p1, Pokemon p2) {
        aplicarStatus(p1);
        aplicarStatus(p2);

        if (estacionamento != null) {
            estacionamento.curaCanteiro(p1);
            estacionamento.curaCanteiro(p2);
            if (registro != null) {
                registro.registrarEfeitoTerreno(p1.getNome(), estacionamento.getTerrenoAtual());
                registro.registrarEfeitoTerreno(p2.getNome(), estacionamento.getTerrenoAtual());
            }
        }

        Pokemon primeiro = ordem(p1, p2);
        Pokemon segundo = (primeiro == p1) ? p2 : p1;

        if (primeiro.isVivo() && segundo.isVivo()) {
            double dano1 = calcularDano(primeiro, segundo);
            if (dano1 != Double.POSITIVE_INFINITY) {
                segundo.setHp(segundo.getHp() - (int) dano1);
            } else {
                segundo.setHp(0);
            }
        }

        if (segundo.isVivo() && primeiro.isVivo()) {
            double dano2 = calcularDano(segundo, primeiro);
            if (dano2 != Double.POSITIVE_INFINITY) {
                primeiro.setHp(primeiro.getHp() - (int) dano2);
            } else {
                primeiro.setHp(0);
            }
        }
    }

    public Pokemon executarBatalha(Pokemon p1, Pokemon p2) {
        int turnos = 0;
        final int MAX_TURNOS = 50;

        while (p1.isVivo() && p2.isVivo() && turnos < MAX_TURNOS) {
            simularRound(p1, p2);
            turnos++;
        }

        return p1.isVivo() ? p1 : p2;
    }

    public int executarBatalhaComTurnos(Treinador t1, Treinador t2) {
        Pokemon p1 = t1.getPokemon();
        Pokemon p2 = t2.getPokemon();
        int turnos = 0;
        final int MAX_TURNOS = 50;

        while (p1.isVivo() && p2.isVivo() && turnos < MAX_TURNOS) {
            simularRound(p1, p2);
            turnos++;
        }

        return turnos;
    }
}