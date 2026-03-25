# Tasks: Migração do Frontend para Novo Shell Horizontal

**Input**: Design documents from `/specs/007-migrate-frontend-shell/`
**Prerequisites**: [plan.md](D:/desenv/fidc-cdc-kogito/specs/007-migrate-frontend-shell/plan.md), [spec.md](D:/desenv/fidc-cdc-kogito/specs/007-migrate-frontend-shell/spec.md), [research.md](D:/desenv/fidc-cdc-kogito/specs/007-migrate-frontend-shell/research.md), [data-model.md](D:/desenv/fidc-cdc-kogito/specs/007-migrate-frontend-shell/data-model.md), [ui-shell-migration-contract.md](D:/desenv/fidc-cdc-kogito/specs/007-migrate-frontend-shell/contracts/ui-shell-migration-contract.md)

**Tests**: Incluídos porque o plano exige Vitest, Testing Library, testes de integração existentes e validação guiada dos fluxos priorizados.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Path Conventions

- Web app paths use `frontend/src/` for implementation and `frontend/tests/` for automated validation.

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Registrar a baseline técnica e preparar os pontos de entrada da migração

- [X] T001 Revisar e registrar o reúso obrigatório de componentes em `frontend/src/components/ui/` e a estratégia de exceção em `specs/007-migrate-frontend-shell/contracts/ui-shell-migration-contract.md`
- [X] T002 Mapear no plano de trabalho as rotas do escopo e seus perfis de apresentação em `specs/007-migrate-frontend-shell/data-model.md`
- [X] T003 [P] Preparar a suíte de validação da migração adicionando cenário agregador em `frontend/tests/integration/shell-migration.spec.tsx`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Criar a infraestrutura compartilhada que bloqueia todas as user stories

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [X] T004 Criar a configuração central do shell e dos destinos de navegação em `frontend/src/features/navigation/shell-config.ts`
- [X] T005 Documentar a exceção formal e as alternativas rejeitadas para novos componentes de layout em `specs/007-migrate-frontend-shell/contracts/ui-shell-migration-contract.md` e `specs/007-migrate-frontend-shell/plan.md`
- [X] T006 Registrar a aprovação de arquitetura/frontend para os novos componentes de layout em `specs/007-migrate-frontend-shell/contracts/ui-shell-migration-contract.md`
- [X] T007 [P] Criar os componentes base do shell autenticado e do rodapé em `frontend/src/components/layout/app-shell.tsx`, `frontend/src/components/layout/app-header.tsx`, `frontend/src/components/layout/app-footer.tsx`
- [X] T008 [P] Criar os componentes reutilizáveis de barra contextual e seção de página em `frontend/src/components/layout/secondary-nav.tsx`, `frontend/src/components/layout/page-section.tsx`
- [X] T009 [P] Criar a base reutilizável de linha horizontal padronizada em `frontend/src/components/layout/data-row-card.tsx`
- [X] T010 Refatorar `frontend/src/components/ui/topbar-user-menu.tsx` para separar notificações, menu de conta e integração com o novo header preservando alteração de senha e logout
- [X] T011 Atualizar `frontend/src/app/layout.tsx` para usar a infraestrutura compartilhada de shell, tema e resolução de usuário autenticado
- [X] T012 [P] Criar testes de componentes do shell compartilhado em `frontend/src/components/layout/app-shell.test.tsx` e `frontend/src/components/layout/secondary-nav.test.tsx`
- [X] T013 [P] Definir e documentar sinais observáveis do shell para erro, carregamento, resolução de usuário, navegação e ações de conta em `specs/007-migrate-frontend-shell/plan.md` e `specs/007-migrate-frontend-shell/quickstart.md`

**Checkpoint**: Foundation ready - user story implementation can now begin in parallel

---

## Phase 3: User Story 1 - Navegar pelo produto em um shell unificado (Priority: P1) 🎯 MVP

**Goal**: Entregar um shell consistente para login e páginas autenticadas, com header global, menu do usuário e rodapé padronizado

