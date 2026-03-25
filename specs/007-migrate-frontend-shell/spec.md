# Feature Specification: Migração do Frontend para Novo Shell Horizontal

**Feature Branch**: `007-migrate-frontend-shell`  
**Created**: 2026-03-25  
**Status**: Draft  
**Input**: User description: "Migre a interface atual do frontend para uma interface estruturada como um painel web com forte organização horizontal, com header global de largura total, navegação principal central, ações do usuário no topo, barra secundária contextual, conteúdo em seções com linhas horizontais e reaproveitamento do conteúdo já existente das páginas atuais."

## Clarifications

### Session 2026-03-25

- Q: O novo shell deve cobrir apenas a experiência autenticada ou também a tela de login? → A: A tela de login também será migrada, com rodapé simplificado e sem header global.
- Q: O que a navegação principal deve exibir nesta primeira migração? → A: A navegação principal exibe apenas destinos globais reais do produto, como a lista de cessões; análise e auditoria aparecem apenas como destinos contextuais nas rotas aplicáveis.
- Q: Qual é o papel da barra secundária na primeira migração? → A: Combinar navegação contextual da rota atual com filtros, seletores e busca locais.
- Q: A migração entra gradualmente por rota ou em corte único? → A: A migração entra em corte único, sem convivência entre formatos.

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Navegar pelo produto em um shell unificado (Priority: P1)

Como usuário autenticado, quero acessar as páginas principais dentro de uma estrutura visual única e previsível para me orientar rapidamente, alternar entre contextos e encontrar ações globais sem reaprender a interface em cada rota.

**Why this priority**: A navegação e a orientação global são a base da experiência. Sem um shell consistente, a migração visual não entrega valor perceptível nem reduz esforço de uso.

**Independent Test**: Pode ser testada acessando as páginas principais do frontend autenticado e confirmando que todas exibem o mesmo topo global, navegação principal, menu de usuário e rodapé, preservando o acesso ao conteúdo já existente.

**Acceptance Scenarios**:

1. **Given** que o usuário autenticado acessa qualquer página principal do frontend, **When** a página é carregada, **Then** ele vê o mesmo header global ocupando toda a largura da viewport com logo, navegação principal e ações do usuário.
2. **Given** que o usuário autenticado navega entre a lista principal, detalhe, análise e auditoria, **When** muda de rota, **Then** o shell global permanece consistente e o conteúdo específico da rota é exibido abaixo da navegação contextual.
3. **Given** que o usuário está em uma página autenticada, **When** precisa acessar ações de conta, **Then** ele pode abrir o menu do avatar e encontrar pelo menos a opção de alterar senha.
4. **Given** que um usuário acessa a tela de login, **When** a página é carregada, **Then** ela usa a nova linguagem estrutural da aplicação com rodapé simplificado e sem header global.
5. **Given** que a navegação principal é exibida, **When** o usuário avalia os destinos disponíveis, **Then** ele encontra apenas destinos já existentes e relevantes para o contexto atual do produto.

---

### User Story 2 - Encontrar contexto e ações locais com rapidez (Priority: P2)

Como usuário autenticado, quero ver filtros, abas e atalhos da página atual em uma faixa contextual dedicada para identificar o estado da tela e executar ações locais sem procurar controles espalhados pelo conteúdo.

**Why this priority**: A barra contextual organiza navegação local e reduz a dispersão de filtros e atalhos, melhorando a leitura e a eficiência nas tarefas mais frequentes.

**Independent Test**: Pode ser testada verificando se cada rota relevante expõe sua navegação contextual e seus controles locais em uma segunda faixa horizontal abaixo do header global, sem remover capacidades que já existiam.

**Acceptance Scenarios**:

1. **Given** que o usuário acessa uma página com múltiplas visões ou estados, **When** a interface é exibida, **Then** as abas ou indicadores de contexto aparecem em uma barra secundária dedicada abaixo do header.
2. **Given** que a página possui busca, filtros ou seletores já existentes, **When** o usuário visualiza a barra secundária, **Then** esses controles aparecem organizados nela e continuam funcionando.
3. **Given** que uma página não possui todos os tipos de controles contextuais, **When** a barra secundária é exibida, **Then** ela mostra apenas os elementos aplicáveis sem criar espaços vazios ou ações irrelevantes.
4. **Given** que a rota possui subáreas navegáveis e controles locais, **When** a barra secundária é renderizada, **Then** ela reúne ambos os elementos na mesma faixa contextual de forma consistente.

---

### User Story 3 - Consumir listas e blocos de trabalho em linhas padronizadas (Priority: P3)

Como usuário autenticado, quero que o conteúdo das páginas seja reorganizado em seções com linhas horizontais padronizadas para comparar informações, localizar status e agir sobre itens de forma mais direta.

**Why this priority**: A reorganização do conteúdo é o principal ganho visual da migração, mas depende do shell e da navegação contextual para fazer sentido no fluxo completo.

**Independent Test**: Pode ser testada validando que listas, painéis e agrupamentos existentes foram migrados para seções com títulos e linhas horizontais alinhadas, mantendo textos, dados, estados e ações que já estavam disponíveis.

