# Tasks: Fechamento de Ajustes e Persistencia de Tema

**Input**: Design documents from `/specs/004-fix-user-modal-theme/`
**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/

**Tests**: A feature nao exige TDD, entao as tarefas priorizam implementacao e validacao guiada proporcional ao risco.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Consolidar contexto, contratos e pontos de entrada da correcao

- [ ] T001 Review clarified feature scope in D:\desenv\fidc-cdc-kogito\specs\004-fix-user-modal-theme\spec.md
- [ ] T002 Review implementation constraints and component governance in D:\desenv\fidc-cdc-kogito\specs\004-fix-user-modal-theme\plan.md
- [ ] T003 [P] Review account settings flow contract in D:\desenv\fidc-cdc-kogito\specs\004-fix-user-modal-theme\contracts\account-settings-flow.md
- [ ] T004 [P] Review current dialog, theme, and save touchpoints in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\account-settings-dialog.tsx

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Fixar as regras compartilhadas de tema, dialogo, retorno protegido e migracao para componentes aderentes ao padrao tecnico exigido

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [ ] T005 Define `localStorage`-first theme source-of-truth helpers in D:\desenv\fidc-cdc-kogito\frontend\src\lib\auth.ts
- [ ] T006 [P] Align document bootstrap and cookie synchronization with the theme contract in D:\desenv\fidc-cdc-kogito\frontend\src\app\layout.tsx
- [ ] T007 Define account-settings dialog state transitions for dirty, discard-confirmation, success, and error in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\account-settings-dialog.tsx
- [ ] T008 Normalize protected return behavior for account save actions in D:\desenv\fidc-cdc-kogito\frontend\src\features\security\actions.ts
- [ ] T009 Identify and replace non-compliant UI primitives in the account settings flow with constitution-compliant components in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\account-settings-dialog.tsx

**Checkpoint**: Foundation ready - user story implementation can now begin in parallel

---

## Phase 3: User Story 1 - Fechar a janela de ajustes (Priority: P1) 🎯 MVP

**Goal**: Permitir fechamento explicito da janela com confirmacao de descarte apenas quando houver alteracoes nao salvas

**Independent Test**: Abrir a janela, fechar sem editar e confirmar fechamento imediato; depois editar um campo, fechar, cancelar descarte e confirmar que a janela permanece aberta com os valores em edicao

### Implementation for User Story 1

- [ ] T010 [US1] Add explicit close control and open/close transitions in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\account-settings-dialog.tsx
- [ ] T011 [US1] Implement dirty-state tracking and discard-confirmation flow in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\account-settings-dialog.tsx
- [ ] T012 [P] [US1] Implement keyboard and focus behavior for close and discard confirmation in the constitution-compliant dialog used by D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\account-settings-dialog.tsx
- [ ] T013 [US1] Preserve edited values when discard confirmation is cancelled in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\account-settings-dialog.tsx
- [ ] T014 [US1] Keep the topbar account-entry flow stable after dialog close and discard decisions in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\topbar-user-menu.tsx

**Checkpoint**: At this point, User Story 1 should be fully functional and testable independently

---

## Phase 4: User Story 2 - Preservar tema apos salvar dados (Priority: P1)

**Goal**: Preservar o tema ativo apos salvar email ou senha, com fechamento automatico da janela e feedback de sucesso na tela protegida

**Independent Test**: Selecionar tema diferente do padrao, salvar email e senha em execucoes separadas, confirmar fechamento automatico da janela, feedback de sucesso na tela protegida e manutencao do mesmo tema antes e depois da acao

### Implementation for User Story 2

- [ ] T015 [US2] Preserve the active theme across successful email save completion in D:\desenv\fidc-cdc-kogito\frontend\src\features\security\actions.ts
- [ ] T016 [US2] Preserve the active theme across successful password save completion in D:\desenv\fidc-cdc-kogito\frontend\src\features\security\actions.ts
- [ ] T017 [P] [US2] Keep ThemeToggle synchronized with the `localStorage`-first theme contract in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\theme-toggle.tsx
- [ ] T018 [US2] Close the dialog automatically on successful save while preserving theme state in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\account-settings-dialog.tsx
- [ ] T019 [US2] Render success feedback in the protected context after automatic dialog close in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\topbar-user-menu.tsx
- [ ] T020 [US2] Keep failure feedback visible in the dialog with the active theme unchanged and no protected-context loss in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\account-settings-dialog.tsx

**Checkpoint**: At this point, User Stories 1 and 2 should both work independently

---

## Phase 5: User Story 3 - Manter contexto apos salvar (Priority: P2)

**Goal**: Concluir salvamentos na mesma rota protegida, sem retorno para a tela inicial nem perda do shell autenticado

**Independent Test**: Abrir ajustes a partir de uma tela protegida interna, salvar email e senha em execucoes separadas e confirmar que a rota, o shell autenticado e o contexto funcional permanecem os mesmos

