# Data Model: Migração do Frontend para Novo Shell Horizontal

## 1. ShellGlobal

**Purpose**: Estrutura compartilhada das páginas autenticadas.

**Fields**:
- `brand`: identificação textual/visual da aplicação exibida no topo.
- `primaryNavigation`: coleção ordenada de destinos principais disponíveis no shell.
- `globalActions`: coleção ordenada de ações globais disponíveis no topo.
- `secondaryNavigation`: configuração contextual específica da rota atual.
- `contentRegions`: conjunto de seções que compõem o corpo da página.
- `footer`: links utilitários e identidade resumida do produto.

**Rules**:
- Deve existir em todas as páginas autenticadas dentro do escopo.
- Deve manter logo à esquerda, navegação principal central e ações do usuário à direita.
- Não pode absorver regras de negócio específicas das features.

## 2. LoginShell

**Purpose**: Estrutura simplificada da tela de login.

**Fields**:
- `contentCard`: bloco central do formulário de autenticação.
- `supportingCopy`: textos de apoio e mensagens de erro.
- `footer`: versão simplificada do rodapé global.

**Rules**:
- Não possui header global.
- Deve preservar o fluxo de autenticação atual.
- Deve seguir a mesma linguagem estrutural do produto.

## 3. NavigationDestination

**Purpose**: Item navegável do topo global ou da barra secundária.

**Fields**:
- `label`: nome exibido ao usuário.
- `href`: destino navegável.
- `scope`: `global` ou `contextual`.
- `active`: indica seleção da rota ou subrota atual.
- `availability`: regra de exibição baseada no contexto da página.

**Rules**:
- Navegação principal só pode conter destinos reais já existentes no produto.
- Navegação contextual só aparece quando aplicável à rota atual.

## 4. GlobalAction

**Purpose**: Ação persistente exibida no topo autenticado.

**Fields**:
- `kind`: destaque, notificações, conta, tema ou equivalente permitido.
- `label`: rótulo acessível.
- `status`: default, pendente, indisponível ou equivalente.
- `interaction`: clique simples, abertura de menu ou diálogo.

**Rules**:
- O bloco de conta deve expor alteração de senha.
- Notificações devem suportar contador visível quando houver pendências.

## 5. SecondaryNavigationModel

**Purpose**: Configuração da barra secundária de cada rota.

**Fields**:
- `contextTabs`: abas ou indicadores de contexto.
- `filters`: filtros disponíveis para a rota.
- `search`: busca local, quando aplicável.
- `selectors`: seletores adicionais, quando aplicáveis.

**Rules**:
- Combina navegação contextual e controles locais na mesma faixa.
- Oculta apenas elementos não aplicáveis; a região continua coerente.

## 6. PageSection

**Purpose**: Agrupamento visual do conteúdo principal.

**Fields**:
- `title`: título curto da seção.
- `description`: apoio opcional para orientar leitura.
- `rows`: coleção de linhas padronizadas.
- `fallbackState`: estado vazio/erro/carregamento, quando necessário.

**Rules**:
- Cada página pode ter múltiplas seções.
- Estados especiais devem manter o contexto da seção ou da página.

## 7. DataRowCard

**Purpose**: Unidade de apresentação repetível usada para listas e agrupamentos comparáveis.

**Fields**:
- `leadingElement`: chip, ícone, ação compacta ou marcador.
- `primaryContent`: identificação principal do item.
- `secondaryContent`: metadados ou apoio.
- `summaryColumns`: dados resumidos comparáveis entre linhas.
- `primaryAction`: ação ou status predominante.
- `secondaryActions`: ações auxiliares alinhadas ao extremo direito.

**Rules**:
- Colunas equivalentes devem manter alinhamento estável dentro da mesma seção.
- Pode representar um item clicável inteiro ou um item com ações localizadas.

## 8. RoutePresentationProfile

**Purpose**: Mapeia cada rota do escopo ao tipo de shell e às regiões esperadas.

**Instances**:
- `home-login`: `LoginShell` com formulário central e rodapé simplificado.
- `cessoes-lista`: `ShellGlobal` com barra secundária, seção de criação e seção de lista.
- `cessao-detalhe`: `ShellGlobal` com barra secundária, resumo/status e seção de etapas.
- `cessao-analise`: `ShellGlobal` com barra secundária, resumo/status e múltiplas seções analíticas.
- `cessao-auditoria`: `ShellGlobal` com barra secundária, contexto da task e linha do tempo auditável.

## 9. State Coverage

**Required states**:
- `loading`
- `empty`
- `error`
- `forbidden`
- `success-feedback`
- `notification-pending`
- `account-menu-open`
- `secondary-navigation-active`

**Rules**:
- Cada estado deve permanecer acessível e compreensível dentro do novo shell.
- Nenhum estado pode remover acesso às ações globais compatíveis com o contexto.
