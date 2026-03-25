# Implementation Plan: Migracao Visual da Interface por Referencia

**Branch**: `006-reference-ui-redesign` | **Date**: 2026-03-25 | **Spec**: [spec.md](D:/desenv/fidc-cdc-kogito/specs/006-reference-ui-redesign/spec.md)
**Input**: Feature specification from `/specs/006-reference-ui-redesign/spec.md`

## Summary

Migrar as telas priorizadas do frontend para uma linguagem visual inspirada na imagem
de referencia, preservando fluxos, dados, permissoes, cores e tipografia atuais do
produto, com `shadcn/ui` como base estrutural obrigatoria, reuso estrito do catalogo
existente e proibicao de novos componentes customizados sem justificativa forte.

## Technical Context

**Language/Version**: Java 21 no backend; TypeScript 5 / React 19 / Next.js 15 no frontend  
**Primary Dependencies**: React 19, Next.js 15, Tailwind CSS 3, `shadcn/ui`, Radix UI primitives, `class-variance-authority`, `tailwind-merge`, `clsx`, `lucide-react`, Vitest, Testing Library  
**Storage**: N/A para a migracao visual em si; frontend continua consumindo APIs, cookies HTTP-only de sessao, `localStorage` de tema e tokens CSS existentes  
**Testing**: Vitest, Testing Library, testes de integracao do frontend e validacao manual guiada dos fluxos priorizados  
**Target Platform**: Aplicacao web interna em navegadores desktop modernos e viewport responsivo ja suportado pelo produto  
**Project Type**: Web application com `backend/` e `frontend/`  
**Performance Goals**: Preservar fluidez atual das telas priorizadas, com transicoes e interacoes locais sem degradacao perceptivel e identificacao da pagina/acao principal em ate 5 segundos na validacao guiada  
**Constraints**: Nao criar funcionalidades novas; manter paleta, semantica de cores e tipografia atuais; usar `shadcn/ui` como base estrutural; consultar primeiro o catalogo oficial do `shadcn/ui`, depois `frontend/src/components/ui`, depois composicao por props/variants, antes de qualquer excecao; nao criar componente customizado sem justificativa forte, owner, cobertura de estados e rastreabilidade  
**Scale/Scope**: Migracao visual das superficies prioritarias do frontend autenticado e de acesso, cobrindo `frontend/src/app/layout.tsx`, `frontend/src/app/page.tsx`, `frontend/src/app/cessoes/page.tsx`, `frontend/src/app/cessoes/[businessKey]/page.tsx`, `frontend/src/app/cessoes/[businessKey]/analise/page.tsx`, `frontend/src/app/cessoes/[businessKey]/auditoria/page.tsx`, componentes de `frontend/src/features/cessao`, `frontend/src/features/analise`, `frontend/src/features/security` e primitives/composites em `frontend/src/components/ui`

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- Simplicity gate: aprovado. O escopo trata apenas migracao visual e composicao de
  superficies existentes, sem abrir novas features.
- Architecture gate: aprovado. O plano separa tokens, primitives `ui`, componentes
  compostos e telas consumidoras sem criar uma camada paralela de design system.
- API design gate: nao aplicavel. Nao ha nova API REST.
- API versioning gate: nao aplicavel. Nao ha mudanca de contrato publico REST.
- Hypermedia gate: nao aplicavel. Nao ha alteracao de navegacao por API.
- API error contract gate: nao aplicavel. Nao ha alteracao de contratos HTTP.
- Maintainability/scalability gate: aprovado. O plano reduz divergencia visual e
  concentra reuso em `frontend/src/components/ui` com governanca explicita.
- Security/compliance gate: aprovado. A migracao nao altera autorizacao nem dados,
  mas exige preservar clareza de mensagens, segregacao de areas autenticadas e nao
  expor dados sensiveis em novos adornos visuais.
- Observability gate: aprovado. A validacao inclui evidencias tecnicas para detectar
  regressao visual, perda de hierarquia, quebra de foco e confusao operacional.
- UX/performance gate: aprovado. O plano preserva acessibilidade, contraste,
  responsividade, semantica cromatica atual e meta de identificacao rapida da acao
  principal.
- Frontend implementation gate: aprovado. A implementacao permanece em React,
  Next.js, TypeScript e Tailwind, com `shadcn/ui` como base tecnica e tokens atuais
  como contrato visual.
- Component governance gate: aprovado com restricao obrigatoria. Antes de criar
  qualquer componente novo, a equipe MUST validar: (1) equivalente no `shadcn/ui`;
  (2) reutilizacao no catalogo local `frontend/src/components/ui`; (3) variacao por
  props/variants ou composicao. Sem isso, a entrega falha no gate.

## Project Structure

### Documentation (this feature)

```text
specs/006-reference-ui-redesign/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── ui-redesign-contract.md
└── tasks.md
```

### Source Code (repository root)

