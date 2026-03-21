# Observability Notes

Local observability assets for the FIDC CDC Kogito stack.

## Covered metrics

- `fidc.process.events`: processo, timers BPMN e callbacks do Jobs Service
- `fidc.registradora.calls`: chamadas sincronas da registradora por operacao e outcome
- `fidc.registradora.retries`: retries automaticos da registradora
- `fidc.console.access`: exposicao de contexto para Task Console, Management Console e auditoria
- `fidc.readmodel.projection.lag.ms`: atraso/processamento da projecao do read model

## Local scrape targets

- Backend Spring Boot Prometheus: `http://backend:8080/actuator/prometheus`
- Frontend Next.js: sem endpoint Prometheus; validar via logs e health page do container
- Kafka/Data Index/Jobs Service/Task Console/Management Console: validar disponibilidade pelo compose e operacao de ponta a ponta

## Arquivos

- `prometheus.yml`: configuracao local minima para coleta do backend
