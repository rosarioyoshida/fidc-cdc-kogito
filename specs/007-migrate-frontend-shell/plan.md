# Implementation Plan: Migração do Frontend para Novo Shell Horizontal

**Branch**: `007-migrate-frontend-shell` | **Date**: 2026-03-25 | **Spec**: [spec.md](D:/desenv/fidc-cdc-kogito/specs/007-migrate-frontend-shell/spec.md)
**Input**: Feature specification from `/specs/007-migrate-frontend-shell/spec.md`

## Summary

Migrar o frontend atual para um shell horizontal compartilhado, com experiência autenticada unificada, tela de login alinhada à mesma linguagem estrutural em versão simplificada, navegação global e contextual consistentes e reorganização do conteúdo existente em seções e linhas padronizadas, preservando rotas, dados, ações, regras de acesso e estados já disponíveis.

Navegação global desta entrega: acesso principal à lista de cessões no header autenticado. Navegação contextual por rota: destinos relacionados como resumo, análise e auditoria, além de filtros, busca, seletores e ações locais aplicáveis na barra secundária.

## Technical Context

**Language/Version**: Java 21 no backend; TypeScript 5 / React 19 / Next.js 15 no frontend  
**Primary Dependencies**: React 19, Next.js 15 App Router, Tailwind CSS 3, `shadcn/ui` e componentes locais em `frontend/src/components/ui`, Radix primitives já instaladas, `clsx`, `class-variance-authority`, `tailwind-merge`, `lucide-react`, Vitest, Testing Library  
**Storage**: N/A para a migração em si; frontend continua consumindo dados do backend, sessão autenticada via cookie HTTP-only, preferência visual via `localStorage` e cookie de tema já existentes  
**Testing**: Vitest, Testing Library, testes de integração existentes do frontend, validação manual guiada dos fluxos de login, lista, detalhe, análise, auditoria e menu do usuário  
**Target Platform**: Aplicação web interna em navegadores desktop modernos e larguras responsivas reduzidas  
**Project Type**: Web application com `backend/` e `frontend/`  
**Performance Goals**: Navegação entre rotas prioritárias e abertura de controles globais sem degradação perceptível; header, barra secundária, menu do usuário e listas reorganizadas devem responder em até 1 segundo em ambiente local para fluxos priorizados  
**Constraints**: Reaproveitar conteúdo real das páginas existentes; adotar `shadcn/ui` como base estrutural e Design System Atlassian como fonte normativa visual/comportamental; validar reúso de `button`, `input`, `dropdown-menu`, `badge`, `card`, `separator`, `dialog`, `table` e `topbar-user-menu` antes de criar novas peças; aplicar a migração em corte único; login entra no novo formato sem header global  
**Scale/Scope**: Abrange `frontend/src/app/layout.tsx`, `frontend/src/app/page.tsx`, `frontend/src/app/cessoes/page.tsx`, `frontend/src/app/cessoes/[businessKey]/page.tsx`, `frontend/src/app/cessoes/[businessKey]/analise/page.tsx`, `frontend/src/app/cessoes/[businessKey]/auditoria/page.tsx`, componentes compartilhados de shell e os componentes consumidores em `frontend/src/features/security`, `frontend/src/features/cessao`, `frontend/src/features/analise` e `frontend/src/features/auditoria`

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- Simplicity gate: aprovado. O plano substitui a casca visual fragmentada por um shell compartilhado e evita criar arquiteturas paralelas ou rollout gradual.
- Architecture gate: aprovado. O design separa shell global, shell simplificado de login, navegação contextual e conteúdo de rota sem mover regras de negócio para a camada compartilhada.
- API design gate: não aplicável. Não há criação ou alteração de API REST.
- API versioning gate: não aplicável. Não há mudança de contrato REST.
- Hypermedia gate: não aplicável. Não há nova navegação por API.
- API error contract gate: não aplicável. Não há mudança de payloads HTTP.
- Maintainability/scalability gate: aprovado. O shell compartilhado reduz duplicação estrutural e prepara crescimento de novas páginas sem reescrever topo, rodapé e contexto.
- Security/compliance gate: aprovado. O plano preserva autenticação atual, menu de conta com alteração de senha, estados de permissão e trilha auditável existente.
- Observability gate: aprovado. O design exige validação explícita de falhas de carregamento do shell, resolução do usuário autenticado, abertura de notificações e menu de conta, quebras de navegação contextual e rastreabilidade das ações de conta.
- UX/performance gate: aprovado. O plano explicita hierarquia entre navegação global, barra contextual e conteúdo, além de acessibilidade por teclado, foco visível e resposta sem degradação perceptível.
- Frontend implementation gate: aprovado. A implementação permanece em React, Next.js, TypeScript, `shadcn/ui` como base estrutural e tokens centrais do projeto alinhados ao Design System da Atlassian.
- Component governance gate: aprovado. O plano exige auditoria de reúso do catálogo `frontend/src/components/ui` antes de qualquer nova variação e limita novos componentes ao que não for coberto por composição.

## Project Structure

### Documentation (this feature)