**Acceptance Scenarios**:

1. **Given** que uma página exibe coleções, painéis ou blocos operacionais, **When** o conteúdo é renderizado no novo formato, **Then** os itens aparecem em seções verticais com títulos curtos e linhas horizontais consistentes.
2. **Given** que um item possui ação principal, status e ações secundárias, **When** ele é apresentado em uma linha, **Then** esses elementos aparecem em posições previsíveis e estáveis ao longo da lista.
3. **Given** que o usuário compara múltiplos itens na mesma seção, **When** percorre as linhas, **Then** as colunas permanecem alinhadas para facilitar leitura e comparação.

### Edge Cases

- O sistema deve manter a navegação funcional quando uma página tiver conteúdo insuficiente para preencher uma linha completa de dados ou não possuir filtros locais.
- O sistema deve preservar mensagens de erro, vazio, carregamento e acesso negado dentro do novo shell sem ocultar o contexto da página.
- O sistema deve se adaptar a nomes longos, identificadores extensos e ações múltiplas sem quebrar o alinhamento principal nem impedir o acesso aos controles.
- O menu do usuário e os controles globais devem continuar acessíveis em larguras menores, com reorganização responsiva que preserve o acesso às ações prioritárias.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: O sistema MUST aplicar um shell global único às páginas autenticadas do frontend, com header de largura total, área principal centralizada e rodapé padronizado.
- **FR-001b**: O sistema MUST migrar a tela de login para a nova linguagem estrutural da aplicação, preservando seu fluxo atual e adotando rodapé simplificado sem header global.
- **FR-002**: O sistema MUST exibir no header global o logo da aplicação, uma navegação principal horizontal e uma área de ações do usuário visualmente separada do conteúdo da página.
- **FR-002b**: O sistema MUST compor a navegação principal apenas com destinos existentes e relevantes para a primeira entrega da migração, sem introduzir áreas fictícias ou placeholders navegáveis.
- **FR-003**: O sistema MUST disponibilizar na área de ações do usuário um botão de destaque, um indicador de notificações com contador visível quando houver pendências e um bloco de conta com avatar e indicador de menu expansível.
- **FR-004**: O sistema MUST abrir um menu de conta ao interagir com o avatar e incluir, no mínimo, a ação de alterar senha além das ações de conta já existentes que continuarem válidas.
- **FR-005**: O sistema MUST posicionar abaixo do header uma barra secundária de contexto para exibir navegação local, estado da tela, filtros, busca e seletores aplicáveis à rota atual.
- **FR-005b**: O sistema MUST tratar a barra secundária como o ponto padrão para combinar navegação contextual da rota atual com filtros, seletores e busca locais, salvo quando algum desses elementos não se aplicar.
- **FR-006**: O sistema MUST migrar o conteúdo hoje exibido nas páginas autenticadas existentes para o novo shell sem remover dados, textos, regras de acesso, mensagens, fluxos ou ações já disponíveis ao usuário.
- **FR-007**: O sistema MUST reorganizar listas, painéis e agrupamentos existentes em seções com títulos curtos e linhas horizontais padronizadas sempre que a natureza do conteúdo permitir comparação ou ação por item.
- **FR-008**: O sistema MUST apresentar cada linha padronizada com uma sequência consistente de áreas de informação, contemplando elemento inicial compacto, informação principal e secundária, dados resumidos, status ou ação principal e ações secundárias no extremo direito quando aplicável.
- **FR-009**: O sistema MUST manter alinhamento consistente entre linhas equivalentes da mesma seção para facilitar leitura comparativa e varredura visual.
- **FR-010**: O sistema MUST preservar as rotas atuais sempre que possível, tratando a mudança como migração de casca visual e organização da informação, não como substituição funcional das páginas.
- **FR-010b**: O sistema MUST entregar o novo formato como substituição única e consistente das rotas incluídas no escopo da migração, sem manter convivência operacional entre shell antigo e novo nessas rotas.
- **FR-011**: O sistema MUST acomodar estados de carregamento, vazio, erro e falta de permissão dentro do novo formato sem perder contexto de navegação ou acesso às ações globais compatíveis.
- **FR-012**: O sistema MUST permitir que páginas com necessidades distintas reutilizem a mesma estrutura base e ativem apenas os elementos contextuais relevantes para cada rota.

### Non-Functional Requirements *(mandatory)*

