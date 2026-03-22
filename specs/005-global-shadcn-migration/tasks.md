# Tasks: Migracao Global de Componentes Equivalentes para shadcn/ui

**Input**: Design documents from `/specs/005-global-shadcn-migration/`
**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/

**Tests**: A feature exige validacao tecnica de auditoria, observabilidade,
acessibilidade e regressao visual minima; por isso inclui tarefas de teste e
verificacao tecnica direcionadas.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Consolidar escopo, catalogo local e pontos tecnicos de migracao

- [x] T001 Review feature scope and migration criteria in D:\desenv\fidc-cdc-kogito\specs\005-global-shadcn-migration\spec.md
- [x] T002 Review constitutional migration constraints and target structure in D:\desenv\fidc-cdc-kogito\specs\005-global-shadcn-migration\plan.md
- [x] T003 [P] Review the migration decisions and equivalence rationale in D:\desenv\fidc-cdc-kogito\specs\005-global-shadcn-migration\research.md
- [x] T004 [P] Review the current UI catalog in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Preparar dependencias, utilitarios e baseline de tokens para a migracao global

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [x] T005 Add the minimum shadcn/ui adoption dependencies in D:\desenv\fidc-cdc-kogito\frontend\package.json, including required Radix UI packages, `class-variance-authority`, `tailwind-merge`, and icon support needed by the migrated primitives
- [x] T006 [P] Align shared class-composition utilities for shadcn-based primitives in D:\desenv\fidc-cdc-kogito\frontend\src\lib\cn.ts
- [x] T007 [P] Align Tailwind and token-driven global styling for migrated primitives in D:\desenv\fidc-cdc-kogito\frontend\src\app\globals.css
- [x] T008 [P] Preserve token and semantic-color mappings used by migrated components in D:\desenv\fidc-cdc-kogito\frontend\src\design-system\theme.css
- [x] T009 Register the initial component equivalence inventory and migration scope in D:\desenv\fidc-cdc-kogito\specs\005-global-shadcn-migration\research.md

**Checkpoint**: Foundation ready - user story implementation can now begin in parallel

---

## Phase 3: User Story 1 - Migrar primitivas equivalentes de UI (Priority: P1) 🎯 MVP

**Goal**: Substituir as primitives locais equivalentes por bases aderentes ao `shadcn/ui` em todo o frontend

**Independent Test**: Renderizar os fluxos que usam botao, input, dialogo e tabela e confirmar que as primitives migradas funcionam sem erro estrutural ou quebra imediata de interacao

### Implementation for User Story 1

- [x] T010 [US1] Replace the local button primitive with a shadcn-based implementation in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\button.tsx
- [x] T011 [US1] Replace the local input primitive with a shadcn-based implementation in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\input.tsx
- [x] T012 [US1] Replace the local dialog primitive with a shadcn-based implementation in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\dialog.tsx
- [x] T013 [US1] Replace the local table primitive with a shadcn-based implementation in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\table.tsx
- [x] T014 [P] [US1] Align migrated primitive variants and state APIs with project token usage in D:\desenv\fidc-cdc-kogito\frontend\src\design-system\color-semantics.ts
- [x] T015 [US1] Adapt D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\account-settings-dialog.tsx to the migrated button, input, and dialog primitives without breaking current interaction states
- [x] T016 [US1] Adapt D:\desenv\fidc-cdc-kogito\frontend\src\features\security\login-panel.tsx to the migrated button and input primitives without losing authentication feedback
- [x] T017 [US1] Adapt D:\desenv\fidc-cdc-kogito\frontend\src\features\cessao\cessao-list.tsx to the migrated button and input primitives while preserving form semantics
- [x] T018 [US1] Adapt D:\desenv\fidc-cdc-kogito\frontend\src\features\cessao\cessao-detail.tsx to the migrated button and table primitives while preserving action and status behavior
- [x] T019 [US1] Adapt D:\desenv\fidc-cdc-kogito\frontend\src\features\analise\calculo-panel.tsx to the migrated button and table primitives without changing result communication
- [x] T020 [US1] Adapt D:\desenv\fidc-cdc-kogito\frontend\src\features\analise\contratos-panel.tsx to the migrated table primitive while preserving data readability
- [x] T021 [US1] Adapt D:\desenv\fidc-cdc-kogito\frontend\src\features\analise\elegibilidade-panel.tsx to the migrated button and table primitives while preserving eligibility feedback
- [x] T022 [US1] Adapt D:\desenv\fidc-cdc-kogito\frontend\src\features\analise\lastro-panel.tsx to the migrated button, input, and table primitives while preserving validation flow feedback
- [x] T023 [US1] Adapt D:\desenv\fidc-cdc-kogito\frontend\src\features\analise\registradora-panel.tsx to the migrated button primitive while preserving submission semantics

**Checkpoint**: At this point, User Story 1 should be fully functional and testable independently

---