```text
specs/007-migrate-frontend-shell/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── ui-shell-migration-contract.md
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
    │   ├── page.tsx
    │   └── cessoes/
    │       ├── page.tsx
    │       └── [businessKey]/
    │           ├── page.tsx
    │           ├── analise/page.tsx
    │           └── auditoria/page.tsx
    ├── components/
    │   ├── feedback/
    │   │   ├── empty-state.tsx
    │   │   └── forbidden-state.tsx
    │   └── ui/
    │       ├── account-settings-dialog.tsx
    │       ├── badge.tsx
    │       ├── button.tsx
    │       ├── card.tsx
    │       ├── dialog.tsx
    │       ├── dropdown-menu.tsx
    │       ├── input.tsx
    │       ├── separator.tsx
    │       ├── table.tsx
    │       ├── theme-toggle.tsx
    │       └── topbar-user-menu.tsx
    ├── design-system/
    │   ├── theme.css
    │   └── tokens.ts
    ├── features/
    │   ├── navigation/
    │   │   └── shell-config.ts
    │   ├── security/
    │   │   └── login-panel.tsx
    │   ├── cessao/
    │   │   ├── cessao-list.tsx
    │   │   ├── cessao-detail.tsx
    │   │   └── cessao-status-panel.tsx
    │   ├── analise/
    │   │   ├── analise-dashboard.tsx
    │   │   ├── calculo-panel.tsx
    │   │   ├── contratos-panel.tsx
    │   │   ├── elegibilidade-panel.tsx
    │   │   ├── lastro-panel.tsx
    │   │   └── registradora-panel.tsx
    │   └── auditoria/
    │       ├── audit-timeline.tsx
    │       └── task-context-panel.tsx
    └── lib/
        ├── auth.ts
        └── api-client.ts
```

**Structure Decision**: A feature será implementada inteiramente no frontend atual. O núcleo da mudança ficará em `frontend/src/app/layout.tsx`, em novos componentes compartilhados de shell e no módulo `frontend/src/features/navigation/`, responsável pela configuração da navegação global e contextual por rota. As rotas existentes continuam as mesmas e terão apenas a composição visual reorganizada. Os componentes de feature continuam donos do conteúdo de negócio e passam a ser encaixados no shell por seções, barra contextual e linhas padronizadas.

## Phase 0: Outline & Research

As decisões de pesquisa desta feature foram consolidadas em [research.md](D:/desenv/fidc-cdc-kogito/specs/007-migrate-frontend-shell/research.md) e resolvem os pontos críticos:

- como introduzir um shell compartilhado sem quebrar as rotas existentes;
- como tratar a tela de login em formato simplificado, sem header global;
- como separar navegação global e barra contextual por rota;
- como reorganizar tabelas, cards e painéis existentes em seções e linhas padronizadas sem perder comportamento;
- como atender a política de reúso do catálogo `frontend/src/components/ui` antes de criar novas peças;
- como validar acessibilidade, responsividade, observabilidade mínima e regressão funcional no corte único.

## Phase 1: Design & Contracts

### Data Model

O modelo operacional da migração está descrito em [data-model.md](D:/desenv/fidc-cdc-kogito/specs/007-migrate-frontend-shell/data-model.md) e cobre:

- entidades de shell, navegação, seção e linha de conteúdo;
- mapeamento entre rotas atuais e configuração de shell/contexto;
- estados obrigatórios do shell, do login e dos itens reorganizados.

### Interface Contracts

O contrato funcional da migração está documentado em [ui-shell-migration-contract.md](D:/desenv/fidc-cdc-kogito/specs/007-migrate-frontend-shell/contracts/ui-shell-migration-contract.md) e explicita:

- regiões obrigatórias por tipo de página;
- componentes reutilizados e critérios para novas variações;
- regras de comportamento e preservação dos fluxos atuais por rota.

### Quickstart

O roteiro de validação manual está em [quickstart.md](D:/desenv/fidc-cdc-kogito/specs/007-migrate-frontend-shell/quickstart.md) e cobre login, navegação global, navegação contextual, lista, detalhe, análise, auditoria, menu do usuário, notificações e responsividade.

### Observability and Cutover Readiness

Antes da ativação única do novo shell, a implementação deve validar e registrar:

- sinais visíveis ou rastreáveis para falha de carregamento do shell compartilhado;
- sinais visíveis ou rastreáveis para erro na resolução do usuário autenticado;
- sinais visíveis ou rastreáveis para falha ao abrir notificações e menu de conta;
- sinais visíveis ou rastreáveis para quebra de navegação contextual por rota;
- preservação da auditabilidade de logout e alteração de senha;
- checklist de prontidão do cutover cobrindo todas as rotas do escopo, ações críticas e ausência de dependência do shell antigo.

## Post-Design Constitution Check

- Simplicity gate: mantido. O design concentra a mudança em um shell compartilhado e evita manter dois formatos paralelos.
- Architecture gate: mantido. O shell organiza layout e navegação; conteúdo operacional continua nos módulos de feature.
- Security/compliance gate: mantido. O menu de usuário, alteração de senha, estados de permissão e auditoria permanecem explícitos no desenho.
- Observability gate: mantido. O design inclui validação explícita de falha de shell, resolução de usuário, notificações, menu de conta, navegação contextual e auditabilidade das ações de conta.
- UX/performance gate: mantido. O plano define hierarquia clara, acessibilidade, keyboard support, feedback semântico e metas de resposta sem degradação perceptível.
- Frontend implementation gate: mantido. O design usa Next.js App Router, TypeScript, `shadcn/ui`, tokens centrais e componentes locais já existentes como base.
- Component governance gate: mantido. O contrato reforça auditoria de reúso para `button`, `input`, `dropdown-menu`, `badge`, `card`, `separator`, `dialog`, `table`, `theme-toggle` e `topbar-user-menu` antes de qualquer criação nova, e determina que novos componentes de layout tenham exceção formal, alternativas rejeitadas e aprovação de arquitetura/frontend antes da implementação.

## Complexity Tracking

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| Nenhuma | N/A | N/A |
