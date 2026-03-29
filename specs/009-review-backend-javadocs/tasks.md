# Tasks: Revisao de Javadoc do Backend

**Input**: Design documents from `/specs/009-review-backend-javadocs/`
**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/

**Tests**: A feature exige validacao executavel do gate documental; incluir tarefas de verificacao do fluxo Maven/Javadoc/DocLint faz parte do escopo.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Path Conventions

- **Web app**: `backend/src/`, `frontend/src/`
- Documentation and planning artifacts for this feature live in `specs/009-review-backend-javadocs/`

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Preparar os artefatos da feature e o contexto minimo para a revisao normativa.

- [X] T001 Consolidate the feature inventory baseline in `specs/009-review-backend-javadocs/research.md`
- [X] T002 [P] Establish the canonical evidence model and acceptance workflow in `specs/009-review-backend-javadocs/research.md` and `specs/009-review-backend-javadocs/contracts/javadoc-compliance.md`
- [X] T003 [P] Establish the initial execution flow for inventory and validation in `specs/009-review-backend-javadocs/quickstart.md`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Definir a infraestrutura bloqueante para inventario, geracao e validacao documental antes da remediacao por historia.

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [X] T004 Add Maven Javadoc generation and DocLint blocking configuration in `backend/pom.xml`
- [X] T005 [P] Create the initial inventory artifact and package coverage tracker in `specs/009-review-backend-javadocs/data-model.md`
- [X] T006 [P] Define the expected validation commands, HTML output path, and evidence references in `specs/009-review-backend-javadocs/quickstart.md`
- [X] T007 Define the initial package-level documentation strategy and package contract rules for `backend/src/main/java/com/fidc/cdc/kogito/**/package-info.java` in `specs/009-review-backend-javadocs/contracts/javadoc-compliance.md`
- [X] T008 Verify the foundational documentation gate command path in `backend/pom.xml` and `specs/009-review-backend-javadocs/quickstart.md`

**Checkpoint**: Foundation ready - user story implementation can now begin in parallel

---

## Phase 3: User Story 1 - Mapear e fechar lacunas de documentacao (Priority: P1) 🎯 MVP

**Goal**: Produzir inventario completo e rastreavel das classes e pacotes em escopo, com lacunas documentais identificadas.

**Independent Test**: Executar o inventario do backend e validar que 100% das classes de `backend/src/main/java` e 100% dos pacotes em escopo aparecem com status de conformidade documentado.

### Tests for User Story 1 ⚠️

> **NOTE: Write these tests FIRST, ensure they FAIL before implementation**

- [X] T009 [P] [US1] Add an inventory verification checklist for class and package coverage in `specs/009-review-backend-javadocs/contracts/javadoc-compliance.md`
- [X] T010 [P] [US1] Add a reproducible inventory validation procedure and evidence handoff in `specs/009-review-backend-javadocs/quickstart.md`

### Implementation for User Story 1

- [X] T011 [P] [US1] Inventory root production classes and package gaps in `backend/src/main/java/com/fidc/cdc/kogito/FidcCdcKogitoApplication.java` and `specs/009-review-backend-javadocs/data-model.md`
- [X] T012 [P] [US1] Inventory API package classes in `backend/src/main/java/com/fidc/cdc/kogito/api/**/*.java` and register findings in `specs/009-review-backend-javadocs/data-model.md`
- [X] T013 [P] [US1] Inventory application package classes in `backend/src/main/java/com/fidc/cdc/kogito/application/**/*.java` and register findings in `specs/009-review-backend-javadocs/data-model.md`
- [X] T014 [P] [US1] Inventory domain package classes in `backend/src/main/java/com/fidc/cdc/kogito/domain/**/*.java` and register findings in `specs/009-review-backend-javadocs/data-model.md`
- [X] T015 [P] [US1] Inventory infrastructure, observability, process, and security package classes in `backend/src/main/java/com/fidc/cdc/kogito/{infrastructure,observability,process,security}/**/*.java` and register findings in `specs/009-review-backend-javadocs/data-model.md`
- [X] T016 [US1] Create or update package documentation stubs for root and first-level packages in `backend/src/main/java/com/fidc/cdc/kogito/**/package-info.java`
- [X] T017 [US1] Consolidate the User Story 1 inventory snapshot, package coverage, and backlog ordering in `specs/009-review-backend-javadocs/research.md`
- [X] T018 [US1] Validate the inventory against `backend/src/main/java` and append the User Story 1 canonical evidence reference in `specs/009-review-backend-javadocs/research.md`