- **NFR-001**: A solução MUST documentar como o novo shell preserva controles de autenticação, ações de conta e exposição mínima de dados pessoais no topo da interface.
- **NFR-002**: A solução MUST documentar quais eventos de navegação, troca de contexto e ações de conta precisam continuar auditáveis após a migração visual.
- **NFR-003**: A solução MUST definir quais sinais operacionais permitem identificar falhas de carregamento do shell, indisponibilidade de navegação principal e erros em controles contextuais.
- **NFR-004**: A solução MUST definir componentes e regiões reutilizáveis suficientes para evitar que cada página recrie seu próprio topo, barra contextual, linhas de conteúdo ou rodapé.
- **NFR-005**: A solução MUST acomodar crescimento de novas páginas e novas seções sem exigir reestruturação completa do shell global.
- **NFR-006**: A navegação entre páginas autenticadas e a abertura dos controles globais MUST ocorrer dentro das expectativas normais de uma aplicação web interativa, sem atrasos perceptíveis que prejudiquem a tarefa principal.
- **NFR-007**: A interface MUST manter hierarquia visual clara entre navegação global, navegação contextual, conteúdo principal e ações secundárias.
- **NFR-008**: A interface MUST usar padrões consistentes de espaçamento, repetição estrutural e agrupamento para que usuários reconheçam rapidamente o papel de cada região da tela.
- **NFR-009**: A interface MUST comunicar estados, pendências, sucesso e risco com sinais visuais semanticamente consistentes em todo o shell e nas linhas de conteúdo.
- **NFR-010**: A solução MUST reutilizar ao máximo o catálogo existente de componentes e padrões visuais do frontend antes de introduzir novas variações.
- **NFR-010b**: Toda exceção ao reúso de componentes existentes MUST registrar a lacuna funcional encontrada, o impacto de manutenção e a justificativa para a nova variação.
- **NFR-011**: A experiência MUST ser navegável por teclado, expor foco visível e manter nomes acessíveis para navegação, menus, busca, notificações e ações por item.
- **NFR-012**: A solução MUST identificar uma única fonte de verdade para estrutura do shell, navegação, padrões visuais e composição das regiões compartilhadas do frontend.
- **NFR-013**: Controles interativos do shell e das linhas padronizadas MUST definir estados de repouso, foco, abertura, indisponibilidade e resposta a interação de maneira consistente.
- **NFR-014**: Quando a página expuser coleções navegáveis ou conjuntos filtráveis, a apresentação MUST manter nomenclatura e organização compreensíveis para usuários de negócio.
- **NFR-015**: Listas e seções com filtros MUST deixar claro quais critérios estão ativos e como o usuário retorna para uma visão mais ampla do conjunto.
- **NFR-016**: Mudanças no shell compartilhado MUST preservar compatibilidade visual e comportamental suficiente para que todas as rotas incluídas no escopo entrem no novo formato no mesmo corte sem regressão funcional.
- **NFR-017**: A migração MUST definir um critério de prontidão que assegure consistência de navegação, conteúdo e ações antes da ativação única do novo shell.
- **NFR-018**: A navegação global e contextual MUST deixar explícito onde o usuário está, quais destinos relacionados existem e quais ações seguintes são esperadas.
- **NFR-019**: Mensagens de erro apresentadas dentro do shell MUST ser compreensíveis, orientadas à ação e não expor detalhes internos desnecessários.
- **NFR-020**: O shell MUST manter comportamento responsivo suficiente para preservar navegação, menu do usuário, notificações e ações prioritárias em larguras reduzidas.

### Key Entities *(include if feature involves data)*

- **Shell Global**: Estrutura compartilhada que contém navegação principal, ações do usuário, barra contextual, área de conteúdo e rodapé.
- **Destino de Navegação**: Entrada acessível a partir da navegação principal ou contextual, com rótulo, estado ativo e destino associado.
- **Ação Global**: Controle disponível no topo da aplicação, incluindo notificações e ações de conta, com disponibilidade condicionada ao contexto do usuário.
- **Seção de Página**: Agrupamento de conteúdo com título curto e conjunto de itens relacionados exibidos em formato padronizado.
- **Linha de Conteúdo**: Unidade visual repetível que reúne informações principais, informações secundárias, status e ações referentes a um item do processo.

## Assumptions

- A migração abrange prioritariamente as páginas autenticadas já existentes no frontend atual e inclui a tela de login em formato simplificado.
- A página inicial de autenticação mantém necessidades próprias, sem header global, mas deve seguir a mesma linguagem estrutural geral e o rodapé simplificado definido para a migração.
- As rotas atuais de lista, detalhe, análise e auditoria continuam sendo os principais destinos a migrar na primeira entrega.
- Os dados e ações já consumidos pelas páginas atuais permanecem válidos e devem ser reaproveitados no novo formato.
- Quando um conteúdo não se adequar a uma linha horizontal única, ele poderá ser dividido em múltiplas seções mantendo a mesma linguagem estrutural.
- A ativação do novo shell ocorrerá em corte único para o escopo definido, sem operação prolongada de dois formatos concorrentes nas rotas migradas.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Em validação funcional, 100% das páginas autenticadas priorizadas exibem o shell global compartilhado com header, barra contextual aplicável e rodapé padronizado, e a tela de login exibe o formato simplificado definido para ela.
- **SC-002**: Em validação de regressão, 100% das ações críticas já disponíveis nas páginas migradas continuam acessíveis e executáveis após a reorganização visual.
- **SC-003**: Pelo menos 90% dos cenários primários de navegação entre lista, detalhe, análise e auditoria são concluídos pelos avaliadores sem necessidade de orientação adicional sobre onde encontrar a próxima ação.
- **SC-004**: Em revisão comparativa, pelo menos 90% dos itens exibidos em listas ou agrupamentos migrados apresentam alinhamento consistente suficiente para leitura rápida e comparação entre linhas.
