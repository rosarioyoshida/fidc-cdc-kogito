# Implementation Plan: Migracao Global de Componentes Equivalentes para shadcn/ui

**Branch**: `005-global-shadcn-migration` | **Date**: 2026-03-22 | **Spec**: [spec.md](D:/desenv/fidc-cdc-kogito/specs/005-global-shadcn-migration/spec.md)
**Input**: Feature specification from `/specs/005-global-shadcn-migration/spec.md`

## Summary

Migrar em todo o frontend os componentes locais que possuem equivalente oficial no
catalogo do `shadcn/ui`, substituindo a base estrutural atual por componentes
aderentes sem perder comunicacao visual, feedback, foco, acessibilidade, cores
semanticas e rastreabilidade tecnica da migracao.

## Technical Context

**Language/Version**: Java 21 no backend; TypeScript 5 / React 19 / Next.js 15 no frontend  
**Primary Dependencies**: React 19, Next.js 15, Tailwind CSS 3, `shadcn/ui`, Radix UI primitives requeridas pelos componentes gerados, `class-variance-authority`, `tailwind-merge`, `clsx`, `lucide-react`, Vitest, Testing Library  
**Storage**: N/A para a migracao em si; frontend segue consumindo estado local, cookies e dados ja existentes  
**Testing**: Vitest, Testing Library, validacao manual guiada dos fluxos prioritarios e verificacoes tecnicas de observabilidade/auditoria no frontend  
**Target Platform**: Aplicacao web interna em navegador desktop moderno e ambiente local via Docker Compose  
**Project Type**: Web application com `backend/` e `frontend/`  
**Performance Goals**: Componentes migrados devem manter interacao local sem degradacao perceptivel e preservar abertura, fechamento, foco, menu e feedback em ate 1 segundo nos fluxos priorizados em ambiente local  
**Constraints**: Todo componente local com equivalente oficial no catalogo `shadcn/ui` MUST migrar; a troca ocorre em todo o projeto; comportamentos de comunicacao com o usuario, feedback e cores semanticas MUST ser preservados; auditoria e observabilidade MUST ter validacao tecnica; componentes sem equivalente oficial ficam fora do escopo  
**Scale/Scope**: Migracao global dos componentes locais `button`, `input`, `dialog` e `table` e ajuste dos consumidores reais dessas primitives em `frontend/src/components/ui`, `frontend/src/features/security`, `frontend/src/features/cessao` e `frontend/src/features/analise`

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- Simplicity gate: aprovado. A migracao reusa a familia oficial do `shadcn/ui`
  em vez de expandir um catalogo proprietario paralelo.
- Architecture gate: aprovado. O plano separa primitivas aderentes, componentes
  compostos consumidores e validacoes tecnicas sem introduzir camada redundante.
- API design gate: nao aplicavel. Nao ha nova API REST.
- API versioning gate: nao aplicavel. Nao ha mudanca de superficie publica de API.
- Hypermedia gate: nao aplicavel. A feature nao introduz navegacao por API.
- API error contract gate: nao aplicavel. Nao ha mudanca de contratos REST.
- Maintainability/scalability gate: aprovado. A troca reduz divergencia estrutural e
  aproxima o projeto da base tecnica exigida pela constituicao.
- Security/compliance gate: aprovado. A feature nao amplia permissoes, mas exige
  verificar que feedback e interacoes nao exponham dados sensiveis.
- Observability gate: aprovado. O plano inclui validacao tecnica de sinais para
  sucesso, falha e regressao perceptivel em fluxos afetados.
- UX/performance gate: aprovado. A migracao preserva comunicacao visual, foco,
  teclado, hierarquia, contraste e semantica de cores.
- Frontend implementation gate: aprovado. O plano usa React, Next.js, TypeScript,
  `shadcn/ui` como base estrutural e mantem o Design System vigente como contrato
  visual e comportamental.
- Component governance gate: aprovado. O criterio de migracao e objetivo: qualquer
  componente local com equivalente no catalogo oficial do `shadcn/ui` entra no
  escopo e deve ser substituido sem excecao.

## Project Structure

### Documentation (this feature)

```text
specs/005-global-shadcn-migration/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── ui-migration-contract.md
└── tasks.md
```

### Source Code (repository root)

