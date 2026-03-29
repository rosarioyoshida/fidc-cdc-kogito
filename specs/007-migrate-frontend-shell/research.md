# Research: Migração do Frontend para Novo Shell Horizontal

## Decision 1: Introduzir um shell compartilhado acima das rotas atuais

**Decision**: Implementar um shell compartilhado para as páginas autenticadas e um shell simplificado para a tela de login, preservando as rotas existentes e reorganizando a composição no nível de layout.

**Rationale**: O frontend atual já centraliza tema e usuário em `frontend/src/app/layout.tsx`, mas ainda usa um container único com `TopbarUserMenu` isolado e páginas que montam seus próprios headers. Levar a responsabilidade do shell para a camada compartilhada reduz duplicação, melhora previsibilidade entre rotas e permite aplicar o corte único pedido no spec.

**Alternatives considered**:
- Manter cada página com seu próprio header e apenas ajustar estilos locais. Rejeitado porque preserva fragmentação estrutural e não entrega o shell unificado definido no spec.
- Fazer rollout gradual por rota. Rejeitado porque o spec fixou ativação em corte único, sem convivência entre formatos.

## Decision 2: Tratar a tela de login como experiência simplificada, sem header global

**Decision**: Aplicar a mesma linguagem estrutural à página de login, mas com rodapé simplificado e sem header global.

**Rationale**: A tela de login não precisa de navegação autenticada, notificações ou menu de conta. Mantê-la fora do header reduz distração, respeita o fluxo de acesso e ainda preserva consistência visual com o restante do produto.

**Alternatives considered**:
- Aplicar o mesmo header do ambiente autenticado na tela de login. Rejeitado porque adiciona elementos irrelevantes antes da autenticação.
- Não migrar a tela de login. Rejeitado porque conflita com a decisão registrada em clarificação.

## Decision 3: Usar a barra secundária como ponto padrão de navegação contextual e filtros locais

**Decision**: A barra secundária deve combinar, por rota, subnavegação contextual, indicadores de contexto, filtros, busca e seletores locais.

**Rationale**: As rotas atuais já têm ações e atalhos espalhados em headers locais e blocos internos. Centralizar esses controles em uma faixa contextual reduz dispersão, deixa explícito o estado da página e torna o shell consistente entre lista, detalhe, análise e auditoria.

**Alternatives considered**:
- Reservar a barra secundária apenas para filtros. Rejeitado porque separa em excesso a navegação local do estado da tela.
- Reservar a barra secundária apenas para abas de contexto. Rejeitado porque mantém busca e filtros espalhados no conteúdo.

## Decision 4: Reorganizar conteúdo existente em seções e linhas padronizadas sem reescrever regras de negócio

**Decision**: Manter os componentes de feature como donos dos dados e ações, mas adaptar sua apresentação para seções e linhas horizontais sempre que houver ganho real de comparação, leitura ou ação por item.

**Rationale**: `CessaoList`, `CessaoDetail`, `AnaliseDashboard` e `AuditTimeline` já concentram o comportamento funcional. Reescrever os fluxos do zero aumentaria risco. O melhor desenho é compor esses módulos dentro do novo shell e refatorar apenas a casca visual e a organização dos blocos internos.

**Alternatives considered**:
- Reimplementar todas as páginas a partir de mocks visuais. Rejeitado porque viola o requisito de migrar o conteúdo real existente.
- Manter tabelas e painéis exatamente como estão. Rejeitado porque não entrega a reorganização pedida no spec.

## Decision 5: Priorizar reúso estrito do catálogo `frontend/src/components/ui`

**Decision**: Reutilizar `button`, `input`, `dropdown-menu`, `badge`, `card`, `separator`, `dialog`, `table`, `theme-toggle` e `topbar-user-menu` como base estrutural; extrair subcomponentes do shell apenas quando a composição atual não for suficiente.

**Rationale**: A constituição exige `shadcn/ui` como base técnica e reuso estrito do catálogo local antes de novas peças. O projeto já possui primitives e compostos suficientes para cobrir boa parte do shell. A migração deve começar por composição e refatoração dos componentes existentes, não por criar uma nova biblioteca paralela.

**Alternatives considered**:
- Criar um conjunto novo de componentes de shell do zero. Rejeitado porque duplica primitives já existentes e amplia manutenção.
- Reutilizar `TopbarUserMenu` sem decomposição. Rejeitado como abordagem exclusiva, porque o shell demanda separação mais clara entre header, notificações e menu de conta; a refatoração desse componente continua preferível ao descarte.

## Decision 6: Validar o corte único com foco em regressão funcional e navegabilidade

**Decision**: Tratar a migração como cutover único e validar antecipadamente as rotas críticas, o menu do usuário, notificações, estados de erro/vazio/carregamento e responsividade mínima.

**Rationale**: Como não haverá convivência entre formatos, a proteção principal contra regressão está em testes focados nos fluxos já cobertos pelo frontend e em um roteiro manual curto, objetivo e repetível.

**Alternatives considered**:
- Confiar apenas em validação visual manual. Rejeitado porque é insuficiente para um corte único.
- Adiar validação de acessibilidade e estados para depois da entrega. Rejeitado porque contraria a constituição e aumenta risco de regressão invisível.
