# Tasks: Controle de Cessao de FIDC

**Input**: Design documents from `/specs/001-controle-cessao-fidc/`
**Prerequisites**: [plan.md](D:/desenv/fidc-cdc-kogito/specs/001-controle-cessao-fidc/plan.md), [spec.md](D:/desenv/fidc-cdc-kogito/specs/001-controle-cessao-fidc/spec.md), [research.md](D:/desenv/fidc-cdc-kogito/specs/001-controle-cessao-fidc/research.md), [data-model.md](D:/desenv/fidc-cdc-kogito/specs/001-controle-cessao-fidc/data-model.md), [contracts/](D:/desenv/fidc-cdc-kogito/specs/001-controle-cessao-fidc/contracts)

**Tests**: O fluxo exige testes automatizados proporcionais ao risco, incluindo testes
de integracao backend, contratos REST, validacao de BPMN, autorizacao por perfil,
retries da registradora e testes de interface para os fluxos operacionais criticos.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and basic structure

- [X] T001 Create backend Maven structure and dependency management in `backend/pom.xml`
- [X] T002 [P] Create frontend Next.js workspace and package manifest in `frontend/package.json`
- [X] T003 [P] Create Docker Compose base stack with group `fidc-cdc-kogit` in `infra/compose/docker-compose.yml`
- [X] T004 [P] Create backend runtime configuration skeleton in `backend/src/main/resources/application.yml`
- [X] T005 [P] Create frontend app shell, Tailwind, and theme configuration in `frontend/src/app/layout.tsx`, `frontend/tailwind.config.ts`, and `frontend/src/app/globals.css`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core infrastructure that MUST be complete before ANY user story can be implemented

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [X] T006 Configure Spring Boot dependencies for OpenAPI, HATEOAS, Bean Validation, Flyway, JPA, PostgreSQL, Kogito, and remove Logback in `backend/pom.xml`
- [X] T007 [P] Configure Log4j2, SLF4J, correlation IDs, and structured logging in `backend/src/main/resources/log4j2-spring.xml` and `backend/src/main/java/com/fidc/cdc/kogito/infrastructure/logging/CorrelationLoggingFilter.java`
- [X] T008 [P] Configure Basic Auth, Spring Security, and role mapping base in `backend/src/main/java/com/fidc/cdc/kogito/security/SecurityConfig.java`
- [X] T009 [P] Create Flyway baseline migration for core tables in `backend/src/main/resources/db/migration/V1__baseline.sql`
- [X] T010 [P] Create shared JPA auditing, base entity, and persistence configuration in `backend/src/main/java/com/fidc/cdc/kogito/infrastructure/persistence/BaseEntity.java`, `backend/src/main/java/com/fidc/cdc/kogito/infrastructure/persistence/JpaConfig.java`
- [X] T011 [P] Create RFC 9457 problem details handler and error type registry in `backend/src/main/java/com/fidc/cdc/kogito/api/error/ProblemDetailsHandler.java` and `backend/src/main/java/com/fidc/cdc/kogito/api/error/ProblemTypeRegistry.java`
- [X] T012 [P] Create API versioning, REST path conventions, and HATEOAS link factory base in `backend/src/main/java/com/fidc/cdc/kogito/api/config/ApiVersionConfig.java` and `backend/src/main/java/com/fidc/cdc/kogito/api/hateoas/CessaoLinkAssembler.java`
- [X] T013 [P] Create Kogito process project skeleton and BPMN resource placeholder in `backend/src/main/resources/processes/controle-cessao.bpmn`
- [X] T014 [P] Create PostgreSQL, Kafka, Data Index, Jobs Service, Task Console, and Management Console service definitions in `infra/compose/docker-compose.yml` and `infra/postgres/init/01-create-databases.sql`
- [X] T015 [P] Create frontend design token layer and Atlassian-aligned theme contracts in `frontend/src/design-system/tokens.ts`, `frontend/src/design-system/theme.css`, and `frontend/src/design-system/color-semantics.ts`
- [X] T016 [P] Create frontend API client, auth wiring, and RFC 9457 response handling in `frontend/src/lib/api-client.ts`, `frontend/src/lib/auth.ts`, and `frontend/src/lib/problem-details.ts`
- [X] T017 [P] Create reusable UI primitives adapted from shadcn/ui in `frontend/src/components/ui/button.tsx`, `frontend/src/components/ui/input.tsx`, `frontend/src/components/ui/dialog.tsx`, and `frontend/src/components/ui/table.tsx`
- [X] T018 Create shared observability and audit event publication base in `backend/src/main/java/com/fidc/cdc/kogito/observability/ProcessMetricsService.java`, `backend/src/main/java/com/fidc/cdc/kogito/observability/RegistradoraMetricsService.java`, and `backend/src/main/java/com/fidc/cdc/kogito/application/audit/AuditEventPublisher.java`
- [X] T019 [P] Create backend JUnit and Spring Boot integration test base in `backend/src/test/java/com/fidc/cdc/kogito/support/IntegrationTestBase.java`
- [X] T020 [P] Create backend contract test base for REST APIs and RFC 9457 responses in `backend/src/test/java/com/fidc/cdc/kogito/support/ApiContractTestBase.java`
- [X] T021 [P] Create frontend test setup for component and integration tests in `frontend/tests/setup.ts` and `frontend/vitest.config.ts`
- [X] T022 [P] Configure Kogito Jobs Service properties and timer callback support in `backend/src/main/resources/application.yml` and `backend/src/main/java/com/fidc/cdc/kogito/process/JobsCallbackController.java`
- [X] T023 [P] Configure Kafka topics, producers, and event serialization for process, task, and audit events in `backend/src/main/java/com/fidc/cdc/kogito/infrastructure/messaging/`
- [X] T024 Configure Data Index, Task Console, and Management Console runtime wiring in `infra/compose/docker-compose.yml`

