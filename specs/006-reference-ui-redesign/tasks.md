# Tasks: Migracao Visual da Interface por Referencia

**Input**: Design documents from `/specs/006-reference-ui-redesign/`
**Prerequisites**: [plan.md](D:/desenv/fidc-cdc-kogito/specs/006-reference-ui-redesign/plan.md), [spec.md](D:/desenv/fidc-cdc-kogito/specs/006-reference-ui-redesign/spec.md), [research.md](D:/desenv/fidc-cdc-kogito/specs/006-reference-ui-redesign/research.md), [data-model.md](D:/desenv/fidc-cdc-kogito/specs/006-reference-ui-redesign/data-model.md), [contracts/ui-redesign-contract.md](D:/desenv/fidc-cdc-kogito/specs/006-reference-ui-redesign/contracts/ui-redesign-contract.md), [quickstart.md](D:/desenv/fidc-cdc-kogito/specs/006-reference-ui-redesign/quickstart.md)

**Tests**: Incluir atualizacao de testes unitarios e de integracao existentes do frontend, pois a spec exige cenarios testaveis e validacao guiada dos fluxos priorizados.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Path Conventions

- Frontend web app: `frontend/src/` e `frontend/tests/`
- Feature docs: `specs/006-reference-ui-redesign/`

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Preparar a base de componentes `shadcn/ui` sem romper os tokens atuais do produto

- [X] T001 Create `frontend/components.json` aligned with `frontend/tailwind.config.ts` and the aliases used under `frontend/src/`
- [X] T002 Update `frontend/tailwind.config.ts`, `frontend/src/design-system/theme.css`, and `frontend/src/app/globals.css` to lock the current color/token/typography contract for incoming `shadcn/ui` surfaces

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Disponibilizar as superficies compartilhadas que bloqueiam todas as historias

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [X] T003 [P] Add token-aligned `card` and `separator` primitives in `frontend/src/components/ui/card.tsx` and `frontend/src/components/ui/separator.tsx`
- [X] T004 [P] Add token-aligned `badge` and `alert` primitives in `frontend/src/components/ui/badge.tsx` and `frontend/src/components/ui/alert.tsx`
- [X] T005 [P] Add token-aligned menu/navigation primitive in `frontend/src/components/ui/dropdown-menu.tsx`
- [X] T006 [P] Add coverage for the new shared primitives in `frontend/src/components/ui/card.test.tsx`, `frontend/src/components/ui/separator.test.tsx`, `frontend/src/components/ui/badge.test.tsx`, `frontend/src/components/ui/alert.test.tsx`, and `frontend/src/components/ui/dropdown-menu.test.tsx`

**Checkpoint**: Foundation ready - user story implementation can now begin in parallel

---

## Phase 3: User Story 1 - Atualizar a aparencia sem mudar o trabalho do usuario (Priority: P1) 🎯 MVP

**Goal**: Renovar a experiencia visual das telas prioritarias sem alterar fluxos, dados, permissoes ou acoes ja existentes

**Independent Test**: Navegar por login, topbar autenticada, lista de cessoes e detalhe da cessao confirmando nova hierarquia visual com os mesmos passos operacionais atuais

### Tests for User Story 1

- [X] T007 [P] [US1] Update authenticated shell tests in `frontend/src/components/ui/topbar-user-menu.test.tsx`, `frontend/src/components/ui/theme-toggle.test.tsx`, and `frontend/tests/integration/topbar-user-menu.spec.tsx`
- [X] T008 [P] [US1] Update access and cession flow tests in `frontend/src/features/security/login-panel.test.tsx`, `frontend/src/features/cessao/cessao-list.test.tsx`, and `frontend/src/features/cessao/cessao-detail.test.tsx`

### Implementation for User Story 1

- [X] T009 [US1] Refactor the authenticated shell in `frontend/src/app/layout.tsx` and `frontend/src/components/ui/topbar-user-menu.tsx` to use the shared `shadcn/ui` surfaces with current tokens and no new actions
- [X] T010 [US1] Refactor the access screen in `frontend/src/app/page.tsx` and `frontend/src/features/security/login-panel.tsx` to apply the new hierarchy without changing fields, copy intent, or sign-in flow
- [X] T011 [US1] Refactor the cession listing screen in `frontend/src/app/cessoes/page.tsx`, `frontend/src/features/cessao/cessao-list.tsx`, and `frontend/src/components/feedback/empty-state.tsx` to emphasize context and primary action using the approved surfaces
- [X] T012 [US1] Refactor the cession detail screen in `frontend/src/app/cessoes/[businessKey]/page.tsx`, `frontend/src/features/cessao/cessao-detail.tsx`, and `frontend/src/features/cessao/cessao-status-panel.tsx` to preserve task flow and status readability with the new visual composition
- [X] T013 [US1] Align account and access feedback surfaces in `frontend/src/components/ui/account-settings-dialog.tsx` and `frontend/src/components/feedback/forbidden-state.tsx` with the same token-preserving visual language
- [X] T014 [US1] Adjust responsive behavior for `frontend/src/app/page.tsx`, `frontend/src/app/cessoes/page.tsx`, `frontend/src/features/security/login-panel.tsx`, and `frontend/src/features/cessao/cessao-list.tsx` to preserve hierarchy and action access in supported widths

