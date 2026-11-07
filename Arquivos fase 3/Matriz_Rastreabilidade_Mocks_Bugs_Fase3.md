# Rastreabilidade, justificativa dos mocks e registro de bugs — Fase 03

**Projeto:** PokeSal / Pokeduo  
**Data da última execução registrada:** 07/11/2026  
**Validação registrada:** `mvn clean test` — 38 testes, 0 falhas, 0 erros. JaCoCo: 93,5% de linhas e 76,5% de branches.

> Este documento descreve o estado observado no projeto após as correções. A equipe deve conferir os resultados ao repetir a execução e completar os números dos commits depois de registrar as alterações no Git.

## 1. Matriz de rastreabilidade

| Requisito | Casos de teste | Colaboradores mockados | Resultado esperado | Resultado obtido |
|---|---|---|---|---|
| R03-01 — O golpe pode errar; dano zero e HP do alvo permanece igual. | `TorneioTest.testGolpeErraQuandoPrecisaoFalha` | `GeradorAleatorio`, `RegistroBatalha` | `checarAcerto` retorna falso; dano zero e nenhum crítico consultado. | Passou na última execução da suíte. |
| R03-02 — Golpe crítico multiplica o dano por 1,5; chance no gerador real é 6,25%. | `TorneioTest.testAcertoCriticoComGeradorMockado`; `GeradorAleatorioRealTest.chanceCriticaRetornaBooleano` | `GeradorAleatorio`, no teste unitário do `Round` | Mock força crítico para validar o multiplicador; teste concreto executa o gerador real. | Passou na última execução da suíte. |
| R03-03 — Asfalto quente depende do horário; chuva favorece Água. | `TorneioTest.testAsfaltoQuenteApenasDuranteDia`; `RoundTest.testEfeitoTerrenoEstacionamentoUCSal` | `ServicoTerreno` é mockado para verificar a delegação; `Clock` fixo é usado nos cenários dia/noite. | Multiplicador de fogo durante o dia e neutro à noite; chuva com multiplicador de Água. | Passou na última execução da suíte. |
| R03-04 — Eventos do turno devem ser registrados nesta ordem: ataque, dano, status, terreno e item. | `TorneioTest.testSequenciaDeEventosNoTurno`; `RoundCoverageTest.aplicaDanoContinuoAQueimadoEEnvenenadoESkipParaStatusNormalOuNocauteado` | `RegistroBatalha`, `GeradorAleatorio` | `InOrder` deve confirmar a sequência completa exigida. | Parcial: os testes passaram, mas o `Round.simularRound` atual registra status/terreno antes dos ataques. O teste existente só verifica ordem entre ataque/dano e presença de terreno; não comprova a sequência completa. Ver bug B03-08. |
| R03-05 — Torneio eliminatório aceita 4 ou 8 participantes e avança vencedores. | `TorneioTest.testChaveamentoTorneioAvancaVencedores`; `TorneioCoverageTest.validaDependenciasInscricoesEArgumentosDaBatalha`; `TorneioCoverageTest.rejeitaListaComTreinadorNulo` | `Round` | Vencedores avançam; listas inválidas e duplicadas são rejeitadas. | Passou na última execução da suíte. |
| R03-06 — Resultado da batalha contém vencedor, perdedor, turnos e Pokesal. | `TorneioTest.testResultadoSalvoAoFimDaBatalha` | `RepositorioResultados`; `Round` real com gerador mockado | Captura do `ResultadoBatalha` confirma os campos registrados. | Passou na última execução da suíte. |
| R03-07 — Falha no repositório não invalida a batalha; resultado não é salvo antes da conclusão. | `TorneioTest.testFalhaNoRepositorioNaoInvalidaBatalha`; `TorneioTest.testRepositorioNaoChamadoSeBatalhaNaoTerminou` | `RepositorioResultados`; `Round` mockado no caso não concluído | Exceção é simulada com `doThrow`; vencedor permanece definido. No empate/limite sem vencedor, repositório não é chamado. | Passou na última execução da suíte. |
| RA-01 — Vitória do jogador sem itens solicita notificação de conquista; usando item, não notifica. | `TorneioTest.testNotificacaoSemItensEAumentoDeAtributo`; `TorneioTest.requisitoAutoralNotificaConquistaApenasQuandoJogadorVenceSemItens`; `TorneioCoverageTest.vencedorSegundoParticipanteTambemPodeReceberConquistaEBonus` | `ServicoNotificacao`; `Round`/`GeradorAleatorio` nos testes de integração unitária | Notifica uma vez apenas na vitória do jogador sem itens. | Passou na última execução da suíte. |
| RA-02 — Vitória do jogador consulta o colaborador de bônus; bônus positivo é aplicado e negativo não reduz ataque. | `TorneioTest.testNotificacaoSemItensEAumentoDeAtributo`; `TorneioTest.requisitoAutoralBonusCalculadoPorColaboradorEValorNegativoNaoReduzAtaque`; `TorneioCoverageTest.vencedorSegundoParticipanteTambemPodeReceberConquistaEBonus` | `CalculadoraBonus`; `Round`/`GeradorAleatorio` nos testes de integração unitária | Usa o valor mockado após vitória; bônus negativo é limitado a zero. | Passou na última execução da suíte. |
| R03-08 — Geração dos Pokesals da Pokédex e aleatoriedade respeitam seus contratos. | `PokemonDexTest.retornaSeisPokemonComTiposENomesEsperados`; `GeradorAleatorioRealTest.geraValoresDentroDosLimitesERespeitaPrecisao`; `GeradorAleatorioRealTest.rejeitaIntervalosInvalidos` | Nenhum mock nos testes das implementações concretas. | Pokédex contém seis entradas; valores aleatórios respeitam limites e entradas inválidas são rejeitadas. | Passou na última execução da suíte. |