```text
backend/
└── src/
    ├── main/
    └── test/

frontend/
├── components.json
└── src/
    ├── app/
    │   ├── layout.tsx
    │   ├── page.tsx
    │   └── cessoes/
    │       ├── page.tsx
    │       └── [businessKey]/
    │           ├── page.tsx
    │           ├── analise/page.tsx
    │           └── auditoria/page.tsx
    ├── components/
    │   ├── feedback/
    │   └── ui/
    │       ├── account-settings-dialog.tsx
    │       ├── button.tsx
    │       ├── dialog.tsx
    │       ├── input.tsx
    │       ├── table.tsx
    │       ├── theme-toggle.tsx
    │       └── topbar-user-menu.tsx
    ├── design-system/
    │   ├── color-semantics.ts
    │   ├── theme.css
    │   └── tokens.ts
    ├── features/
    │   ├── analise/
    │   ├── cessao/
    │   └── security/
    └── lib/
```

**Structure Decision**: A migracao ocorrera no frontend existente, preservando
`frontend/src/design-system/theme.css` e o mapeamento de cores/radius/sombra em
`frontend/tailwind.config.ts` como fonte central de tokens. As telas continuam
consumindo primitives e componentes compostos a partir de `frontend/src/components/ui`.
Quando faltar componente de superficie para executar o redesign, a ordem obrigatoria
sera: verificar equivalente no `shadcn/ui`, reutilizar componente local em
`frontend/src/components/ui`, resolver por props/variants ou composicao e so entao
avaliar excecao formal para componente novo. O arquivo `frontend/components.json`
passa a ser parte da base frontend para rastrear a configuracao do catalogo `shadcn/ui`.

## Phase 0: Outline & Research

As decisoes de pesquisa desta feature foram consolidadas em
[research.md](D:/desenv/fidc-cdc-kogito/specs/006-reference-ui-redesign/research.md)
e resolvem os pontos criticos:

- preservar os tokens atuais de cor, sombra e radius definidos em `theme.css` e
  `tailwind.config.ts`;
- preservar a tipografia atual do produto, isto e, a stack sans hoje herdada pela
  aplicacao, sem introduzir nova familia tipografica na migracao;
- definir as superficies priorizadas do redesign sem alterar escopo funcional;
- usar `shadcn/ui` como base estrutural para cards, badges, separators, tabs,
  dropdowns e outras superficies necessarias antes de qualquer criacao local;
- registrar um fluxo de excecao formal para qualquer componente inexistente no acervo
  local e sem equivalente utilizavel por composicao no `shadcn/ui`;
- definir evidencias de acessibilidade, observabilidade visual e responsividade para
  validar a migracao.

## Phase 1: Design & Contracts

### Data Model

O modelo operacional da migracao esta descrito em
[data-model.md](D:/desenv/fidc-cdc-kogito/specs/006-reference-ui-redesign/data-model.md)
e cobre:

- telas priorizadas e suas superficies visuais;
- inventario de componentes reutilizaveis, componentes shadcn candidatos e excecoes;
- contratos de preservacao de tokens, estados e semantica funcional.

### Interface Contracts

O contrato funcional da migracao visual esta documentado em
[ui-redesign-contract.md](D:/desenv/fidc-cdc-kogito/specs/006-reference-ui-redesign/contracts/ui-redesign-contract.md)
e explicita:

- quais telas e superficies entram no escopo;
- qual sequencia de decisao governa reuso e criacao de componentes;
- o que deve ser preservado em cores, tipografia, acessibilidade e fluxos;
- quais evidencias minimas devem existir para aprovar a entrega.

### Quickstart

O roteiro de validacao manual esta em
[quickstart.md](D:/desenv/fidc-cdc-kogito/specs/006-reference-ui-redesign/quickstart.md)
e cobre login, topbar autenticada, listagem de cessoes, detalhe, analise, auditoria,
tema e responsividade sem regressao funcional.

## Post-Design Constitution Check

- Simplicity gate: mantido. O redesign continua limitado a presentacao e composicao.
- Architecture gate: mantido. Tokens, primitives, compostos e telas permanecem com
  ownership claro.
- Security/compliance gate: mantido. O design nao adiciona novos dados nem novas
  acoes e exige validacao de mensagens e estados sensiveis.
- Observability gate: mantido. O plano define sinais tecnicos e checklist de
  validacao para detectar regressao visual e operacional.
- UX/performance gate: mantido. O design reforca hierarquia, foco, contraste,
  semantica cromatica e responsividade com a mesma paleta/tipografia atuais.
- Frontend implementation gate: mantido. Toda nova superficie deve nascer de
  `shadcn/ui` ou de composicao do catalogo local.
- Component governance gate: mantido. Componente customizado novo permanece bloqueado
  sem justificativa forte, alternativa rejeitada, owner e prazo de revisao.

## Complexity Tracking

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| Nenhuma | N/A | N/A |
