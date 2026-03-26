# Tasks: Addon Process SVG no Management Console

**Input**: Design documents from `/specs/008-management-process-svg/`
**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/, quickstart.md

**Tests**: A feature exige validacao tecnica e de integracao com o Management Console; por isso as tarefas incluem testes de integracao e arquitetura no backend.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g. `US1`, `US2`, `US3`)
- Include exact file paths in descriptions

## Path Conventions

- Backend application code lives under `backend/src/main/java/com/fidc/cdc/kogito/`
- Backend resources live under `backend/src/main/resources/`
- Backend automated tests live under `backend/src/test/java/com/fidc/cdc/kogito/`
- Feature artifacts live under `specs/008-management-process-svg/`

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Preparar dependencias, estrutura de recursos e baseline de teste para o addon oficial.

- [X] T001 Adicionar a dependencia `org.kie:kie-addons-springboot-process-svg` em `backend/pom.xml`
- [X] T002 Criar a pasta oficial do addon e registrar o SVG do processo em `backend/src/main/resources/META-INF/processSVG/controle-cessao.svg`
- [X] T003 [P] Registrar configuracoes e observacoes operacionais do addon Process SVG em `backend/src/main/resources/application.yml` e `infra/compose/docker-compose.yml`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Eliminar duplicidade de fonte SVG, alinhar seguranca e preparar a base de observabilidade antes de qualquer user story.

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [X] T004 Revisar o contrato do runtime e documentar a migracao do endpoint SVG manual para o addon oficial em `specs/008-management-process-svg/contracts/ui-process-svg-contract.md`
- [X] T005 Refatorar `backend/src/main/java/com/fidc/cdc/kogito/api/process/KogitoConsoleRuntimeController.java` para remover ou restringir o uso do endpoint manual `/svg/processes/{processId}/instances/{processInstanceId}` como contrato principal
- [X] T006 Refatorar `backend/src/main/java/com/fidc/cdc/kogito/application/process/ConsoleRuntimeEndpointService.java` para retirar a geracao manual de SVG e preservar apenas responsabilidades nao cobertas pelo addon oficial
- [X] T007 [P] Definir e aplicar a estrategia de autenticacao/autorizacao entre Management Console e backend para os endpoints SVG e `management/processes` em `backend/src/main/java/com/fidc/cdc/kogito/security/SecurityConfig.java`, `infra/compose/docker-compose.yml` e `specs/008-management-process-svg/contracts/ui-process-svg-contract.md`
- [X] T008 [P] Validar a compatibilidade da autenticacao do Management Console com os endpoints do addon em `backend/src/test/java/com/fidc/cdc/kogito/integration/console/ConsoleRuntimeEndpointsIntegrationTest.java`
- [X] T009 [P] Instrumentar sinais observaveis de sucesso, SVG ausente, etapa nao resolvida e falhas de consulta em `backend/src/main/java/com/fidc/cdc/kogito/api/security/PermissionController.java` e `backend/src/main/java/com/fidc/cdc/kogito/application/process/ManagementConsoleSupport.java`
- [X] T010 [P] Atualizar o baseline de integracao da stack externa em `backend/src/test/java/com/fidc/cdc/kogito/architecture/ContainerCommunicationTopologyTest.java` e `infra/compose/docker-compose.yml`

**Checkpoint**: Foundation ready - user story implementation can now begin in parallel

---

## Phase 3: User Story 1 - Visualizar Fluxo Atual (Priority: P1) 🎯 MVP

**Goal**: Habilitar o addon `Process SVG` para instancias elegiveis e exibir o fluxo real em SVG no Management Console.

**Independent Test**: Abrir uma instancia elegivel no Management Console e confirmar que o addon aparece e renderiza o SVG com o caminho executado.

### Tests for User Story 1

- [X] T011 [P] [US1] Atualizar o teste de elegibilidade e contexto do console em `backend/src/test/java/com/fidc/cdc/kogito/integration/console/KogitoConsoleIntegrationTest.java`
- [X] T012 [P] [US1] Atualizar a validacao ponta a ponta do diagrama no console em `backend/src/test/java/com/fidc/cdc/kogito/integration/console/ConsoleRuntimeEndpointsIntegrationTest.java`

### Implementation for User Story 1

