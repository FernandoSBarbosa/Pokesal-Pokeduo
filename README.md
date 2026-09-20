# Pokeduo

Paródia de Pokémon em Java, feita como projeto de faculdade. Dois treinadores escolhem um Pokesal e batalham por turnos até que um seja derrotado.

## Modos de jogo

- **Batalha contra outro jogador**: dois treinadores, cada um escolhe seu Pokesal e batalham, podendo atacar ou usar itens (Poção, Super-Poção, Antídoto).
- **Torneio contra bots**: o jogador enfrenta 5 bots em sequência, com dificuldade crescente. A cada vitória, o Pokémon do jogador recebe um reforço permanente em ataque, defesa, velocidade e um pouco de cura.

## Como jogar

1. Execute a classe `BatalhaPokemon`.
2. Informe seu nome e escolha um Pokesal.
3. Escolha o modo de jogo (batalha simples ou torneio).
4. A batalha segue até que um dos lados perca.

## Classes principais

- `Pokemon`, `Treinador`, `Round`, `Estacionamento`: lógica de batalha, dano e terreno.
- `PokemonDex`: lista dos seis Pokesal disponíveis.
- `GeradorBots` e `Torneio`: geram os bots e conduzem o modo Torneio.
