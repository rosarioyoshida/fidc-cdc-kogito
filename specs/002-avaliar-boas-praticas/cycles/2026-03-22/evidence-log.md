# Evidence Log - 2026-03-22

## Metadados

- Ciclo: 2026-03-22
- Data: 2026-03-22
- Responsavel: Codex

## Evidencias consolidadas

| ID | Area | Tipo | Origem | Resumo | Confiabilidade |
|----|------|------|--------|--------|----------------|
| E-001 | Implementacao | configuracao | D:\desenv\fidc-cdc-kogito\backend\src\main\resources\application.yml | Configuracao externalizada para datasource, Kafka, jobs e registradora | Alta |
| E-002 | Seguranca | codigo | D:\desenv\fidc-cdc-kogito\backend\src\main\java\com\fidc\cdc\kogito\security\SecurityConfig.java | Basic Auth em memoria no backend | Alta |
| E-003 | Observabilidade | configuracao | D:\desenv\fidc-cdc-kogito\backend\src\main\resources\log4j2-spring.xml | `correlationId` presente no pattern de logs | Alta |
| E-004 | UX | codigo | D:\desenv\fidc-cdc-kogito\frontend\src\design-system\theme.css | Tokens semanticos para luz/escuro | Alta |
| E-005 | Testes | comando | `mvn test` | Build sucesso com integracoes majoritariamente skipped sem Docker valido | Media |
| E-006 | Testes | comando | `npm test` | Suite Vitest aprovada | Alta |
| E-007 | Qualidade de entrega | comando | `npm run lint` | Lint interativo e nao automatizavel | Alta |
| E-008 | Operacao | compose | D:\desenv\fidc-cdc-kogito\infra\compose\docker-compose.yml | Stack local completa com Kogito, consoles e Keycloak | Alta |
| E-009 | Observabilidade | documentacao | D:\desenv\fidc-cdc-kogito\infra\observability\README.md | Metricas e alvo Prometheus definidos | Alta |
| E-010 | Governanca | documentacao | D:\desenv\fidc-cdc-kogito\AGENTS.md | Arquivo ainda contem linhas placeholder herdadas do template | Alta |

## Limitacoes

- sem Docker valido no host, testes Testcontainers nao exerceram toda a integracao;
- nao houve adicao de dependencias nem alteracao da stack durante este ciclo.