## 2. Justificativa dos mocks

| Colaborador | Por que foi mockado | Onde aparece |
|---|---|---|
| `GeradorAleatorio` | Controla acerto, erro, crítico e desempate por velocidade. O mock torna a sequência previsível e evita flutuação aleatória nos resultados. | `TorneioTest`, `RoundCoverageTest`. |
| `RegistroBatalha` | Permite verificar quais eventos foram emitidos e a ordem em que ocorreram, sem acoplar os testes a saída de console ou armazenamento. | `TorneioTest`, `RoundCoverageTest`. |
| `ServicoTerreno` | Permite verificar que `Estacionamento` delega o cálculo de terreno ao serviço e testar valores controlados. Para dia/noite também se usa `Clock.fixed`. | `TorneioTest`, `EstacionamentoTest`. |
| `RepositorioResultados` | Permite capturar o objeto salvo e simular indisponibilidade com `doThrow`, sem banco real. | `TorneioTest`, `TorneioCoverageTest`. |
| `ServicoNotificacao` | Permite verificar envio e ausência de envio da conquista sem integrar um serviço externo. | `TorneioTest`, `TorneioCoverageTest`. |
| `CalculadoraBonus` | Permite controlar retorno positivo, negativo e sequencial, verificando a política do torneio sem depender de uma fórmula externa. | `TorneioTest`, `TorneioCoverageTest`. |
| `Round` | É colaborador de `Torneio`; nos testes de orquestração é mockado para escolher deterministicamente o vencedor e número de turnos. | `TorneioTest`, `TorneioCoverageTest`. |

**Objetos mantidos reais:** a classe sob teste em cada teste; `Pokemon` e `Treinador` para validar o estado de domínio; enums como `Tipos`, `Status` e `TiposTerreno`; `Tipos.multiplicadorContra`; `PokemonDex`; e `GeradorAleatorioReal` nos testes específicos de suas implementações. `Clock.fixed` é usado em vez do relógio corrente nos cenários temporais determinísticos.