```text
backend/
└── src/
    ├── main/
    └── test/

frontend/
└── src/
    ├── app/
    │   ├── layout.tsx
    │   └── page.tsx
    ├── components/
    │   └── ui/
    │       ├── account-settings-dialog.tsx
    │       ├── button.tsx
    │       ├── dialog.tsx
    │       ├── input.tsx
    │       ├── table.tsx
    │       ├── theme-toggle.tsx
    │       └── topbar-user-menu.tsx
    ├── design-system/
    ├── features/
    │   ├── analise/
    │   │   ├── calculo-panel.tsx
    │   │   ├── contratos-panel.tsx
    │   │   ├── elegibilidade-panel.tsx
    │   │   ├── lastro-panel.tsx
    │   │   └── registradora-panel.tsx
    │   ├── cessao/
    │   │   ├── cessao-detail.tsx
    │   │   └── cessao-list.tsx
    │   └── security/
    │       ├── actions.ts
    │       └── login-panel.tsx
    └── lib/
        ├── auth.ts
        └── cn.ts
```

**Structure Decision**: A migracao ocorrera no proprio catalogo
`frontend/src/components/ui`, substituindo as implementacoes locais de primitives por
implementacoes aderentes ao `shadcn/ui` e ajustando todos os consumidores reais dessas
primitives nas pastas `components/ui`, `features/security`, `features/cessao` e
`features/analise`. O restante do frontend permanece na mesma estrutura.

## Phase 0: Outline & Research

As decisoes de pesquisa desta feature foram consolidadas em
[research.md](D:/desenv/fidc-cdc-kogito/specs/005-global-shadcn-migration/research.md)
e resolvem os pontos criticos:

- usar o catalogo oficial do `shadcn/ui` como criterio normativo de equivalencia;
- substituir em `frontend/src/components/ui` as primitives locais com equivalente;
- preservar tokens, comunicacao visual e cores semanticas do projeto nos componentes
  migrados;
- ajustar componentes compostos afetados (`theme-toggle`, `topbar-user-menu` e
  `account-settings-dialog`) para consumir a nova base estrutural;
- ajustar os consumidores reais restantes em `features/security`, `features/cessao` e
  `features/analise` para a nova base estrutural;
- documentar impacto de seguranca, dados sensiveis e autorizacao nos fluxos afetados;
- definir sinais tecnicos minimos para detectar regressao de renderizacao, perda de
  feedback semantico, falha de abertura/fechamento de dialogo e quebra de interacao;
- validar tecnicamente observabilidade, auditoria de migracao e acessibilidade minima.

## Phase 1: Design & Contracts

### Data Model

O modelo operacional da migracao esta descrito em
[data-model.md](D:/desenv/fidc-cdc-kogito/specs/005-global-shadcn-migration/data-model.md)
e cobre:

- inventario de componentes locais avaliados;
- mapa de equivalencia entre componente local e equivalente oficial no `shadcn/ui`;
- fluxos sensiveis em que feedback, foco e cor semantica nao podem regredir.

### Interface Contracts

O contrato funcional da migracao esta documentado em
[ui-migration-contract.md](D:/desenv/fidc-cdc-kogito/specs/005-global-shadcn-migration/contracts/ui-migration-contract.md)
e explicita:

- quais componentes entram no escopo;
- como os componentes compostos devem consumir a nova base;
- quais comportamentos de comunicacao visual precisam ser preservados;
- quais validacoes tecnicas sao obrigatorias ao final da migracao.

### Quickstart

O roteiro de validacao manual esta em
[quickstart.md](D:/desenv/fidc-cdc-kogito/specs/005-global-shadcn-migration/quickstart.md)
e cobre tema, menu do usuario, notificacoes, dialogo de ajustes e apresentacao de
feedback sem regressao perceptivel.

## Post-Design Constitution Check

- Simplicity gate: mantido. A estrategia elimina desvio estrutural em vez de criar
  adaptadores adicionais.
- Architecture gate: mantido. Primitivas, componentes compostos e validacoes ficaram
  com fronteiras claras.
- Security/compliance gate: mantido. A migracao exige verificacao de que mensagens e
  estados nao exponham dados sensiveis.
- Observability gate: mantido. O design inclui validacao tecnica de sucesso, falha e
  regressao de feedback em fluxos afetados.
- UX/performance gate: mantido. O design preserva foco, teclado, contraste, cores
  semanticas e interacao local sem degradacao perceptivel.
- Frontend implementation gate: mantido. `shadcn/ui` passa a ser a base estrutural
  das primitives equivalentes do frontend.
- Component governance gate: mantido. O escopo global segue o criterio da
  constituicao e do usuario: se existe equivalente oficial, o componente local deve
  ser substituido.

## Complexity Tracking

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| Migracao global de primitives equivalentes | A constituicao e a diretriz do usuario nao aceitam manter componentes locais equivalentes fora da base `shadcn/ui` | Corrigir apenas o fluxo de ajustes manteria o desvio estrutural no restante do frontend |
