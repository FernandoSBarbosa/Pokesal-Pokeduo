# Relatório de Inspeção e Revisão de Requisitos
## Módulo de Torneio contra Bots — Projeto Pokeduo

### 1. Introdução

Este relatório apresenta os resultados de uma inspeção e revisão dos requisitos definidos para a funcionalidade extra do projeto Pokeduo: o modo de torneio contra bots. O objetivo desta análise é identificar ambiguidades, omissões e contradições presentes na descrição inicial dos requisitos, antes de sua codificação, de modo a antecipar riscos de retrabalho e inconsistências de implementação.

### 2. Requisitos Analisados

Os requisitos originais, definidos de forma informal pela equipe antes do início da codificação, previam a criação de um torneio composto por cinco bots de dificuldade crescente, gerados a partir de uma fórmula de escala aplicada sobre os atributos base de um Pokémon sorteado, em vez de bots com Pokémon pré-definidos. Previam também que, ao vencer cada bot, o Pokémon do jogador receberia uma cura parcial de HP e um aumento permanente e significativo de ataque e defesa, sem que esse reforço equiparasse o jogador ao bot do nível seguinte. Por fim, previam que o modo torneio conviveria, por meio de um menu inicial, com o modo de batalha original entre dois jogadores.

### 3. Ambiguidades Identificadas

A expressão "aumento significativo" de ataque e defesa não definia um valor numérico ou percentual, deixando em aberto o quanto seria necessário para caracterizar um reforço como significativo. De forma semelhante, a expressão "cura parcial" de HP não especificava se o percentual de cura incidiria sobre o HP máximo do Pokémon ou sobre o HP atual no momento da vitória — distinção relevante, já que uma cura percentual sobre o HP atual tenderia a diminuir em valor absoluto a cada vitória, contrariando a intenção de fortalecer o jogador progressivamente. Também não havia definição sobre a forma de crescimento da fórmula de escala dos bots — se linear, percentual composta ou por incrementos fixos — nem sobre o fator de crescimento a ser aplicado por nível. Por fim, não estava claro se os bots teriam Pokémon fixos e pré-determinados por nível ou se seriam sorteados de uma base comum e, em seguida, escalados conforme o nível do torneio.

### 4. Omissões Identificadas

Não havia, na descrição inicial, um critério explícito para a definição do número de níveis do torneio, tampouco uma justificativa para esse número. Também não havia especificação sobre o comportamento esperado em caso de derrota do jogador durante o torneio, isto é, se o torneio deveria ser interrompido, reiniciado ou se o jogador retornaria ao menu principal. Faltava, ainda, definição sobre como o terreno, o estacionamento e os efeitos de status seriam tratados durante as batalhas do torneio — se seguiriam as mesmas regras do modo de batalha simples ou um conjunto simplificado de regras. Por fim, não havia menção explícita sobre em que momento e de que forma o jogador escolheria seu Pokémon antes do início do torneio, nem se essa escolha seria compartilhada com o fluxo do modo de batalha simples.

### 5. Contradições Identificadas

O requisito de fortalecer o jogador a cada vitória sem equipará-lo ao bot do próximo nível apresentava, em princípio, uma contradição com a ausência de um teto definido para a fórmula de escalonamento dos bots: sem esse teto, o crescimento do jogador não seria necessariamente inferior ao crescimento do próximo bot, a depender dos valores escolhidos para os fatores de fortalecimento e de dificuldade. Havia, além disso, uma tensão entre a intenção inicial de manter toda a lógica do torneio dentro do método principal do jogo e o objetivo, também declarado pela equipe, de manter esse método organizado e de fácil leitura — objetivo que só se tornou compatível com a primeira intenção após a decisão de isolar essa lógica em uma classe própria.

### 6. Recomendações

Recomenda-se que extensões futuras do projeto tenham seus requisitos redigidos com valores numéricos explícitos para qualquer reforço, cura ou fator de escala mencionado, com a definição prévia de casos de borda, como derrota do jogador ou esgotamento do HP, e com critérios de aceite estabelecidos antes do início da codificação. Essa prática tende a reduzir a necessidade de decisões de projeto tomadas de improviso durante a implementação e a diminuir o retrabalho decorrente de ambiguidades não resolvidas antecipadamente.
