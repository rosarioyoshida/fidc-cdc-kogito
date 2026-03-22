# Tasks: Fechamento de Ajustes e Persistencia de Tema

**Input**: Design documents from `/specs/004-fix-user-modal-theme/`
**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/account-settings-flow.md, quickstart.md

**Tests**: Nao ha exigencia de TDD nesta feature. A validacao sera feita por cenarios integrados, quickstart e verificacoes proporcionais ao risco.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Preparar a base documental e os pontos de entrada da correcao

- [X] T001 Review feature contract and flow notes in D:\desenv\fidc-cdc-kogito\specs\004-fix-user-modal-theme\contracts\account-settings-flow.md
- [X] T002 Review current account-settings flow touchpoints in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\account-settings-dialog.tsx
- [X] T003 [P] Review theme persistence bootstrap in D:\desenv\fidc-cdc-kogito\frontend\src\app\layout.tsx
- [X] T004 [P] Review post-save redirect behavior in D:\desenv\fidc-cdc-kogito\frontend\src\features\security\actions.ts

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Centralizar as regras compartilhadas de dialogo, tema e navegacao protegida

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [X] T005 Define shared close-and-return behavior for account settings in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\account-settings-dialog.tsx
- [X] T006 [P] Define theme preservation source of truth for account actions in D:\desenv\fidc-cdc-kogito\frontend\src\lib\auth.ts
- [X] T007 [P] Define protected redirect normalization for account saves in D:\desenv\fidc-cdc-kogito\frontend\src\features\security\actions.ts
- [X] T008 Align layout theme bootstrap with preserved session theme in D:\desenv\fidc-cdc-kogito\frontend\src\app\layout.tsx
- [X] T009 Verify RFC 9457 and error feedback behavior remains compatible with account saves in D:\desenv\fidc-cdc-kogito\frontend\src\lib\api-client.ts

**Checkpoint**: Foundation ready - user story implementation can now begin in parallel

---

## Phase 3: User Story 1 - Fechar a janela de ajustes (Priority: P1) 🎯 MVP

**Goal**: Permitir que o usuario feche a janela de ajustes explicitamente, sem salvar automaticamente

**Independent Test**: Abrir a janela de ajustes, editar campos sem salvar, acionar o fechamento e confirmar que a janela fecha sem aplicar mudancas

### Implementation for User Story 1

- [X] T010 [US1] Add a visible close action for the account settings dialog header in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\account-settings-dialog.tsx
- [X] T011 [US1] Ensure closing the account settings dialog clears only UI state without triggering save in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\account-settings-dialog.tsx
- [X] T012 [P] [US1] Align dialog close affordance and keyboard behavior in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\dialog.tsx
- [X] T013 [US1] Preserve topbar account entry behavior after dialog close in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\topbar-user-menu.tsx

**Checkpoint**: At this point, User Story 1 should be fully functional and testable independently

---

## Phase 4: User Story 2 - Preservar tema apos salvar dados (Priority: P1)

**Goal**: Manter o modo visual escolhido anteriormente apos salvar email ou senha

**Independent Test**: Selecionar um tema diferente do padrao, salvar email e senha em execucoes separadas e confirmar que o modo visual permanece inalterado

### Implementation for User Story 2

- [X] T014 [US2] Preserve active theme during email save flow in D:\desenv\fidc-cdc-kogito\frontend\src\features\security\actions.ts
- [X] T015 [US2] Preserve active theme during password save flow in D:\desenv\fidc-cdc-kogito\frontend\src\features\security\actions.ts
- [X] T016 [P] [US2] Refine browser theme session helpers for post-save restoration in D:\desenv\fidc-cdc-kogito\frontend\src\lib\auth.ts
- [X] T017 [P] [US2] Keep theme toggle state synchronized with restored theme in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\theme-toggle.tsx
- [X] T018 [US2] Keep account settings success and error feedback in the protected user context with the active theme preserved in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\account-settings-dialog.tsx

**Checkpoint**: At this point, User Stories 1 and 2 should both work independently

---

## Phase 5: User Story 3 - Manter contexto apos salvar (Priority: P2)

**Goal**: Concluir o salvamento sem redirecionar o usuario para a tela inicial e sem perder a rota protegida de origem

**Independent Test**: Abrir os ajustes a partir de uma tela protegida interna, salvar email e senha em execucoes separadas e confirmar que o usuario permanece no mesmo contexto funcional

