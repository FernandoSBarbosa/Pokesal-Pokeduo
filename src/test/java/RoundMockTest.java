import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Testes da Fase 03 do Round, isolado de Estacionamento, gerador e registro (todos mockados).
 * Não mockamos Pokemon, Treinador, Golpe nem Tipos: são dados simples e a matriz de tipos
 * faz parte da regra que queremos exercitar.
 *
 * Matchers: usamos valores exatos quando o valor é o objetivo do teste (ex.: dano 24.0) e
 * any()/eq() quando o argumento não importa para aquela verificação.
 */
@ExtendWith(MockitoExtension.class)
public class RoundMockTest {

    @Mock
    private Estacionamento estacionamento;
    @Mock
    private GeradorAleatorio gerador;
    @Mock
    private RegistroBatalha registro;
    @InjectMocks
    private Round round;

    private Pokemon criarAtacante() {
        return new Pokemon("CharSal", 50, 40, Tipos.FOGO, 65, 39, Status.NORMAL);
    }

    private Pokemon criarDefensor() {
        return new Pokemon("Bulbasal", 33, 50, Tipos.PLANTA, 45, 100, Status.NORMAL);
    }

    @Test
    public void testAcertoCriticoComGeradorMockado() {
        Pokemon atacante = criarAtacante();
        Pokemon defensor = criarDefensor();
        when(gerador.proximoInt(100)).thenReturn(0);       // 0 < 100: o golpe acerta
        when(gerador.proximoDouble()).thenReturn(0.01);    // 0.01 < 0.0625: é crítico
        when(estacionamento.multiplicadorTerreno(Tipos.FOGO)).thenReturn(1.0);

        double dano = round.executarAtaque(atacante, defensor);

        assertEquals(24.0, dano, 0.001);          // 50/50 * 8 * 2.0 (elemental) * 1.5 (crítico)
        assertEquals(76, defensor.getHp());
        verify(registro).registrarDano("Bulbasal", 24.0);
    }

    @Test
    public void testAcertoSemCriticoCausaDanoNormal() {
        Pokemon atacante = criarAtacante();
        Pokemon defensor = criarDefensor();
        when(gerador.proximoInt(100)).thenReturn(0);
        when(gerador.proximoDouble()).thenReturn(0.0625);  // limite: 0.0625 não é menor que 0.0625
        when(estacionamento.multiplicadorTerreno(Tipos.FOGO)).thenReturn(1.0);

        double dano = round.executarAtaque(atacante, defensor);

        assertEquals(16.0, dano, 0.001);
        assertEquals(84, defensor.getHp());
    }

    @Test
    public void testGolpeErraQuandoPrecisaoFalha() {
        Pokemon atacante = criarAtacante();
        atacante.setGolpe(new Golpe("Golpe incerto", 70));
        Pokemon defensor = criarDefensor();
        when(gerador.proximoInt(100)).thenReturn(70);      // 70 não é menor que 70: erra

        double dano = round.executarAtaque(atacante, defensor);

        assertEquals(0.0, dano, 0.001);
        assertEquals(100, defensor.getHp());               // HP do alvo inalterado
        verify(gerador, never()).proximoDouble();          // sem acerto não há sorteio de crítico
        verify(estacionamento, never()).multiplicadorTerreno(any(Tipos.class));
        verify(registro).registrarDano("Bulbasal", 0.0);
    }

    @Test
    public void testPrecisaoMaximaAcertaMesmoComOMaiorSorteio() {
        Pokemon atacante = criarAtacante();
        Pokemon defensor = criarDefensor();
        when(gerador.proximoInt(100)).thenReturn(99);      // maior sorteio possível
        when(gerador.proximoDouble()).thenReturn(0.5);
        when(estacionamento.multiplicadorTerreno(Tipos.FOGO)).thenReturn(1.0);

        assertEquals(16.0, round.executarAtaque(atacante, defensor), 0.001);
    }