**Checkpoint**: At this point, User Story 1 should be fully functional and testable independently

---

## Phase 4: User Story 2 - Documentar classes Java como contrato de API (Priority: P2)

**Goal**: Remediar a documentacao das classes e pacotes em escopo ate cobertura integral aderente ao contrato de API.

**Independent Test**: Inspecionar classes e `package-info.java` em todos os pacotes do backend e confirmar que o contrato documental cobre resumo, comportamento observavel, tags relevantes e contexto arquitetural.

### Tests for User Story 2 ⚠️

- [X] T019 [P] [US2] Add review criteria for class-level and package-level Javadoc quality, including platform-specific and implementation-specific notes when applicable, in `specs/009-review-backend-javadocs/contracts/javadoc-compliance.md`
- [X] T020 [P] [US2] Add a documentation review matrix for compliant versus partial contracts in `specs/009-review-backend-javadocs/data-model.md`

### Implementation for User Story 2

- [X] T021 [P] [US2] Add or update Javadocs for root, security, process, and observability classes in `backend/src/main/java/com/fidc/cdc/kogito/{FidcCdcKogitoApplication.java,security/*.java,process/*.java,observability/*.java}`
- [X] T022 [P] [US2] Add or update Javadocs for API layer classes in `backend/src/main/java/com/fidc/cdc/kogito/api/**/*.java`
- [X] T023 [P] [US2] Add or update Javadocs for application layer classes in `backend/src/main/java/com/fidc/cdc/kogito/application/**/*.java`
- [X] T024 [P] [US2] Add or update Javadocs for domain layer classes in `backend/src/main/java/com/fidc/cdc/kogito/domain/**/*.java`
- [X] T025 [P] [US2] Add or update Javadocs for infrastructure layer classes in `backend/src/main/java/com/fidc/cdc/kogito/infrastructure/**/*.java`
- [X] T026 [US2] Create or revise package contracts in `backend/src/main/java/com/fidc/cdc/kogito/**/package-info.java`
- [X] T027 [US2] Reconcile inventory statuses and mark all remediated classes/packages as compliant in `specs/009-review-backend-javadocs/data-model.md`
- [X] T028 [US2] Update the User Story 2 canonical compliance evidence after remediation in `specs/009-review-backend-javadocs/research.md`
- [X] T029 [US2] Review remediated documentation against `.specify/memory/constitution.md` and the Oracle Javadoc guidance, then append the User Story 2 review outcome in `specs/009-review-backend-javadocs/research.md`

**Checkpoint**: At this point, User Stories 1 AND 2 should both work independently

---

## Phase 5: User Story 3 - Validar a documentacao antes da entrega (Priority: P3)

**Goal**: Tornar o gate documental executavel, bloqueante e auditavel antes do merge.

**Independent Test**: Executar o fluxo de geracao e validacao da documentacao do backend e confirmar resultado `pass` sem erros ou avisos relevantes, com evidencia unica localizavel.

### Tests for User Story 3 ⚠️

- [X] T030 [P] [US3] Finalize execution steps and expected outcomes for the Javadoc gate in `specs/009-review-backend-javadocs/quickstart.md`
- [X] T031 [P] [US3] Finalize pass/fail validation criteria and evidence rules in `specs/009-review-backend-javadocs/contracts/javadoc-compliance.md`

### Implementation for User Story 3

- [X] T032 [US3] Finalize the Maven gate configuration for Javadoc generation, DocLint, and reviewable HTML output in `backend/pom.xml`
- [X] T033 [US3] Execute the backend documentation generation and validation flow and capture the User Story 3 command results and output paths in `specs/009-review-backend-javadocs/research.md`
- [X] T034 [US3] Update the final compliance snapshot for classes and packages in `specs/009-review-backend-javadocs/data-model.md`

**Checkpoint**: All user stories should now be independently functional

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Consolidar evidencias finais, limpar lacunas residuais e garantir que a entrega fique pronta para revisao.

- [X] T035 Review final terminology consistency across `backend/src/main/java/com/fidc/cdc/kogito/**/package-info.java` and `backend/src/main/java/com/fidc/cdc/kogito/**/*.java`
- [X] T036 [P] Run the full quickstart validation and align any drift in `specs/009-review-backend-javadocs/quickstart.md`
- [X] T037 [P] Finalize the evidence package for reviewers in `specs/009-review-backend-javadocs/research.md` and `specs/009-review-backend-javadocs/contracts/javadoc-compliance.md`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Stories (Phase 3+)**: All depend on Foundational phase completion
  - User stories can then proceed in parallel where file ownership does not overlap
  - Or sequentially in priority order (P1 → P2 → P3)
