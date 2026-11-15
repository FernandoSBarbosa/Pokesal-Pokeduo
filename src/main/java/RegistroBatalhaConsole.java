/** Mostra os eventos da batalha no console. */
public class RegistroBatalhaConsole implements RegistroBatalha {
    @Override
    public void registrarAtaque(String atacante, String defensor) {
        System.out.println(atacante + " atacou " + defensor + "!");
    }

    @Override
    public void registrarDano(String alvo, double dano) {
        if (dano == 0) {
            System.out.println("  O golpe errou!");
        } else {
            System.out.printf("  %s sofreu %.1f de dano%n", alvo, dano);
        }
    }

    @Override
    public void registrarEfeitoStatus(String pokemon, Status status) {
        System.out.println("  Status " + status + " afetou " + pokemon);
    }

    @Override
    public void registrarEfeitoTerreno(String pokemon, TiposTerreno terreno) {
        System.out.println("  Terreno " + terreno + " beneficiou " + pokemon);
    }

    @Override
    public void registrarUsoItem(String treinador, Item item) {
        System.out.println("  " + treinador + " usou " + item);
    }
}
