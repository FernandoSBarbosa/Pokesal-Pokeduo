public class Round {
    private static final double FATOR_DANO = 8;
    private static final double CHANCE_CRITICO = 0.0625;
    private static final double MULTIPLICADOR_CRITICO = 1.5;
    private static final int ESCALA_PRECISAO = 100;
    private static final int DANO_STATUS = 5;
    private static final int REDUCAO_STATUS = 5;
    private static final int VALOR_MINIMO = 1;
    private static final int LIMITE_TURNOS = 50;

    private final Estacionamento estacionamento;
    private final GeradorAleatorio gerador;
    private final RegistroBatalha registro;

    public Round(Estacionamento estacionamento, GeradorAleatorio gerador, RegistroBatalha registro) {
        this.estacionamento = estacionamento;
        this.gerador = gerador;
        this.registro = registro;
    }

    /**
     * Define quem ataca primeiro: o mais rápido. Em caso de empate, o primeiro informado.
     *
     * @param p1 primeiro Pokémon
     * @param p2 segundo Pokémon
     * @return Pokémon que ataca primeiro
     */
    public Pokemon ordem(Pokemon p1, Pokemon p2) {
        return p2.getVel() > p1.getVel() ? p2 : p1;
    }

    /**
     * Calcula o dano base (atributos, vantagem elemental e terreno), sem sorteios.
     *
     * @param atacante Pokémon que ataca
     * @param defensor Pokémon que recebe o golpe
     * @return dano calculado
     */
    public double calcularDano(Pokemon atacante, Pokemon defensor) {
        double multElemental = atacante.getTipo().multiplicadorContra(defensor.getTipo());
        double multTerreno = estacionamento.multiplicadorTerreno(atacante.getTipo());
        return ((double) atacante.getAtk() / defensor.getDef())
                * FATOR_DANO
                * multElemental
                * multTerreno;
    }

    /**
     * Executa um ataque: o golpe pode errar (dano zero) ou ser crítico (dano x1.5).
     *
     * @param atacante Pokémon que ataca
     * @param defensor Pokémon que recebe o golpe
     * @return dano causado
     */
    public double executarAtaque(Pokemon atacante, Pokemon defensor) {
        registro.registrarAtaque(atacante.getNome(), defensor.getNome());
        double dano = 0.0;
        if (gerador.proximoInt(ESCALA_PRECISAO) < atacante.getGolpe().precisao()) {
            dano = calcularDano(atacante, defensor);
            if (gerador.proximoDouble() < CHANCE_CRITICO) {
                dano *= MULTIPLICADOR_CRITICO;
            }
        }
        defensor.setHp((int) (defensor.getHp() - dano));
        registro.registrarDano(defensor.getNome(), dano);
        return dano;
    }

    /**
     * Turno em que os dois treinadores só atacam.
     *
     * @param t1 primeiro treinador
     * @param t2 segundo treinador
     */
    public void executarTurno(Treinador t1, Treinador t2) {
        executarTurno(t1, null, t2, null);
    }

    /**
     * Executa um turno completo. Quem usa item não ataca nesse turno. Os eventos são
     * registrados na ordem: ataque, dano, status, terreno e item (itens valem no fim do turno).
     *
     * @param t1    primeiro treinador
     * @param item1 item do primeiro treinador, ou null para atacar
     * @param t2    segundo treinador
     * @param item2 item do segundo treinador, ou null para atacar
     */
    public void executarTurno(Treinador t1, Item item1, Treinador t2, Item item2) {
        Pokemon p1 = t1.getPokemon();
        Pokemon p2 = t2.getPokemon();

        if (ordem(p1, p2) == p1) {
            atacar(item1 == null, p1, p2);
            atacar(item2 == null, p2, p1);
        } else {
            atacar(item2 == null, p2, p1);
            atacar(item1 == null, p1, p2);
        }

        aplicarStatus(p1);
        aplicarStatus(p2);
        aplicarTerreno(p1);
        aplicarTerreno(p2);
        usarItem(t1, item1);
        usarItem(t2, item2);
    }

    /**
     * Luta até um Pokémon cair ou até o limite de turnos.
     *
     * @param t1 primeiro treinador
     * @param t2 segundo treinador
     * @return quantidade de turnos disputados
     */
    public int executarBatalha(Treinador t1, Treinador t2) {
        int turnos = 0;
        while (t1.getPokemon().isVivo() && t2.getPokemon().isVivo() && turnos < LIMITE_TURNOS) {
            executarTurno(t1, t2);
            turnos++;
        }
        return turnos;
    }

    /**
     * Aplica o efeito do status no fim do turno: queimado perde HP e ATK, envenenado perde
     * HP e paralisado perde VEL (atributos nunca ficam abaixo de 1).
     *
     * @param pokemon Pokémon afetado
     */
    public void aplicarStatus(Pokemon pokemon) {
        if (!pokemon.isVivo() || pokemon.getStatus() == Status.NORMAL) {
            return;
        }
        switch (pokemon.getStatus()) {
            case QUEIMADO -> {
                pokemon.setHp(pokemon.getHp() - DANO_STATUS);
                pokemon.setAtk(Math.max(VALOR_MINIMO, pokemon.getAtk() - REDUCAO_STATUS));
            }
            case ENVENENADO -> pokemon.setHp(pokemon.getHp() - DANO_STATUS);
            case PARALISADO -> pokemon.setVel(Math.max(VALOR_MINIMO, pokemon.getVel() - REDUCAO_STATUS));
            default -> { }
        }
        registro.registrarEfeitoStatus(pokemon.getNome(), pokemon.getStatus());
    }

    private void atacar(boolean ataca, Pokemon atacante, Pokemon defensor) {
        if (ataca && atacante.isVivo() && defensor.isVivo()) {
            executarAtaque(atacante, defensor);
        }
    }

    private void aplicarTerreno(Pokemon pokemon) {
        if (pokemon.isVivo() && estacionamento.curaCanteiro(pokemon)) {
            registro.registrarEfeitoTerreno(pokemon.getNome(), estacionamento.getTerrenoAtual());
        }
    }

    private void usarItem(Treinador treinador, Item item) {
        if (item != null && treinador.getPokemon().isVivo()) {
            treinador.usarItem(item);
            registro.registrarUsoItem(treinador.getNome(), item);
        }
    }
}