**Checkpoint**: Foundation ready - user story implementation can now begin in parallel

---

## Phase 3: User Story 1 - Conduzir a cessao ponta a ponta (Priority: P1) 🎯 MVP

**Goal**: Permitir registrar a cessao, iniciar o workflow BPMN, avancar pelas etapas controladas e consultar status e historico ponta a ponta.

**Independent Test**: Criar uma cessao, iniciar o fluxo, executar a progressao controlada entre etapas e consultar status/historico sem depender das regras detalhadas de elegibilidade nem da configuracao final de perfis.

### Implementation for User Story 1

- [X] T025 [P] [US1] Create `Cessao` and `EtapaCessao` domain entities in `backend/src/main/java/com/fidc/cdc/kogito/domain/cessao/Cessao.java` and `backend/src/main/java/com/fidc/cdc/kogito/domain/cessao/EtapaCessao.java`
- [X] T026 [P] [US1] Create `CessaoRepository` and `EtapaCessaoRepository` in `backend/src/main/java/com/fidc/cdc/kogito/domain/cessao/CessaoRepository.java` and `backend/src/main/java/com/fidc/cdc/kogito/domain/cessao/EtapaCessaoRepository.java`
- [X] T027 [P] [US1] Create Flyway migration for `cessao` and `etapa_cessao` tables in `backend/src/main/resources/db/migration/V2__cessao_e_etapas.sql`
- [X] T028 [US1] Model the 15-step BPMN flow with gateways, human tasks, and timer hooks in `backend/src/main/resources/processes/controle-cessao.bpmn`
- [X] T029 [US1] Implement BPMN timers and runtime scheduling integration with Kogito Jobs Service in `backend/src/main/java/com/fidc/cdc/kogito/application/process/TimerSchedulingService.java`
- [X] T030 [US1] Implement process bootstrap and stage transition orchestration in `backend/src/main/java/com/fidc/cdc/kogito/application/cessao/CessaoProcessService.java`
- [X] T031 [US1] Implement REST commands for criar cessao, consultar cessao, consultar etapas, and avancar etapa in `backend/src/main/java/com/fidc/cdc/kogito/api/cessao/CessaoController.java`
- [X] T032 [US1] Implement request/response DTOs and HATEOAS representations for cessao and etapas in `backend/src/main/java/com/fidc/cdc/kogito/api/cessao/CessaoRequest.java`, `backend/src/main/java/com/fidc/cdc/kogito/api/cessao/CessaoResponse.java`, and `backend/src/main/java/com/fidc/cdc/kogito/api/cessao/EtapaResponse.java`
- [X] T033 [US1] Enforce business key uniqueness and duplicate-blocking rules in `backend/src/main/java/com/fidc/cdc/kogito/application/cessao/CessaoRegistrationService.java`
- [X] T034 [US1] Implement process and task event publication compatible with Kogito Data Index in `backend/src/main/java/com/fidc/cdc/kogito/application/cessao/CessaoEventPublisher.java`
- [X] T035 [US1] Implement indexed event consumption and synchronization of consolidated read model in `backend/src/main/java/com/fidc/cdc/kogito/application/readmodel/CessaoReadModelProjector.java`
- [X] T036 [US1] Implement PostgreSQL-backed consolidated read model persistence in `backend/src/main/java/com/fidc/cdc/kogito/application/readmodel/CessaoReadModelDocument.java`, `backend/src/main/java/com/fidc/cdc/kogito/application/readmodel/CessaoReadModelRepository.java`, and `backend/src/main/resources/db/migration/V6__cessao_read_model.sql`
- [X] T037 [US1] Implement frontend list and detail screens for cessoes and etapas in `frontend/src/app/cessoes/page.tsx` and `frontend/src/app/cessoes/[businessKey]/page.tsx`
- [X] T038 [US1] Implement frontend actions to iniciar cessao, avancar etapa, and refresh status in `frontend/src/features/cessao/actions.ts`, `frontend/src/features/cessao/cessao-list.tsx`, and `frontend/src/features/cessao/cessao-detail.tsx`
- [X] T039 [US1] Add operational loading, empty, and error states for the cessao flow in `frontend/src/features/cessao/cessao-status-panel.tsx` and `frontend/src/components/feedback/empty-state.tsx`
- [X] T040 [P] [US1] Create backend integration test for criar cessao, iniciar fluxo e consultar historico in `backend/src/test/java/com/fidc/cdc/kogito/integration/cessao/CessaoFlowIntegrationTest.java`
- [X] T041 [P] [US1] Create backend integration test for bloqueio de avanço entre etapas dependentes in `backend/src/test/java/com/fidc/cdc/kogito/integration/cessao/EtapaDependenciaIntegrationTest.java`
- [X] T042 [P] [US1] Create integration test for process event publication and read model propagation through Kafka and PostgreSQL in `backend/src/test/java/com/fidc/cdc/kogito/integration/readmodel/ProcessReadModelProjectionIntegrationTest.java`
- [X] T043 [P] [US1] Create integration test for BPMN timer scheduling through Kogito Jobs Service in `backend/src/test/java/com/fidc/cdc/kogito/integration/process/JobsServiceTimerIntegrationTest.java`
- [X] T044 [P] [US1] Create frontend integration test for lista e detalhe de cessao in `frontend/tests/integration/cessao-flow.spec.tsx`

