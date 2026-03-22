# Tasks: Controle de Acesso e Menu do Usuario

**Input**: Design documents from `/specs/003-basic-auth-menu/`
**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/

**Tests**: Esta feature exige testes de integracao e contrato proporcionais ao risco
para autenticacao, conta do usuario e logout.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (`US1`, `US2`, `US3`)
- Include exact file paths in descriptions

## Path Conventions

- **Web app**: `backend/src/`, `frontend/src/`, `frontend/tests/`
- **Feature docs**: `specs/003-basic-auth-menu/`

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Preparar a base da feature sem alterar a stack nem criar novos perfis

- [X] T001 Confirm seeded profiles and users in D:\desenv\fidc-cdc-kogito\backend\src\main\resources\db\migration\V5__seed_roles_permissions.sql
- [X] T002 Create feature notes and API mapping in D:\desenv\fidc-cdc-kogito\specs\003-basic-auth-menu\implementation-notes.md
- [X] T003 [P] Add frontend test fixture plan for seeded users in D:\desenv\fidc-cdc-kogito\frontend\tests\integration\auth-menu-fixtures.ts

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Preparar a infraestrutura compartilhada que bloqueia todas as histórias

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [X] T004 Implement backend current-user query support in D:\desenv\fidc-cdc-kogito\backend\src\main\java\com\fidc\cdc\kogito\application\security\CurrentUserQueryService.java
- [X] T005 [P] Implement frontend authenticated session utilities in D:\desenv\fidc-cdc-kogito\frontend\src\lib\auth.ts
- [X] T006 [P] Define shared user-account types for frontend in D:\desenv\fidc-cdc-kogito\frontend\src\features\security\user-account-types.ts
- [X] T007 Create RFC 9457-compatible account/auth error mapping in D:\desenv\fidc-cdc-kogito\frontend\src\lib\problem-details.ts
- [X] T008 Implement top-level protected layout strategy in D:\desenv\fidc-cdc-kogito\frontend\src\app\layout.tsx
- [X] T009 Document audit and observability expectations for auth/account events in D:\desenv\fidc-cdc-kogito\specs\003-basic-auth-menu\implementation-notes.md

**Checkpoint**: Foundation ready - user story implementation can now begin in parallel

---

## Phase 3: User Story 1 - Entrar com credenciais validas (Priority: P1) 🎯 MVP

**Goal**: Exigir autenticacao Basic Auth para acesso a areas protegidas e identificar a sessao autenticada.

**Independent Test**: Abrir a interface sem sessao ativa, informar credenciais validas
de usuario seedado e confirmar que o acesso protegido e liberado; repetir com
credenciais invalidas e confirmar que o acesso e negado com feedback claro.

### Tests for User Story 1 ⚠️

- [X] T010 [P] [US1] Add backend integration test for current authenticated user in D:\desenv\fidc-cdc-kogito\backend\src\test\java\com\fidc\cdc\kogito\integration\security\CurrentUserIntegrationTest.java
- [X] T011 [P] [US1] Add frontend integration test for protected access and login failure in D:\desenv\fidc-cdc-kogito\frontend\tests\integration\basic-auth-access.spec.tsx

### Implementation for User Story 1

- [X] T012 [P] [US1] Add authenticated user endpoint controller in D:\desenv\fidc-cdc-kogito\backend\src\main\java\com\fidc\cdc\kogito\api\security\CurrentUserController.java
- [X] T013 [P] [US1] Add authenticated user application service in D:\desenv\fidc-cdc-kogito\backend\src\main\java\com\fidc\cdc\kogito\application\security\CurrentUserService.java
- [X] T014 [US1] Update backend security flow for protected current-user access in D:\desenv\fidc-cdc-kogito\backend\src\main\java\com\fidc\cdc\kogito\security\SecurityConfig.java
- [X] T015 [US1] Implement frontend login/session bootstrap in D:\desenv\fidc-cdc-kogito\frontend\src\lib\auth.ts
- [X] T016 [US1] Create protected access flow and login screen behavior in D:\desenv\fidc-cdc-kogito\frontend\src\app\page.tsx
- [X] T017 [US1] Add auth failure feedback handling in D:\desenv\fidc-cdc-kogito\frontend\src\components\feedback\forbidden-state.tsx
- [X] T018 [US1] Implement audit logging for login success, login failure and logout in D:\desenv\fidc-cdc-kogito\backend\src\main\java\com\fidc\cdc\kogito\infrastructure\audit\AuthAuditService.java
- [X] T019 [US1] Implement authentication observability signals for login, failure and logout in D:\desenv\fidc-cdc-kogito\backend\src\main\java\com\fidc\cdc\kogito\infrastructure\observability\AuthMetricsService.java

