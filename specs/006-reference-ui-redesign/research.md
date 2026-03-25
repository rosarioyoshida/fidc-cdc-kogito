# Research: Migracao Visual da Interface por Referencia

## Decision 1: Preservar tokens visuais atuais como contrato normativo

- **Decision**: A migracao mantera as cores, semantica cromatica, radius e sombras
  atuais definidos em `frontend/src/design-system/theme.css` e expostos em
  `frontend/tailwind.config.ts`.
- **Rationale**: O usuario determinou preservacao das cores atuais, e a constituicao
  exige camada central de tokens.
- **Alternatives considered**:
  - Introduzir nova paleta inspirada diretamente na imagem: rejeitado por conflito com
    a diretriz do usuario.
  - Duplicar tokens por feature: rejeitado por quebrar governanca do design system.

## Decision 2: Preservar a tipografia atual do produto

- **Decision**: A migracao nao introduzira nova familia tipografica; a interface
  permanecera com a stack sans atual herdada pela aplicacao e com a mesma hierarquia
  textual hoje usada nas paginas.
- **Rationale**: O usuario exigiu manter fontes atuais, e o frontend nao possui hoje
  uma fonte customizada centralizada a ser substituida.
- **Alternatives considered**:
  - Adotar a tipografia da referencia: rejeitado por mudanca fora do escopo.
  - Criar uma nova escala tipografica por tela: rejeitado por risco de inconsistencia.

## Decision 3: Tratar a imagem apenas como referencia de composicao e atmosfera

- **Decision**: A referencia visual influenciara hierarquia, densidade, agrupamento,
  acabamento de superficies e ritmo de espacamento, sem criar dados, filtros, modulos,
  indicadores ou acoes inexistentes.
- **Rationale**: A spec ja veda expansao funcional, e isso precisa virar criterio de
  projeto e revisao.
- **Alternatives considered**:
  - Replicar elementos da referencia literalmente: rejeitado por violar FR-007.
  - Fazer apenas skinning superficial sem rever hierarquia: rejeitado por nao atender
    o objetivo principal de clareza e consistencia.

## Decision 4: Aplicar governanca de componentes em tres etapas obrigatorias

- **Decision**: Cada necessidade visual nova seguira a ordem: (1) reuso de componente
  em `frontend/src/components/ui`; (2) verificacao de equivalente no acervo oficial do
  `shadcn/ui` e adicao ao catalogo local quando necessario; (3) excecao formal para
  componente novo apenas se os dois passos anteriores falharem.
- **Rationale**: A constituicao e a diretriz do usuario exigem reuso estrito e
  bloqueio de criacao ad hoc.
- **Alternatives considered**:
  - Criar componentes locais rapidamente por conveniencia: rejeitado por aumentar
    divergencia estrutural.
  - Permitir variacoes locais baseadas apenas em classes: rejeitado por ferir a
    governanca de UI.

## Decision 5: Priorizar superficies shadcn de layout e feedback antes de excecoes

- **Decision**: O redesign deve procurar primeiro por primitives e superficies como
  `button`, `input`, `dialog`, `table`, `card`, `badge`, `separator`, `tabs`,
  `dropdown-menu`, `sheet`, `alert` e equivalentes disponiveis na base `shadcn/ui`
  antes de considerar implementacao local.
- **Rationale**: O catalogo local atual cobre apenas parte das superficies necessarias
  para um redesign consistente.
- **Alternatives considered**:
  - Manter composicoes manuais com `div` em todas as telas: rejeitado por reduzir
    consistencia e reaproveitamento.
  - Expandir o catalogo local sem referencia ao acervo oficial: rejeitado por perder o
    baseline tecnico exigido.

## Decision 6: Definir telas prioritarias e superficies de migracao

- **Decision**: O plano prioriza as seguintes superficies: login, topbar/autenticacao,
  listagem de cessoes, detalhe da cessao, dashboard de analise e tela de auditoria,
  incluindo headers, blocos de contexto, formularios, listas, estados vazios, acoes e
  agrupamentos secundarios.
- **Rationale**: Essas telas representam os fluxos mais visiveis e concentram os
  padroes que depois se propagam ao restante do frontend.