**Independent Test**: Acessar `/`, `/cessoes`, `/cessoes/[businessKey]`, `/cessoes/[businessKey]/analise` e `/cessoes/[businessKey]/auditoria` e confirmar shell consistente, login simplificado sem header, navegação principal real e menu do usuário com alteração de senha

### Tests for User Story 1

- [X] T014 [P] [US1] Atualizar o teste de menu do usuário para o novo header em `frontend/tests/integration/topbar-user-menu.spec.tsx`
- [X] T015 [P] [US1] Criar teste de integração do shell autenticado entre rotas em `frontend/tests/integration/shell-authenticated-navigation.spec.tsx`
- [X] T016 [P] [US1] Criar teste de integração da tela de login simplificada em `frontend/tests/integration/login-shell.spec.tsx`

### Implementation for User Story 1

- [X] T017 [US1] Implementar o shell simplificado do login em `frontend/src/app/page.tsx` e `frontend/src/features/security/login-panel.tsx`
- [X] T018 [US1] Aplicar o shell autenticado global às rotas em `frontend/src/app/cessoes/page.tsx`, `frontend/src/app/cessoes/[businessKey]/page.tsx`, `frontend/src/app/cessoes/[businessKey]/analise/page.tsx`, `frontend/src/app/cessoes/[businessKey]/auditoria/page.tsx`
- [X] T019 [US1] Implementar no header apenas os destinos globais definidos em `frontend/src/components/layout/app-header.tsx` e `frontend/src/features/navigation/shell-config.ts`
- [X] T020 [US1] Integrar notificações, avatar, alteração de senha e logout ao novo header em `frontend/src/components/ui/topbar-user-menu.tsx` e `frontend/src/components/ui/account-settings-dialog.tsx`
- [X] T021 [US1] Ajustar metadados visuais e identidade comum do shell em `frontend/src/app/globals.css`, `frontend/src/design-system/theme.css` e `frontend/src/design-system/tokens.ts`

**Checkpoint**: At this point, User Story 1 should be fully functional and testable independently

---

## Phase 4: User Story 2 - Encontrar contexto e ações locais com rapidez (Priority: P2)

**Goal**: Entregar a barra secundária como ponto padrão de contexto da rota, filtros, busca e ações locais

**Independent Test**: Navegar entre lista, detalhe, análise e auditoria verificando barra secundária ativa, controles locais preservados e ausência de espaços vazios ou ações irrelevantes

### Tests for User Story 2

- [X] T022 [P] [US2] Criar teste de integração da barra secundária e dos estados ativos em `frontend/tests/integration/secondary-nav-context.spec.tsx`
- [X] T023 [P] [US2] Criar teste de componentes da configuração contextual por rota em `frontend/src/features/navigation/shell-config.test.ts`

### Implementation for User Story 2

- [X] T024 [US2] Configurar por rota os destinos exclusivamente contextuais, filtros, seletores e busca em `frontend/src/features/navigation/shell-config.ts`
- [X] T025 [US2] Migrar ações locais e contexto da lista de cessões para a barra secundária em `frontend/src/app/cessoes/page.tsx` e `frontend/src/features/cessao/cessao-list.tsx`
- [X] T026 [US2] Migrar ações locais e destinos relacionados do detalhe para a barra secundária em `frontend/src/app/cessoes/[businessKey]/page.tsx` e `frontend/src/features/cessao/cessao-detail.tsx`
- [X] T027 [US2] Migrar ações locais e contexto da análise para a barra secundária em `frontend/src/app/cessoes/[businessKey]/analise/page.tsx` e `frontend/src/features/analise/analise-dashboard.tsx`
- [X] T028 [US2] Migrar ações locais e contexto da auditoria para a barra secundária em `frontend/src/app/cessoes/[businessKey]/auditoria/page.tsx`, `frontend/src/features/auditoria/task-context-panel.tsx` e `frontend/src/features/auditoria/audit-timeline.tsx`

**Checkpoint**: At this point, User Stories 1 AND 2 should both work independently

---

