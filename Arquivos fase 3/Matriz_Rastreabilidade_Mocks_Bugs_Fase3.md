# Matriz de Rastreabilidade, Justificativa dos Mocks e Registro de Bugs - Fase 03

Resultado da última execução (`mvn clean test`): **54 testes, 0 falhas**. Cobertura medida pelo JaCoCo nas classes de regra de negócio: **97,8% de linhas e 87,9% de branches** (metas: 80% e 70%). Preencher aqui os números finais do relatório do JaCoCo gerado na máquina da equipe.

## 1. Matriz de rastreabilidade

| Requisito | Caso de teste (classe) | Colaboradores mockados | Resultado esperado | Obtido |
| :--- | :--- | :--- | :--- | :---: |
| 2.1 Golpe erra e causa dano zero | `testGolpeErraQuandoPrecisaoFalha` (RoundMockTest) | Estacionamento, GeradorAleatorio, RegistroBatalha | Dano 0.0, HP do alvo inalterado, sem sorteio de crítico | Passou |
| 2.1 Crítico multiplica o dano por 1.5 | `testAcertoCriticoComGeradorMockado` (RoundMockTest) | Estacionamento, GeradorAleatorio, RegistroBatalha | Dano 24.0 (16.0 x 1.5), HP 76 | Passou |
| 2.1 Limite do crítico (6,25%) | `testAcertoSemCriticoCausaDanoNormal` (RoundMockTest) | idem | 0.0625 não é crítico: dano 16.0 | Passou |
| 2.1 Precisão 100 sempre acerta | `testPrecisaoMaximaAcertaMesmoComOMaiorSorteio` (RoundMockTest) | idem | Sorteio 99 acerta: dano 16.0 | Passou |
| 2.2 Asfalto quente só de dia | `testAsfaltoQuenteApenasDuranteODia` (ServicoTerrenoPadraoTest) | Clock | 1.15 de dia, 1.0 à noite | Passou |
| 2.2 Limites do horário de dia | `testTerrenoNosLimitesDoHorarioDeDia` (ServicoTerrenoPadraoTest) | Clock | 06:00 é dia, 18:00 é noite | Passou |
| 2.2 Chuva tem prioridade | `testChuvaTemPrioridadeSobreOHorario` (ServicoTerrenoPadraoTest) | Clock | Terreno CHUVA sem consultar o relógio | Passou |
| 2.2 Efeitos do terreno | 5 testes em `EstacionamentoTest` | ServicoTerreno | Bônus de fogo, de água, cura do canteiro | Passou |
| 2.3 Resultado salvo ao fim da batalha | `testResultadoSalvoAoFimDaBatalha` (TorneioTest) | Round, RepositorioResultados | Salvo 1 vez com vencedor, perdedor, turnos e Pokésal | Passou |
| 2.3 Falha no repositório | `testFalhaNoRepositorioNaoInvalidaBatalha` (TorneioTest) | Round, RepositorioResultados | Exceção no repositório; vencedor continua definido | Passou |
| 2.3 Batalha não terminou | `testRepositorioNaoChamadoSeBatalhaNaoTerminou` (TorneioTest) | Round, RepositorioResultados | Round falha; repositório nunca chamado | Passou |
| 2.3 Chaveamento de 4 e de 8 | `testChaveamentoTorneioAvancaVencedores`, `testTorneioDeOitoTreinadoresTemSeteBatalhas`, `testTorneioRejeitaQuantidadeInvalidaDeTreinadores` (TorneioTest) | Round, RepositorioResultados | Vencedores avançam na ordem; 3 e 7 batalhas; outro número é rejeitado | Passou |
| 2.3 Decisão no limite de turnos | `testNoLimiteDeTurnosVenceQuemTemMaisHp`, `testEmpateNoLimiteDeTurnosVenceOPrimeiroTreinador` (TorneioTest) | Round, RepositorioResultados | Vence quem tem mais HP; empate vence o primeiro | Passou |
| 2.4 Ordem dos eventos do turno | `testSequenciaDeEventosNoTurno` (RoundMockTest) | Estacionamento, GeradorAleatorio, RegistroBatalha | Ataque, dano, status, terreno e item, nessa ordem, e nada além | Passou |
| Autoral 1: conquista vitória perfeita | `testNotificaConquistaQuandoJogadorVenceSemUsarItens`, `testNaoNotificaConquistaQuandoJogadorUsouItem`, `testNaoNotificaConquistaQuandoOBotVence` (TorneioTest) | Round, ServicoNotificacao | Notifica só quando o jogador vence sem itens | Passou |
| Autoral 2: bônus de ataque | `testBonusDeAtaqueAplicadoAoJogadorVencedor`, `testBonusRecebeOsTurnosDaBatalha`, `testBonusNaoEAplicadoQuandoJogadorPerde` (TorneioTest) | Round, CalculadoraBonus | Atk 60 (50 x 1.10 + 5); recebe os turnos; sem bônus se perde | Passou |
| Fase 02: vantagem elemental | `testVantagemElemental` (RoundTest) | (nenhum: usa mocks vazios) | 16.0 e 4.0 nas 6 combinações | Passou |
| Fase 02: terreno do estacionamento | `testEfeitoTerrenoEstacionamentoUCSal` (RoundTest) | (nenhum) | 1.15, 1.10 e cura de 5 | Passou |
| Fase 02: ordem por velocidade | `testOrdemDeAtaquePorVelocidade` (RoundTest) | (nenhum) | Mais rápido primeiro; empate: o primeiro | Passou |
| Fase 02: limite de itens | `testUsoLimiteDeItensExcedido` (TreinadorTest) | (nenhum) | 3º uso lança IllegalStateException | Passou |
| Fase 02: valores limite de dano | `testCalculoDanoBoundaryValues` (RoundTest) | (nenhum) | Defesa 0: infinito; ataque 0: 0.0 | Passou |
| Autorais Fase 01: fortalecer e bots | `testFortalecer` (PokemonTest); 4 testes em `GeradorBotsTest` | GeradorAleatorio | +10% e cura total; +15% por nível | Passou |