## Phase 4: User Story 2 - Preservar comunicacao visual e feedback (Priority: P1)

**Goal**: Garantir que a migracao estrutural nao degrade feedback, foco, tema e cores semanticas nos fluxos do usuario

**Independent Test**: Validar tema, menu do usuario, notificacoes e ajustes da conta confirmando preservacao de mensagens, estados, foco e cores semanticas apos a migracao

### Implementation for User Story 2

- [x] T024 [US2] Update the theme toggle to consume migrated primitives without losing labels, theme feedback, or semantic clarity in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\theme-toggle.tsx
- [x] T025 [US2] Update the topbar user menu to consume migrated primitives while preserving notification emphasis and semantic colors in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\topbar-user-menu.tsx
- [x] T026 [US2] Update account settings forms and feedback states to consume migrated primitives without communication regressions in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\account-settings-dialog.tsx
- [x] T027 [P] [US2] Preserve semantic color and feedback mappings used by the migrated consumers in D:\desenv\fidc-cdc-kogito\frontend\src\design-system\color-semantics.ts
- [x] T028 [P] [US2] Validate route-level rendering stability for migrated UI flows in D:\desenv\fidc-cdc-kogito\frontend\src\app\layout.tsx
- [x] T029 [US2] Validate home and authenticated entry rendering after primitive migration in D:\desenv\fidc-cdc-kogito\frontend\src\app\page.tsx
- [x] T030 [US2] Validate semantic feedback, focus visibility, and user-facing communication in D:\desenv\fidc-cdc-kogito\frontend\src\features\cessao\cessao-list.tsx
- [x] T031 [US2] Validate semantic feedback, focus visibility, and user-facing communication in D:\desenv\fidc-cdc-kogito\frontend\src\features\cessao\cessao-detail.tsx
- [x] T032 [US2] Validate semantic feedback, focus visibility, and user-facing communication in D:\desenv\fidc-cdc-kogito\frontend\src\features\analise\calculo-panel.tsx
- [x] T033 [US2] Validate semantic feedback, focus visibility, and user-facing communication in D:\desenv\fidc-cdc-kogito\frontend\src\features\analise\contratos-panel.tsx
- [x] T034 [US2] Validate semantic feedback, focus visibility, and user-facing communication in D:\desenv\fidc-cdc-kogito\frontend\src\features\analise\elegibilidade-panel.tsx
- [x] T035 [US2] Validate semantic feedback, focus visibility, and user-facing communication in D:\desenv\fidc-cdc-kogito\frontend\src\features\analise\lastro-panel.tsx
- [x] T036 [US2] Validate semantic feedback, focus visibility, and user-facing communication in D:\desenv\fidc-cdc-kogito\frontend\src\features\analise\registradora-panel.tsx

**Checkpoint**: At this point, User Stories 1 and 2 should both work independently

---

## Phase 5: User Story 3 - Validar governanca, auditoria e observabilidade da migracao (Priority: P2)

**Goal**: Comprovar tecnicamente a aderencia da migracao, a rastreabilidade das substituicoes e a ausencia de regressao relevante nos fluxos priorizados

**Independent Test**: Executar testes e validacoes tecnicas dos componentes migrados e confirmar evidencias de acessibilidade minima, rastreabilidade e observabilidade dos fluxos afetados

### Implementation for User Story 3

- [x] T037 [P] [US3] Add technical validation coverage for the migrated theme toggle in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\theme-toggle.test.tsx
- [x] T038 [P] [US3] Add technical validation coverage for the migrated account settings dialog in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\account-settings-dialog.test.tsx
- [x] T039 [P] [US3] Add technical validation coverage for the migrated topbar user menu in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\topbar-user-menu.test.tsx
- [x] T040 [US3] Document and validate security, sensitive-data, and authorization impact across migrated UI flows in D:\desenv\fidc-cdc-kogito\specs\005-global-shadcn-migration\research.md
- [x] T041 [US3] Define and validate technical observability signals for migrated UI flows in D:\desenv\fidc-cdc-kogito\specs\005-global-shadcn-migration\contracts\ui-migration-contract.md
- [x] T042 [US3] Record migrated components, out-of-scope components, and audit evidence in D:\desenv\fidc-cdc-kogito\specs\005-global-shadcn-migration\research.md
- [x] T043 [US3] Finalize the validation contract for migration traceability and technical checks in D:\desenv\fidc-cdc-kogito\specs\005-global-shadcn-migration\contracts\ui-migration-contract.md
- [x] T044 [US3] Capture final validation scenarios for accessibility, observability, and semantic feedback in D:\desenv\fidc-cdc-kogito\specs\005-global-shadcn-migration\quickstart.md
- [x] T045 [US3] Add technical validation coverage for the migrated login panel in D:\desenv\fidc-cdc-kogito\frontend\src\features\security\login-panel.test.tsx
- [x] T046 [US3] Add technical validation coverage for the migrated cession list flow in D:\desenv\fidc-cdc-kogito\frontend\src\features\cessao\cessao-list.test.tsx
- [x] T047 [US3] Add technical validation coverage for the migrated cession detail flow in D:\desenv\fidc-cdc-kogito\frontend\src\features\cessao\cessao-detail.test.tsx
- [x] T048 [US3] Add technical validation coverage for the migrated analysis calculation flow in D:\desenv\fidc-cdc-kogito\frontend\src\features\analise\calculo-panel.test.tsx
- [x] T049 [US3] Add technical validation coverage for the migrated contracts flow in D:\desenv\fidc-cdc-kogito\frontend\src\features\analise\contratos-panel.test.tsx
- [x] T050 [US3] Add technical validation coverage for the migrated eligibility flow in D:\desenv\fidc-cdc-kogito\frontend\src\features\analise\elegibilidade-panel.test.tsx
- [x] T051 [US3] Add technical validation coverage for the migrated collateral flow in D:\desenv\fidc-cdc-kogito\frontend\src\features\analise\lastro-panel.test.tsx
- [x] T052 [US3] Add technical validation coverage for the migrated registry flow in D:\desenv\fidc-cdc-kogito\frontend\src\features\analise\registradora-panel.test.tsx