**Checkpoint**: At this point, User Story 1 should be fully functional and testable independently

---

## Phase 4: User Story 2 - Reconhecer padroes visuais consistentes entre telas (Priority: P2)

**Goal**: Aplicar padroes consistentes de cabecalho, agrupamento, destaque e feedback nas telas operacionais e analiticas priorizadas

**Independent Test**: Alternar entre detalhe, analise e auditoria confirmando que navegacao, destaque, blocos de informacao e mensagens seguem o mesmo padrao visual

### Tests for User Story 2

- [X] T015 [P] [US2] Update analysis regression coverage in `frontend/tests/integration/analise-cessao.spec.tsx`, `frontend/src/features/analise/calculo-panel.test.tsx`, and `frontend/src/features/analise/contratos-panel.test.tsx`
- [X] T016 [P] [US2] Update analysis and audit panel coverage in `frontend/src/features/analise/elegibilidade-panel.test.tsx`, `frontend/src/features/analise/lastro-panel.test.tsx`, `frontend/src/features/analise/registradora-panel.test.tsx`, and `frontend/tests/integration/auditoria-permissoes.spec.tsx`

### Implementation for User Story 2

- [X] T017 [US2] Refactor the analysis shell in `frontend/src/app/cessoes/[businessKey]/analise/page.tsx` and `frontend/src/features/analise/analise-dashboard.tsx` to standardize headers, feedback, and grouping
- [X] T018 [P] [US2] Refactor analysis surfaces in `frontend/src/features/analise/calculo-panel.tsx`, `frontend/src/features/analise/contratos-panel.tsx`, and `frontend/src/features/analise/elegibilidade-panel.tsx` to use the same card/badge/alert vocabulary
- [X] T019 [P] [US2] Refactor analysis surfaces in `frontend/src/features/analise/lastro-panel.tsx` and `frontend/src/features/analise/registradora-panel.tsx` to match the same spacing, emphasis, and action hierarchy
- [X] T020 [US2] Refactor the audit screen in `frontend/src/app/cessoes/[businessKey]/auditoria/page.tsx`, `frontend/src/features/auditoria/task-context-panel.tsx`, and `frontend/src/features/auditoria/audit-timeline.tsx` to align with the shared layout language without adding data or controls
- [X] T021 [US2] Normalize supporting interaction surfaces in `frontend/src/components/ui/theme-toggle.tsx`, `frontend/src/components/ui/account-settings-dialog.tsx`, and `frontend/src/features/cessao/cessao-status-panel.tsx` so equivalent statuses and actions look consistent across screens
- [X] T022 [US2] Adjust responsive behavior for `frontend/src/features/cessao/cessao-detail.tsx`, `frontend/src/features/analise/analise-dashboard.tsx`, `frontend/src/features/auditoria/task-context-panel.tsx`, and `frontend/src/features/auditoria/audit-timeline.tsx` to preserve layout clarity in supported widths

**Checkpoint**: At this point, User Stories 1 AND 2 should both work independently

---

## Phase 5: User Story 3 - Proteger o escopo contra criacao indevida de features (Priority: P3)

**Goal**: Garantir que a referencia visual nao introduza novos dados, acoes, componentes ad hoc ou desvios de governanca

**Independent Test**: Revisar todas as telas da primeira onda confirmando que qualquer superficie nova vem do catalogo local ou do `shadcn/ui` e que nenhum novo fluxo foi criado

### Tests for User Story 3

- [X] T023 [P] [US3] Add scope-regression assertions in `frontend/tests/integration/analise-cessao.spec.tsx`, `frontend/tests/integration/auditoria-permissoes.spec.tsx`, and `frontend/tests/integration/topbar-user-menu.spec.tsx` to verify no new actions or data paths were introduced

### Implementation for User Story 3

- [X] T024 [P] [US3] Record the component inventory and reuse decisions in `specs/006-reference-ui-redesign/research.md` and `specs/006-reference-ui-redesign/contracts/ui-redesign-contract.md` for every new surface added during implementation
- [X] T025 [US3] Review the first-wave routes in `frontend/src/app/page.tsx`, `frontend/src/app/cessoes/page.tsx`, `frontend/src/app/cessoes/[businessKey]/page.tsx`, `frontend/src/app/cessoes/[businessKey]/analise/page.tsx`, and `frontend/src/app/cessoes/[businessKey]/auditoria/page.tsx` to remove any accidental new feature, filter, or CTA introduced during redesign
- [X] T026 [P] [US3] Consolidate all newly adopted `shadcn/ui` surfaces under `frontend/src/components/ui/` and update `frontend/components.json` so no first-wave screen depends on uncatalogued local UI

**Checkpoint**: All user stories should now be independently functional

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Fechar validacao transversal, responsividade e rastreabilidade final

