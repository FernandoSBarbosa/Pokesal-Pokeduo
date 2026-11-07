import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TorneioCoverageTest {
    @Mock Round round;
    @Mock RepositorioResultados repositorio;
    @Mock ServicoNotificacao notificacao;
    @Mock CalculadoraBonus calculadoraBonus;

    private Treinador treinador(String nome) {
        return new Treinador(nome, new Pokemon(nome + "Sal", 20, 20, Tipos.FOGO, 10, 100));
    }

    @Test
    void validaDependenciasInscricoesEArgumentosDaBatalha() {
        assertThrows(IllegalArgumentException.class, () -> new Torneio(null, null, null, null, null, null, null));
        Torneio torneio = new Torneio(null, null, round, null, repositorio, null, null);
        assertThrows(IllegalArgumentException.class, () -> torneio.executarBatalha(null, treinador("B")));
        assertThrows(IllegalArgumentException.class, () -> torneio.executarTorneio(null));
        assertThrows(IllegalArgumentException.class, () -> torneio.executarTorneio(List.of(treinador("A"))));
        Treinador repetido = treinador("Repetido");
        assertThrows(IllegalArgumentException.class,
                () -> torneio.executarTorneio(List.of(repetido, repetido, treinador("C"), treinador("D"))));
    }

    @Test
    void vencedorSegundoParticipanteTambemPodeReceberConquistaEBonus() {
        Treinador primeiro = treinador("A");
        Treinador jogador = treinador("Jogador");
        when(round.executarBatalhaComTurnos(primeiro, jogador)).thenAnswer(invocation -> {
            primeiro.getPokemon().setHp(0);
            return 3;
        });
        when(calculadoraBonus.calcularBonusXp(1, 3)).thenReturn(7);
        Torneio torneio = new Torneio(jogador, null, round, null, repositorio, notificacao, calculadoraBonus);

        assertSame(jogador, torneio.executarBatalha(primeiro, jogador));
        assertEquals(27, jogador.getPokemon().getAtk());
        verify(notificacao).notificarConquista("Jogador", "Vitória Perfeita sem Itens");
        verify(repositorio).salvarResultado(any(ResultadoBatalha.class));
    }

    @Test
    void naoChamaBonusOuNotificacaoQuandoJogadorPerdeOuQuandoDependenciasEstaoAusentes() {
        Treinador jogador = treinador("Jogador");
        Treinador adversario = treinador("Adversario");
        when(round.executarBatalhaComTurnos(jogador, adversario)).thenAnswer(invocation -> {
            jogador.getPokemon().setHp(0);
            return 2;
        });
        Torneio torneio = new Torneio(jogador, null, round, null, repositorio, notificacao, calculadoraBonus);

        assertSame(adversario, torneio.executarBatalha(jogador, adversario));
        verifyNoInteractions(notificacao, calculadoraBonus);

        Treinador a = treinador("A2");
        Treinador b = treinador("B2");
        when(round.executarBatalhaComTurnos(a, b)).thenAnswer(invocation -> {
            b.getPokemon().setHp(0);
            return 1;
        });
        Torneio semColaboradores = new Torneio(a, null, round, null, null, null, null);
        assertSame(a, semColaboradores.executarBatalha(a, b));
    }

    @Test
    void rejeitaListaComTreinadorNulo() {
        Torneio torneio = new Torneio(null, null, round, null, repositorio, null, null);
        assertThrows(IllegalArgumentException.class,
                () -> torneio.executarTorneio(java.util.Arrays.asList(treinador("A"), null, treinador("C"), treinador("D"))));
    }
}