**Checkpoint**: At this point, User Story 1 should be fully functional and testable independently

---

## Phase 4: User Story 2 - Validar elegibilidade, valores e documentos (Priority: P1)

**Goal**: Processar elegibilidade, calculo financeiro, documentos, lastros e interacoes sincronas com a registradora dentro do fluxo da cessao.

**Independent Test**: Processar uma cessao com contratos, parcelas e lastros, verificar reprovacao/aceitacao de regras, valor calculado, retries de registradora e bloqueio da aceitacao final quando houver inconsistencias documentais.

### Implementation for User Story 2

- [X] T045 [P] [US2] Create `RegraElegibilidade`, `Contrato`, `Parcela`, `Lastro`, `Pagamento`, `OfertaRegistradora`, and `TermoAceite` entities in `backend/src/main/java/com/fidc/cdc/kogito/domain/analise/`
- [X] T046 [P] [US2] Create repositories for analysis, payment, document, and registradora artifacts in `backend/src/main/java/com/fidc/cdc/kogito/domain/analise/`
- [X] T047 [P] [US2] Create Flyway migration for analysis, payment, lastro, offer, and termo tables in `backend/src/main/resources/db/migration/V3__analise_pagamento_documentos.sql`
- [X] T048 [US2] Implement eligibility evaluation service and blocking outcomes in `backend/src/main/java/com/fidc/cdc/kogito/application/analise/ElegibilidadeService.java`
- [X] T049 [US2] Implement payment calculation and approval basis service in `backend/src/main/java/com/fidc/cdc/kogito/application/financeiro/CalculoValorPagarService.java`
- [X] T050 [US2] Implement lastro intake and validation service in `backend/src/main/java/com/fidc/cdc/kogito/application/documental/LastroValidationService.java`
- [X] T051 [US2] Implement registradora REST client with retries, request/response evidence, and problem details handling in `backend/src/main/java/com/fidc/cdc/kogito/infrastructure/registradora/RegistradoraClient.java` and `backend/src/main/java/com/fidc/cdc/kogito/infrastructure/registradora/RegistradoraRetryPolicy.java`
- [X] T052 [US2] Implement workflow activities for carteira, contratos, parcelas, oferta, aceite, pagamento, and lastros in `backend/src/main/java/com/fidc/cdc/kogito/application/process/RegistradoraWorkflowHandler.java`
- [X] T053 [US2] Extend REST API with endpoints for regras, contratos, parcelas, pagamentos, lastros, and historico documental in `backend/src/main/java/com/fidc/cdc/kogito/api/analise/AnaliseController.java`
- [X] T054 [US2] Update OpenAPI contract and registradora integration contract in `specs/001-controle-cessao-fidc/contracts/backend-openapi.yaml` and `specs/001-controle-cessao-fidc/contracts/registradora-integration.md`
- [X] T055 [US2] Extend consolidated read model with financial and documental summaries in `backend/src/main/java/com/fidc/cdc/kogito/application/readmodel/CessaoReadModelProjector.java`
- [X] T056 [US2] Implement frontend screens for elegibilidade, contratos, parcelas, pagamentos, and lastros in `frontend/src/app/cessoes/[businessKey]/analise/page.tsx` and `frontend/src/features/analise/`
- [X] T057 [US2] Implement frontend forms, validation feedback, and retry visibility for registradora interactions in `frontend/src/features/analise/registradora-panel.tsx`, `frontend/src/features/analise/lastro-panel.tsx`, and `frontend/src/features/analise/calculo-panel.tsx`
- [X] T058 [P] [US2] Create backend integration test for elegibilidade e bloqueio por reprovacao impeditiva in `backend/src/test/java/com/fidc/cdc/kogito/integration/analise/ElegibilidadeIntegrationTest.java`
- [X] T059 [P] [US2] Create backend integration test for calculo do valor a pagar in `backend/src/test/java/com/fidc/cdc/kogito/integration/financeiro/CalculoValorPagarIntegrationTest.java`
- [X] T060 [P] [US2] Create backend integration test for validacao de lastros e bloqueio da aceitacao final in `backend/src/test/java/com/fidc/cdc/kogito/integration/documental/LastroValidationIntegrationTest.java`
- [X] T061 [P] [US2] Create backend contract test for registradora retries and RFC 9457 error mapping in `backend/src/test/java/com/fidc/cdc/kogito/contract/registradora/RegistradoraContractTest.java`
- [X] T062 [P] [US2] Create frontend integration test for analise, calculo e feedback de retry da registradora in `frontend/tests/integration/analise-cessao.spec.tsx`