**Checkpoint**: All user stories should now be independently functional

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Consolidar o modelo final, validar regressao e fechar guardrails tecnicos

- [x] T053 Update final migration statuses and component relationships in D:\desenv\fidc-cdc-kogito\specs\005-global-shadcn-migration\data-model.md
- [x] T054 Validate accessibility, focus visibility, and keyboard behavior in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\dialog.tsx
- [x] T055 Validate semantic color, contrast, and feedback consistency across migrated primitives in D:\desenv\fidc-cdc-kogito\frontend\src\design-system\theme.css
- [x] T056 Validate the full migrated frontend surface against the quickstart in D:\desenv\fidc-cdc-kogito\specs\005-global-shadcn-migration\quickstart.md

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Stories (Phase 3+)**: All depend on Foundational phase completion
  - User stories can then proceed in parallel if capacity exists
  - Or sequentially in priority order (US1 -> US2 -> US3)
- **Polish (Phase 6)**: Depends on all desired user stories being complete

### User Story Dependencies

- **User Story 1 (P1)**: Can start after Foundational (Phase 2) - no dependency on other stories
- **User Story 2 (P1)**: Can start after User Story 1 because composite consumers depend on migrated primitives
- **User Story 3 (P2)**: Can start after User Stories 1 and 2 because it validates the migrated surface and evidence

### Within Each User Story

- Shared tooling and token baseline before primitive replacement
- Primitive migration before composite consumer adjustments
- Consumer adjustments before validation coverage
- Technical validation before final documentation closure

### Parallel Opportunities

- `T003` and `T004`
- `T006`, `T007`, and `T008`
- `T010`, `T011`, `T012`, and `T013`
- `T027` and `T028`
- `T037`, `T038`, and `T039`

---

## Parallel Example: User Story 1

```bash
Task: "Replace the local button primitive in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\button.tsx"
Task: "Replace the local input primitive in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\input.tsx"
Task: "Replace the local table primitive in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\table.tsx"
```

---

## Parallel Example: User Story 2

```bash
Task: "Preserve semantic color mappings in D:\desenv\fidc-cdc-kogito\frontend\src\design-system\color-semantics.ts"
Task: "Validate route-level rendering stability in D:\desenv\fidc-cdc-kogito\frontend\src\app\layout.tsx"
```

---

## Parallel Example: User Story 3

```bash
Task: "Add technical validation coverage for theme toggle in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\theme-toggle.test.tsx"
Task: "Add technical validation coverage for topbar user menu in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\topbar-user-menu.test.tsx"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational
3. Complete Phase 3: User Story 1
4. **STOP and VALIDATE**: Confirm that the equivalent local primitives have been replaced by shadcn-based implementations

### Incremental Delivery

1. Complete Setup + Foundational -> migration baseline ready
2. Add User Story 1 -> validate primitive replacement
3. Add User Story 2 -> validate semantic feedback and communication preservation
4. Add User Story 3 -> validate technical evidence, auditability, and observability
5. Finish with full-surface validation and final data/model closure

### Parallel Team Strategy

With multiple developers:

1. One developer prepara dependencias e baseline de tokens
2. Outro developer executa a migracao das primitives equivalentes
3. Depois da migracao base, consumidores compostos e validacoes tecnicas podem seguir em paralelo

---

## Notes

- [P] tasks = different files, no dependencies
- [Story] label maps task to specific user story for traceability
- Each user story should be independently completable and testable
- Equivalent local primitives must be replaced, not preserved as the active structural base
- Observability and auditability are part of the implementation scope and require technical validation
- Preserve semantic colors, feedback, focus visibility, and communication clarity throughout the migration