    @Test
    public void testSequenciaDeEventosNoTurno() {
        Pokemon pokemon1 = criarAtacante();
        pokemon1.setStatus(Status.QUEIMADO);
        Pokemon pokemon2 = criarDefensor();
        pokemon2.setHp(50);
        Treinador t1 = new Treinador("T1", pokemon1);
        Treinador t2 = new Treinador("T2", pokemon2);

        when(gerador.proximoInt(100)).thenReturn(0);
        when(gerador.proximoDouble()).thenReturn(0.5);
        when(estacionamento.multiplicadorTerreno(Tipos.FOGO)).thenReturn(1.0);
        // retornos em sequência: a 1ª chamada (Pokémon 1) não cura, a 2ª (Pokémon 2) cura
        when(estacionamento.curaCanteiro(any(Pokemon.class))).thenReturn(false, true);
        when(estacionamento.getTerrenoAtual()).thenReturn(TiposTerreno.CANTEIRO);

        round.executarTurno(t1, null, t2, Item.POTION);   // T1 ataca; T2 usa poção

        InOrder ordem = inOrder(registro);
        ordem.verify(registro).registrarAtaque("CharSal", "Bulbasal");
        ordem.verify(registro).registrarDano(eq("Bulbasal"), anyDouble()); // o valor é testado acima
        ordem.verify(registro).registrarEfeitoStatus("CharSal", Status.QUEIMADO);
        ordem.verify(registro).registrarEfeitoTerreno("Bulbasal", TiposTerreno.CANTEIRO);
        ordem.verify(registro).registrarUsoItem("T2", Item.POTION);
        verifyNoMoreInteractions(registro);
        assertEquals(44, pokemon2.getHp());   // 50 - 16 de dano + 10 da poção
        assertEquals(34, pokemon1.getHp());   // 39 - 5 da queimadura
    }

    @Test
    public void testItemNaoEUsadoPorPokemonDerrotado() {
        Pokemon pokemon1 = criarAtacante();
        Pokemon pokemon2 = criarDefensor();
        pokemon2.setHp(1);
        Treinador t1 = new Treinador("T1", pokemon1);
        Treinador t2 = new Treinador("T2", pokemon2);
        when(gerador.proximoInt(100)).thenReturn(0);
        when(gerador.proximoDouble()).thenReturn(0.5);
        when(estacionamento.multiplicadorTerreno(Tipos.FOGO)).thenReturn(1.0);

        round.executarTurno(t1, null, t2, Item.POTION);   // T2 tenta curar, mas cai antes

        assertFalse(pokemon2.isVivo());
        assertEquals(0, t2.getItensUsados());
        verify(registro, never()).registrarUsoItem(anyString(), any(Item.class));
    }

    @Test
    public void testStatusReduzAtributosSemChegarAZero() {
        Pokemon queimado = new Pokemon("Q", 3, 10, Tipos.FOGO, 10, 20, Status.QUEIMADO);
        Pokemon envenenado = new Pokemon("E", 10, 10, Tipos.FOGO, 10, 20, Status.ENVENENADO);
        Pokemon paralisado = new Pokemon("P", 10, 10, Tipos.FOGO, 4, 20, Status.PARALISADO);
        Pokemon normal = new Pokemon("N", 10, 10, Tipos.FOGO, 10, 20, Status.NORMAL);

        round.aplicarStatus(queimado);
        round.aplicarStatus(envenenado);
        round.aplicarStatus(paralisado);
        round.aplicarStatus(normal);

        assertEquals(15, queimado.getHp());
        assertEquals(1, queimado.getAtk());       // 3 - 5, mas nunca abaixo de 1
        assertEquals(15, envenenado.getHp());
        assertEquals(1, paralisado.getVel());     // 4 - 5, mas nunca abaixo de 1
        assertEquals(20, paralisado.getHp());
        verify(registro).registrarEfeitoStatus("Q", Status.QUEIMADO);
        verify(registro).registrarEfeitoStatus("E", Status.ENVENENADO);
        verify(registro).registrarEfeitoStatus("P", Status.PARALISADO);
        verify(registro, never()).registrarEfeitoStatus(eq("N"), any(Status.class));
    }

    @Test
    public void testBatalhaTerminaQuandoUmPokemonCai() {
        Pokemon forte = new Pokemon("CharSal", 100, 40, Tipos.FOGO, 65, 39, Status.NORMAL);
        Pokemon fraco = new Pokemon("Bulbasal", 33, 50, Tipos.PLANTA, 45, 10, Status.NORMAL);
        when(gerador.proximoInt(100)).thenReturn(0);
        when(gerador.proximoDouble()).thenReturn(0.5);
        when(estacionamento.multiplicadorTerreno(Tipos.FOGO)).thenReturn(1.0);

        int turnos = round.executarBatalha(new Treinador("T1", forte), new Treinador("T2", fraco));

        assertEquals(1, turnos);
        assertFalse(fraco.isVivo());
    }

    @Test
    public void testBatalhaTerminaNoLimiteDeTurnosQuandoNinguemAcerta() {
        Pokemon p1 = criarAtacante();
        p1.setGolpe(new Golpe("Golpe falho", 0));
        Pokemon p2 = criarDefensor();
        p2.setGolpe(new Golpe("Golpe falho", 0));
        when(gerador.proximoInt(100)).thenReturn(0);       // com precisão 0, nunca acerta

        int turnos = round.executarBatalha(new Treinador("T1", p1), new Treinador("T2", p2));

        assertEquals(50, turnos);                           // para no limite, sem loop infinito
        verify(gerador, times(100)).proximoInt(100);        // 2 ataques por turno
    }
}