**Checkpoint**: At this point, User Stories 1 AND 2 should both work independently

---

## Phase 5: User Story 3 - Aplicar segregacao de funcao e auditoria (Priority: P1)

**Goal**: Garantir autorizacao por etapa, trilha de auditoria, consulta auditavel e integracao operacional com Task Console e Management Console.

**Independent Test**: Executar o fluxo com perfis distintos, bloquear acoes sem permissao, registrar eventos auditaveis e consultar o historico completo e tarefas operacionais.

### Implementation for User Story 3

- [X] T063 [P] [US3] Create `Usuario`, `PerfilAcesso`, and `EventoAuditoria` entities in `backend/src/main/java/com/fidc/cdc/kogito/domain/security/` and `backend/src/main/java/com/fidc/cdc/kogito/domain/audit/`
- [X] T064 [P] [US3] Create Flyway migration for users, roles, permissions, and audit events in `backend/src/main/resources/db/migration/V4__security_and_audit.sql`
- [X] T065 [US3] Create seed for operational roles, stage permissions, and default access mappings in `backend/src/main/resources/db/migration/V5__seed_roles_permissions.sql`
- [X] T066 [US3] Implement role-to-stage authorization policies in `backend/src/main/java/com/fidc/cdc/kogito/application/security/StageAuthorizationService.java`
- [X] T067 [US3] Implement immutable audit event persistence and retrieval in `backend/src/main/java/com/fidc/cdc/kogito/application/audit/AuditTrailService.java`
- [X] T068 [US3] Integrate stage authorization and audit registration into workflow commands in `backend/src/main/java/com/fidc/cdc/kogito/application/cessao/CessaoProcessService.java`
- [X] T069 [US3] Expose audit trail, permissions, and operational history endpoints in `backend/src/main/java/com/fidc/cdc/kogito/api/audit/AuditController.java` and `backend/src/main/java/com/fidc/cdc/kogito/api/security/PermissionController.java`
- [X] T070 [US3] Configure human task metadata, assignments, and task visibility for Task Console in `backend/src/main/resources/processes/controle-cessao.bpmn`
- [X] T071 [US3] Implement task identity and assignment support for authorized operational profiles in `backend/src/main/java/com/fidc/cdc/kogito/application/process/TaskAssignmentService.java`
- [X] T072 [US3] Implement process instance administration support for Management Console in `backend/src/main/java/com/fidc/cdc/kogito/application/process/ManagementConsoleSupport.java`
- [X] T073 [US3] Configure process and job visibility required by Management Console in `backend/src/main/resources/application.yml` and `infra/compose/docker-compose.yml`
- [X] T074 [US3] Extend consolidated read model with audit and pending human task indicators in `backend/src/main/java/com/fidc/cdc/kogito/application/readmodel/CessaoReadModelProjector.java`
- [X] T075 [US3] Implement frontend permission-aware actions and audit trail screens in `frontend/src/features/security/permission-guard.tsx`, `frontend/src/app/cessoes/[businessKey]/auditoria/page.tsx`, and `frontend/src/features/auditoria/audit-timeline.tsx`
- [X] T076 [US3] Implement frontend user feedback for forbidden actions, audit queries, and human-task context in `frontend/src/components/feedback/forbidden-state.tsx` and `frontend/src/features/auditoria/task-context-panel.tsx`
- [X] T077 [P] [US3] Create backend integration test for autorizacao por perfil e etapa in `backend/src/test/java/com/fidc/cdc/kogito/integration/security/StageAuthorizationIntegrationTest.java`
- [X] T078 [P] [US3] Create backend integration test for persistencia e consulta da trilha de auditoria in `backend/src/test/java/com/fidc/cdc/kogito/integration/audit/AuditTrailIntegrationTest.java`
- [X] T079 [P] [US3] Create integration test for task lifecycle visibility in Task Console and process visibility in Management Console in `backend/src/test/java/com/fidc/cdc/kogito/integration/console/KogitoConsoleIntegrationTest.java`
- [X] T080 [P] [US3] Create frontend integration test for forbidden actions and audit timeline in `frontend/tests/integration/auditoria-permissoes.spec.tsx`