## Phase 5: User Story 3 - Consumir listas e blocos de trabalho em linhas padronizadas (Priority: P3)

**Goal**: Reorganizar o conteúdo existente em seções e linhas horizontais padronizadas sem perder estados, ações ou regras de permissão

**Independent Test**: Validar que lista, etapas operacionais, blocos analíticos e linha do tempo auditável foram reorganizados em seções coerentes com alinhamento consistente e ações preservadas

### Tests for User Story 3

- [X] T029 [P] [US3] Atualizar os testes da lista de cessões para a nova estrutura em `frontend/src/features/cessao/cessao-list.test.tsx`
- [X] T030 [P] [US3] Atualizar os testes do detalhe da cessão para a nova estrutura em `frontend/src/features/cessao/cessao-detail.test.tsx` e `frontend/src/app/cessoes/[businessKey]/page.test.tsx`
- [X] T031 [P] [US3] Criar teste de integração da reorganização visual de análise e auditoria em `frontend/tests/integration/content-shell-sections.spec.tsx`

### Implementation for User Story 3

- [X] T032 [US3] Reorganizar a lista de cessões em seções e linhas horizontais usando o componente compartilhado em `frontend/src/features/cessao/cessao-list.tsx`
- [X] T033 [US3] Reorganizar o detalhe da cessão, status e etapas operacionais em seções e linhas consistentes em `frontend/src/features/cessao/cessao-detail.tsx` e `frontend/src/features/cessao/cessao-status-panel.tsx`
- [X] T034 [US3] Reorganizar o dashboard de análise em seções horizontais consistentes em `frontend/src/features/analise/analise-dashboard.tsx`, `frontend/src/features/analise/elegibilidade-panel.tsx`, `frontend/src/features/analise/calculo-panel.tsx`, `frontend/src/features/analise/contratos-panel.tsx`, `frontend/src/features/analise/lastro-panel.tsx` e `frontend/src/features/analise/registradora-panel.tsx`
- [X] T035 [US3] Reorganizar a auditoria em seções e linha do tempo compatível com as linhas padronizadas em `frontend/src/features/auditoria/audit-timeline.tsx` e `frontend/src/features/auditoria/task-context-panel.tsx`
- [X] T036 [US3] Garantir preservação de estados vazio, erro, loading, forbidden e feedback de sucesso no novo formato em `frontend/src/components/feedback/empty-state.tsx`, `frontend/src/components/feedback/forbidden-state.tsx`, `frontend/src/app/cessoes/loading.tsx`, `frontend/src/app/cessoes/[businessKey]/loading.tsx` e `frontend/src/app/cessoes/[businessKey]/error.tsx`

**Checkpoint**: All user stories should now be independently functional

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Consolidar qualidade, governança e validação final do corte único

- [X] T037 [P] Executar a revisão final de acessibilidade, foco visível e navegação por teclado em `frontend/src/components/layout/app-shell.tsx`, `frontend/src/components/layout/secondary-nav.tsx` e `frontend/src/components/ui/topbar-user-menu.tsx`
- [X] T038 [P] Validar alinhamento com tokens, semântica visual e hierarquia de informação em `frontend/src/app/globals.css`, `frontend/src/design-system/theme.css` e `frontend/src/design-system/tokens.ts`
- [X] T039 Validar e fechar o registro de exceções de novos componentes em `specs/007-migrate-frontend-shell/contracts/ui-shell-migration-contract.md` e `specs/007-migrate-frontend-shell/plan.md`
- [X] T040 Executar a validação técnica de observabilidade, falhas de shell, navegação contextual e rastreabilidade de ações de conta em `specs/007-migrate-frontend-shell/quickstart.md`
- [X] T041 Executar o checklist de prontidão do cutover único em `specs/007-migrate-frontend-shell/quickstart.md`
- [X] T041a Registrar o resultado da validação dos cenários primários de navegação e confirmar atendimento do critério de 90% em `specs/007-migrate-frontend-shell/quickstart.md`
- [X] T042 Executar o roteiro de validação final e atualizar evidências em `specs/007-migrate-frontend-shell/quickstart.md`
- [X] T043 Rodar lint e testes do frontend via `frontend/package.json` e corrigir regressões encontradas em `frontend/tests/integration/`, `frontend/src/components/` e `frontend/src/features/`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Story 1 (Phase 3)**: Depends on Foundational completion
- **User Story 2 (Phase 4)**: Depends on Foundational completion and benefits from User Story 1 shell structure
- **User Story 3 (Phase 5)**: Depends on Foundational completion and on the shell/context patterns established in US1 and US2
- **Polish (Phase 6)**: Depends on all desired user stories being complete

