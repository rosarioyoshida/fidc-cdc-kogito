# Implementation Plan: Fechamento de Ajustes e Persistencia de Tema

**Branch**: `004-fix-user-modal-theme` | **Date**: 2026-03-22 | **Spec**: [spec.md](D:/desenv/fidc-cdc-kogito/specs/004-fix-user-modal-theme/spec.md)
**Input**: Feature specification from `/specs/004-fix-user-modal-theme/spec.md`

## Summary

Corrigir o fluxo de ajustes da conta para adicionar fechamento explicito com
confirmacao de descarte quando houver alteracoes pendentes, fechar a janela
automaticamente apos salvamento bem-sucedido e preservar contexto protegido e
tema ativo usando `localStorage` como fonte primaria, com sincronizacao do
atributo visual do documento e do cookie.

## Technical Context

**Language/Version**: Java 21 no backend; TypeScript 5 / React 19 / Next.js 15 no frontend  
**Primary Dependencies**: Spring Boot 3.3, Spring Security, React, Next.js, Tailwind CSS, Vitest, Testing Library  
**Storage**: PostgreSQL para dados de conta; `localStorage` como fonte primaria da preferencia visual; cookie HTTP-only para sessao autenticada; cookie de tema e atributo `data-theme` do documento sincronizados a partir de `localStorage`  
**Testing**: JUnit e testes de integracao no backend quando necessario; Vitest e Testing Library no frontend; validacao guiada do fluxo de dialogo, descarte, sucesso e falha  
**Target Platform**: Aplicacao web interna executada em navegador desktop moderno e ambiente local via Docker Compose  
**Project Type**: Web application com `backend/` e `frontend/`  
**Performance Goals**: Fechamento da janela, exibicao da confirmacao de descarte e retorno visual apos salvamento em ate 1 segundo em uso local; feedback de sucesso ou falha visivel no mesmo fluxo do usuario  
**Constraints**: A feature MUST migrar o fluxo de ajustes da conta para componentes aderentes ao padrao tecnico definido na constituicao; nao e permitido reutilizar componentes locais fora desse padrao mesmo com custo adicional; manter contratos de backend existentes; evitar redirecionamento para `/`; preservar uma unica fonte primaria de verdade para tema  
**Scale/Scope**: Correcao localizada no fluxo de conta autenticada, concentrada em dialogo, estado visual e navegacao protegida

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- Simplicity gate: aprovado. A correcao permanece no fluxo atual de dialogo e nao
  introduz novo endpoint, nova tela ou nova camada de persistencia.
- Architecture gate: aprovado. O desenho separa dialogo, persistencia de tema,
  acao de conta e navegacao protegida sem duplicar regras.
- API design gate: aprovado. Nao ha nova API REST nem alteracao de URIs.
- API versioning gate: nao aplicavel. Nao ha mudanca de superficie publica de API.
- Hypermedia gate: nao aplicavel. A feature nao introduz navegacao dinamica por API.
- API error contract gate: aprovado sem mudancas. O backend permanece com contratos
  existentes; o frontend so consome sucesso e falha ja suportados.
- Maintainability/scalability gate: aprovado. A feature explicita uma unica fonte
  primaria para tema e evita espalhar persistencia entre formularios.
- Security/compliance gate: aprovado. Nao amplia escopo de credenciais ou permissoes;
  apenas preserva sessao e contexto protegido ja existentes.
- Observability gate: aprovado. O plano exige sinais para sucesso, falha,
  cancelamento de descarte e deteccao de redirecionamento indevido.
- UX/performance gate: aprovado. O fluxo define confirmacao de descarte, fechamento
  automatico apos sucesso, feedback na tela protegida, operacao por teclado e foco
  visivel.
- Frontend implementation gate: reprovado no desenho anterior e ajustado neste plano.
  A implementacao nao pode depender da reutilizacao de componentes locais fora do
  padrao tecnico exigido pela constituicao. O fluxo de ajustes deve ser migrado para
  componentes aderentes antes da implementacao da feature.
