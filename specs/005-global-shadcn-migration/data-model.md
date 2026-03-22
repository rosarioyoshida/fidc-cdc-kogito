# Data Model: Migracao Global de Componentes Equivalentes para shadcn/ui

## Entities

### 1. Local UI Component

- **Description**: Componente atualmente mantido em `frontend/src/components/ui`
- **Fields**:
  - `name`: identificador do componente local
  - `path`: caminho absoluto no catalogo local
  - `hasShadcnEquivalent`: booleano
  - `shadcnComponent`: nome do equivalente oficial quando existir
  - `migrationStatus`: `pending | migrated | out_of_scope`
  - `notes`: observacoes sobre impacto visual, acessibilidade ou tokens

### 2. Composite Consumer

- **Description**: Componente de negocio ou de interface que consome uma ou mais
  primitives migradas
- **Fields**:
  - `name`: identificador do componente composto
  - `path`: caminho do arquivo consumidor
  - `dependsOn`: lista de primitives usadas
  - `criticalFeedbackFlow`: booleano indicando risco de regressao em comunicacao visual
  - `validationStatus`: `pending | validated`

### 3. Validation Signal

- **Description**: Evidencia tecnica usada para validar migracao, feedback e
  observabilidade
- **Fields**:
  - `name`: nome curto do sinal
  - `type`: `test | runtime-check | manual-proof`
  - `covers`: fluxo ou componente coberto
  - `expectedOutcome`: resultado esperado

## Relationships

- Um `Local UI Component` pode ser consumido por varios `Composite Consumers`.
- Um `Composite Consumer` pode depender de varias primitives migradas.
- Um `Validation Signal` pode cobrir um ou mais componentes ou fluxos compostos.

## Initial Scope Mapping

- `button.tsx` -> equivalente oficial `button`
- `input.tsx` -> equivalente oficial `input`
- `dialog.tsx` -> equivalente oficial `dialog`
- `table.tsx` -> equivalente oficial `table`
- `theme-toggle.tsx` -> consumidor composto
- `topbar-user-menu.tsx` -> consumidor composto
- `account-settings-dialog.tsx` -> consumidor composto
