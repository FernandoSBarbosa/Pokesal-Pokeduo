public class Pokemon {
    private static final double FORTALECIMENTO = 1.10;
    private String nome;
    private int atk;
    private int def;
    private Tipos tipo;
    private int vel;
    private int hp;
    private int maxHP;
    private Status status;

    public Pokemon(String nome, int atk, int def, Tipos tipo, int vel, int hp, Status status) {
        this.nome = nome;
        this.atk = atk;
        this.def = def;
        this.tipo = tipo;
        this.vel = vel;
        this.hp = hp;
        this.maxHP = hp;
        this.status = status != null ? status : Status.NORMAL;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public int getAtk() { return atk; }
    public void setAtk(int atk) { this.atk = atk; }

    public int getDef() { return def; }
    public void setDef(int def) { this.def = def; }

    public Tipos getTipo() { return tipo; }
    public void setTipo(Tipos tipo) { this.tipo = tipo; }

    public int getVel() { return vel; }
    public void setVel(int vel) { this.vel = vel; }

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

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public void fortalecer() {
        this.hp = this.maxHP;
        this.atk = (int) (this.atk * FORTALECIMENTO);
        this.def = (int) (this.def * FORTALECIMENTO);
        this.vel = (int) (this.vel * FORTALECIMENTO);
    }
}