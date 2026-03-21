# Quickstart: Controle de Cessao de FIDC

## Objetivo

Subir o ambiente minimo local para desenvolvimento e validacao do fluxo de cessao com
backend, frontend, workflow BPMN, consulta consolidada e servicos auxiliares.

## Pre-requisitos

- Docker e Docker Compose disponiveis
- Java 21 para desenvolvimento local do backend
- Node.js LTS para desenvolvimento local do frontend

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

## Baseline operacional inicial

- ao menos 50 cessoes ativas simultaneamente
- centenas de contratos por cessao
- milhares de parcelas por ciclo operacional
- consulta de status e historico em ate 3 segundos
- atualizacao da visao consolidada em ate 10 segundos