- **Polish (Phase 6)**: Depends on all desired user stories being complete

### User Story Dependencies

- **User Story 1 (P1)**: Can start after Foundational (Phase 2) - No dependencies on other stories
- **User Story 2 (P2)**: Depends on User Story 1 inventory and package backlog to ensure full coverage without omissions
- **User Story 3 (P3)**: Depends on User Story 2 remediation because the gate must validate the completed documentation set

### Within Each User Story

- Validation tasks before broad implementation whenever they define acceptance evidence
- Inventory before remediation
- Package contracts before final compliance reconciliation
- Maven gate finalization before final evidence capture
- Story complete before moving to the next dependent priority

### Parallel Opportunities

- `T002` and `T003` can run in parallel
- `T005`, `T006`, and `T007` can run in parallel after `T004`
- `T011` to `T015` can run in parallel because they split inventory by package groups
- `T021` to `T025` can run in parallel if different contributors own disjoint package trees
- `T030` and `T031` can run in parallel before `T032`
- `T036` and `T037` can run in parallel in the polish phase

---

## Parallel Example: User Story 1

```bash
# Launch package inventory tasks together:
Task: "Inventory API package classes in backend/src/main/java/com/fidc/cdc/kogito/api/**/*.java and register findings in specs/009-review-backend-javadocs/data-model.md"
Task: "Inventory application package classes in backend/src/main/java/com/fidc/cdc/kogito/application/**/*.java and register findings in specs/009-review-backend-javadocs/data-model.md"
Task: "Inventory domain package classes in backend/src/main/java/com/fidc/cdc/kogito/domain/**/*.java and register findings in specs/009-review-backend-javadocs/data-model.md"
Task: "Inventory infrastructure, observability, process, and security package classes in backend/src/main/java/com/fidc/cdc/kogito/{infrastructure,observability,process,security}/**/*.java and register findings in specs/009-review-backend-javadocs/data-model.md"
```

## Parallel Example: User Story 2

```bash
# Launch documentation remediation by layer:
Task: "Add or update Javadocs for API layer classes in backend/src/main/java/com/fidc/cdc/kogito/api/**/*.java"
Task: "Add or update Javadocs for application layer classes in backend/src/main/java/com/fidc/cdc/kogito/application/**/*.java"
Task: "Add or update Javadocs for domain layer classes in backend/src/main/java/com/fidc/cdc/kogito/domain/**/*.java"
Task: "Add or update Javadocs for infrastructure layer classes in backend/src/main/java/com/fidc/cdc/kogito/infrastructure/**/*.java"
```

## Parallel Example: User Story 3

```bash
# Prepare validation documentation in parallel before the final Maven gate run:
Task: "Finalize execution steps and expected outcomes for the Javadoc gate in specs/009-review-backend-javadocs/quickstart.md"
Task: "Finalize pass/fail validation criteria and evidence rules in specs/009-review-backend-javadocs/contracts/javadoc-compliance.md"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational
3. Complete Phase 3: User Story 1
4. **STOP and VALIDATE**: confirm the inventory covers 100% of production classes and packages
5. Review the remediation backlog before broad documentation updates

### Incremental Delivery

1. Complete Setup + Foundational → documentation gate baseline ready
2. Add User Story 1 → validate inventory completeness
3. Add User Story 2 → validate documentation remediation coverage
4. Add User Story 3 → validate executable Maven/Javadoc/DocLint gate
5. Finish with Polish → publish final evidence package for review

### Parallel Team Strategy

With multiple developers:

1. Team completes Setup + Foundational together
2. Once Foundational is done:
   - Developer A: API and root/security/process documentation
   - Developer B: application and domain documentation
   - Developer C: infrastructure/package-info coverage plus validation artifacts
3. Rejoin for final Maven gate execution and evidence consolidation

---

## Notes

- [P] tasks = different files or disjoint package trees, no dependencies
- [Story] label maps each task to the relevant user story
- Every task includes an exact file path
- Inventory and validation evidence are part of the deliverable, not optional documentation
- Avoid mixing package groups in parallel if two contributors would touch the same `package-info.java`
- Avoid parallel edits to `specs/009-review-backend-javadocs/research.md`; consolidate updates by phase
