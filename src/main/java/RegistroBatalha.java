public interface RegistroBatalha {
    void registrarAtaque(String atacante, String defensor);
    void registrarDano(String defensor, double dano, boolean eCritico);
    void registrarEfeitoStatus(String pokemon, Status status);
    void registrarEfeitoTerreno(String pokemon, TiposTerreno terreno);
    void registrarUsoItem(String treinador, Item item);
}