## 2. Recursos do Mockito usados

| Recurso exigido | Onde aparece |
| :--- | :--- |
| `@Mock` e `@InjectMocks` | RoundMockTest, EstacionamentoTest, GeradorBotsTest, TorneioTest |
| `when(...).thenReturn(...)` em sequência | `testSequenciaDeEventosNoTurno` (`curaCanteiro` retorna false e depois true); `testAsfaltoQuenteApenasDuranteODia` (relógio retorna dia e depois noite) |
| `verify` com `times()`, `never()` e `atLeastOnce()` | TorneioTest, RoundMockTest, ServicoTerrenoPadraoTest |
| `InOrder` | `testSequenciaDeEventosNoTurno`, `testChaveamentoTorneioAvancaVencedores` |
| `ArgumentCaptor` | `testResultadoSalvoAoFimDaBatalha`, `testBonusRecebeOsTurnosDaBatalha` |
| `thenThrow` e `doThrow` | `testRepositorioNaoChamadoSeBatalhaNaoTerminou` (`thenThrow`), `testFalhaNoRepositorioNaoInvalidaBatalha` (`doThrow`) |
| Matchers `any()` e `eq()` | RoundMockTest e TorneioTest |

**Quando usar matcher e quando usar valor exato:** usamos valor exato quando o valor é o objetivo do teste (por exemplo, o dano 24.0 do crítico ou o nome "Ana" na conquista). Usamos `any()` quando o argumento não importa para aquela verificação (por exemplo, "alguma batalha foi disputada" ou "o repositório nunca foi chamado"). Quando um argumento usa matcher, todos os outros da mesma chamada também usam (por exemplo, `eq("Bulbasal")` junto com `anyDouble()`).

## 3. Justificativa dos mocks

