# PokeSal - Pokeduo

Paródia de Pokémon em Java (projeto de faculdade). Demonstração: [vídeo no YouTube](https://youtu.be/FeZLaXlMq1c?si=0JsFalrapc4QwXlC)

## Modos de jogo

- **Batalha entre dois jogadores**: cada treinador escolhe um Pokesal e, a cada turno, ataca ou usa um item
  (Poção, Super poção ou Antídoto; no máximo 2 por batalha). Usar item consome o turno.
- **Torneio eliminatório**: o jogador enfrenta bots em chaves de 4 ou 8 treinadores. Os bots ficam mais fortes
  a cada nível. Quando o jogador vence, seu Pokémon é fortalecido e ganha um bônus de ataque; se vencer sem
  usar itens, ganha uma conquista.

## Como jogar

1. Execute a classe `BatalhaPokemon`.
2. Escolha o modo (1 ou 2) e siga as instruções na tela.

## Como rodar os testes e a cobertura

```
mvn clean test
```

O relatório do JaCoCo fica em `target/site/jacoco/index.html`.

## Estrutura (Fase 03 - Inversão de Dependência)

As classes de regra de negócio não criam seus colaboradores: recebem tudo pelo construtor.
Só a classe `BatalhaPokemon` (main) cria os objetos e liga as peças.

| Regra de negócio | Interface recebida | Implementação |
| :--- | :--- | :--- |
| `Round` | `GeradorAleatorio`, `RegistroBatalha` | `GeradorAleatorioReal`, `RegistroBatalhaConsole` |
| `Estacionamento` | `ServicoTerreno` | `ServicoTerrenoPadrao` (usa `java.time.Clock`) |
| `Torneio` | `RepositorioResultados`, `ServicoNotificacao`, `CalculadoraBonus` | `RepositorioResultadosMemoria`, `ServicoNotificacaoConsole`, `CalculadoraBonusPadrao` |
| `GeradorBots` | `GeradorAleatorio` | `GeradorAleatorioReal` |

Documentação da Fase 03: pasta `Arquivos fase 3` (diagramas, relatório de refatoração, matriz de rastreabilidade).
