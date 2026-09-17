public class Pokemon {
    private String nome;
    private int atk;
    private int def;
    private Tipos tipo;
    private int vel;
    private int hp;
    private int maxHP;
    private Status status;

    public int getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }

    public int getVel() {
        return vel;
    }

    public void setVel(int vel) {
        this.vel = vel;
    }

    public Tipos getTipo() {
        return tipo;
    }

    public void setTipo(Tipos tipo) {
        this.tipo = tipo;
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        if (hp < 0){
            this.hp = 0;
        } else if (hp > this.maxHP) {
            this.hp = this.maxHP;
        } else{
            this.hp = hp;
        }

    }

    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }

    public Pokemon(String nome, int atk, int def, Tipos tipo, int vel, int hp, Status status) {
        this.nome = nome;
        this.atk = atk;
        this.def = def;
        this.tipo = tipo;
        this.vel = vel;
        this.hp = hp;
        this.maxHP = hp;
        this.status = Status.NORMAL;
    }
}
