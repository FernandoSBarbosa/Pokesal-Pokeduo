# Relatório Comparativo de Qualidade de Código — SonarQube (Fase 02 vs. Fase 03)

**Projeto:** Pokeduo  
**Status do Quality Gate (Fase 03):** `Passed` (Todas as condições aprovadas)

---

## 1. Visão Geral

Este relatório apresenta a evolução da qualidade estática do código do projeto **Pokeduo** entre a **Fase 02** e a **Fase 03**, com base nas métricas extraídas pelo **SonarQube Community**.

> **Nota sobre nomenclatura:** Nas versões mais recentes do SonarQube, as categorias tradicionais foram mapeadas para os pilares de *Software Quality*:
> - **Code Smells** $\rightarrow$ *Maintainability issues*
> - **Bugs** $\rightarrow$ *Reliability issues*
> - **Vulnerabilities** $\rightarrow$ *Security issues*

---

## 2. Tabela Comparativa de Métricas

| Métrica | Fase 02 | Fase 03 | Variação ($\Delta$) | Status / Evolução |
| :--- | :---: | :---: | :---: | :--- |
| **Code Smells** (*Maintainability*) | **59** (Rating **A**) | **0** (Rating **A**) | $-59$ ($-100\%$) | Melhorou significativamente |
| **Bugs** (*Reliability*) | **1** (Rating **D**) | **1** (Rating **D**) | $0$ ($0\%$) | Estável (mantido) |
| **Vulnerabilities** (*Security*) | **0** (Rating **A**) | **0** (Rating **A**) | $0$ ($0\%$) | Estável (mantido) |
| **Coverage %** | **33,3%** | **0,0%** (*No data*) | $-33,3\text{ p.p.}$ | **Piorou** |
| **Duplications** | **0,0%** | **0,0%** | $0,0\text{ p.p.}$ | Estável (mantido) |

*Outras métricas observadas:*
- **Security Hotspots:** Manteve-se em **0** (Rating **A**).
- **Total de Issues Abertas:** Redução de **60** (Fase 02) para **1** (Fase 03).

---

## 3. Análise das Métricas

1. **Code Smells (59 $\rightarrow$ 0):** Houve uma eliminação completa de todos os 59 problemas de manutenibilidade apontados na Fase 02 (como alta complexidade cognitiva e uso direto de `System.out`), demonstrando um forte esforço de refatoração e limpeza de código na Fase 03.
2. **Bugs (1 $\rightarrow$ 1):** O projeto manteve **1 issue** de confiabilidade de severidade *High* (100% das issues de *Reliability*), o que mantém a nota **D** neste pilar.
3. **Vulnerabilities (0 $\rightarrow$ 0):** Nenhuma vulnerabilidade de segurança ou *Security Hotspot* foi introduzido, mantendo a nota máxima (**A**).
4. **Duplications (0,0% $\rightarrow$ 0,0%):** Mesmo após as refatorações realizadas para zerar os *Code Smells*, o código permaneceu totalmente livre de blocos duplicados.
5. **Coverage % (33,3% $\rightarrow$ Sem dados / 0,0%):** Foi a única métrica que apresentou retrocesso no painel do SonarQube em relação à Fase 02.

---

## 4. Justificativa das Métricas que Pioraram

### 4.1. Queda na Cobertura de Testes (*Coverage %*)

Na Fase 02, o SonarQube registrava **33,3%** de cobertura de testes (sobre 303 linhas cobríveis). Na Fase 03, o painel passou a exibir **"No data to display yet"** (ausência de dados de cobertura reportados).

Essa regressão justifica-se pelos seguintes fatores técnicos:

1. **Ausência de geração ou importação do relatório de cobertura (JaCoCo):**  
   O SonarQube não executa os testes por conta própria; ele apenas lê o arquivo XML gerado por ferramentas de cobertura (como o **JaCoCo** em projetos Java/Maven/Gradle). Quando a análise da Fase 03 foi disparada diretamente sem a etapa prévia de execução dos testes e geração do relatório (por exemplo, executando apenas `mvn sonar:sonar` em vez de `mvn clean test jacoco:report sonar:sonar`), o *SonarScanner* não encontrou métricas de cobertura para importar.
2. **Impacto das refatorações estruturais da Fase 03:**  
   Para reduzir os *Code Smells* de **59 para 0**, métodos e classes passaram por refatorações significativas (como extração de métodos para reduzir complexidade cognitiva e alteração de assinaturas/responsabilidades). Caso os testes unitários da Fase 02 tenham ficado desatualizados em relação às novas assinaturas ou não tenham sido executados durante o *build* da análise, o relatório de cobertura deixa de ser produzido.

### 4.2. Ponto de Atenção: Estagnação em *Bugs* (*Reliability*)

Embora a métrica de **Bugs** não tenha piorado numericamente (permanecendo em **1 issue** de severidade *High* e nota **D**), ela também não evoluiu em comparação à Fase 02. Isso indica que todo o esforço de melhoria da Fase 03 foi direcionado à **Manutenibilidade** (*Code Smells*), deixando pendente a correção da única falha de confiabilidade existente no código.

---

## 5. Recomendações para a Próxima Fase

- **Restaurar e ampliar o *Coverage %*:** Garantir que o plugin do **JaCoCo** esteja configurado no `pom.xml` (ou `build.gradle`) e executar o ciclo completo de testes antes da análise do SonarQube (`mvn clean verify sonar:sonar`), atualizando os testes unitários para cobrir os métodos refatorados.
- **Alcançar Rating A em *Reliability*:** Corrigir a única *issue* de severidade *High* restante na categoria *Reliability* (como o reaproveitamento de instância de `Random` em `GeradorBots.java`), elevando a nota de **D** para **A**.