- [ ] T027 [P] Record the final manual validation evidence in `specs/006-reference-ui-redesign/quickstart.md` and `specs/006-reference-ui-redesign/contracts/ui-redesign-contract.md`, including the 5-evaluator timing checklist per first-wave screen
- [X] T028 [P] Capture the final implementation inventory and any approved exceptions in `specs/006-reference-ui-redesign/research.md` and `specs/006-reference-ui-redesign/data-model.md`
- [X] T029 Refine shared empty/feedback semantics in `frontend/src/components/feedback/empty-state.tsx`, `frontend/src/components/feedback/forbidden-state.tsx`, and `frontend/src/components/ui/alert.tsx` after end-to-end validation across all stories
- [X] T030 [P] Review critical frontend feedback signals in `frontend/src/components/feedback/empty-state.tsx`, `frontend/src/components/feedback/forbidden-state.tsx`, `frontend/src/components/ui/alert.tsx`, and `frontend/tests/integration/analise-cessao.spec.tsx` to ensure success, error, and empty states remain detectable in tests and manual validation

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Stories (Phase 3+)**: Depend on Foundational completion
- **Polish (Phase 6)**: Depends on all desired user stories being complete

### User Story Dependencies

- **User Story 1 (P1)**: Can start after Foundational - no dependency on other stories
- **User Story 2 (P2)**: Can start after Foundational, but benefits from US1 because it extends the same visual language to analysis and audit surfaces
- **User Story 3 (P3)**: Can start after Foundational and should be finalized after US1/US2 to review the implemented surfaces and prevent scope drift

### Within Each User Story

- Story tests should be updated before or alongside the corresponding UI refactor
- Shared surfaces should be added before screens that consume them
- Screen shells should be refactored before secondary panels inside the same story
- Validation artifacts should be updated only after the implementation for that story is stable

### Parallel Opportunities

- `T003`, `T004`, `T005`, and `T006` can run in parallel after Setup
- `T007` and `T008` can run in parallel inside US1
- `T015` and `T016` can run in parallel inside US2
- `T018` and `T019` can run in parallel inside US2
- `T023`, `T024`, and `T026` can run in parallel once the redesign surfaces are implemented
- `T027`, `T028`, and `T030` can run in parallel in the polish phase

---

## Parallel Example: User Story 1

```bash
# Launch test updates for User Story 1 together:
Task: "Update authenticated shell tests in frontend/src/components/ui/topbar-user-menu.test.tsx, frontend/src/components/ui/theme-toggle.test.tsx, and frontend/tests/integration/topbar-user-menu.spec.tsx"
Task: "Update access and cession flow tests in frontend/src/features/security/login-panel.test.tsx, frontend/src/features/cessao/cessao-list.test.tsx, and frontend/src/features/cessao/cessao-detail.test.tsx"

# Launch independent visual refactors for User Story 1 together after shared shell decisions settle:
Task: "Refactor the access screen in frontend/src/app/page.tsx and frontend/src/features/security/login-panel.tsx"
Task: "Refactor the cession listing screen in frontend/src/app/cessoes/page.tsx, frontend/src/features/cessao/cessao-list.tsx, and frontend/src/components/feedback/empty-state.tsx"
```

---

## Parallel Example: User Story 2

```bash
# Launch analysis and audit test updates together:
Task: "Update analysis regression coverage in frontend/tests/integration/analise-cessao.spec.tsx, frontend/src/features/analise/calculo-panel.test.tsx, and frontend/src/features/analise/contratos-panel.test.tsx"
Task: "Update analysis and audit panel coverage in frontend/src/features/analise/elegibilidade-panel.test.tsx, frontend/src/features/analise/lastro-panel.test.tsx, frontend/src/features/analise/registradora-panel.test.tsx, and frontend/tests/integration/auditoria-permissoes.spec.tsx"

# Launch independent panel refactors together:
Task: "Refactor analysis surfaces in frontend/src/features/analise/calculo-panel.tsx, frontend/src/features/analise/contratos-panel.tsx, and frontend/src/features/analise/elegibilidade-panel.tsx"
Task: "Refactor analysis surfaces in frontend/src/features/analise/lastro-panel.tsx and frontend/src/features/analise/registradora-panel.tsx"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational
3. Complete Phase 3: User Story 1
4. Stop and validate login, topbar, cession list, and cession detail against the quickstart

### Incremental Delivery

1. Deliver shared `shadcn/ui` surfaces and token contract first
2. Deliver US1 as the MVP for the first-wave operational flow
3. Deliver US2 to propagate the same language to analysis and audit
4. Deliver US3 to freeze governance, prevent scope drift, and close documentation evidence
5. Finish with cross-cutting validation and polish

### Parallel Team Strategy

1. One developer handles Setup + Foundational surfaces
2. After Foundation:
   - Developer A: US1 access and cession flow
   - Developer B: US2 analysis and audit consistency
   - Developer C: US3 governance evidence and scope-regression validation

---

## Notes

- [P] tasks = different files, no dependencies on incomplete work
- [US1], [US2], and [US3] map directly to the clarified stories in `spec.md`
- MVP scope is User Story 1
- Every task above includes exact file paths
- No task authorizes a custom component outside `frontend/src/components/ui` without the formal exception flow defined in the contract