- **Alternatives considered**:
  - Redesenhar tudo de uma vez sem priorizacao: rejeitado por aumentar risco.
  - Limitar a migracao a uma tela: rejeitado por nao produzir consistencia suficiente.

## Decision 7: Validar redesign por semantica operacional, nao apenas por estetica

- **Decision**: A aprovacao da migracao exigira evidencias de que o usuario consegue
  localizar pagina, contexto e acao primaria rapidamente, mantendo foco visivel,
  contraste, teclado, tema e responsividade.
- **Rationale**: A constituicao trata experiencia e performance como requisito
  funcional, nao apenas decorativo.
- **Alternatives considered**:
  - Validacao apenas visual por screenshot: rejeitado por nao cobrir usabilidade.
  - Validacao apenas automatizada: rejeitado por nao cobrir percepcao guiada de fluxo.

## Security and Audit Notes

- O redesign nao pode expor credenciais, emails, perfis, identificadores de negocio ou
  mensagens sensiveis em adornos visuais desnecessarios.
- Mensagens de erro e sucesso devem manter semantica atual sem exagerar destaque de
  informacoes secundarias sobre a acao principal.
- Qualquer excecao de componente novo deve ficar registrada com justificativa,
  alternativas rejeitadas, owner e prazo de revisao.

## Component Reuse Baseline

| Surface need | Local component available | shadcn candidate to verify | Planned action |
|--------------|---------------------------|----------------------------|----------------|
| Buttons and CTA | `button.tsx` | `button` | Reuse local primitive |
| Form inputs | `input.tsx` | `input` | Reuse local primitive |
| Modal/account settings | `dialog.tsx` | `dialog` | Reuse local primitive |
| Tabular data | `table.tsx` | `table` | Reuse local primitive |
| Context cards and panels | none dedicated | `card` | Verify shadcn and add if needed |
| Status emphasis | none dedicated | `badge` or `alert` | Verify shadcn and add if needed |
| Secondary grouping | none dedicated | `separator` | Verify shadcn and add if needed |
| Navigation clusters | none dedicated | `tabs`, `dropdown-menu`, `sheet` | Verify shadcn and add only if needed |

## Technical Observability Signals

- `visual-hierarchy-check`: evidencia de que cada tela priorizada expoe contexto,
  conteudo principal e acao primaria de forma distinguivel
- `token-preservation-check`: verificacao de uso continuado dos tokens atuais de cor,
  shadow e radius
- `component-governance-check`: inventario das superficies novas com prova de reuso ou
  justificativa de excecao
- `accessibility-state-check`: validacao de foco visivel, contraste, estados e teclado
- `responsive-layout-check`: verificacao das telas priorizadas nas larguras ja
  suportadas pelo produto

## Final Implementation Inventory

| Surface | Final source | Notes |
|---------|--------------|-------|
| Authenticated shell and account navigation | `frontend/src/components/ui/card.tsx`, `frontend/src/components/ui/dropdown-menu.tsx`, `frontend/src/components/ui/badge.tsx` | Topbar migrada sem novas acoes |
| Login and access feedback | `frontend/src/components/ui/card.tsx`, `frontend/src/components/ui/alert.tsx` | Campos e fluxo preservados |
| Cession listing and detail | `frontend/src/components/ui/card.tsx`, `frontend/src/components/ui/badge.tsx`, `frontend/src/components/ui/alert.tsx` | Lista, status e detalhe mantidos |
| Analysis dashboard and panels | `frontend/src/components/ui/card.tsx`, `frontend/src/components/ui/badge.tsx`, `frontend/src/components/ui/alert.tsx` | Paineis padronizados por composicao |
| Audit page and evidence timeline | `frontend/src/components/ui/card.tsx`, `frontend/src/components/ui/badge.tsx`, `frontend/src/components/ui/button.tsx` | Sem novos filtros ou comandos |
| Shared feedback states | `frontend/src/components/ui/alert.tsx`, `frontend/src/components/ui/card.tsx` | `empty-state` e `forbidden-state` continuam detectaveis |

## Approved Exceptions

- Nenhuma excecao formal foi necessaria nesta implementacao.
- Todas as superficies novas da primeira onda foram resolvidas com reuso local,
  adicao de primitives `shadcn/ui` ao catalogo local ou composicao dos primitives
  existentes.
