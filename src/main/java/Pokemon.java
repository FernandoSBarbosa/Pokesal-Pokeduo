public class Pokemon {
    private static final double FORTALECIMENTO = 1.10;
    private String nome;
    private int atk;
    private int def;
    private Tipos tipo;
    private int vel;
    private int hp;
    private int maxHP;
    private int precisao;
    private Status status;

    public Pokemon(String nome, int atk, int def, Tipos tipo, int vel, int hp, Status status) {
        setNome(nome);
        setAtk(atk);
        setDef(def);
        setTipo(tipo);
        setVel(vel);
        setMaxHP(hp);
        this.hp = hp;
        setPrecisao(100);
        setStatus(status);
    }

    public Pokemon(String nome, int atk, int def, Tipos tipo, int vel, int hp) {
        this(nome, atk, def, tipo, vel, hp, Status.NORMAL);
    }

    public String getNome() { return nome; }
    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Nome é obrigatório.");
        this.nome = nome;
    }

    public int getAtk() { return atk; }
    public int getAtaque() { return atk; }
    public void setAtk(int atk) { this.atk = Math.max(0, atk); }
    public void setAtaque(int atk) { setAtk(atk); }

    public int getDef() { return def; }
    public int getDefesa() { return def; }
    public void setDef(int def) { this.def = Math.max(0, def); }

    public Tipos getTipo() { return tipo; }
    public void setTipo(Tipos tipo) {
        if (tipo == null) throw new IllegalArgumentException("Tipo é obrigatório.");
        this.tipo = tipo;
    }

    public int getVel() { return vel; }
    public int getVelocidade() { return vel; }
    public void setVel(int vel) { this.vel = Math.max(0, vel); }
    public void setVelocidade(int vel) { setVel(vel); }

    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = Math.max(0, Math.min(hp, maxHP)); }

    public int getMaxHP() { return maxHP; }
    public void setMaxHP(int maxHP) {
        if (maxHP <= 0) throw new IllegalArgumentException("HP máximo deve ser positivo.");
        this.maxHP = maxHP;
        this.hp = Math.min(this.hp, maxHP);
    }

    public int getPrecisao() { return precisao; }
    public void setPrecisao(int precisao) {
        if (precisao < 0 || precisao > 100) throw new IllegalArgumentException("Precisão deve estar entre 0 e 100.");
        this.precisao = precisao;
    }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status == null ? Status.NORMAL : status; }

    public boolean isVivo() { return hp > 0; }

    public void fortalecer() {
        hp = maxHP;
        atk = (int) (atk * FORTALECIMENTO);
        def = (int) (def * FORTALECIMENTO);
        vel = (int) (vel * FORTALECIMENTO);
    }
}
