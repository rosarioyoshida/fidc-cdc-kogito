# Tasks: Revisao de Javadoc UTF-8 do Backend

**Input**: Design documents from `/specs/010-javadoc-utf8-review/`
**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/

**Tests**: A feature exige validacao executavel do gate documental e de encoding; incluir tarefas de verificacao do fluxo Maven/Javadoc/DocLint e da preservacao de UTF-8 faz parte do escopo.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this belongs to (e.g., [US1], [US2], [US3])
- Include exact file paths in descriptions

## Path Conventions

- **Web app**: `backend/src/`, `frontend/src/`
- Documentation and planning artifacts for this feature live in `specs/010-javadoc-utf8-review/`

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Preparar os artefatos da feature e o contexto minimo para a revisao normativa com UTF-8.

- [X] T001 Consolidate the feature inventory baseline and UTF-8 scope in `specs/010-javadoc-utf8-review/research.md`
- [X] T002 [P] Establish the canonical evidence model and acceptance workflow in `specs/010-javadoc-utf8-review/research.md` and `specs/010-javadoc-utf8-review/contracts/javadoc-utf8-compliance.md`
- [X] T003 [P] Establish the initial execution flow for inventory, encoding review, and validation in `specs/010-javadoc-utf8-review/quickstart.md`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Definir a infraestrutura bloqueante para inventario, geracao e validacao documental antes da remediacao por historia.

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [X] T004 Add Maven Javadoc generation, UTF-8 encoding, and DocLint blocking configuration in `backend/pom.xml`
- [X] T005 [P] Audit and normalize UTF-8 encoding for scoped Java sources and `package-info.java` files in `backend/src/main/java/com/fidc/cdc/kogito/**/*.java`
- [X] T006 [P] Create the initial inventory artifact, package coverage tracker, and encoding fields in `specs/010-javadoc-utf8-review/data-model.md`
- [X] T007 [P] Define the expected validation commands, HTML output path, encoding review steps, and evidence references in `specs/010-javadoc-utf8-review/quickstart.md`
- [X] T008 Define the initial package-level documentation strategy, UTF-8 rules, and package contract requirements for `backend/src/main/java/com/fidc/cdc/kogito/**/package-info.java` in `specs/010-javadoc-utf8-review/contracts/javadoc-utf8-compliance.md`
- [X] T009 Verify the foundational documentation gate command path, output path, and UTF-8 settings in `backend/pom.xml` and `specs/010-javadoc-utf8-review/quickstart.md`

**Checkpoint**: Foundation ready - user story implementation can now begin in parallel

---

## Phase 3: User Story 1 - Mapear cobertura e codificacao documental (Priority: P1) 🎯 MVP

**Goal**: Produzir inventario completo e rastreavel das classes e pacotes em escopo, com lacunas documentais e de encoding identificadas.

**Independent Test**: Executar o inventario do backend e validar que 100% das classes de `backend/src/main/java` e 100% dos pacotes em escopo aparecem com status de conformidade e status de UTF-8 documentados.

### Tests for User Story 1 ⚠️

> **NOTE: Write these tests FIRST, ensure they FAIL before implementation**

- [X] T010 [P] [US1] Add an inventory verification checklist for class, package, and UTF-8 coverage in `specs/010-javadoc-utf8-review/contracts/javadoc-utf8-compliance.md`
- [X] T011 [P] [US1] Add a reproducible inventory and encoding validation procedure in `specs/010-javadoc-utf8-review/quickstart.md`

### Implementation for User Story 1

- [X] T012 [P] [US1] Inventory root production classes and root package encoding status in `backend/src/main/java/com/fidc/cdc/kogito/FidcCdcKogitoApplication.java`, `backend/src/main/java/com/fidc/cdc/kogito/package-info.java`, and `specs/010-javadoc-utf8-review/data-model.md`
- [X] T013 [P] [US1] Inventory API package classes and package encoding status in `backend/src/main/java/com/fidc/cdc/kogito/api/**/*.java` and `specs/010-javadoc-utf8-review/data-model.md`
- [X] T014 [P] [US1] Inventory application package classes and package encoding status in `backend/src/main/java/com/fidc/cdc/kogito/application/**/*.java` and `specs/010-javadoc-utf8-review/data-model.md`
- [X] T015 [P] [US1] Inventory domain package classes and package encoding status in `backend/src/main/java/com/fidc/cdc/kogito/domain/**/*.java` and `specs/010-javadoc-utf8-review/data-model.md`
- [X] T016 [P] [US1] Inventory infrastructure, observability, process, and security package classes and package encoding status in `backend/src/main/java/com/fidc/cdc/kogito/{infrastructure,observability,process,security}/**/*.java` and `specs/010-javadoc-utf8-review/data-model.md`
- [X] T017 [US1] Create or update package documentation stubs for all scoped packages in `backend/src/main/java/com/fidc/cdc/kogito/**/package-info.java`
- [X] T018 [US1] Consolidate the User Story 1 section of the canonical evidence, covering inventory snapshot, package coverage, and encoding backlog ordering in `specs/010-javadoc-utf8-review/research.md`
- [X] T019 [US1] Validate the inventory against `backend/src/main/java` and append the User Story 1 evidence result in `specs/010-javadoc-utf8-review/research.md`

