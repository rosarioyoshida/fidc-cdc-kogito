# Quickstart: Avaliacao de Boas Praticas do Projeto

## Objetivo

Executar um ciclo minimo de avaliacao usando a stack, a estrutura e os comandos ja
adotados pelo projeto.

## Pre-requisitos

- Java 21 disponivel para o backend
- Node.js compatível com o frontend atual
- Ambiente local configurado para executar Maven e npm
- Nenhuma nova dependencia adicionada sem consulta previa

## 1. Validar o estado atual do repositorio

```powershell
git status --short
```

Confirme quais alteracoes pertencem ao ciclo atual antes de consolidar evidencias.

## 2. Executar validacoes existentes do backend

```powershell
cd D:\desenv\fidc-cdc-kogito\backend
mvn test
```

Use os resultados como evidencia para os criterios de testes, contratos,
observabilidade, seguranca e regressao funcional.

## 3. Executar validacoes existentes do frontend

```powershell
cd D:\desenv\fidc-cdc-kogito\frontend
npm run lint
npm test
```

Use os resultados como evidencia para criterios de UX, acessibilidade, integracao,
organizacao de interface e qualidade de entrega.

## 4. Revisar artefatos estruturais e operacionais

Inspecione, no minimo:

- `AGENTS.md`
- `backend/src/main/resources/application.yml`
- `backend/src/main/resources/db/migration/`
- `backend/src/main/resources/processes/`
- `frontend/src/design-system/`
- `infra/compose/docker-compose.yml`
- `infra/observability/`
- `specs/001-controle-cessao-fidc/`

## 5. Consolidar o relatorio

Monte o relatorio seguindo o contrato em
[assessment-report.md](D:/desenv/fidc-cdc-kogito/specs/002-avaliar-boas-praticas/contracts/assessment-report.md).

O relatorio deve conter:

- escopo avaliado
- criterios aplicados
- evidencias por area
- achados com severidade e impacto
- parecer final de prontidao
- plano de adequacao priorizado

## 6. Repetir em fases futuras

Ao iniciar uma nova fase relevante do projeto:

1. reutilize os mesmos criterios;
2. compare os achados com o ciclo anterior;
3. destaque evolucoes, regressões e pendencias remanescentes;
4. mantenha a classificacao objetiva de prontidao.