- [X] T013 [US1] Configurar o processo `controle-cessao` e o runtime para expor elegibilidade real do addon Process SVG em `backend/src/main/resources/processes/controle-cessao.bpmn`, `backend/src/main/resources/application.yml` e `backend/src/main/java/com/fidc/cdc/kogito/application/process/ManagementConsoleSupport.java`
- [X] T014 [US1] Atualizar a integracao de runtime do console para consumir o contrato oficial do addon Process SVG em `backend/src/main/java/com/fidc/cdc/kogito/api/process/KogitoConsoleRuntimeController.java` e `backend/src/main/java/com/fidc/cdc/kogito/application/process/ConsoleRuntimeEndpointService.java`
- [X] T015 [US1] Garantir que o contexto do Management Console diferencie disponibilidade do SVG e autorizacao de acesso em `backend/src/main/java/com/fidc/cdc/kogito/application/process/ManagementConsoleSupport.java` e `backend/src/main/java/com/fidc/cdc/kogito/application/process/ManagementConsoleContext.java`
- [X] T016 [US1] Validar a integracao do addon com a stack externa e registrar o fluxo esperado em `specs/008-management-process-svg/quickstart.md`

**Checkpoint**: At this point, User Story 1 should be fully functional and testable independently

---

## Phase 4: User Story 2 - Entender Estado Sem Ambiguidade (Priority: P2)

**Goal**: Fazer a indicacao textual da etapa atual seguir a mesma fonte de verdade operacional usada para o destaque visual.

**Independent Test**: Abrir uma instancia com etapa ativa e confirmar que o nome textual da etapa coincide com o destaque visual e reflete atualizacoes do estado.

### Tests for User Story 2

- [X] T017 [P] [US2] Cobrir etapa textual atual e reconciliacao com o status do processo em `backend/src/test/java/com/fidc/cdc/kogito/integration/console/KogitoConsoleIntegrationTest.java`
- [X] T018 [P] [US2] Cobrir atualizacao da etapa textual mais recente na stack externa em `backend/src/test/java/com/fidc/cdc/kogito/integration/console/ConsoleRuntimeEndpointsIntegrationTest.java`

### Implementation for User Story 2

- [X] T019 [US2] Expandir `backend/src/main/java/com/fidc/cdc/kogito/application/process/ManagementConsoleContext.java` para expor rotulo textual, origem da etapa e indicadores de processo encerrado
- [X] T020 [US2] Implementar o mapeamento textual da etapa atual e da ultima etapa concluida em `backend/src/main/java/com/fidc/cdc/kogito/application/process/ManagementConsoleSupport.java`
- [X] T021 [US2] Alinhar a resposta do contexto do console com a nova informacao textual em `backend/src/main/java/com/fidc/cdc/kogito/api/security/PermissionController.java`
- [X] T022 [US2] Atualizar o contrato da feature com a precedencia da etapa textual sobre divergencias visuais em `specs/008-management-process-svg/contracts/ui-process-svg-contract.md`

**Checkpoint**: At this point, User Stories 1 and 2 should both work independently

---

## Phase 5: User Story 3 - Receber Feedback Quando o Fluxo Não Estiver Disponível (Priority: P3)

**Goal**: Entregar feedback claro para SVG ausente, etapa indeterminada, processo encerrado e acesso nao autorizado.

**Independent Test**: Simular ausencia de SVG, processo concluido sem etapa ativa, etapa indeterminada e falha de acesso, confirmando mensagens claras e diagnostico observavel.

### Tests for User Story 3

- [X] T023 [P] [US3] Cobrir ausencia de SVG, processo encerrado e acesso nao autorizado em `backend/src/test/java/com/fidc/cdc/kogito/integration/console/KogitoConsoleIntegrationTest.java`
- [X] T024 [P] [US3] Cobrir comportamento do console para fluxo sem SVG ou com divergencia temporaria em `backend/src/test/java/com/fidc/cdc/kogito/integration/console/ConsoleRuntimeEndpointsIntegrationTest.java`

### Implementation for User Story 3

- [X] T025 [US3] Implementar fallback de processo encerrado e sinalizacao de etapa indeterminada em `backend/src/main/java/com/fidc/cdc/kogito/application/process/ManagementConsoleSupport.java`
- [X] T026 [US3] Implementar razoes de indisponibilidade e autorizacao do addon em `backend/src/main/java/com/fidc/cdc/kogito/application/process/ManagementConsoleContext.java` e `backend/src/main/java/com/fidc/cdc/kogito/api/security/PermissionController.java`
- [X] T027 [US3] Endurecer tratamento de acesso negado e caminhos nao elegiveis em `backend/src/main/java/com/fidc/cdc/kogito/security/SecurityConfig.java` e `backend/src/main/java/com/fidc/cdc/kogito/api/error/ProblemDetailsHandler.java`
- [X] T028 [US3] Registrar evidencias observaveis de `svg-missing`, `no-current-stage`, `unauthorized` e `error` em `backend/src/main/java/com/fidc/cdc/kogito/application/process/ManagementConsoleSupport.java` e `backend/src/main/java/com/fidc/cdc/kogito/infrastructure/observability/`
- [X] T029 [US3] Atualizar o roteiro de validacao dos estados de falha e fallback em `specs/008-management-process-svg/quickstart.md`

