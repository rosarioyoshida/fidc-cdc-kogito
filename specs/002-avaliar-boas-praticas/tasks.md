# Tasks: Avaliacao de Boas Praticas do Projeto

**Input**: Design documents from `/specs/002-avaliar-boas-praticas/`
**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/, quickstart.md

**Tests**: Nao ha obrigacao de TDD nesta feature. As validacoes usam os comandos e
evidencias ja existentes do projeto, conforme `quickstart.md`.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (`[US1]`, `[US2]`, `[US3]`)
- Include exact file paths in descriptions

## Path Conventions

- Artefatos desta feature ficam em `D:\desenv\fidc-cdc-kogito\specs\002-avaliar-boas-praticas\`
- Evidencias podem referenciar arquivos existentes em `backend/`, `frontend/`, `infra/`
  e `specs/001-controle-cessao-fidc/`

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Preparar a estrutura documental reutilizavel do ciclo de avaliacao

- [X] T001 Create report workspace index in D:\desenv\fidc-cdc-kogito\specs\002-avaliar-boas-praticas\README.md
- [X] T002 Create cycle report directory structure in D:\desenv\fidc-cdc-kogito\specs\002-avaliar-boas-praticas\cycles\.gitkeep
- [X] T003 [P] Create reusable report template in D:\desenv\fidc-cdc-kogito\specs\002-avaliar-boas-praticas\templates\assessment-report-template.md
- [X] T004 [P] Create reusable evidence collection template in D:\desenv\fidc-cdc-kogito\specs\002-avaliar-boas-praticas\templates\evidence-log-template.md

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Definir a base normativa e o método de avaliação que bloqueiam todas as histórias

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [X] T005 Consolidate evaluation criteria matrix in D:\desenv\fidc-cdc-kogito\specs\002-avaliar-boas-praticas\criteria-matrix.md
- [X] T006 [P] Define severity and status rubric in D:\desenv\fidc-cdc-kogito\specs\002-avaliar-boas-praticas\severity-model.md
- [X] T007 [P] Define evidence source inventory with absolute repository paths in D:\desenv\fidc-cdc-kogito\specs\002-avaliar-boas-praticas\evidence-sources.md
- [X] T008 Define readiness decision rules in D:\desenv\fidc-cdc-kogito\specs\002-avaliar-boas-praticas\readiness-rules.md
- [X] T009 Record implementation guardrails for stack preservation and dependency approval in D:\desenv\fidc-cdc-kogito\specs\002-avaliar-boas-praticas\governance-constraints.md

**Checkpoint**: Foundation ready - user story implementation can now begin in parallel

---

## Phase 3: User Story 1 - Diagnosticar aderencia do projeto (Priority: P1) 🎯 MVP

**Goal**: Produzir um diagnostico objetivo da aderencia atual do projeto por area,
com criterios, evidencias e status claros.

**Independent Test**: A historia pode ser validada revisando backend, frontend,
seguranca, observabilidade, testes e configuracoes operacionais e confirmando que cada
area recebe um status claro, com evidencias e classificacao de conformidade ou desvio.

### Implementation for User Story 1

- [X] T010 [P] [US1] Map backend implementation evidence in D:\desenv\fidc-cdc-kogito\specs\002-avaliar-boas-praticas\cycles\2026-03-22\backend-evidence.md
- [X] T011 [P] [US1] Map frontend and UX evidence in D:\desenv\fidc-cdc-kogito\specs\002-avaliar-boas-praticas\cycles\2026-03-22\frontend-evidence.md
- [X] T012 [P] [US1] Map infrastructure and observability evidence in D:\desenv\fidc-cdc-kogito\specs\002-avaliar-boas-praticas\cycles\2026-03-22\operations-evidence.md
- [X] T013 [US1] Consolidate evidence log in D:\desenv\fidc-cdc-kogito\specs\002-avaliar-boas-praticas\cycles\2026-03-22\evidence-log.md
- [X] T014 [US1] Assess criteria coverage and status by area in D:\desenv\fidc-cdc-kogito\specs\002-avaliar-boas-praticas\cycles\2026-03-22\coverage-matrix.md
- [X] T015 [US1] Produce initial diagnostic report in D:\desenv\fidc-cdc-kogito\specs\002-avaliar-boas-praticas\cycles\2026-03-22\diagnostic-report.md

**Checkpoint**: At this point, User Story 1 should be fully functional and testable independently

---

## Phase 4: User Story 2 - Priorizar correcoes necessarias (Priority: P1)

**Goal**: Transformar os desvios encontrados em um backlog priorizado e acionavel de adequacao.

**Independent Test**: A historia pode ser validada confirmando que cada desvio possui
prioridade, criterio de aceite esperado e recomendacao suficiente para orientar uma
acao corretiva sem ambiguidades.

### Implementation for User Story 2

- [X] T016 [P] [US2] Extract non-conformities from the diagnostic report into D:\desenv\fidc-cdc-kogito\specs\002-avaliar-boas-praticas\cycles\2026-03-22\findings-register.md
- [X] T017 [P] [US2] Define remediation prioritization model in D:\desenv\fidc-cdc-kogito\specs\002-avaliar-boas-praticas\prioritization-rules.md
- [X] T018 [US2] Build remediation backlog with acceptance criteria in D:\desenv\fidc-cdc-kogito\specs\002-avaliar-boas-praticas\cycles\2026-03-22\remediation-backlog.md
- [X] T019 [US2] Annotate blocker versus deferred improvement decisions in D:\desenv\fidc-cdc-kogito\specs\002-avaliar-boas-praticas\cycles\2026-03-22\decision-log.md
- [X] T020 [US2] Publish prioritized correction summary in D:\desenv\fidc-cdc-kogito\specs\002-avaliar-boas-praticas\cycles\2026-03-22\prioritized-findings.md

**Checkpoint**: At this point, User Stories 1 AND 2 should both work independently

---

## Phase 5: User Story 3 - Comprovar conformidade minima para evolucao (Priority: P2)

**Goal**: Emitir um parecer formal de prontidao e definir como a avaliação será repetida nas fases futuras.

**Independent Test**: A historia pode ser validada verificando que a avaliacao conclui
se o projeto esta apto, apto com ressalvas ou inapto para avancar, com base em
criterios mensuraveis previamente definidos.

### Implementation for User Story 3

- [X] T021 [P] [US3] Create readiness decision record in D:\desenv\fidc-cdc-kogito\specs\002-avaliar-boas-praticas\cycles\2026-03-22\readiness-decision.md
- [X] T022 [P] [US3] Create executive summary for stakeholders in D:\desenv\fidc-cdc-kogito\specs\002-avaliar-boas-praticas\cycles\2026-03-22\executive-summary.md
- [X] T023 [US3] Assemble final readiness opinion in D:\desenv\fidc-cdc-kogito\specs\002-avaliar-boas-praticas\cycles\2026-03-22\readiness-opinion.md
- [X] T024 [US3] Define recurring assessment workflow in D:\desenv\fidc-cdc-kogito\specs\002-avaliar-boas-praticas\recurring-cycle-playbook.md
- [X] T025 [US3] Create cycle comparison template in D:\desenv\fidc-cdc-kogito\specs\002-avaliar-boas-praticas\templates\cycle-comparison-template.md

**Checkpoint**: All user stories should now be independently functional

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Finalizar a feature com validação transversal e guia de uso

- [X] T026 [P] Validate alignment between D:\desenv\fidc-cdc-kogito\specs\002-avaliar-boas-praticas\criteria-matrix.md and D:\desenv\fidc-cdc-kogito\specs\002-avaliar-boas-praticas\contracts\assessment-report.md
- [X] T027 [P] Validate quickstart flow against D:\desenv\fidc-cdc-kogito\specs\002-avaliar-boas-praticas\cycles\2026-03-22\readiness-opinion.md using D:\desenv\fidc-cdc-kogito\specs\002-avaliar-boas-praticas\quickstart.md
- [X] T028 Validate stack-preservation and dependency-approval rules in D:\desenv\fidc-cdc-kogito\specs\002-avaliar-boas-praticas\governance-constraints.md against D:\desenv\fidc-cdc-kogito\AGENTS.md
- [X] T029 Update feature usage guide and artifact index in D:\desenv\fidc-cdc-kogito\specs\002-avaliar-boas-praticas\README.md

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Story 1 (Phase 3)**: Depends on Foundational completion
- **User Story 2 (Phase 4)**: Depends on User Story 1 diagnostic outputs
- **User Story 3 (Phase 5)**: Depends on User Story 1 and User Story 2 outputs
- **Polish (Phase 6)**: Depends on all desired user stories being complete

### User Story Dependencies

- **User Story 1 (P1)**: Can start after Foundational - no dependency on other stories
- **User Story 2 (P1)**: Depends on US1 because prioritization requires extracted achados
- **User Story 3 (P2)**: Depends on US1 and US2 because readiness requires diagnostic and remediation context

### Within Each User Story

- Evidence files before consolidated logs
- Consolidated logs before reports
- Diagnostic report before prioritization backlog
- Prioritized backlog before final readiness opinion
- Readiness opinion before recurring-cycle guidance

### Parallel Opportunities

- Setup templates `T003` and `T004` can run in parallel
- Foundational artifacts `T006` and `T007` can run in parallel
- US1 evidence mapping tasks `T010`, `T011`, and `T012` can run in parallel
- US2 extraction and prioritization model tasks `T016` and `T017` can run in parallel
- US3 readiness decision and executive summary tasks `T021` and `T022` can run in parallel
- Polish validations `T026` and `T027` can run in parallel

---

## Parallel Example: User Story 1

```bash
# Launch evidence collection for the three main project areas together:
Task: "Map backend implementation evidence in specs/002-avaliar-boas-praticas/cycles/2026-03-22/backend-evidence.md"
Task: "Map frontend and UX evidence in specs/002-avaliar-boas-praticas/cycles/2026-03-22/frontend-evidence.md"
Task: "Map infrastructure and observability evidence in specs/002-avaliar-boas-praticas/cycles/2026-03-22/operations-evidence.md"
```

## Parallel Example: User Story 2

```bash
# Run extraction and scoring-definition work together:
Task: "Extract non-conformities into specs/002-avaliar-boas-praticas/cycles/2026-03-22/findings-register.md"
Task: "Define remediation prioritization model in specs/002-avaliar-boas-praticas/prioritization-rules.md"
```

## Parallel Example: User Story 3

```bash
# Prepare the decision package in parallel:
Task: "Create readiness decision record in specs/002-avaliar-boas-praticas/cycles/2026-03-22/readiness-decision.md"
Task: "Create executive summary in specs/002-avaliar-boas-praticas/cycles/2026-03-22/executive-summary.md"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational
3. Complete Phase 3: User Story 1
4. Stop and validate the diagnostic package in `cycles/2026-03-22/`

### Incremental Delivery

1. Complete Setup + Foundational
2. Deliver US1 diagnostic report
3. Add US2 prioritized remediation package
4. Add US3 readiness opinion and recurring-cycle playbook
5. Finish with cross-cutting validation and usage guidance

### Parallel Team Strategy

1. One contributor closes Setup and Foundational artifacts
2. After foundation:
   - Contributor A: backend evidence and overall evidence log for US1
   - Contributor B: frontend/UX and operations evidence for US1
3. After US1:
   - Contributor A: findings register and remediation backlog for US2
   - Contributor B: prioritization rules and decision log for US2
4. After US2:
   - Contributor A: readiness opinion
   - Contributor B: executive summary and recurring-cycle guidance

---

## Notes

- All tasks follow the required checklist format with task ID and exact file path
- `[P]` tasks target different files and can run in parallel safely
- No task introduces a new stack or dependency without prior consultation
- This feature is primarily documental and operational; it does not require a new API
- Validation relies on the existing repository artifacts and commands documented in `quickstart.md`
