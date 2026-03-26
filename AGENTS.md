# fidc-cdc-kogito Development Guidelines

Auto-generated from all feature plans. Last updated: 2026-03-25

## Active Technologies
- [e.g., Python 3.11, Swift 5.9, Rust 1.75 or NEEDS CLARIFICATION] + [e.g., FastAPI, UIKit, LLVM or NEEDS CLARIFICATION] (main)
- [if applicable, e.g., PostgreSQL, CoreData, files or N/A] (main)
- Java 21 no backend e TypeScript 5 / React 19 / Next.js 15 no frontend + Spring Boot 3.3, Spring Security, Spring HATEOAS, JPA/Hibernate, Bean Validation, Flyway, Log4j2/SLF4J, Kogito 10.1, React, Next.js, shadcn/ui, Tailwind CSS, Vitest, Testing Library (002-avaliar-boas-praticas)
- PostgreSQL para persistencia operacional; arquivos Markdown em `specs/` para artefatos de avaliacao (002-avaliar-boas-praticas)
- Java 21 no backend e TypeScript 5 / React 19 / Next.js 15 no frontend + Spring Boot 3.3, Spring Security, Spring HATEOAS, JPA/Hibernate, Bean Validation, Flyway, Log4j2/SLF4J, React, Next.js, shadcn/ui, Tailwind CSS, Vitest, Testing Library (003-basic-auth-menu)
- PostgreSQL para dados de usuarios e perfis seedados; estado de sessao e configuracao da interface no frontend; arquivos Markdown em `specs/003-basic-auth-menu/` para artefatos da feature (003-basic-auth-menu)
- PostgreSQL para dados de conta; cookie HTTP-only para sessao autenticada; `localStorage` e atributo visual do documento para persistencia de tema; arquivos Markdown em `specs/004-fix-user-modal-theme/` para artefatos da feature (004-fix-user-modal-theme)
- Java 21 no backend; TypeScript 5 / React 19 / Next.js 15 no frontend + Spring Boot 3.3, Spring Security, React, Next.js, Tailwind CSS, Vitest, Testing Library (004-fix-user-modal-theme)
- PostgreSQL para dados de conta; `localStorage` como fonte primaria da preferencia visual; cookie HTTP-only para sessao autenticada; cookie de tema e atributo `data-theme` do documento sincronizados a partir de `localStorage` (004-fix-user-modal-theme)
- Java 21 no backend; TypeScript 5 / React 19 / Next.js 15 no frontend + React 19, Next.js 15, Tailwind CSS 3, `shadcn/ui`, Radix UI primitives requeridas pelos componentes gerados, `clsx`, `lucide-react`, Vitest, Testing Library (005-global-shadcn-migration)
- N/A para a migracao em si; frontend segue consumindo estado local, cookies e dados ja existentes (005-global-shadcn-migration)
- Java 21 no backend; TypeScript 5 / React 19 / Next.js 15 no frontend + React 19, Next.js 15 App Router, Tailwind CSS 3, `shadcn/ui` e componentes locais em `frontend/src/components/ui`, Radix primitives já instaladas, `clsx`, `class-variance-authority`, `tailwind-merge`, `lucide-react`, Vitest, Testing Library (007-migrate-frontend-shell)
- N/A para a migração em si; frontend continua consumindo dados do backend, sessão autenticada via cookie HTTP-only, preferência visual via `localStorage` e cookie de tema já existentes (007-migrate-frontend-shell)
- Java 21 no backend + Spring Boot 3.3, Spring Security, Spring HATEOAS, JPA/Hibernate, Flyway, Log4j2/SLF4J, Kogito 10.1.0, `kie-addons-springboot-process-management`, `jbpm-addons-springboot-task-management`, `kogito-addons-springboot-jobs-management`, `kie-addons-springboot-monitoring-prometheus`, addon oficial `org.kie:kie-addons-springboot-process-svg` (008-management-process-svg)
- PostgreSQL para estado operacional; Data Index para consulta do runtime; BPMN em `backend/src/main/resources/processes`; SVG do processo em `META-INF/processSVG/{processId}.svg` no classpath, com `kogito.svg.folder.path` reservado apenas para excecao operacional (008-management-process-svg)

- Java 21 no backend; TypeScript LTS no frontend + Spring Boot LTS, Spring Security, Spring HATEOAS, JPA/Hibernate, Bean Validation, Flyway, Swagger/OpenAPI, Log4j2/SLF4J, Kogito, React, Next.js, shadcn/ui, Tailwind CSS (001-controle-cessao-fidc)

## Project Structure

```text
backend/
frontend/
tests/
```

## Commands

npm test; npm run lint

## Code Style

Java 21 no backend; TypeScript LTS no frontend: Follow standard conventions

## Recent Changes
- 008-management-process-svg: Added Java 21 no backend + Spring Boot 3.3, Spring Security, Spring HATEOAS, JPA/Hibernate, Flyway, Log4j2/SLF4J, Kogito 10.1.0, `kie-addons-springboot-process-management`, `jbpm-addons-springboot-task-management`, `kogito-addons-springboot-jobs-management`, `kie-addons-springboot-monitoring-prometheus`, addon oficial `org.kie:kie-addons-springboot-process-svg`
- 007-migrate-frontend-shell: Added Java 21 no backend; TypeScript 5 / React 19 / Next.js 15 no frontend + React 19, Next.js 15 App Router, Tailwind CSS 3, `shadcn/ui` e componentes locais em `frontend/src/components/ui`, Radix primitives já instaladas, `clsx`, `class-variance-authority`, `tailwind-merge`, `lucide-react`, Vitest, Testing Library
- 005-global-shadcn-migration: Added Java 21 no backend; TypeScript 5 / React 19 / Next.js 15 no frontend + React 19, Next.js 15, Tailwind CSS 3, `shadcn/ui`, Radix UI primitives requeridas pelos componentes gerados, `clsx`, `lucide-react`, Vitest, Testing Library


<!-- MANUAL ADDITIONS START -->
<!-- MANUAL ADDITIONS END -->
