public class BatalhaPokemon {
    public static void main(String[] args) {
        Pokemon bulbaSal = new Pokemon("Bulbasal", 49, 49, Tipos.PLANTA, 45, 45);
        Pokemon charSal = new Pokemon("CharSal", 52, 43, Tipos.FOGO, 65, 39);
        Pokemon squirtSal = new Pokemon("SquirtSal", 48, 65, Tipos.AGUA, 43, 44);
        Pokemon chikoSal = new Pokemon("ChikoSal", 49, 65, Tipos.PLANTA, 45, 45);
        Pokemon cyndaSal = new Pokemon("CyndaSal", 55, 40, Tipos.FOGO, 65, 39);
        Pokemon totosal = new Pokemon("Totosal", 65, 64, Tipos.AGUA, 43, 50);
        Treinador asho = new Treinador("Asho", charSal);
        Treinador gary = new Treinador("Gary", chikoSal);
        Pokemon p1 = asho.getPokemon();
        Pokemon p2 = gary.getPokemon();
        Estacionamento terreno = new Estacionamento(TiposTerreno.DIA);
        Round gerenciadorRound = new Round(terreno);
        System.out.println("--- INÍCIO DA BATALHA ---");
        System.out.println(asho.getNome() + " escolheu " + p1.getNome());
        System.out.println(gary.getNome() + " escolheu " + p2.getNome());
        System.out.println("Terreno ativo: " + terreno.getTerrenoAtual());
        System.out.println("-------------------------");
        int numeroRound = 1;
        while (p1.getHp() > 0 && p2.getHp() > 0) {
            System.out.println("\n--- ROUND " + numeroRound + " ---");
            gerenciadorRound.simularRound(p1,p2);
            numeroRound++;

        }
        System.out.println("\n=========================");
        if (p1.getHp() > 0) {
            System.out.println("VENCEDOR: " + asho.getNome() + " com " + p1.getNome() + "!");
        } else if (p2.getHp() > 0) {
            System.out.println("VENCEDOR: " + gary.getNome() + " com " + p2.getNome() + "!");
        } else {
            System.out.println("EMPATE! Ambos os Pokésal foram derrotados!");
        }
        System.out.println("=========================");
    }
}


