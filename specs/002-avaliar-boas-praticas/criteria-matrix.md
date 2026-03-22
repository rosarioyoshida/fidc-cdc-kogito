# Criteria Matrix

## Objetivo

Padronizar os criterios usados para avaliar o projeto com base nas diretrizes do
repositorio, na constituicao vigente e em boas praticas complementares apenas quando
houver lacunas.

| ID | Area | Criterio | Fonte principal | Obrigatorio | Evidencia esperada |
|----|------|----------|-----------------|-------------|--------------------|
| CR-001 | Implementacao | Modulos e fluxos principais possuem coesao, nomes claros e separacao consistente de responsabilidades | D:\desenv\fidc-cdc-kogito\.specify\memory\constitution.md | Sim | Classes, componentes e estrutura de pastas |
| CR-002 | Configuracao | Configuracoes operacionais estao externalizadas e com defaults coerentes por ambiente | D:\desenv\fidc-cdc-kogito\backend\src\main\resources\application.yml | Sim | Arquivos de configuracao e compose |
| CR-003 | Seguranca | Autenticacao, autorizacao e tratamento de dados sensiveis estao explicitados e coerentes com o ambiente | D:\desenv\fidc-cdc-kogito\backend\src\main\java\com\fidc\cdc\kogito\security\SecurityConfig.java | Sim | Configuracao de seguranca, seeds e quickstart |
| CR-004 | Observabilidade | Logs, metricas, health checks e correlacao cobrem os fluxos criticos | D:\desenv\fidc-cdc-kogito\infra\observability\README.md | Sim | README, Prometheus e logging |
| CR-005 | Testes | O projeto possui comandos reexecutaveis e suites relevantes para regressao | D:\desenv\fidc-cdc-kogito\backend\pom.xml | Sim | Comandos Maven/Vitest e resultados |
| CR-006 | UX | A interface usa tokens, semantica visual e componentes consistentes com o design system adotado | D:\desenv\fidc-cdc-kogito\frontend\src\design-system\theme.css | Sim | Tokens, componentes e testes de interface |
| CR-007 | Documentacao operacional | Setup, operacao local e contratos estao documentados para a equipe | D:\desenv\fidc-cdc-kogito\specs\001-controle-cessao-fidc\quickstart.md | Sim | Quickstarts, contratos e AGENTS |
| CR-008 | Governanca | A stack permanece consistente e novas dependencias exigem decisao explicita | D:\desenv\fidc-cdc-kogito\specs\002-avaliar-boas-praticas\plan.md | Sim | Plano, tasks e restricoes documentadas |

## Regras de uso

- Todo achado deve apontar para pelo menos um criterio desta matriz.
- Todo criterio obrigatorio deve ser coberto por evidencia ou marcado como
  `nao verificavel` com justificativa.
