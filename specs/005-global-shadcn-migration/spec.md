# Feature Specification: Migracao Global de Componentes Equivalentes para shadcn/ui

**Feature Branch**: `005-global-shadcn-migration`  
**Created**: 2026-03-22  
**Status**: Closed  
**Input**: User description: "Migrar todos os componentes equivalentes do frontend para shadcn/ui e preservar feedback, comunicacao visual e cores semanticas em todo o projeto"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Migrar primitivas equivalentes de UI (Priority: P1)

Como time de frontend, quero substituir em todo o projeto os componentes locais que
possuem equivalente no catalogo oficial do `shadcn/ui` para alinhar a base tecnica do
frontend a constituicao do repositorio.

**Why this priority**: Esta e a exigencia estrutural minima para eliminar o desvio
mais direto em relacao a constituicao e parar de expandir uma base de componentes nao
aderente.

**Independent Test**: A historia pode ser validada identificando os componentes locais
equivalentes, substituindo-os por componentes baseados no catalogo do `shadcn/ui` e
confirmando que o projeto continua compilando e renderizando os fluxos existentes sem
regressao funcional basica.

**Acceptance Scenarios**:

1. **Given** um componente local em `frontend/src/components/ui` com equivalente no
   catalogo oficial do `shadcn/ui`, **When** a migracao for executada, **Then** esse
   componente passa a usar a base tecnica aderente ao `shadcn/ui`.
2. **Given** o conjunto atual de componentes `button`, `input`, `dialog` e `table`,
   **When** a migracao for concluida, **Then** o frontend deixa de depender das
   implementacoes locais anteriores como base estrutural desses componentes.

---

### User Story 2 - Preservar comunicacao visual e feedback (Priority: P1)

Como usuario do sistema, quero que a migracao de componentes preserve mensagens,
feedback, cores semanticas e comunicacao visual da interface para nao perder clareza
nem contexto durante o uso.

**Why this priority**: Uma migracao tecnica que degrade feedback, estados de erro,
sucesso, destaque e a semantica visual quebra a experiencia do usuario mesmo se a
troca estrutural estiver correta.

**Independent Test**: A historia pode ser validada navegando pelos fluxos
autenticados, alternando tema, abrindo menu de usuario, visualizando notificacoes e
interagindo com ajustes da conta, confirmando preservacao de textos, estados, foco e
cores semanticas.

**Acceptance Scenarios**:

1. **Given** um fluxo que usa feedback de sucesso, erro, alerta ou informacao,
   **When** os componentes aderentes substituirem os atuais, **Then** a semantica de
   cor e a mensagem ao usuario permanecem claras e consistentes.
2. **Given** um fluxo interativo com abertura, fechamento, foco ou loading, **When**
   a migracao for aplicada, **Then** o comportamento perceptivel ao usuario permanece
   funcional e coerente com o Design System adotado.

---

### User Story 3 - Validar governanca, auditoria e observabilidade da migracao (Priority: P2)

Como responsavel tecnico, quero validar tecnicamente a migracao de componentes para
garantir aderencia a constituicao, cobertura de observabilidade e registro dos pontos
relevantes de auditoria.

**Why this priority**: Depois da substituicao estrutural e da preservacao da
experiencia, o projeto precisa evidenciar que a migracao e rastreavel, observavel e
governada.

**Independent Test**: A historia pode ser validada executando verificacoes tecnicas e
documentais sobre componentes migrados, sinais de sucesso/falha e rastreabilidade das
substituicoes realizadas.

**Acceptance Scenarios**:

1. **Given** um componente migrado, **When** a validacao tecnica for executada,
   **Then** a equipe consegue comprovar que ele usa base aderente ao `shadcn/ui`.
2. **Given** fluxos com sucesso, erro ou estados interativos relevantes, **When** a
   migracao for validada, **Then** existem sinais tecnicos suficientes para detectar
   regressao, falha visual ou comportamento indevido.

---

### Edge Cases

- Componente local possui equivalente no `shadcn/ui`, mas contem classes ou tokens
  proprios que sustentam semantica visual critica
- Componente migrado preserva a estrutura, mas perde foco visivel, navegacao por
  teclado ou `aria-label`
- Fluxo autenticado continua funcional, mas feedback visual muda de cor ou perde
  hierarquia semantica
- Tema claro/escuro continua alternando, mas o componente migrado passa a divergir da
  fonte central de tokens
- Um componente composto como `topbar-user-menu` depende de multiplas primitivas e a
  migracao parcial gera regressao de interacao
- Um componente local nao tem equivalente no catalogo oficial do `shadcn/ui` e deve
  permanecer fora desta migracao

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: O projeto MUST identificar todos os componentes locais em
  `frontend/src/components/ui` que possuem equivalente no catalogo oficial do
  `shadcn/ui`.
- **FR-002**: O projeto MUST substituir em todo o frontend os componentes locais com
  equivalente oficial por componentes baseados no `shadcn/ui`.
- **FR-003**: A migracao MUST preservar textos, feedback e comportamento perceptivel
  dos fluxos ja existentes para o usuario final.
- **FR-004**: A migracao MUST preservar a semantica de cores para sucesso, erro,
  aviso, informacao e neutralidade conforme a constituicao.
- **FR-005**: A migracao MUST preservar foco visivel, operacao por teclado e demais
  estados interativos relevantes dos componentes migrados.
- **FR-006**: Componentes compostos do projeto, incluindo `theme-toggle`,
  `topbar-user-menu` e `account-settings-dialog`, MUST ser ajustados para consumir as
  novas bases aderentes apos a migracao das primitivas.
