# UI Contract: Migração para Novo Shell Horizontal

## 1. Scope

Este contrato define o comportamento obrigatório da casca visual compartilhada e a forma como as páginas existentes devem ser migradas para ela sem perder conteúdo, ações ou contexto.

Rotas cobertas no primeiro corte:
- `/`
- `/cessoes`
- `/cessoes/[businessKey]`
- `/cessoes/[businessKey]/analise`
- `/cessoes/[businessKey]/auditoria`

## 2. Shared Regions

### 2.1 Authenticated Shell

Toda rota autenticada do escopo deve expor:
- header de largura total;
- logo na extremidade esquerda;
- navegação principal horizontal no centro;
- área de ações do usuário na extremidade direita;
- barra secundária logo abaixo do header;
- conteúdo principal centralizado em container largo;
- rodapé padronizado.

### 2.2 Login Shell

A rota `/` deve expor:
- conteúdo principal focado no formulário de login;
- linguagem estrutural compatível com o produto;
- rodapé simplificado;
- ausência de header global.

## 3. Global Header Contract

O header autenticado deve conter:
- logo;
- destinos principais reais do produto, sem placeholders;
- um botão de destaque;
- controle de notificações com contador quando houver pendências;
- bloco de usuário com avatar e indicador de abertura de menu.

O menu do usuário deve conter no mínimo:
- identificação do usuário;
- ação de alteração de senha;
- ações adicionais de conta que permaneçam válidas;
- ação de saída.

## 4. Secondary Navigation Contract

A barra secundária deve:
- combinar navegação contextual da rota atual com filtros, seletores e busca locais;
- deixar o contexto ativo explícito;
- ocultar apenas controles não aplicáveis;
- preservar ações locais relevantes já existentes na página atual.

## 5. Content Migration Contract by Route

### 5.1 `/`

Migrar o `LoginPanel` atual para o `LoginShell`, preservando:
- campos de usuário e senha;
- submissão do formulário;
- mensagens de erro;
- textos de orientação já existentes.

### 5.2 `/cessoes`

Migrar a página de lista para:
- seção de entrada/ação para registrar nova cessão;
- seção principal de listagem em linhas horizontais padronizadas;
- estados vazio e erro já existentes;
- navegação para detalhe por item.

### 5.3 `/cessoes/[businessKey]`

Migrar a página de detalhe para:
- resumo principal da cessão dentro do shell;
- barra contextual com destinos relacionados e ações locais;
- apresentação das etapas operacionais com alinhamento consistente;
- preservação de permissões e ação de avançar etapa.

### 5.4 `/cessoes/[businessKey]/analise`

Migrar o dashboard de análise para:
- resumo da cessão e status;
- barra contextual com retorno e ações locais;
- múltiplas seções organizadas para elegibilidade, cálculo, contratos, lastro e registradora;
- preservação de mensagens de sucesso e erro.

### 5.5 `/cessoes/[businessKey]/auditoria`

Migrar a página de auditoria para:
- barra contextual coerente com o contexto da cessão;
- seção de contexto operacional;
- linha do tempo auditável em estrutura compatível com as linhas padronizadas;
- preservação de navegação de retorno e estados vazios.

## 6. Reuse and Component Governance

Antes de criar novos componentes, a implementação deve validar reúso de:
- `button.tsx`
- `input.tsx`
- `dropdown-menu.tsx`
- `badge.tsx`
- `card.tsx`
- `separator.tsx`
- `dialog.tsx`
- `table.tsx`
- `theme-toggle.tsx`
- `topbar-user-menu.tsx`
- `account-settings-dialog.tsx`

Novos componentes só são aceitáveis quando:
- a composição dos componentes acima não cobre a necessidade funcional;
- a justificativa registrar lacuna, impacto de manutenção e cobertura de estados;
- a nova peça nascer com estados e acessibilidade proporcionais ao uso.

Exceções formalmente aprovadas neste corte:
- `frontend/src/components/layout/app-shell.tsx`
- `frontend/src/components/layout/app-header.tsx`
- `frontend/src/components/layout/app-footer.tsx`
- `frontend/src/components/layout/secondary-nav.tsx`
- `frontend/src/components/layout/page-section.tsx`
- `frontend/src/components/layout/data-row-card.tsx`

## 7. State and Accessibility Contract

O shell e as páginas migradas devem preservar:
- navegação por teclado;
- foco visível;
- nomes acessíveis em controles globais e locais;
- estados de loading, empty, error e forbidden;
- feedback de sucesso quando já existir no fluxo;
- comportamento responsivo mínimo para larguras reduzidas.

## 8. Cutover Contract

A migração entra em corte único para o escopo definido.

Condições mínimas para ativação:
- todas as rotas do escopo renderizam no novo formato;
- ações críticas continuam acessíveis;
- menu do usuário e alteração de senha continuam funcionando;
- estados críticos permanecem visíveis e compreensíveis;
- não existem placeholders navegáveis nem dependência do shell antigo nas rotas cobertas.