### Implementation for User Story 3

- [X] T019 [US3] Preserve protected origin routing for email save completion in D:\desenv\fidc-cdc-kogito\frontend\src\features\security\actions.ts
- [X] T020 [US3] Preserve protected origin routing for password save completion in D:\desenv\fidc-cdc-kogito\frontend\src\features\security\actions.ts
- [X] T021 [P] [US3] Keep current pathname propagation stable from the dialog forms in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\account-settings-dialog.tsx
- [X] T022 [P] [US3] Prevent authenticated home redirection side effects from leaking into protected return flows in D:\desenv\fidc-cdc-kogito\frontend\src\app\page.tsx
- [X] T023 [US3] Preserve protected page shell and user context after successful account save in D:\desenv\fidc-cdc-kogito\frontend\src\app\layout.tsx

**Checkpoint**: All user stories should now be independently functional

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Consolidar validacao, documentacao e guardrails de regressao da feature

- [X] T024 [P] Document account-settings audit expectations for close, save success and save failure in D:\desenv\fidc-cdc-kogito\specs\004-fix-user-modal-theme\research.md
- [X] T025 [P] Document observable signals for theme preservation and incorrect post-save redirect detection in D:\desenv\fidc-cdc-kogito\specs\004-fix-user-modal-theme\research.md
- [X] T026 Update quickstart validation steps with the final close/theme/return behavior in D:\desenv\fidc-cdc-kogito\specs\004-fix-user-modal-theme\quickstart.md
- [X] T027 Validate account settings flow against contract coverage in D:\desenv\fidc-cdc-kogito\specs\004-fix-user-modal-theme\contracts\account-settings-flow.md
- [X] T028 Validate accessibility, focus visibility and semantic feedback of the dialog flow in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\account-settings-dialog.tsx
- [X] T029 Validate preserved theme and protected return behavior through the quickstart scenarios in D:\desenv\fidc-cdc-kogito\specs\004-fix-user-modal-theme\quickstart.md

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Stories (Phase 3+)**: All depend on Foundational phase completion
- **Polish (Phase 6)**: Depends on all desired user stories being complete

### User Story Dependencies

- **User Story 1 (P1)**: Can start after Foundational (Phase 2) - No dependencies on other stories
- **User Story 2 (P1)**: Can start after Foundational (Phase 2) - Reuses shared theme behavior but remains independently testable
- **User Story 3 (P2)**: Can start after Foundational (Phase 2) - Reuses shared redirect behavior but remains independently testable

### Within Each User Story

- Shared flow decisions before story-specific edits
- Dialog behavior before polish of consuming components
- Theme/session helpers before final feedback validation
- Redirect normalization before protected return validation

### Parallel Opportunities

- `T003` and `T004`
- `T006` and `T007`
- `T012` and `T013`
- `T016` and `T017`
- `T021` and `T022`
- `T024` and `T025`

---

## Parallel Example: User Story 2

```bash
Task: "Refine browser theme session helpers in D:\desenv\fidc-cdc-kogito\frontend\src\lib\auth.ts"
Task: "Keep theme toggle state synchronized in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\theme-toggle.tsx"
```

---

## Parallel Example: User Story 3

```bash
Task: "Keep current pathname propagation stable in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\account-settings-dialog.tsx"
Task: "Prevent home redirection side effects in D:\desenv\fidc-cdc-kogito\frontend\src\app\page.tsx"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational
3. Complete Phase 3: User Story 1
4. Validate explicit dialog close behavior before expanding the fix

### Incremental Delivery

1. Complete Setup + Foundational
2. Add User Story 1 and validate close behavior
3. Add User Story 2 and validate theme persistence
4. Add User Story 3 and validate protected return flow
5. Finish with quickstart and accessibility validation

### Parallel Team Strategy

1. One developer finaliza fundacao de tema e redirect
2. Outro developer implementa o comportamento do dialogo
3. Depois da fundacao, US2 e US3 podem seguir em paralelo

---

## Notes

- [P] tasks = different files, no dependencies
- [Story] label maps task to specific user story for traceability
- Each user story should remain independently testable
- No new dependencies should be introduced without prior consultation
- Keep the fix aligned with existing React, Next.js, TypeScript and shadcn/ui usage