- **FR-007**: Componentes locais sem equivalente oficial no catalogo do `shadcn/ui`
  MUST permanecer fora do escopo desta feature, sem serem reescritos sem necessidade.
- **FR-008**: O projeto MUST validar tecnicamente que a migracao nao removeu feedback
  ao usuario, estados semanticos nem comunicacao visual critica.
- **FR-009**: O projeto MUST registrar rastreabilidade da migracao, identificando
  quais componentes foram substituidos e quais permaneceram fora do escopo por nao
  possuirem equivalente oficial.

### Non-Functional Requirements *(mandatory)*

- **NFR-001**: A solucao MUST documentar impacto de seguranca, dados sensiveis e
  autorizacao nos fluxos afetados, mesmo que a migracao nao altere o backend.
- **NFR-002**: A solucao MUST registrar evidencias tecnicas de auditoria da migracao,
  incluindo componentes substituidos, componentes mantidos fora do escopo e criterio
  de equivalencia adotado.
- **NFR-003**: A solucao MUST definir e validar tecnicamente sinais de observabilidade
  suficientes para detectar sucesso, falha ou regressao relevante nos fluxos de UI
  afetados.
- **NFR-004**: A solucao MUST manter ownership claro e baixo acoplamento entre
  primitivas aderentes, componentes compostos e fluxos de negocio.
- **NFR-005**: A solucao MUST limitar o escopo de migracao a componentes com
  equivalente oficial no `shadcn/ui`, evitando reescrita arbitraria de componentes
  sem esse criterio.
- **NFR-006**: A migracao MUST manter desempenho funcional equivalente nos principais
  fluxos visuais, sem degradacao perceptivel em abertura, fechamento, interacao e
  renderizacao local dos componentes migrados.
- **NFR-007**: A solucao MUST preservar usabilidade, acessibilidade, foco visivel,
  leitura de estados e comunicacao visual proporcional ao escopo global da migracao.
- **NFR-008**: A solucao MUST preservar hierarquia visual, espacamento, repeticao de
  padroes e contraste adequados apos a substituicao estrutural.
- **NFR-009**: A solucao MUST preservar cores semanticas e feedback visual coerente
  com o contrato definido na constituicao e no Design System adotado.
- **NFR-010**: A solucao MUST usar `shadcn/ui` como base tecnica dos componentes
  equivalentes migrados e manter tokens/estilizacao alinhados ao contrato visual
  vigente do projeto.
- **NFR-010b**: Para cada componente local avaliado, a solucao MUST registrar se ha
  equivalente no catalogo oficial do `shadcn/ui` e a decisao de migrar ou manter.
- **NFR-011**: A solucao MUST validar acessibilidade minima dos componentes migrados,
  incluindo navegacao por teclado, foco visivel e semantica apropriada.
- **NFR-012**: A migracao web MUST permanecer dentro da stack React, Next.js,
  TypeScript e `shadcn/ui`, sem introduzir outro framework de componentes.
- **NFR-013**: Componentes migrados MUST explicitar variantes, estados e regras de
  estilo dirigidas por tokens de forma consistente.

### Key Entities *(include if feature involves data)*

- **Catalogo de Componentes Locais**: Conjunto atual de componentes em
  `frontend/src/components/ui` que deve ser avaliado contra o catalogo oficial do
  `shadcn/ui`.
- **Mapa de Equivalencia shadcn/ui**: Relacao entre componente local, equivalente
  oficial existente e decisao de migracao ou permanencia fora do escopo.
- **Fluxos Sensiveis de Comunicacao Visual**: Interacoes do frontend em que feedback,
  cor semantica, foco, estados ou mensagens ao usuario nao podem regredir apos a
  migracao.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: 100% dos componentes locais com equivalente oficial identificado no
  catalogo do `shadcn/ui` sao avaliados e classificados como migrados ou fora do
  escopo com justificativa objetiva.
- **SC-002**: 100% das primitivas locais `button`, `input`, `dialog` e `table` deixam
  de ser a base estrutural ativa dos fluxos do frontend apos a migracao.
- **SC-003**: Pelo menos 95% dos consumidores reais identificados das primitives
  migradas mantem feedback, foco, semantica visual e interacao sem regressao
  perceptivel apos a migracao.
- **SC-004**: 100% das primitives migradas e dos consumidores priorizados
  `theme-toggle`, `topbar-user-menu`, `account-settings-dialog`, `login-panel`,
  `cessao-list`, `cessao-detail`, `calculo-panel`, `contratos-panel`,
  `elegibilidade-panel`, `lastro-panel` e `registradora-panel` possuem validacao
  tecnica registrada para acessibilidade minima, observabilidade e rastreabilidade
  da substituicao.

## Assumptions

- O catalogo atual de componentes locais relevantes esta em `frontend/src/components/ui`.
- O criterio de equivalencia sera o catalogo oficial publicado em
  `https://ui.shadcn.com/docs/components`.
- A migracao nao altera contratos de backend nem regras de autorizacao, salvo ajuste
  incidental de integracao no frontend.

## Out of Scope

- Reescrita de componentes sem equivalente oficial no catalogo do `shadcn/ui`
- Mudanca de backend, APIs REST, entidades de dominio ou regras de seguranca do servidor
- Redesenho amplo do contrato visual alem do necessario para manter feedback,
  comunicacao, cores semanticas e aderencia tecnica
