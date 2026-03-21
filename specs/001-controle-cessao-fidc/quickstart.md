# Quickstart: Controle de Cessao de FIDC

## Objetivo

Subir o ambiente minimo local para desenvolvimento e validacao do fluxo de cessao com
backend, frontend, workflow BPMN, consulta consolidada e servicos auxiliares.

## Pre-requisitos

- Docker e Docker Compose disponiveis
- Java 21 para desenvolvimento local do backend
- Node.js LTS para desenvolvimento local do frontend

## Variaveis de ambiente principais

### Backend

- `SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/fidc_cdc`
- `SPRING_DATASOURCE_USERNAME=fidc`
- `SPRING_DATASOURCE_PASSWORD=fidc`
- `SPRING_DATA_MONGODB_URI=mongodb://localhost:27017/fidc_cdc_read`
- `SPRING_KAFKA_BOOTSTRAP_SERVERS=localhost:9092`
- `FIDC_JOBS_SERVICE_URL=http://localhost:8085`
- `FIDC_JOBS_CALLBACK_BASE_URL=http://localhost:8080/api/v1/process/jobs/callbacks`
- `FIDC_DATA_INDEX_URL=http://localhost:8180/graphql`
- `FIDC_TASK_CONSOLE_URL=http://localhost:8280`
- `FIDC_MANAGEMENT_CONSOLE_URL=http://localhost:8380`
- `FIDC_REGISTRADORA_BASE_URL=http://localhost:8090`

### Frontend

- `NEXT_PUBLIC_FIDC_API_URL=http://localhost:8080/api/v1`
- `NEXT_PUBLIC_FIDC_API_USERNAME=operador`
- `NEXT_PUBLIC_FIDC_API_PASSWORD=operador123`
- `NEXT_PUBLIC_FIDC_TASK_CONSOLE_URL=http://localhost:8280`
- `NEXT_PUBLIC_FIDC_MANAGEMENT_CONSOLE_URL=http://localhost:8380`

## Servicos previstos no compose

- `backend`
- `frontend`
- `mysql`
- `mongodb`
- `kafka`
- `jobs-service`
- `task-console`
- `management-console`
- `data-index`

## Nome do agrupador

O compose deve usar o agrupador:

```text
fidc-cdc-kogit
```

## Fluxo de inicializacao esperado

1. Iniciar infraestrutura de dados e eventos.
2. Iniciar servicos do ecossistema Kogito.
3. Iniciar backend.
4. Iniciar frontend.
5. Validar acesso aos consoles e a pagina inicial da aplicacao.

## Comandos locais

### Subir stack minima

```powershell
docker compose -f infra/compose/docker-compose.yml up -d
docker compose -f infra/compose/docker-compose.yml ps
```

### Backend

```powershell
cd backend
mvn -q -DskipTests compile
mvn -q test -DskipITs
```

### Frontend

```powershell
cd frontend
npm run test
npm run build
npx next typegen
npx tsc --noEmit --incremental false
```

## URLs locais esperadas

- Frontend: `http://localhost:3000`
- Backend API: `http://localhost:8080/api/v1`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- Jobs Service: `http://localhost:8085`
- Data Index: `http://localhost:8180/graphql`
- Task Console: `http://localhost:8280`
- Management Console: `http://localhost:8380`
- Prometheus endpoint backend: `http://localhost:8080/actuator/prometheus`

## Validacoes minimas

1. Importar uma cessao de teste.
2. Confirmar criacao da instancia de processo.
3. Consultar a cessao na interface operacional.
4. Executar ao menos uma etapa integrada com registradora simulada ou controlada.
5. Validar visibilidade no Task Console e no Management Console.
6. Confirmar atualizacao da visao consolidada.

## Evidencias esperadas

- API do backend documentada
- fluxo BPMN implantado
- retries da registradora observaveis
- dados transacionais persistidos no MySQL
- visao consolidada disponivel via Data Index e MongoDB
- links HATEOAS para `analise`, `auditoria`, `permissoes`, `etapas` e `historico`
- payloads RFC 9457 com `type`, `title`, `detail`, `instance`, `timestamp` e `correlationId`
- metricas Prometheus para processo, registradora, consoles e projecao

## Baseline operacional inicial

- ao menos 50 cessoes ativas simultaneamente
- centenas de contratos por cessao
- milhares de parcelas por ciclo operacional
- consulta de status e historico em ate 3 segundos
- atualizacao da visao consolidada em ate 10 segundos

## Validacao de metas operacionais

1. Criar uma cessao e confirmar retorno `201` com links HATEOAS completos.
2. Confirmar consulta de detalhe e historico em ate `3s` no ambiente local controlado.
3. Confirmar que `ultimaAtualizacao` do read model permanece em ate `10s` da execucao do evento de processo.
4. Validar retries da registradora por logs e metricas `fidc.registradora.retries`.
5. Confirmar exposicao de `fidc.process.events`, `fidc.console.access` e `fidc.readmodel.projection.lag.ms` em `/actuator/prometheus`.

## Notas de validacao executada

- `docker compose config`: ok
- `mvn -q -DskipTests compile`: ok
- `mvn -q test -DskipITs`: ok, com testes Testcontainers pulados por indisponibilidade de Docker valido no host
- `npm run test`: ok
- `npm run build`: ok
- `npx next typegen`: ok
- `npx tsc --noEmit --incremental false`: ok

## Regressao critica recomendada

1. Criar cessao e avancar etapas permitidas com `operador`.
2. Bloquear tentativa de etapa com perfil indevido e validar RFC 9457.
3. Executar operacao da registradora com retry controlado.
4. Consultar `analise`, `auditoria` e `permissoes` no frontend.
5. Confirmar visibilidade no Task Console e no Management Console.