### User Story Dependencies

- **User Story 1 (P1)**: Can start after Foundational (Phase 2) - establishes the MVP shell
- **User Story 2 (P2)**: Starts after Foundational and should be integrated on top of the shell delivered in US1
- **User Story 3 (P3)**: Starts after Foundational and should leverage shell and contextual navigation already delivered by US1 and US2

### Within Each User Story

- Tests for the story should be written or updated before implementation and should fail before the implementation is completed
- Shared config before route integration
- Route composition before final state handling
- Implementation before quickstart validation

### Parallel Opportunities

- **Setup**: `T003` can run in parallel with `T001` and `T002`
- **Foundational**: `T007`, `T008`, `T009`, `T012` and `T013` can run in parallel after `T004`, `T005` and `T006`
- **US1**: `T014`, `T015`, `T016` can run in parallel; `T020` can proceed in parallel with `T019` after `T018`
- **US2**: `T022` and `T023` can run in parallel; route tasks `T025` to `T028` can be split by route once `T024` is complete
- **US3**: `T029`, `T030`, `T031` can run in parallel; content migration tasks `T032` to `T035` can be split by feature area after shared row patterns are stable
- **Polish**: `T037` and `T038` can run in parallel before `T040` to `T043`

---

## Parallel Example: User Story 2

```bash
# Launch contextual navigation validation together:
Task: "Create test of contextual secondary navigation in frontend/tests/integration/secondary-nav-context.spec.tsx"
Task: "Create route context config test in frontend/src/features/navigation/shell-config.test.ts"

# After shell-config is stable, split route integrations:
Task: "Migrate list context controls in frontend/src/app/cessoes/page.tsx and frontend/src/features/cessao/cessao-list.tsx"
Task: "Migrate detail context controls in frontend/src/app/cessoes/[businessKey]/page.tsx and frontend/src/features/cessao/cessao-detail.tsx"
Task: "Migrate analysis context controls in frontend/src/app/cessoes/[businessKey]/analise/page.tsx and frontend/src/features/analise/analise-dashboard.tsx"
Task: "Migrate audit context controls in frontend/src/app/cessoes/[businessKey]/auditoria/page.tsx and frontend/src/features/auditoria/task-context-panel.tsx"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational
3. Complete Phase 3: User Story 1
4. Validate login simplificado, header global, menu do usuário e navegação principal
5. Demo or cut internal preview if stable

### Incremental Delivery

1. Setup + Foundational create the shared shell infrastructure
2. Deliver User Story 1 as MVP shell
3. Add User Story 2 to centralize contextual navigation and route-level actions
4. Add User Story 3 to reorganize lists and content sections
5. Finish with cross-cutting validation, accessibility and cutover readiness

### Parallel Team Strategy

With multiple developers:

1. One developer finalizes shared shell primitives and layout integration
2. One developer prepares tests and login/authenticated shell validation
3. After foundation:
   - Developer A: list/detail contextual navigation
   - Developer B: analysis/audit contextual navigation
   - Developer C: content-section migration by feature area

---

## Notes

- [P] tasks = different files, no dependencies on incomplete tasks
- [Story] label maps task to specific user story for traceability
- Each user story is independently testable against the criteria recorded in `spec.md`
- The feature is a cutover, so final validation must confirm no route in scope still depends on the old shell
- Component reuse review is mandatory before any new UI abstraction is merged
