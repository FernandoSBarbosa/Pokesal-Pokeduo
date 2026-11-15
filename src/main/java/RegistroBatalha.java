/** Recebe os eventos de cada turno, na ordem: ataque, dano, status, terreno e item. */
public interface RegistroBatalha {
    void registrarAtaque(String atacante, String defensor);

    void registrarDano(String alvo, double dano);

    void registrarEfeitoStatus(String pokemon, Status status);

    void registrarEfeitoTerreno(String pokemon, TiposTerreno terreno);

    void registrarUsoItem(String treinador, Item item);
}
