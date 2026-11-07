import java.util.ArrayList;
import java.util.List;

public class Round {
    private final Estacionamento estacionamento;
    private final GeradorAleatorio gerador;
    private final RegistroBatalha registro;

    public Round(Estacionamento estacionamento, GeradorAleatorio gerador, RegistroBatalha registro) {
        this.estacionamento = estacionamento;
        this.gerador = gerador;
        this.registro = registro;
    }

    public Round(Estacionamento estacionamento) { this(estacionamento, null, null); }

    public Pokemon ordem(Pokemon p1, Pokemon p2) {
        if (p1.getVel() > p2.getVel()) return p1;
        if (p2.getVel() > p1.getVel()) return p2;
        return gerador != null && gerador.proximoDouble() < 0.5 ? p1 : p2;
    }

    public void aplicarStatus(Pokemon p) {
        aplicarStatus(p, null);
    }

    private void aplicarStatus(Pokemon p, List<Runnable> eventosAdiados) {
        if (p == null || !p.isVivo() || p.getStatus() == Status.NORMAL) return;
        switch (p.getStatus()) {
            case QUEIMADO -> p.setHp(p.getHp() - 5);
            case ENVENENADO -> p.setHp(p.getHp() - 5);
            case PARALISADO -> { /* A velocidade é reduzida uma única vez ao aplicar o status. */ }
            default -> { }
        }
        if (registro != null) {
            Runnable evento = () -> registro.registrarEfeitoStatus(p.getNome(), p.getStatus());
            if (eventosAdiados == null) evento.run();
            else eventosAdiados.add(evento);
        }
    }

    public double calcularDano(Pokemon atacante, Pokemon defensor, int precisao) {
        if (atacante == null || defensor == null) throw new IllegalArgumentException("Pokémon atacante e defensor são obrigatórios.");
        if (precisao < 0 || precisao > 100) throw new IllegalArgumentException("Precisão deve estar entre 0 e 100.");
        if (registro != null) registro.registrarAtaque(atacante.getNome(), defensor.getNome());

        boolean acertou = gerador == null || gerador.checarAcerto(precisao);
        if (!acertou) {
            if (registro != null) registro.registrarDano(defensor.getNome(), 0.0, false);
            return 0.0;
        }
        if (defensor.getDef() == 0) {
            if (registro != null) registro.registrarDano(defensor.getNome(), Double.POSITIVE_INFINITY, false);
            return Double.POSITIVE_INFINITY;
        }

        double modVantagem = atacante.getTipo().multiplicadorContra(defensor.getTipo());
        double modTerreno = estacionamento == null ? 1.0 : estacionamento.multiplicadorTerreno(atacante.getTipo());
        boolean critico = gerador != null && gerador.checarCritico();
        double dano = ((double) atacante.getAtk() / defensor.getDef()) * 10.0 * modVantagem * modTerreno * (critico ? 1.5 : 1.0);
        if (registro != null) registro.registrarDano(defensor.getNome(), dano, critico);
        return dano;
    }

    public double calcularDano(Pokemon atacante, Pokemon defensor) {
        return calcularDano(atacante, defensor, atacante.getPrecisao());
    }

    private void atacar(Pokemon atacante, Pokemon defensor) {
        if (!atacante.isVivo() || !defensor.isVivo()) return;
        double dano = calcularDano(atacante, defensor);
        if (dano == Double.POSITIVE_INFINITY) defensor.setHp(0);
        else defensor.setHp(defensor.getHp() - (int) dano);
    }

    public void simularRound(Pokemon p1, Pokemon p2) {
        if (p1 == null || p2 == null) throw new IllegalArgumentException("Dois Pokémon são obrigatórios.");
        List<Runnable> eventosAdiados = new ArrayList<>();
        aplicarStatus(p1, eventosAdiados);
        aplicarStatus(p2, eventosAdiados);

        if (estacionamento != null) {
            estacionamento.curaCanteiro(p1);
            estacionamento.curaCanteiro(p2);
            if (registro != null) {
                TiposTerreno terreno = estacionamento.getTerrenoAtual();
                eventosAdiados.add(() -> registro.registrarEfeitoTerreno(p1.getNome(), terreno));
                eventosAdiados.add(() -> registro.registrarEfeitoTerreno(p2.getNome(), terreno));
            }
        }
        Pokemon primeiro = ordem(p1, p2);
        Pokemon segundo = primeiro == p1 ? p2 : p1;
        atacar(primeiro, segundo);
        atacar(segundo, primeiro);
        eventosAdiados.forEach(Runnable::run);
    }

    public Pokemon executarBatalha(Pokemon p1, Pokemon p2) {
        executarBatalhaComTurnos(p1, p2);
        if (p1.isVivo() == p2.isVivo()) return null;
        return p1.isVivo() ? p1 : p2;
    }

    public int executarBatalhaComTurnos(Treinador t1, Treinador t2) {
        if (t1 == null || t2 == null) throw new IllegalArgumentException("Dois treinadores são obrigatórios.");
        return executarBatalhaComTurnos(t1.getPokemon(), t2.getPokemon());
    }

    private int executarBatalhaComTurnos(Pokemon p1, Pokemon p2) {
        int turnos = 0;
        while (p1.isVivo() && p2.isVivo() && turnos < 50) {
            simularRound(p1, p2);
            turnos++;
        }
        return turnos;
    }
}