**Checkpoint**: All user stories should now be independently functional

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Improvements that affect multiple user stories

- [X] T081 [P] Update environment variables, local run instructions, and service URLs in `backend/src/main/resources/application.yml`, `frontend/.env.local.example`, and `specs/001-controle-cessao-fidc/quickstart.md`
- [X] T082 Validate REST resource naming, versioning strategy, HATEOAS links, and RFC 9457 payloads in `backend/src/main/java/com/fidc/cdc/kogito/api/`
- [X] T083 Validate observability coverage for process, registradora retries, Kafka propagation, Data Index, Jobs Service, Task Console, and Management Console in `backend/src/main/java/com/fidc/cdc/kogito/observability/` and `infra/observability/`
- [X] T084 [P] Validate frontend accessibility, semantic colors, dark/light themes, and Atlassian token consistency in `frontend/src/design-system/` and `frontend/src/components/`
- [X] T085 [P] Validate Docker Compose startup order, minimum resources, and service health checks in `infra/compose/docker-compose.yml`
- [X] T086 Define operational baseline for volume, projection lag, and consultation targets in `specs/001-controle-cessao-fidc/quickstart.md`
- [X] T087 [P] Create backend integration test for read-model propagation lag through Kafka, Data Index, and PostgreSQL in `backend/src/test/java/com/fidc/cdc/kogito/integration/readmodel/ProjectionLagIntegrationTest.java`
- [X] T088 Validate consultation response time and projection lag against operational targets in `specs/001-controle-cessao-fidc/quickstart.md`
- [X] T089 Validate end-to-end operational flow for Kafka, Data Index, Jobs Service, Task Console, and Management Console in `specs/001-controle-cessao-fidc/quickstart.md`
- [X] T090 Run end-to-end quickstart validation and capture implementation notes in `specs/001-controle-cessao-fidc/quickstart.md`
- [X] T091 Run backend and frontend critical-path regression for BPMN, registradora, autorizacao e auditoria in `specs/001-controle-cessao-fidc/quickstart.md`
- [X] T092 [P] Validate PostgreSQL-backed container communication topology and Kogito runtime wiring in `backend/src/test/java/com/fidc/cdc/kogito/architecture/ContainerCommunicationTopologyTest.java`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Stories (Phase 3+)**: All depend on Foundational phase completion
- **Polish (Phase 6)**: Depends on all desired user stories being complete

