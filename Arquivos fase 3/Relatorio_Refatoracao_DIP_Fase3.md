# Relatório de Refatoração — Fase 03

## Objetivo

A refatoração preparou a lógica de batalha e de torneio para ser testada de forma previsível e reduziu o acoplamento entre as regras de negócio e os serviços usados pelo sistema. As mudanças concentram-se em `Round`, `Estacionamento`, `ServicoTerreno`, `Torneio` e nas interfaces de colaboração.

## Refatorações realizadas

`Round` recebe `GeradorAleatorio` e `RegistroBatalha`, que controlam os resultados aleatórios dos golpes e registram os eventos da batalha. Isso permite substituir esses colaboradores por mocks nos testes. `Estacionamento` consulta `ServicoTerreno` para obter o multiplicador do terreno; `ServicoTerreno`, por sua vez, recebe um `Clock`, permitindo testar condições de dia e noite sem depender do horário real.

`Torneio` recebe `RepositorioResultados`, `ServicoNotificacao` e `CalculadoraBonus` pelo construtor. Assim, salvar resultados, enviar conquistas e calcular bônus dependem de contratos explícitos. A falha ao salvar um resultado é tratada sem invalidar o vencedor. A lógica também distingue empate de vitória quando a batalha atinge o limite de turnos.

## Acoplamento e Inversão de Dependência

Antes da refatoração, regras que dependessem diretamente de implementações concretas — como aleatoriedade real, horário do sistema ou gravação de resultados — seriam difíceis de testar isoladamente. A dependência de detalhes de infraestrutura poderia tornar os testes imprevisíveis e obrigar a execução de serviços reais.

Com a Inversão de Dependência, as classes de alto nível recebem colaboradores por construtor e trabalham com interfaces, como `GeradorAleatorio`, `RegistroBatalha`, `RepositorioResultados`, `ServicoNotificacao` e `CalculadoraBonus`. Os testes podem fornecer mocks dessas interfaces; a aplicação pode fornecer implementações reais. `ServicoTerreno` também recebe `Clock`, permitindo injetar um relógio fixo nos testes.

Essa organização facilita testar regras sem depender de banco de dados, notificações externas, horário corrente ou sorteios não determinísticos. As classes ficam menos presas às implementações específicas e mais fáceis de manter e estender.

## Observações

No modo de jogo, as implementações concretas de notificação e cálculo de bônus precisam ser fornecidas ao construtor de `Torneio`; se forem passados `null`, essas funções opcionais não são executadas. Além disso, a interface `CalculadoraBonus` retorna XP, enquanto a implementação atual aplica o valor ao ataque. A equipe deve confirmar se essa conversão corresponde ao requisito pretendido.