**Checkpoint**: At this point, User Story 1 should be fully functional and testable independently

---

## Phase 4: User Story 2 - Documentar classes Java como contrato de API em UTF-8 (Priority: P2)

**Goal**: Remediar a documentacao das classes e pacotes em escopo ate cobertura integral aderente ao contrato de API e preservada em UTF-8.

**Independent Test**: Inspecionar classes e `package-info.java` em todos os pacotes do backend e confirmar que o contrato documental cobre resumo, comportamento observavel, tags relevantes, contexto arquitetural e preservacao de caracteres em UTF-8.

### Tests for User Story 2 ⚠️

- [X] T020 [P] [US2] Add review criteria for class-level and package-level Javadoc quality, including UTF-8 readability checks, in `specs/010-javadoc-utf8-review/contracts/javadoc-utf8-compliance.md`
- [X] T021 [P] [US2] Add a documentation and encoding review matrix for compliant versus partial contracts in `specs/010-javadoc-utf8-review/data-model.md`

### Implementation for User Story 2

- [X] T022 [P] [US2] Add or update Javadocs in UTF-8 for root, security, process, and observability classes in `backend/src/main/java/com/fidc/cdc/kogito/{FidcCdcKogitoApplication.java,security/*.java,process/*.java,observability/*.java}`
- [X] T023 [P] [US2] Add or update Javadocs in UTF-8 for API layer classes in `backend/src/main/java/com/fidc/cdc/kogito/api/**/*.java`
- [X] T024 [P] [US2] Add or update Javadocs in UTF-8 for application layer classes in `backend/src/main/java/com/fidc/cdc/kogito/application/**/*.java`
- [X] T025 [P] [US2] Add or update Javadocs in UTF-8 for domain layer classes in `backend/src/main/java/com/fidc/cdc/kogito/domain/**/*.java`
- [X] T026 [P] [US2] Add or update Javadocs in UTF-8 for infrastructure layer classes in `backend/src/main/java/com/fidc/cdc/kogito/infrastructure/**/*.java`
- [X] T027 [US2] Create or revise package contracts in UTF-8 in `backend/src/main/java/com/fidc/cdc/kogito/**/package-info.java`
- [X] T028 [US2] Reconcile inventory statuses and mark all remediated classes/packages as compliant and `utf8` in `specs/010-javadoc-utf8-review/data-model.md`
- [X] T029 [US2] Update the User Story 2 section of the canonical compliance and encoding evidence after remediation in `specs/010-javadoc-utf8-review/research.md`
- [X] T030 [US2] Review remediated documentation against `.specify/memory/constitution.md` and the Oracle Javadoc guidance, then append the User Story 2 review outcome in `specs/010-javadoc-utf8-review/research.md`

**Checkpoint**: At this point, User Stories 1 AND 2 should both work independently

---

## Phase 5: User Story 3 - Validar geracao documental e DocLint com UTF-8 (Priority: P3)

**Goal**: Tornar o gate documental e de encoding executavel, bloqueante e auditavel antes do merge.

**Independent Test**: Executar o fluxo de geracao e validacao da documentacao do backend e confirmar resultado `pass`, sem erros ou avisos relevantes, com HTML revisavel e caracteres preservados em UTF-8.

### Tests for User Story 3 ⚠️

- [X] T031 [P] [US3] Finalize execution steps and expected outcomes for the Javadoc UTF-8 gate in `specs/010-javadoc-utf8-review/quickstart.md`
- [X] T032 [P] [US3] Finalize pass/fail validation criteria, UTF-8 evidence rules, and artifact expectations in `specs/010-javadoc-utf8-review/contracts/javadoc-utf8-compliance.md`

### Implementation for User Story 3