**Checkpoint**: At this point, User Story 1 should be fully functional and testable independently

---

## Phase 4: User Story 2 - Ver dados do usuario no menu superior (Priority: P1)

**Goal**: Exibir nome e perfil seedado do usuario autenticado de forma consistente no menu superior.

**Independent Test**: Autenticar um usuario seedado e confirmar que o menu superior
mostra nome e perfil corretos nas telas protegidas relevantes.

### Tests for User Story 2 ⚠️

- [X] T020 [P] [US2] Add backend contract test for `GET /api/v1/usuarios/me` in D:\desenv\fidc-cdc-kogito\backend\src\test\java\com\fidc\cdc\kogito\integration\api\CurrentUserContractIntegrationTest.java
- [X] T021 [P] [US2] Add frontend integration test for topbar identity rendering in D:\desenv\fidc-cdc-kogito\frontend\tests\integration\topbar-user-menu.spec.tsx

### Implementation for User Story 2

- [X] T022 [P] [US2] Implement current-user response model in D:\desenv\fidc-cdc-kogito\backend\src\main\java\com\fidc\cdc\kogito\api\security\CurrentUserResponse.java
- [X] T023 [P] [US2] Implement frontend account query client in D:\desenv\fidc-cdc-kogito\frontend\src\lib\api-client.ts
- [X] T024 [US2] Create topbar user menu component in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\topbar-user-menu.tsx
- [X] T025 [US2] Integrate topbar user menu into protected layout in D:\desenv\fidc-cdc-kogito\frontend\src\app\layout.tsx
- [X] T026 [US2] Ensure seeded profile display mapping in D:\desenv\fidc-cdc-kogito\frontend\src\features\security\types.ts

**Checkpoint**: At this point, User Stories 1 AND 2 should both work independently

---

## Phase 5: User Story 3 - Gerenciar conta e encerrar sessao (Priority: P2)

**Goal**: Permitir ao usuario autenticado alterar o proprio email, alterar a propria senha e fazer logout imediato.

**Independent Test**: Autenticar um usuario seedado, abrir o menu superior, alterar o
proprio email, alterar a propria senha e confirmar que o logout invalida
imediatamente a sessao.

### Tests for User Story 3 ⚠️

- [X] T027 [P] [US3] Add backend integration test for self-service account update in D:\desenv\fidc-cdc-kogito\backend\src\test\java\com\fidc\cdc\kogito\integration\security\SelfAccountIntegrationTest.java
- [X] T028 [P] [US3] Add frontend integration test for account actions and logout in D:\desenv\fidc-cdc-kogito\frontend\tests\integration\account-actions.spec.tsx

### Implementation for User Story 3

- [X] T029 [P] [US3] Add self-account email update request model in D:\desenv\fidc-cdc-kogito\backend\src\main\java\com\fidc\cdc\kogito\api\security\UpdateOwnEmailRequest.java
- [X] T030 [P] [US3] Add self-account password update request model in D:\desenv\fidc-cdc-kogito\backend\src\main\java\com\fidc\cdc\kogito\api\security\UpdateOwnPasswordRequest.java
- [X] T031 [US3] Add self-account application service in D:\desenv\fidc-cdc-kogito\backend\src\main\java\com\fidc\cdc\kogito\application\security\SelfAccountService.java
- [X] T032 [US3] Add self-account endpoints for email and password update in D:\desenv\fidc-cdc-kogito\backend\src\main\java\com\fidc\cdc\kogito\api\security\SelfAccountController.java
- [X] T033 [US3] Implement audit logging for self-account update events in D:\desenv\fidc-cdc-kogito\backend\src\main\java\com\fidc\cdc\kogito\infrastructure\audit\AccountAuditService.java
- [X] T034 [US3] Implement account dialog or panel in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\account-settings-dialog.tsx
- [X] T035 [US3] Implement logout invalidation flow in D:\desenv\fidc-cdc-kogito\frontend\src\lib\auth.ts
- [X] T036 [US3] Wire account actions into topbar user menu in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\topbar-user-menu.tsx

**Checkpoint**: All user stories should now be independently functional

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Validacoes finais de qualidade, contratos, acessibilidade e documentacao

