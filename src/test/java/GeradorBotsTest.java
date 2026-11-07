import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GeradorBotsTest {
    @Mock GeradorAleatorio gerador;

    @Test
    void escalaPokemonPorNivel() {
        Pokemon base = new Pokemon("CharSal", 50, 40, Tipos.FOGO, 60, 40);
        GeradorBots bots = new GeradorBots(gerador);
        Pokemon nivel1 = bots.escalarPokemon(base, 1);
        assertEquals(50, nivel1.getAtk());
        assertEquals(40, nivel1.getHp());
        Pokemon nivel3 = bots.escalarPokemon(base, 3);
        assertEquals(65, nivel3.getAtk());
        assertEquals(52, nivel3.getHp());
        assertEquals(52, nivel3.getDef());
        assertEquals(78, nivel3.getVel());
    }

    @Test
    void criarBotUsaColaboradorAleatorioInjetado() {
        when(gerador.gerarNumero(30, 60)).thenReturn(40);
        when(gerador.gerarNumero(20, 50)).thenReturn(30);
        when(gerador.gerarNumero(40, 70)).thenReturn(55);
        when(gerador.proximoInt(Tipos.values().length)).thenReturn(1);
        Treinador bot = new GeradorBots(gerador).criarBot("Teste");
        assertEquals("Teste", bot.getNome());
        assertEquals(40, bot.getPokemon().getAtk());
        assertEquals(Tipos.AGUA, bot.getPokemon().getTipo());
        verify(gerador).gerarNumero(30, 60);
    }
}


