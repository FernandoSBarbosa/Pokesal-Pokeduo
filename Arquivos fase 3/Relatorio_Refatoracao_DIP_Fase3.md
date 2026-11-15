# Relatório de Refatoração - Fase 03

## Por que refatorar

Na Fase 02 as regras de negócio criavam ou conheciam diretamente tudo de que precisavam: o sorteio (`new Random()` dentro do `GeradorBots`), o terreno (fixo dentro do `Estacionamento`) e a saída no console. Isso gerava **acoplamento forte** entre as classes: para testar o `Round` era preciso usar o terreno real e aceitar resultados aleatórios, o que torna os testes imprevisíveis. A Fase 03 acrescentou novas dependências (precisão e crítico, serviço de terreno com relógio, repositório de resultados e registro de eventos), então a refatoração foi necessária para que cada classe pudesse ser testada isolada com Mockito.

## Princípio da Inversão de Dependência (SOLID)

O Princípio da Inversão de Dependência diz que as classes de alto nível (as regras de negócio) não devem depender de implementações concretas, e sim de abstrações. Por isso criamos interfaces para cada colaborador e passamos as implementações pelo construtor (injeção de dependências). Só a classe `BatalhaPokemon` (o `main`) cria os objetos concretos e liga as peças.

## Classes refatoradas

- **`Round`**: recebe `Estacionamento`, `GeradorAleatorio` e `RegistroBatalha`. Todo sorteio (precisão e crítico) vem do gerador, e as regras de 6,25% de crítico e de acerto ficam no `Round`, não no gerador. O dano base (`calcularDano`) continua sem sorteios, para manter os testes da Fase 02. O turno agora segue a ordem exigida de eventos (ataque, dano, status, terreno e item), e a batalha tem limite de 50 turnos.
- **`Estacionamento`**: deixou de guardar um terreno fixo e passou a consultar um `ServicoTerreno`. A implementação `ServicoTerrenoPadrao` recebe um `java.time.Clock` e o clima: chovendo vira poça de chuva, de dia vira asfalto quente e à noite vira canteiro central.
- **`Torneio`**: passou a ter chave eliminatória de 4 ou 8 treinadores. Recebe `Round`, `RepositorioResultados`, `ServicoNotificacao` e `CalculadoraBonus`. Se o repositório falhar, o erro vai para o log e a batalha continua válida.
- **`GeradorBots`**: recebe o `GeradorAleatorio` em vez de criar um `Random`, o que também resolveu o problema apontado pelo SonarQube na Fase 02.
- **`Treinador`**: deixou de ter qualquer dependência de saída; o registro do uso de item passou para o `Round`.

## Novas interfaces e implementações

`GeradorAleatorio` (`GeradorAleatorioReal`), `ServicoTerreno` (`ServicoTerrenoPadrao`), `RegistroBatalha` (`RegistroBatalhaConsole`), `RepositorioResultados` (`RepositorioResultadosMemoria`), `ServicoNotificacao` (`ServicoNotificacaoConsole`) e `CalculadoraBonus` (`CalculadoraBonusPadrao`).

## Requisitos autorais da Fase 03

1. **Conquista "vitória perfeita"** (colaborador: `ServicoNotificacao`): quando o jogador vence uma batalha sem usar nenhum item, ele recebe uma notificação de conquista.
2. **Bônus de ataque por vitória** (colaborador: `CalculadoraBonus`): quando o jogador vence, ganha um bônus de ataque que é maior quanto mais rápida for a vitória (10 menos o número de turnos, nunca negativo).
