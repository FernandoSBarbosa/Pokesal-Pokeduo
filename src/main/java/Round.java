public class Round {
    private final ServicoTerreno servicoTerreno;
    private final GeradorAleatorio gerador;
    private final RegistroBatalha registro;

    public Round(ServicoTerreno servicoTerreno, GeradorAleatorio gerador, RegistroBatalha registro) {
        if (servicoTerreno == null || gerador == null || registro == null) {
            throw new IllegalArgumentException("Todas as dependências da classe Round são obrigatórias.");
        }
        this.servicoTerreno = servicoTerreno;
        this.gerador = gerador;
        this.registro = registro;
    }

    public void aplicarStatus(Pokemon p) {
        if (p == null || p.getStatus() == Status.NORMAL) return;

        switch (p.getStatus()) {
            case QUEIMADO:
                p.setAtaque((int) (p.getAtaque() * 0.5)); // Redução de 50% no ATK
                p.setHp(p.getHp() - 5);
                break;
            case ENVENENADO:
                p.setHp(p.getHp() - 5); // Dano contínuo
                break;
            case PARALISADO:
                p.setVelocidade((int) (p.getVelocidade() * 0.5)); // Redução de 50% na VEL
                break;
        }
    }

    public double calcularDano(Pokemon atacante, Pokemon defensor) {
        int sorteioAcerto = gerador.gerarNumero(1, 100);
        boolean acertou = sorteioAcerto <= atacante.getPrecisao();

        // Registra a tentativa no histórico MESMO SE ERRAR
        registro.registrarAtaque(atacante, defensor, acertou);

        if (!acertou) {
            return 0.0;
        }

        double danoBase = Math.max(1.0, atacante.getAtaque() - defensor.getDefesa());
        double modTerreno = servicoTerreno.obterModificadorTerreno();

        boolean critico = gerador.gerarNumero(1, 100) <= 10;
        double modCritico = critico ? 1.5 : 1.0;

        return danoBase * modTerreno * modCritico;
    }

    public void simularRound(Pokemon p1, Pokemon p2) {
        aplicarStatus(p1);
        aplicarStatus(p2);

        if (p1.isVivo() && p2.isVivo()) {
            double dano1 = calcularDano(p1, p2);
            p2.setHp(p2.getHp() - (int) dano1);
        }

        if (p2.isVivo() && p1.isVivo()) {
            double dano2 = calcularDano(p2, p1);
            p1.setHp(p1.getHp() - (int) dano2);
        }
    }

    public Pokemon executarBatalha(Pokemon p1, Pokemon p2) {
        int turnos = 0;
        final int MAX_TURNOS = 100; // Limite para evitar travamentos

        while (p1.isVivo() && p2.isVivo() && turnos < MAX_TURNOS) {
            simularRound(p1, p2);
            turnos++;
        }

        if (turnos >= MAX_TURNOS && p1.isVivo() && p2.isVivo()) {
            System.out.println("⚠️ Batalha encerrada por limite de turnos (Empate técnico).");
        }

        return p1.isVivo() ? p1 : p2;
    }
}