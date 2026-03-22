# Implementation Plan: Fechamento de Ajustes e Persistencia de Tema

**Branch**: `004-fix-user-modal-theme` | **Date**: 2026-03-22 | **Spec**: [spec.md](D:/desenv/fidc-cdc-kogito/specs/004-fix-user-modal-theme/spec.md)
**Input**: Feature specification from `/specs/004-fix-user-modal-theme/spec.md`

## Summary

Corrigir o fluxo da janela de ajustes da conta para garantir um fechamento
explicito, preservar o modo visual previamente escolhido e evitar redirecionamento
indevido para a tela inicial apos salvar email ou senha. A implementacao deve
reutilizar a stack atual, respeitar a navegacao protegida existente e nao adicionar
dependencias novas.

## Technical Context

**Language/Version**: Java 21 no backend e TypeScript 5 / React 19 / Next.js 15 no frontend  
**Primary Dependencies**: Spring Boot 3.3, Spring Security, Spring HATEOAS, JPA/Hibernate, Bean Validation, Flyway, Log4j2/SLF4J, React, Next.js, shadcn/ui, Tailwind CSS, Vitest, Testing Library  
**Storage**: PostgreSQL para dados de conta; cookie HTTP-only para sessao autenticada; `localStorage` e atributo visual do documento para persistencia de tema; arquivos Markdown em `specs/004-fix-user-modal-theme/` para artefatos da feature  
**Testing**: JUnit e testes de integracao/contrato no backend quando houver impacto de conta; Vitest e Testing Library no frontend; validacao guiada do fluxo de ajustes, tema e navegacao protegida  
**Target Platform**: Aplicacao web containerizada para Linux com uso via navegador moderno  
**Project Type**: Aplicacao web com backend REST e frontend Next.js  
**Performance Goals**: Fechamento da janela e retorno apos salvar devem ocorrer sem atraso perceptivel; usuario nao deve perder o contexto protegido nem o tema ativo durante o fluxo; ajuste de conta deve ser concluido em uma unica tentativa na maior parte dos casos  
**Constraints**: Manter a stack atual do projeto; nao adicionar dependencias sem consulta previa; reutilizar o fluxo de conta, tema e autenticacao existentes; preservar o modo visual anteriormente selecionado; nao alterar regras de permissao nem perfis seedados; manter alinhamento com Design System e RFC 9457 onde houver erros de conta  
**Scale/Scope**: Correcao concentrada na janela de ajustes da conta, na persistencia do tema visual e na navegacao apos salvar email ou senha nas telas protegidas existentes

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- Simplicity gate: aprovado. A feature corrige comportamento existente e reaproveita
  o fluxo atual de dialogo, tema e navegação, sem introduzir camadas ou dependencias.
- Architecture gate: aprovado. O plano separa claramente apresentacao da janela,
  persistencia de preferencia visual e redirecionamento apos acoes de conta.
- API design gate: aprovado. Nao ha necessidade de nova superficie REST; qualquer
  ajuste em chamadas existentes permanece nos endpoints de conta ja estabelecidos.
- API versioning gate: aprovado. Como nao ha contrato REST novo, a politica atual de
  versionamento segue inalterada.
- Hypermedia gate: aprovado. Nao ha novo requisito de navegacao dinamica via API.
- API error contract gate: aprovado. Erros de salvamento de conta permanecem sob RFC
  9457 e nao exigem alteracao estrutural de contrato.
- Maintainability/scalability gate: aprovado. A correcao deve centralizar as regras
  de tema e retorno de navegacao para evitar duplicacao futura.
- Security/compliance gate: aprovado. A feature nao amplia superficie de acesso e
  apenas preserva o comportamento seguro do fluxo autenticado existente.
- Observability gate: aprovado. O plano considera rastreabilidade suficiente para
  detectar falha de salvamento e redirecionamento indevido.
- UX/performance gate: aprovado. A feature reforca controle do usuario, foco visivel,
  consistencia de tema e ausencia de saltos inesperados de navegacao.
- Frontend implementation gate: aprovado. A correcao permanece em React, Next.js,
  TypeScript, shadcn/ui como base estrutural e Design System existente como fonte
  normativa de comportamento visual.

## Project Structure

### Documentation (this feature)

```text
specs/004-fix-user-modal-theme/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── account-settings-flow.md
└── tasks.md
```

### Source Code (repository root)

```text
backend/
├── src/main/java/com/fidc/cdc/kogito/
│   ├── api/security/
│   ├── application/security/
│   ├── infrastructure/audit/
│   └── security/
└── src/test/java/com/fidc/cdc/kogito/
    ├── integration/security/
    └── integration/api/

frontend/
├── src/app/
│   ├── layout.tsx
│   ├── page.tsx
│   └── cessoes/
├── src/components/ui/
│   ├── account-settings-dialog.tsx
│   ├── dialog.tsx
│   ├── theme-toggle.tsx
│   └── topbar-user-menu.tsx
├── src/features/security/
│   ├── actions.ts
│   └── user-account-types.ts
└── src/lib/
    ├── auth.ts
    └── api-client.ts
```