- Component governance gate: pendente de execucao e aprovado apenas com migracao.
  O plano deve identificar quais componentes do fluxo de ajustes serao substituidos
  por componentes aderentes ao padrao constitucional, sem abrir excecao.

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
    │       ├── theme-toggle.tsx
    │       └── topbar-user-menu.tsx
    ├── features/
    │   └── security/
    │       └── actions.ts
    └── lib/
        └── auth.ts
```

**Structure Decision**: Manter a correcao concentrada no frontend em torno do
fluxo de ajustes, bootstrap de tema e actions de conta, mas substituir no proprio
fluxo os componentes nao aderentes pelo padrao tecnico exigido pela constituicao.
O backend permanece inalterado salvo necessidade minima descoberta na implementacao.

## Phase 0: Outline & Research

As decisoes de pesquisa desta feature foram consolidadas em
[research.md](D:/desenv/fidc-cdc-kogito/specs/004-fix-user-modal-theme/research.md)
e resolvem os pontos criticos:

- migrar o fluxo de ajustes para componentes aderentes ao padrao tecnico exigido
  pela constituicao;
- usar `localStorage` como fonte primaria da preferencia visual e sincronizar
  atributo do documento e cookie;
- fechar a janela automaticamente apos sucesso e exibir feedback na tela
  protegida;
- confirmar descarte apenas quando houver alteracoes nao salvas;
- documentar seguranca de sessao e dados sensiveis no fluxo de conta;
- medir a experiencia de fechamento e retorno com meta de ate 1 segundo em uso local.

## Phase 1: Design & Contracts

### Data Model

O modelo funcional desta feature esta descrito em
[data-model.md](D:/desenv/fidc-cdc-kogito/specs/004-fix-user-modal-theme/data-model.md)
e cobre:

- estado da janela de ajustes, incluindo `dirty`, confirmacao de descarte e
  fechamento automatico no sucesso;
- preferencia visual com `localStorage` como fonte primaria e sincronizacao para
  cookie e atributo do documento;
- retorno pos-salvamento com feedback de sucesso na tela protegida e manutencao
  de contexto em falhas.

### Interface Contracts

O contrato funcional do fluxo esta documentado em
[account-settings-flow.md](D:/desenv/fidc-cdc-kogito/specs/004-fix-user-modal-theme/contracts/account-settings-flow.md)
e explicita:

- estados do dialogo;
- confirmacao de descarte para alteracoes nao salvas;
- sucesso com fechamento automatico da janela;
- falha com permanencia da janela aberta;
- preservacao de tema e contexto protegido;
- obrigacao de migrar o fluxo para componentes aderentes ao padrao tecnico da
  constituicao.

### Quickstart

O roteiro de validacao manual esta em
[quickstart.md](D:/desenv/fidc-cdc-kogito/specs/004-fix-user-modal-theme/quickstart.md)
e cobre descarte cancelado, sucesso de email, sucesso de senha e falha sem perda
de contexto.

## Post-Design Constitution Check

- Simplicity gate: mantido. A correcao continua localizada em arquivos existentes.
- Architecture gate: mantido. Persistencia de tema e navegacao continuam
  centralizadas em `frontend/src/lib/auth.ts`, `frontend/src/app/layout.tsx` e
  `frontend/src/features/security/actions.ts`.
- Security/compliance gate: mantido. Nenhuma nova superficie de dados sensiveis.
- Observability gate: mantido. O design exige validacao de sucesso, falha,
  cancelamento e ausencia de redirecionamento indevido.
- UX/performance gate: mantido. Estados de dialogo, foco, descarte, sucesso e
  falha ficaram testaveis com meta local de ate 1 segundo.
- Component governance gate: mantido somente com substituicao dos componentes nao
  aderentes no fluxo de ajustes; o catalogo local atual nao pode ser reutilizado como
  base desta feature.

## Complexity Tracking

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| Migracao de componentes do fluxo de ajustes | A constituicao 1.9.0 exige base tecnica aderente ao padrao definido e o reuso atual nao e aceito | Reutilizar o catalogo local foi rejeitado por conflito direto com a constituicao |