### User Story Dependencies

- **User Story 1 (P1)**: Can start after Foundational - no dependency on other stories
- **User Story 2 (P1)**: Can start after Foundational and depends functionally on the flow skeleton from US1
- **User Story 3 (P1)**: Can start after Foundational and should be integrated over US1 flow commands and US2 operational evidence

### Within Each User Story

- Domain entities and migrations before orchestration services
- Orchestration services before REST controllers and UI integration
- Backend command/query support before frontend screens
- Consolidated read model updates before operational dashboards depending on them

### Parallel Opportunities

- `T002`, `T003`, `T004`, and `T005` can run in parallel during setup
- `T007` to `T024` contain multiple parallelizable foundational tracks across backend, frontend, infrastructure, messaging, Kogito services, and test setup
- In US1, `T025`, `T026`, and `T027` can run in parallel before orchestration, API, projection, and tests
- In US2, `T045`, `T046`, and `T047` can run in parallel before services, controllers, and tests
- In US3, `T063` and `T064` can run in parallel before authorization, audit integration, console support, and tests
- `T081`, `T084`, `T085`, `T087`, and `T092` can run in parallel in the polish phase

---

## Parallel Example: User Story 1

```bash
# Parallel domain setup for US1
T025 Create Cessao and EtapaCessao entities
T026 Create repositories for Cessao and EtapaCessao
T027 Create Flyway migration for cessao and etapa tables

# Parallel UI work after backend contracts stabilize
T037 Implement frontend list and detail screens
T039 Add loading, empty, and error states
```

---

## Parallel Example: User Story 2

```bash
# Parallel persistence setup for US2
T045 Create analysis-related entities
T046 Create repositories for analysis and payment
T047 Create Flyway migration for analysis and documents

# Parallel UI panels after service contracts stabilize
T056 Implement frontend analysis screens
T057 Implement retry and validation feedback panels
```

---

## Parallel Example: User Story 3

```bash
# Parallel security foundation for US3
T063 Create user, role, and audit entities
T064 Create security and audit migration

# Parallel operational UI work after audit endpoints exist
T075 Implement permission-aware screens
T076 Implement forbidden and task context feedback
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational
3. Complete Phase 3: User Story 1
4. Validate that a cessao can be created, progressed, and queried end to end

### Incremental Delivery

1. Deliver US1 for core BPMN orchestration and operational consultation
2. Add US2 for business validation, registradora integration, and document flow
3. Add US3 for authorization, segregation of duties, and auditability
4. Run Phase 6 to harden observability, UX, API compliance, and local operation

### Suggested MVP Scope

- Phase 1
- Phase 2
- Phase 3

---

## Notes

- [P] tasks = different files, no dependencies on incomplete tasks
- [US1], [US2], and [US3] map directly to the user stories in `spec.md`
- Every task includes an explicit file path for immediate execution
- Security, compliance, observability, auditability, maintainability, scalability, API consistency, and UI/UX constraints from the constitution are represented in setup, foundation, story, and polish phases