- [X] T037 [P] Validate quickstart scenarios in D:\desenv\fidc-cdc-kogito\specs\003-basic-auth-menu\quickstart.md
- [X] T038 [P] Validate contract alignment in D:\desenv\fidc-cdc-kogito\specs\003-basic-auth-menu\contracts\auth-account-api.md
- [X] T039 Validate seeded profile usage against D:\desenv\fidc-cdc-kogito\backend\src\main\resources\db\migration\V5__seed_roles_permissions.sql
- [X] T040 Validate RFC 9457 handling for auth/account errors in D:\desenv\fidc-cdc-kogito\backend\src\main\java\com\fidc\cdc\kogito\api\error\ProblemDetailsHandler.java
- [X] T041 Validate topbar accessibility, focus visibility and keyboard support in D:\desenv\fidc-cdc-kogito\frontend\src\components\ui\topbar-user-menu.tsx
- [X] T042 [P] Add login-to-protected-area timing validation in D:\desenv\fidc-cdc-kogito\frontend\tests\integration\basic-auth-performance.spec.tsx
- [X] T043 Update feature documentation and usage notes in D:\desenv\fidc-cdc-kogito\specs\003-basic-auth-menu\quickstart.md

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Story 1 (Phase 3)**: Depends on Foundational completion
- **User Story 2 (Phase 4)**: Depends on User Story 1 because it consumes authenticated identity
- **User Story 3 (Phase 5)**: Depends on User Story 1 and integrates with User Story 2 menu surface
- **Polish (Phase 6)**: Depends on all desired user stories being complete

### User Story Dependencies

- **User Story 1 (P1)**: Can start after Foundational - no dependencies on other stories
- **User Story 2 (P1)**: Depends on US1 for authenticated session and current-user data
- **User Story 3 (P2)**: Depends on US1 for session handling and US2 for menu entry point

### Within Each User Story

- Tests MUST be written and fail before implementation
- Backend service and models before controller wiring
- Frontend session and client wiring before UI integration
- Core implementation before cross-story polish

### Parallel Opportunities

- Setup task `T003` can run in parallel with `T001` and `T002`
- Foundational tasks `T005` and `T006` can run in parallel
- US1 tests `T010` and `T011` can run in parallel
- US1 backend tasks `T012` and `T013` can run in parallel
- US2 tests `T020` and `T021` can run in parallel
- US2 backend/frontend prep tasks `T022` and `T023` can run in parallel
- US3 tests `T027` and `T028` can run in parallel
- US3 backend prep tasks `T029` and `T030` can run in parallel
- Polish tasks `T037` and `T038` can run in parallel

---

## Parallel Example: User Story 1

```bash
# Launch User Story 1 tests together:
Task: "Add backend integration test for current authenticated user in backend/src/test/java/com/fidc/cdc/kogito/integration/security/CurrentUserIntegrationTest.java"
Task: "Add frontend integration test for protected access and login failure in frontend/tests/integration/basic-auth-access.spec.tsx"

# Launch backend preparation work together:
Task: "Add authenticated user endpoint controller in backend/src/main/java/com/fidc/cdc/kogito/api/security/CurrentUserController.java"
Task: "Add authenticated user application service in backend/src/main/java/com/fidc/cdc/kogito/application/security/CurrentUserService.java"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational
3. Complete Phase 3: User Story 1
4. **STOP and VALIDATE**: Confirm authenticated access works with seeded users

### Incremental Delivery

1. Complete Setup + Foundational
2. Deliver US1 authenticated access
3. Add US2 topbar identity display
4. Add US3 self-account actions and logout
5. Finish with cross-cutting validation and documentation

### Parallel Team Strategy

1. Team completes Setup + Foundational together
2. Once Foundational is done:
   - Developer A: User Story 1 backend
   - Developer B: User Story 1 frontend
3. After US1:
   - Developer A: User Story 2 backend identity contract
   - Developer B: User Story 2 topbar UI
4. After US2:
   - Developer A: User Story 3 backend account actions and audit trail
   - Developer B: User Story 3 frontend account dialog and logout flow

---

## Notes

- All tasks follow the required checklist format with task ID, optional `[P]`, optional `[USx]`, and exact file path
- Tests are included because the plan explicitly requires integration and contract validation
- No task should create new roles or permissions outside the seeded profiles
- No task should add dependencies without prior consultation
- Preserve Basic Auth and seeded profile alignment across backend, frontend and docs