**Checkpoint**: All user stories should now be independently functional

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Fechar governanca, evidencias finais e validacao completa da entrega.

- [X] T030 [P] Revisar impactos arquiteturais e remover referencias residuais ao SVG manual em `backend/src/main/java/com/fidc/cdc/kogito/api/process/` e `backend/src/main/java/com/fidc/cdc/kogito/application/process/`
- [X] T031 [P] Validar observabilidade, auditoria e mensagens de erro da feature em `specs/008-management-process-svg/quickstart.md` e `specs/008-management-process-svg/research.md`
- [X] T032 Validar performance e prontidao do addon em `specs/008-management-process-svg/quickstart.md` usando `backend/src/test/java/com/fidc/cdc/kogito/integration/console/ConsoleRuntimeEndpointsIntegrationTest.java`
- [X] T033 Executar a validacao completa do quickstart e registrar evidencias finais em `specs/008-management-process-svg/quickstart.md`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Stories (Phase 3+)**: All depend on Foundational phase completion
- **Polish (Phase 6)**: Depends on all desired user stories being complete

### User Story Dependencies

- **User Story 1 (P1)**: Can start after Foundational - no dependency on other stories
- **User Story 2 (P2)**: Depends on User Story 1 because the indicacao textual precisa reconciliar o addon Process SVG ja habilitado
- **User Story 3 (P3)**: Depends on User Stories 1 and 2 because os fallbacks e mensagens de falha dependem do fluxo visual e textual ja integrados

### Within Each User Story

- Tests must be updated before implementation is considered complete
- Runtime/addon integration before context reconciliation
- Context reconciliation before fallback and error-state hardening
- Quickstart validation after implementation for each completed story

### Parallel Opportunities

- Setup: `T003`
- Foundational: `T007`, `T008`, `T009`, `T010`
- US1: `T011`, `T012`
- US2: `T017`, `T018`
- US3: `T023`, `T024`
- Polish: `T030`, `T031`

---

## Parallel Example: User Story 1

```bash
# Launch both User Story 1 validation tasks together:
Task: "Atualizar o teste de elegibilidade e contexto do console em backend/src/test/java/com/fidc/cdc/kogito/integration/console/KogitoConsoleIntegrationTest.java"
Task: "Atualizar a validacao ponta a ponta do diagrama no console em backend/src/test/java/com/fidc/cdc/kogito/integration/console/ConsoleRuntimeEndpointsIntegrationTest.java"
```

---

## Parallel Example: User Story 3

```bash
# Launch User Story 3 failure-state validations together:
Task: "Cobrir ausencia de SVG, processo encerrado e acesso nao autorizado em backend/src/test/java/com/fidc/cdc/kogito/integration/console/KogitoConsoleIntegrationTest.java"
Task: "Cobrir comportamento do console para fluxo sem SVG ou com divergencia temporaria em backend/src/test/java/com/fidc/cdc/kogito/integration/console/ConsoleRuntimeEndpointsIntegrationTest.java"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational
3. Complete Phase 3: User Story 1
4. Validate the addon rendering in the Management Console
5. Stop for review if only MVP is desired

### Incremental Delivery

1. Setup + Foundational establish the official addon and remove the manual SVG path as primary contract
2. Add User Story 1 to expose the diagram in the Management Console
3. Add User Story 2 to reconcile textual current-stage context
4. Add User Story 3 to cover no-SVG, no-stage, unauthorized and ended-process feedback
5. Finish with polish, observability and quickstart evidence

### Parallel Team Strategy

With multiple developers:

1. One developer handles addon/runtime wiring (`T001`-`T006`)
2. One developer handles security and observability (`T007`-`T009`)
3. After foundation, one developer focuses on console diagram validation (`US1`), another on textual reconciliation (`US2`), and another on fallback/error states (`US3`)

---

## Notes

- [P] tasks = different files, no dependencies on incomplete tasks
- [Story] labels map each task to a user story
- Each story remains independently testable through the console/runtime integration flow
- The feature introduces no new frontend-local React components
- REST and security tasks explicitly cover the runtime endpoints touched by the addon
