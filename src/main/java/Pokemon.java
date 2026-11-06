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
        this.nome = nome;
        this.atk = atk;
        this.def = def;
        this.tipo = tipo;
        this.vel = vel;
        this.hp = hp;
        this.maxHP = hp;
        this.precisao = 100;
        this.status = status != null ? status : Status.NORMAL;
    }

    public Pokemon(String nome, int atk, int def, Tipos tipo, int vel, int hp) {
        this(nome, atk, def, tipo, vel, hp, Status.NORMAL);
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public int getAtk() { return atk; }
    public int getAtaque() { return atk; }
    public void setAtk(int atk) { this.atk = Math.max(0, atk); }
    public void setAtaque(int atk) { setAtk(atk); }

    public int getDef() { return def; }
    public int getDefesa() { return def; }
    public void setDef(int def) { this.def = Math.max(0, def); }

    public Tipos getTipo() { return tipo; }
    public void setTipo(Tipos tipo) { this.tipo = tipo; }

    public int getVel() { return vel; }
    public int getVelocidade() { return vel; }
    public void setVel(int vel) { this.vel = Math.max(0, vel); }
    public void setVelocidade(int vel) { setVel(vel); }

    public int getHp() { return hp; }
    public void setHp(int hp) {
        if (hp < 0) {
            this.hp = 0;
        } else if (hp > this.maxHP) {
            this.hp = this.maxHP;
        } else {
            this.hp = hp;
        }
    }

    public int getMaxHP() { return maxHP; }
    public void setMaxHP(int maxHP) { this.maxHP = maxHP; }

    public int getPrecisao() { return precisao; }
    public void setPrecisao(int precisao) { this.precisao = precisao; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public boolean isVivo() { return this.hp > 0; }

    public void fortalecer() {
        this.hp = this.maxHP;
        this.atk = (int) (this.atk * FORTALECIMENTO);
        this.def = (int) (this.def * FORTALECIMENTO);
        this.vel = (int) (this.vel * FORTALECIMENTO);
    }
}