import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GeradorBotsTest {

    @Mock
    private GeradorAleatorio gerador;
    @InjectMocks
    private GeradorBots geradorBots;

    private Pokemon criarBase() {
        return new Pokemon("CharSal", 50, 40, Tipos.FOGO, 60, 40, Status.NORMAL, new Golpe("Golpe de Chama", 80));
    }

    @Test
    public void testEscalarPokemonNivel1() {
        Pokemon resultado = geradorBots.escalarPokemon(criarBase(), 1);

        assertEquals(40, resultado.getHp());
        assertEquals(50, resultado.getAtk());
        assertEquals(60, resultado.getVel());
        assertEquals(40, resultado.getDef());
    }

    @Test
    public void testEscalarPokemonNivel3() {
        Pokemon base = criarBase();

        Pokemon resultado = geradorBots.escalarPokemon(base, 3);

        // multiplicador = 1 + 0.15 * (3 - 1) = 1.3
        assertEquals(65, resultado.getAtk());
        assertEquals(52, resultado.getHp());
        assertEquals(52, resultado.getDef());
        assertEquals(78, resultado.getVel());
        assertEquals(40, base.getHp());                         // o Pokémon original não muda
        assertEquals(80, resultado.getGolpe().precisao());      // a precisão do golpe é mantida
    }

    @Test
    public void testSortearBaseUsaOGeradorInjetado() {
        when(gerador.proximoInt(6)).thenReturn(2);

        Pokemon sorteado = geradorBots.sortearBase();

        assertEquals("SquirtSal", sorteado.getNome());
        verify(gerador).proximoInt(6);
    }

    @Test
    public void testGerarBotCombinaSorteioENivel() {
        when(gerador.proximoInt(6)).thenReturn(1);   // CharSal (atk 52)

        Treinador bot = geradorBots.gerarBot(3);

        assertEquals("Bot nível 3", bot.getNome());
        assertEquals("CharSal", bot.getPokemon().getNome());
        assertEquals(67, bot.getPokemon().getAtk());   // 52 * 1.3 = 67.6 -> 67
    }
}