- [X] T033 [US3] Finalize the Maven gate configuration for Javadoc generation, UTF-8 output, DocLint, and reviewable HTML output in `backend/pom.xml`
- [X] T034 [US3] Execute the backend documentation generation and validation flow, including UTF-8 review, and capture the User Story 3 command results and output paths in the User Story 3 section of `specs/010-javadoc-utf8-review/research.md`
- [X] T035 [US3] Review generated HTML for UTF-8 legibility in `backend/target/site/apidocs/index.html` and record evidence in the User Story 3 section of `specs/010-javadoc-utf8-review/research.md`
- [X] T036 [US3] Update the final compliance and encoding snapshot for classes and packages in `specs/010-javadoc-utf8-review/data-model.md`

**Checkpoint**: All user stories should now be independently functional

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Consolidar evidencias finais, limpar lacunas residuais e garantir que a entrega fique pronta para revisao.

- [X] T037 Review final terminology and UTF-8 consistency across `backend/src/main/java/com/fidc/cdc/kogito/**/package-info.java` and `backend/src/main/java/com/fidc/cdc/kogito/**/*.java` as a final polish task
- [X] T038 [P] Run the full quickstart validation and align any drift in `specs/010-javadoc-utf8-review/quickstart.md`
- [X] T039 [P] Finalize the evidence package for reviewers in `specs/010-javadoc-utf8-review/research.md` and `specs/010-javadoc-utf8-review/contracts/javadoc-utf8-compliance.md`

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
- `T012` to `T016` can run in parallel because they split inventory by package groups
- `T022` to `T026` can run in parallel if different contributors own disjoint package trees
- `T031` and `T032` can run in parallel before `T033`
- `T038` and `T039` can run in parallel in the polish phase

---

## Parallel Example: User Story 1

```bash
# Launch package inventory tasks together:
Task: "Inventory API package classes and package encoding status in backend/src/main/java/com/fidc/cdc/kogito/api/**/*.java and specs/010-javadoc-utf8-review/data-model.md"
Task: "Inventory application package classes and package encoding status in backend/src/main/java/com/fidc/cdc/kogito/application/**/*.java and specs/010-javadoc-utf8-review/data-model.md"
Task: "Inventory domain package classes and package encoding status in backend/src/main/java/com/fidc/cdc/kogito/domain/**/*.java and specs/010-javadoc-utf8-review/data-model.md"
Task: "Inventory infrastructure, observability, process, and security package classes and package encoding status in backend/src/main/java/com/fidc/cdc/kogito/{infrastructure,observability,process,security}/**/*.java and specs/010-javadoc-utf8-review/data-model.md"
```

## Parallel Example: User Story 2

```bash
# Launch documentation remediation by layer:
Task: "Add or update Javadocs in UTF-8 for API layer classes in backend/src/main/java/com/fidc/cdc/kogito/api/**/*.java"
Task: "Add or update Javadocs in UTF-8 for application layer classes in backend/src/main/java/com/fidc/cdc/kogito/application/**/*.java"
Task: "Add or update Javadocs in UTF-8 for domain layer classes in backend/src/main/java/com/fidc/cdc/kogito/domain/**/*.java"
Task: "Add or update Javadocs in UTF-8 for infrastructure layer classes in backend/src/main/java/com/fidc/cdc/kogito/infrastructure/**/*.java"
```

## Parallel Example: User Story 3

```bash
# Prepare validation documentation in parallel before the final Maven gate run:
Task: "Finalize execution steps and expected outcomes for the Javadoc UTF-8 gate in specs/010-javadoc-utf8-review/quickstart.md"
Task: "Finalize pass/fail validation criteria, UTF-8 evidence rules, and artifact expectations in specs/010-javadoc-utf8-review/contracts/javadoc-utf8-compliance.md"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational
3. Complete Phase 3: User Story 1
4. **STOP and VALIDATE**: confirm the inventory covers 100% of production classes, packages, and encoding status
5. Review the remediation backlog before broad documentation updates

### Incremental Delivery

1. Complete Setup + Foundational → documentation and UTF-8 gate baseline ready
2. Add User Story 1 → validate inventory completeness
3. Add User Story 2 → validate documentation and encoding remediation coverage
4. Add User Story 3 → validate executable Maven/Javadoc/DocLint/UTF-8 gate
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
- Avoid parallel edits to `specs/010-javadoc-utf8-review/research.md`; consolidate updates by phase
- Consolidate `research.md` updates by phase: US1 in `T018-T019`, US2 in `T029-T030`, and US3/polish in `T034-T039`