**Structure Decision**: A implementacao permanece concentrada na aplicacao web ja
existente. O backend so sera tocado se o fluxo corrigido exigir ajuste minimo de
contrato ou auditoria; o foco principal desta feature esta no frontend e no
encadeamento entre `layout.tsx`, `account-settings-dialog.tsx`, `theme-toggle.tsx`
e `actions.ts`.

## Phase 0: Research Outcomes

As decisoes consolidadas em
[research.md](D:/desenv/fidc-cdc-kogito/specs/004-fix-user-modal-theme/research.md)
resolvem os pontos necessarios para implementacao:

- reutilizar o dialogo de ajustes de conta existente e adicionar fechamento explicito
  consistente para todo o fluxo;
- preservar o tema a partir da fonte de verdade ja adotada pelo produto em vez de
  reinicializar a interface no modo light;
- manter o usuario na rota protegida de origem apos salvar email ou senha, evitando
  retorno indevido ao fluxo da tela inicial;
- concentrar a correcao no frontend, sem expandir desnecessariamente o contrato do
  backend.

Nao ha pendencias abertas de esclarecimento para a fase de desenho.

## Phase 1: Design Artifacts

### Data model

O modelo de dados detalhado esta em
[data-model.md](D:/desenv/fidc-cdc-kogito/specs/004-fix-user-modal-theme/data-model.md)
e define:

- `JanelaAjustesConta` como estado visivel/oculto e origem de navegacao;
- `PreferenciaTemaVisual` como estado a ser preservado no fluxo;
- `RetornoPosSalvamento` como contexto protegido para reentrada apos sucesso ou
  falha.

### Contracts

O contrato definido para a fase atual e:

- [account-settings-flow.md](D:/desenv/fidc-cdc-kogito/specs/004-fix-user-modal-theme/contracts/account-settings-flow.md):
  contrato funcional da janela de ajustes, incluindo fechamento, salvamento,
  preservacao de tema e manutencao da rota protegida.

### Operational bootstrap

O fluxo minimo de inicializacao e validacao esta em
[quickstart.md](D:/desenv/fidc-cdc-kogito/specs/004-fix-user-modal-theme/quickstart.md)
e cobre:

- abrir a janela de ajustes a partir de uma tela protegida;
- fechar a janela sem salvar;
- salvar email preservando tema e contexto;
- salvar senha preservando tema e contexto;
- validar que falhas nao resetam tema nem expulsam o usuario do fluxo atual.

## Phase 2: Implementation Direction

### Frontend

- Ajustar `account-settings-dialog.tsx` para oferecer uma acao de fechamento
  consistente em toda a janela, nao apenas em parte do fluxo.
- Corrigir o encadeamento entre `account-settings-dialog.tsx`, `actions.ts` e
  `layout.tsx` para que o tema previamente selecionado permaneça ativo apos salvar.
- Garantir que a navegacao apos salvar reutilize a rota protegida de origem e nao
  recaia no redirecionamento padrao da tela inicial.
- Preservar feedback de sucesso e falha na propria experiencia protegida do usuario,
  sem desmontar o contexto visual.
- Validar foco visivel, acessibilidade por teclado e consistencia do dialogo com os
  componentes existentes de UI.

### Backend

- Reutilizar os endpoints de conta e credenciais ja existentes.
- Tocar o backend apenas se algum ajuste minimo de resposta, auditoria ou contrato
  for necessario para sustentar o comportamento corrigido sem gambiarra no frontend.

### Theme and navigation behavior

- Tratar a preferencia de tema como estado persistente do usuario durante a sessao.
- Tratar a rota protegida de origem como fonte de retorno apos sucesso no salvamento.
- Evitar qualquer redirecionamento implicito para `/` quando a operacao de conta
  terminar com sucesso.

## Post-Design Constitution Re-Check

- Simplicidade: mantida. A correcao reaproveita estruturas existentes e foca no
  comportamento defeituoso identificado.
- Arquitetura e coesao: mantidas. Tema, navegacao e dialogo permanecem com
  responsabilidades claras.
- Seguranca e compliance: mantidas. Nenhuma regra de acesso ou dado sensivel e
  ampliado alem do fluxo ja autenticado.
- Observabilidade: mantida. O desenho preserva espaco para diagnosticar falhas de
  salvamento e retorno incorreto.
- UX e frontend: fortalecidos. A feature devolve controle explicito ao usuario e
  preserva consistencia visual e contextual.

## Complexity Tracking

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| None | N/A | N/A |
