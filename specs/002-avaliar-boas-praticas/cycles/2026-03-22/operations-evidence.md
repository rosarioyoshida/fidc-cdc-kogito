# Operations and Observability Evidence

## Escopo

- compose local
- observabilidade minima
- quickstart e governanca operacional

## Evidencias

| ID | Origem | Resumo | Leitura |
|----|--------|--------|---------|
| OE-001 | D:\desenv\fidc-cdc-kogito\infra\compose\docker-compose.yml | Compose sobe postgres, kafka, jobs-service, data-index, keycloak, consoles, backend e frontend | Boa cobertura de ecossistema local |
| OE-002 | D:\desenv\fidc-cdc-kogito\infra\observability\README.md | README define metricas locais e alvo Prometheus do backend | Observabilidade minima explicitada |
| OE-003 | D:\desenv\fidc-cdc-kogito\specs\001-controle-cessao-fidc\quickstart.md | Quickstart detalha usuarios, URLs, servicos e regressao critica | Boa documentacao operacional |
| OE-004 | D:\desenv\fidc-cdc-kogito\infra\compose\docker-compose.yml e D:\desenv\fidc-cdc-kogito\specs\001-controle-cessao-fidc\quickstart.md | Nome do agrupador diverge: `fidc-cdc-kogito` no compose e `fidc-cdc-kogit` no quickstart | Inconsistencia documental/operacional |
| OE-005 | D:\desenv\fidc-cdc-kogito\AGENTS.md | Arquivo ainda contem entradas placeholder do template | Lacuna de governanca documental |

## Observacoes

- a operacao local esta bem representada, mas ha sinais de drift entre documentacao,
  compose e contexto do agente.