## 3. Registro de bugs e correções

| Bug | Como foi identificado | Correção aplicada | Evidência/teste | Commit |
|---|---|---|---|---|
| B03-01 — `ClassesBaseTest` usava construtores e tipos que não correspondiam às APIs (`Round`, `Pokemon`, repositório). | Compilação dos testes falhava com incompatibilidade de tipos/construtores e `salvarResultado(String)`. | Testes alinhados às assinaturas reais; resultado capturado como `ResultadoBatalha`. | Compilação dos testes e `TorneioTest.testResultadoSalvoAoFimDaBatalha`. | Pendente: alterações ainda sem commit. |
| B03-02 — Expectativas antigas de dano não correspondiam à fórmula implementada (`(ataque / defesa) × 10 × vantagem × terreno × crítico`). | Falha de asserção nos valores de dano elemental. | Valores esperados atualizados conforme os atributos e multiplicadores usados em cada cenário. | `RoundTest.testVantagemElemental`. | Pendente: alterações ainda sem commit. |
| B03-03 — Mock de `GeradorAleatorio` sem configuração fazia todos os golpes errarem, impedindo que as batalhas de teste tivessem vencedor. | Testes de torneio recebiam `null`, não salvavam resultado e não chamavam bônus/notificação. | Acerto e crítico configurados explicitamente nos casos que precisam de uma vitória determinística. | Testes de resultado, repositório, bônus e notificação em `TorneioTest`. | Pendente: alterações ainda sem commit. |
| B03-04 — Teste de limite de itens tentava usar poção com HP máximo. | `Treinador.usarItem` rejeitava o primeiro uso sem efeito. | HP inicial reduzido para que duas poções sejam válidas antes de testar o limite de uso. | `TreinadorTest.testUsoLimiteDeItensExcedido`. | Pendente: alterações ainda sem commit. |
| B03-05 — O gerador real aceitava precisão fora de 0–100 e intervalo de geração invertido. | Teste de contrato encontrou ausência de validação; a primeira execução de `mvn clean test` falhou neste caso. | `checarAcerto` valida a faixa e `gerarNumero` rejeita `min > max`. | `GeradorAleatorioRealTest.rejeitaIntervalosInvalidos`; suíte final com 38 testes passou. | Pendente: alterações ainda sem commit. |
| B03-06 — A regra de notificação verificava itens usados pelo primeiro treinador, mesmo quando o jogador podia ser o segundo. | Revisão da condição no fluxo de vitória. | Condição alterada para consultar os itens do treinador vencedor. | `TorneioCoverageTest.vencedorSegundoParticipanteTambemPodeReceberConquistaEBonus`. | Pendente: alterações ainda sem commit. |
| B03-07 — Classes auxiliares de console reduziam a métrica de cobertura das regras de negócio sem serem o foco da suíte unitária. | Relatório JaCoCo incluía implementações privadas aninhadas em `BatalhaPokemon`. | JaCoCo passou a excluir a classe interativa e suas classes auxiliares de console. | Execução final do JaCoCo: 93,5% de linhas e 76,5% de branches no conjunto reportado. | Pendente: alterações ainda sem commit. |
| B03-08 — Ordem de registro de eventos do turno diverge da ordem exigida na Fase 03. | Inspeção de `Round.simularRound` e comparação com a regra 2.4 do PDF. | Ainda não corrigido: `aplicarStatus` registra status e o bloco de terreno registra terreno antes das chamadas de ataque/dano. | `TorneioTest.testSequenciaDeEventosNoTurno` não verifica a sequência total; precisa ser ampliado depois da correção. | Aberto; commit ainda não existe. |

### Situação dos commits

O `git status` do projeto mostra alterações locais e arquivos adicionados/removidos ainda não commitados. Nenhum hash foi informado ou criado nesta etapa. Após a equipe revisar o documento e criar os commits, substitua “Pendente: alterações ainda sem commit” pelo hash correspondente a cada conjunto de correções.