- **`GeradorAleatorio`**: mockado para controlar o sorteio e tornar o teste previsível (acerto, erro e crítico).
- **`Estacionamento` (no teste do `Round`)**: mockado para isolar o `Round` do terreno; o próprio `Estacionamento` é testado à parte, com `ServicoTerreno` mockado.
- **`ServicoTerreno` e `Clock`**: mockados para simular dia, noite e chuva sem depender do horário real.
- **`RegistroBatalha`**: mockado porque o objetivo é verificar quais eventos foram enviados e em que ordem.
- **`RepositorioResultados`**: mockado para capturar o que foi salvo e para simular falhas do banco.
- **`Round` (no teste do `Torneio`)**: mockado para decidir o resultado das batalhas e testar só o chaveamento, o salvamento e as recompensas.
- **`ServicoNotificacao` e `CalculadoraBonus`**: mockados porque são os colaboradores dos requisitos autorais.

**O que não foi mockado:** a própria classe sob teste; `Pokemon`, `Treinador` e `Golpe`, que são dados simples cujo comportamento faz parte do que está sendo testado; a matriz de tipos (`Tipos`), que é regra pura e determinística; e `PokemonDex`, que só cria objetos.

## 4. Registro de bugs encontrados e corrigidos

Preencher a coluna "Commit" com o hash do commit de cada correção.

| Id | Bug | Como foi encontrado | Correção | Teste | Commit |
| :--- | :--- | :--- | :--- | :--- | :--- |
| B01 | O modo de batalha entre dois jogadores quebrava logo no início (poção em Pokémon com HP cheio gerava exceção) e não tinha escolha de ação | Execução do jogo | `Treinador` voltou a aceitar o item; o `main` pede a ação de cada treinador a cada turno | `testUsoLimiteDeItensExcedido` | |
| B02 | A batalha podia nunca terminar se ninguém causasse dano | Revisão do código | Limite de 50 turnos no `Round` | `testBatalhaTerminaNoLimiteDeTurnosQuandoNinguemAcerta` | |
| B03 | Efeitos de status e de terreno eram aplicados antes dos ataques, mas registrados depois | Revisão do código | Aplicados no fim do turno, na ordem dos eventos | `testSequenciaDeEventosNoTurno` | |
| B04 | Queimado não reduzia o ATK e paralisado não reduzia a VEL | Revisão do código | Reduções restauradas, com piso de 1 | `testStatusReduzAtributosSemChegarAZero` | |
| B05 | Poção ou cura do canteiro podiam reviver um Pokémon derrotado | Revisão do código | Item e terreno só valem para Pokémon vivo | `testItemNaoEUsadoPorPokemonDerrotado`, `testPokemonDerrotadoNaoUsaItem` | |
| B06 | Torneio de 8 treinadores jogava só com os 4 primeiros | Execução com 8 inscritos | Chave genérica de 4 ou 8 | `testTorneioDeOitoTreinadoresTemSeteBatalhas` | |
| B07 | Empate no limite de turnos derrubava o torneio com exceção | Revisão do código | Vence quem tem mais HP; empate vence o primeiro | `testNoLimiteDeTurnosVenceQuemTemMaisHp` | |
| B08 | No torneio só bots lutavam, então os requisitos autorais nunca rodavam no jogo | Execução do jogo | O jogador participa e recebe as recompensas | `testNotificaConquistaQuandoJogadorVenceSemUsarItens` | |
| B09 | A regra de dano e o desempate de velocidade tinham mudado em relação à Fase 02 | Comparação com os testes da Fase 02 | Fator de dano voltou a 8; empate vence o primeiro | `testVantagemElemental`, `testOrdemDeAtaquePorVelocidade` | |
| B10 | `new Random()` criado a cada sorteio de bot (apontado pelo Sonar na Fase 02) | SonarQube | `GeradorBots` usa o `GeradorAleatorio` injetado | `testSortearBaseUsaOGeradorInjetado` | |
