# Data Model: Migracao Visual da Interface por Referencia

## Entities

### 1. Prioritized Screen

- **Description**: Tela existente do produto escolhida para receber a nova linguagem
  visual sem alteracao de escopo funcional.
- **Fields**:
  - `name`: nome identificador da tela
  - `route`: rota da pagina
  - `primaryUserGoal`: objetivo principal do usuario na tela
  - `visualSections`: secoes visuais obrigatorias da tela
  - `primaryAction`: acao principal que deve permanecer evidente
  - `status`: `pending | designed | validated`

### 2. Visual Surface

- **Description**: Bloco de interface reutilizavel ou repetivel dentro das telas
  priorizadas.
- **Fields**:
  - `name`: nome da superficie
  - `kind`: `header | panel | form | list | table | status | navigation | empty-state | feedback`
  - `usesExistingFeature`: booleano
  - `componentSource`: `local-ui | shadcn-ui | composition | approved-exception`
  - `tokensUsed`: lista de tokens obrigatorios
  - `requiredStates`: estados esperados como `default`, `hover`, `focus-visible`,
    `disabled`, `loading`, `selected`

### 3. Component Decision

- **Description**: Registro da decisao de reuso ou criacao para cada necessidade de UI.
- **Fields**:
  - `surfaceName`: superficie associada
  - `localCandidate`: componente local reutilizavel, se existir
  - `shadcnCandidate`: componente oficial a verificar, se aplicavel
  - `decision`: `reuse-local | add-shadcn | compose-existing | exception-approved`
  - `justification`: racional obrigatorio quando a decisao nao for reuso simples
  - `owner`: responsavel tecnico

### 4. Validation Evidence

- **Description**: Evidencia usada para aprovar a migracao visual.
- **Fields**:
  - `name`: identificador curto
  - `type`: `unit-test | integration-test | manual-validation | review-check`
  - `covers`: telas ou superficies cobertas
  - `expectedOutcome`: resultado esperado

## Relationships

- Uma `Prioritized Screen` contem varias `Visual Surfaces`.
- Uma `Visual Surface` exige uma `Component Decision`.
- Uma `Validation Evidence` pode cobrir varias telas e superficies.

## Initial Scope Mapping

- `Home/Login` -> rota `/` -> superficies `hero de acesso`, `formulario`, `feedback de erro` -> `status: validated`
- `Cessoes List` -> rota `/cessoes` -> superficies `header`, `formulario de criacao`,
  `cards/lista`, `empty-state` -> `status: validated`
- `Cessao Detail` -> rota `/cessoes/[businessKey]` -> superficies `header contextual`,
  `acoes`, `status`, `tabela operacional` -> `status: validated`
- `Analise Dashboard` -> rota `/cessoes/[businessKey]/analise` -> superficies `header`,
  `mensagem de feedback`, `paineis`, `agrupamentos` -> `status: validated`
- `Auditoria` -> rota `/cessoes/[businessKey]/auditoria` -> superficies `header`,
  `trilha/registro`, `filtros existentes`, `estados de informacao` -> `status: validated`
- `Authenticated Topbar` -> componente `topbar-user-menu` -> superficies `identificacao`,
  `tema`, `notificacoes`, `menu de conta` -> `status: validated`

## Component Decision Rules

- `button`, `input`, `dialog` e `table` -> `decision: reuse-local`
- `context cards / panels` -> `decision: add-shadcn` via verificacao de `card`
- `status emphasis / badges / alerts` -> `decision: add-shadcn` via verificacao de
  `badge` e `alert`
- `secondary separators` -> `decision: add-shadcn` via verificacao de `separator`
- `navigation clusters` -> `decision: add-shadcn` ou `compose-existing`, dependendo do
  encaixe com `tabs`, `dropdown-menu` e `sheet`
- qualquer superficie sem cobertura local ou shadcn por composicao -> `decision:
  exception-approved` somente com justificativa forte e rastreavel

## Token Contract

- `colors`: `surface`, `surface-raised`, `text`, `text-subtle`, `brand`, `success`,
  `warning`, `danger`, `border`
- `radius`: `md`, `lg`
- `shadow`: `soft`
- `typography`: stack sans atual do aplicativo, sem nova familia

## Validation Evidence Set

- `screen-recognition-check` -> manual-validation
- `token-usage-review` -> review-check
- `component-governance-review` -> review-check
- `topbar-user-menu.test.tsx` -> unit-test
- `login-panel.test.tsx` -> unit-test
- `cessao-list.test.tsx` -> unit-test
- `cessao-detail.test.tsx` -> unit-test
- `analise-cessao.spec.tsx` -> integration-test
- `auditoria-permissoes.spec.tsx` -> integration-test
- `topbar-user-menu.spec.tsx` -> integration-test

## Final Component Decisions

| Surface | Decision | Final source | Owner |
|---------|----------|--------------|-------|
| Context cards and panels | `add-shadcn` | `card.tsx` | Frontend |
| Status emphasis and feedback | `add-shadcn` | `badge.tsx`, `alert.tsx` | Frontend |
| Secondary separators and grouped menus | `add-shadcn` | `separator.tsx`, `dropdown-menu.tsx` | Frontend |
| Dashboard and audit compositions | `compose-existing` | `card`, `badge`, `alert`, `button`, `table` | Frontend |
| Exceptions | `reuse-local` / `compose-existing` | Nenhuma excecao aprovada | Frontend |