### Implementation for User Story 3

- [ ] T021 [US3] Preserve `currentPath` propagation for account save submissions in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\account-settings-dialog.tsx
- [ ] T022 [US3] Keep successful email saves on the protected origin without redirecting to `/` in D:\desenv\fidc-cdc-kogito\frontend\src\features\security\actions.ts
- [ ] T023 [US3] Keep successful password saves on the protected origin without redirecting to `/` in D:\desenv\fidc-cdc-kogito\frontend\src\features\security\actions.ts
- [ ] T024 [P] [US3] Preserve the authenticated shell and refresh behavior after account saves in D:\desenv\fidc-cdc-kogito\frontend\src\app\layout.tsx
- [ ] T025 [P] [US3] Prevent home-page authenticated redirect side effects from interfering with protected return flows in D:\desenv\fidc-cdc-kogito\frontend\src\app\page.tsx

**Checkpoint**: All user stories should now be independently functional

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Consolidar documentacao, validacao e guardrails de regressao

- [ ] T026 Update component-governance, observability, and constitution-alignment notes in D:\desenv\fidc-cdc-kogito\specs\004-fix-user-modal-theme\research.md
- [ ] T027 Update data-state and validation details for discard, success, and theme persistence in D:\desenv\fidc-cdc-kogito\specs\004-fix-user-modal-theme\data-model.md
- [ ] T028 Update quickstart scenarios for discard confirmation, automatic close, and protected-context feedback in D:\desenv\fidc-cdc-kogito\specs\004-fix-user-modal-theme\quickstart.md
- [ ] T029 Validate the final flow contract against the implemented states and behaviors in D:\desenv\fidc-cdc-kogito\specs\004-fix-user-modal-theme\contracts\account-settings-flow.md
- [ ] T030 Validate accessibility, focus visibility, semantic feedback, and protected-context behavior in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\account-settings-dialog.tsx
- [ ] T031 Document and validate session preservation, protected-context continuity, and sensitive-data handling for account updates in D:\desenv\fidc-cdc-kogito\specs\004-fix-user-modal-theme\research.md
- [ ] T032 Validate close, discard-confirmation, and post-save response timing against the 1-second local goal in D:\desenv\fidc-cdc-kogito\specs\004-fix-user-modal-theme\quickstart.md

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
- **User Story 2 (P1)**: Can start after Foundational (Phase 2) - depends only on shared theme and dialog foundations
- **User Story 3 (P2)**: Can start after Foundational (Phase 2) - depends only on shared return-path and shell foundations

### Within Each User Story

- Shared state and flow decisions before story-specific behavior
- Dialog behavior before consuming topbar or shell adjustments
- Theme/session helpers before final success and failure feedback adjustments
- Return-path propagation before protected-shell validation

### Parallel Opportunities

- `T003` and `T004`
- `T005` and `T006`
- `T012` and `T014`
- `T017` and `T019`
- `T024` and `T025`
- `T026`, `T027`, and `T028`

---

## Parallel Example: User Story 1

```bash
Task: "Extend keyboard and focus behavior in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\dialog.tsx"
Task: "Keep the topbar account-entry flow stable in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\topbar-user-menu.tsx"
```

---

## Parallel Example: User Story 2

```bash
Task: "Keep ThemeToggle synchronized in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\theme-toggle.tsx"
Task: "Render success feedback in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\topbar-user-menu.tsx"
```

---

## Parallel Example: User Story 3

```bash
Task: "Preserve the authenticated shell in D:\desenv\fidc-cdc-kogito\frontend\src\app\layout.tsx"
Task: "Prevent home-page redirect side effects in D:\desenv\fidc-cdc-kogito\frontend\src\app\page.tsx"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational
3. Complete Phase 3: User Story 1
4. **STOP and VALIDATE**: Confirm explicit close behavior and discard-cancel flow

### Incremental Delivery

1. Complete Setup + Foundational -> foundation ready
2. Add User Story 1 -> validate close and discard behavior
3. Add User Story 2 -> validate theme persistence and protected-context feedback
4. Add User Story 3 -> validate protected return flow
5. Finish with documentation and accessibility validation

### Parallel Team Strategy

With multiple developers:

1. One developer finaliza fundacao de tema e retorno protegido
2. Outro developer implementa o comportamento do dialogo e descarte
3. Depois da fundacao, US2 e US3 podem seguir em paralelo

---

## Notes

- [P] tasks = different files, no dependencies
- [Story] label maps task to specific user story for traceability
- Each user story should be independently completable and testable
- Replace non-compliant UI primitives in this flow instead of reusing them
- Keep the fix aligned with React, Next.js, TypeScript, and the constitutional UI baseline
- Session safety, protected-context continuity, and the 1-second local response goal must be validated before completion
