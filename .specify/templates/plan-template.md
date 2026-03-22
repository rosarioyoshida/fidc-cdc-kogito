# Implementation Plan: [FEATURE]

**Branch**: `[###-feature-name]` | **Date**: [DATE] | **Spec**: [link]
**Input**: Feature specification from `/specs/[###-feature-name]/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. See `.specify/templates/plan-template.md` for the execution workflow.

## Summary

[Extract from feature spec: primary requirement + technical approach from research]

## Technical Context

<!--
  ACTION REQUIRED: Replace the content in this section with the technical details
  for the project. The structure here is presented in advisory capacity to guide
  the iteration process.
-->

**Language/Version**: [e.g., Python 3.11, Swift 5.9, Rust 1.75 or NEEDS CLARIFICATION]  
**Primary Dependencies**: [e.g., FastAPI, UIKit, LLVM or NEEDS CLARIFICATION]  
**Storage**: [if applicable, e.g., PostgreSQL, CoreData, files or N/A]  
**Testing**: [e.g., pytest, XCTest, cargo test or NEEDS CLARIFICATION]  
**Target Platform**: [e.g., Linux server, iOS 15+, WASM or NEEDS CLARIFICATION]
**Project Type**: [e.g., library/cli/web-service/mobile-app/compiler/desktop-app or NEEDS CLARIFICATION]  
**Performance Goals**: [domain-specific, e.g., 1000 req/s, 10k lines/sec, 60 fps or NEEDS CLARIFICATION]  
**Constraints**: [domain-specific, e.g., <200ms p95, <100MB memory, offline-capable or NEEDS CLARIFICATION]  
**Scale/Scope**: [domain-specific, e.g., 10k users, 1M LOC, 50 screens or NEEDS CLARIFICATION]

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- Simplicity gate: solution keeps KISS/YAGNI and justifies any added abstraction,
  indirection, or configuration surface.
- Architecture gate: design documents identify module boundaries, reuse strategy, and
  where SOLID/DRY materially apply without premature generalization.
- API design gate: when the feature exposes REST APIs, resource naming follows noun
  based URIs, consistent pluralization, lowercase hyphenated paths, proper hierarchy,
  HTTP verbs for actions, and query parameters for filtering/sorting/pagination.
- API versioning gate: when the feature exposes REST APIs, the plan defines the
  versioning strategy, breaking-change criteria, major-version policy, and
  deprecation/compatibility approach.
- Hypermedia gate: when the feature benefits from dynamic API navigation or workflow
  discovery, the plan defines whether HATEOAS is required, which link format is used,
  and how relations and next actions are exposed.
- API error contract gate: when the feature exposes REST APIs, the plan defines the
  RFC 9457 problem details strategy, problem type URIs, extension members, and
  safeguards against leaking implementation internals.
- Maintainability/scalability gate: ownership, change impact boundaries, expected
  growth, bottlenecks, and scaling strategy are explicit for critical components.
- Security/compliance gate: threat surface, sensitive data handling, access control,
  audit trail needs, and regulatory constraints are explicit.
- Observability gate: logs, metrics, tracing/correlation, health signals, and failure
  diagnostics are defined for critical flows.
- UX/performance gate: user-facing workflows define usability expectations,
  accessibility constraints, hierarchy of information, user control, semantic color
  rules, Design System constraints, token/component reuse, and measurable performance
  targets.
- Frontend implementation gate: when the feature has web UI, the plan identifies
  React, Next.js, and TypeScript usage, shadcn/ui as the structural base, Atlassian
  Design System as the visual/behavioral source of truth, centralized tokens, and
  required component states/variants.
- Component governance gate: when new UI components are proposed, the plan must verify
  reuse of available shadcn/ui and repository-local components in
  `frontend/src/components/ui` before creating new components, and document any
  exception with owner, state coverage, maintenance impact, test plan, approval
  path, and PR rationale.

## Project Structure

### Documentation (this feature)

```text
specs/[###-feature]/
├── plan.md              # This file (/speckit.plan command output)
├── research.md          # Phase 0 output (/speckit.plan command)
├── data-model.md        # Phase 1 output (/speckit.plan command)
├── quickstart.md        # Phase 1 output (/speckit.plan command)
├── contracts/           # Phase 1 output (/speckit.plan command)
└── tasks.md             # Phase 2 output (/speckit.tasks command - NOT created by /speckit.plan)
```

### Source Code (repository root)
<!--
  ACTION REQUIRED: Replace the placeholder tree below with the concrete layout
  for this feature. Delete unused options and expand the chosen structure with
  real paths (e.g., apps/admin, packages/something). The delivered plan must
  not include Option labels.
-->

```text
# [REMOVE IF UNUSED] Option 1: Single project (DEFAULT)
src/
├── models/
├── services/
├── cli/
└── lib/

tests/
├── contract/
├── integration/
└── unit/

# [REMOVE IF UNUSED] Option 2: Web application (when "frontend" + "backend" detected)
backend/
├── src/
│   ├── models/
│   ├── services/
│   └── api/
└── tests/

frontend/
├── src/
│   ├── components/
│   ├── pages/
│   └── services/
└── tests/

# [REMOVE IF UNUSED] Option 3: Mobile + API (when "iOS/Android" detected)
api/
└── [same as backend above]

ios/ or android/
└── [platform-specific structure: feature modules, UI flows, platform tests]
```

**Structure Decision**: [Document the selected structure and reference the real
directories captured above]

## Complexity Tracking

> **Fill ONLY if Constitution Check has violations that must be justified**

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| [e.g., 4th project] | [current need] | [why 3 projects insufficient] |
| [e.g., Repository pattern] | [specific problem] | [why direct DB access insufficient] |
