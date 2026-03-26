# Feature Specification: Addon Process SVG no Management Console

**Feature Branch**: `008-management-process-svg`  
**Created**: 2026-03-25  
**Status**: Draft  
**Input**: User description: "adicionar no management console o addon Process SVG para exibicao do fluxo em formato svg e a indicacao de qual etapa o fluxo se encontrada."

## Clarifications

### Session 2026-03-25

- Q: Em quais instâncias o addon `Process SVG` deve aparecer no Management Console? → A: Apenas para instâncias cujos processos já tenham diagrama SVG disponível.
- Q: Como o addon deve se comportar quando o processo já estiver encerrado e não houver etapa ativa? → A: Destacar a última etapa concluída e informar que não há etapa ativa.
- Q: Qual informação deve prevalecer quando houver divergência temporária entre o estado da instância e a correspondência visual do SVG? → A: A etapa mais recente informada pelo estado atual da instância.

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Visualizar Fluxo Atual (Priority: P1)

Como usuário que acompanha uma instância no Management Console, quero abrir um addon de Process SVG para ver o fluxo completo em formato visual e identificar imediatamente em qual etapa o processo está.

**Why this priority**: Essa é a entrega central da feature e gera valor direto ao reduzir ambiguidade na leitura do estado do processo.

**Independent Test**: Pode ser testada abrindo uma instância com fluxo disponível e confirmando que o diagrama SVG aparece com destaque claro da etapa atual.

**Acceptance Scenarios**:

1. **Given** uma instância com diagrama de processo disponível, **When** o usuário abre o addon Process SVG, **Then** o fluxo é exibido em SVG dentro do Management Console.
2. **Given** uma instância em andamento, **When** o addon é carregado, **Then** a etapa atual do fluxo aparece visualmente destacada no diagrama.

---

### User Story 2 - Entender Estado Sem Ambiguidade (Priority: P2)

Como usuário que monitora a execução do processo, quero ver uma indicação textual complementar da etapa atual para confirmar o estado mesmo quando a leitura visual do diagrama não for suficiente.

**Why this priority**: O destaque visual resolve o fluxo principal, mas a indicação textual aumenta confiabilidade e reduz interpretação incorreta.

**Independent Test**: Pode ser testada verificando que o addon mostra o nome da etapa atual em texto consistente com o destaque exibido no diagrama.

**Acceptance Scenarios**:

1. **Given** uma instância com etapa atual conhecida, **When** o addon é exibido, **Then** o nome da etapa atual é mostrado em texto junto ao diagrama.
2. **Given** uma mudança de etapa entre atualizações da instância, **When** o usuário reabre ou atualiza o addon, **Then** a indicação textual reflete a etapa mais recente.

---

### User Story 3 - Receber Feedback Quando o Fluxo Não Estiver Disponível (Priority: P3)

Como usuário do Management Console, quero receber feedback claro quando o diagrama SVG ou a etapa atual não puderem ser determinados, para saber se o problema é de disponibilidade de dados ou de estado do processo.

**Why this priority**: A visualização precisa falhar de forma compreensível para evitar diagnóstico incorreto do processo.

**Independent Test**: Pode ser testada simulando ausência de diagrama, ausência de etapa atual ou falha de carregamento e validando a mensagem exibida.

**Acceptance Scenarios**:

1. **Given** uma instância sem diagrama SVG disponível, **When** o usuário abre o addon, **Then** o sistema informa que o fluxo não pode ser exibido naquele momento.
2. **Given** uma instância sem etapa atual identificável, **When** o addon é carregado, **Then** o sistema mantém o diagrama quando possível e informa que a etapa atual não foi determinada.

---

### Edge Cases

- O processo já terminou e não existe etapa ativa no momento da visualização; nesse caso, o addon deve destacar a última etapa concluída disponível e informar que não há etapa ativa.
- O diagrama SVG existe, mas não há correspondência confiável entre o estado atual e um elemento do fluxo.
- O usuário abre o addon para uma instância inexistente, inacessível ou sem permissão de visualização.
- O fluxo é atualizado entre duas consultas e o destaque visual precisa refletir a etapa mais recente informada pelo estado atual da instância, mesmo durante divergências temporárias.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: O Management Console MUST disponibilizar um addon identificado como `Process SVG` para instâncias de processo elegíveis.
- **FR-001b**: O addon `Process SVG` MUST aparecer apenas para instâncias cujos processos já tenham diagrama SVG disponível.
- **FR-002**: O addon MUST exibir o fluxo do processo em formato SVG quando o diagrama estiver disponível para a instância consultada.
- **FR-003**: O addon MUST destacar visualmente a etapa atual do fluxo quando houver etapa ativa identificável.
- **FR-004**: O addon MUST exibir uma indicação textual da etapa atual em linguagem consistente com o estado mostrado no diagrama.
- **FR-005**: O addon MUST manter coerência entre o destaque visual e a indicação textual da etapa atual.
- **FR-006**: O addon MUST permitir atualização da visualização para refletir o estado mais recente da instância quando o usuário reabrir ou atualizar a área correspondente.
- **FR-006b**: Quando houver divergência temporária entre a correspondência visual do SVG e o estado atual da instância, o addon MUST usar a etapa mais recente informada pelo estado atual da instância como fonte de verdade para o destaque e para a indicação textual.
- **FR-007**: O addon MUST informar de forma clara quando o diagrama SVG não estiver disponível para a instância consultada.
- **FR-008**: O addon MUST informar de forma clara quando a etapa atual não puder ser determinada.
- **FR-008b**: Quando o processo estiver encerrado, o addon MUST destacar a última etapa concluída disponível no fluxo e informar textualmente que não há etapa ativa.
- **FR-009**: O addon MUST impedir exibição de dados de instâncias para usuários sem acesso autorizado no Management Console.
- **FR-010**: O addon MUST preservar a navegação principal do Management Console, sem bloquear outras ações da tela quando a visualização falhar.

### Non-Functional Requirements *(mandatory)*

- **NFR-001**: A solução MUST explicitar quais controles de acesso do Management Console se aplicam à visualização do addon e ao acesso ao diagrama do processo.
- **NFR-002**: A solução MUST registrar evidências suficientes para auditoria de falhas de carregamento do diagrama e divergências entre etapa textual e destaque visual.
- **NFR-003**: A solução MUST definir sinais observáveis para carregamento bem-sucedido, ausência de SVG, ausência de etapa atual e falhas de consulta.
- **NFR-004**: A solução MUST manter responsabilidade clara entre a origem dos dados do processo, a montagem do SVG e a lógica de destaque da etapa atual.
- **NFR-005**: A solução MUST suportar uso repetido por operadores e auditores sem degradação perceptível em fluxos de acompanhamento rotineiro.
- **NFR-006**: A visualização inicial do addon MUST ficar pronta em até 2 segundos para instâncias com diagrama disponível em condições operacionais normais.
- **NFR-007**: A experiência MUST manter leitura simples, com destaque visual claro e rótulo textual da etapa atual sempre visível quando houver dados disponíveis.
- **NFR-008**: A interface MUST preservar hierarquia visual entre título do addon, diagrama do processo, destaque da etapa atual e mensagens de feedback.
- **NFR-009**: A interface MUST usar semântica visual distinta para estado normal, etapa ativa, ausência de dados, aviso e erro.
- **NFR-010**: A solução MUST reutilizar padrões visuais e componentes já aprovados no console antes de introduzir novas variações específicas do addon.
- **NFR-011**: A visualização MUST oferecer nomes acessíveis, foco visível e alternativas textuais suficientes para leitura assistida do estado atual do processo.

### Key Entities *(include if feature involves data)*

- **Instância de Processo**: Representa a execução consultada no Management Console, incluindo seu identificador, estado atual e elegibilidade para visualização do fluxo.
- **Diagrama de Processo SVG**: Representa a versão visual do fluxo exibida no addon, com nós e transições passíveis de destaque.
- **Etapa Atual do Fluxo**: Representa o ponto ativo ou mais recente da instância, usado para destaque visual e indicação textual.
- **Estado de Visualização do Addon**: Representa se o addon está carregando, exibindo diagrama, sem SVG disponível, sem etapa identificada ou em erro.

## Assumptions

- O Management Console já possui contexto suficiente para identificar a instância de processo aberta pelo usuário.
- Existe uma fonte confiável para obter o diagrama SVG do processo ou derivá-lo de artefatos já disponíveis no ambiente.
- A etapa atual pode ser determinada a partir do estado operacional atual da instância ou de seus metadados mais recentes.
- Em caso de divergência temporária entre metadados visuais e estado operacional, o estado atual da instância prevalece como fonte de verdade.
- O addon será aplicado primeiro às instâncias de processo que já possuem representação gráfica do fluxo.
- A elegibilidade visual do addon depende da disponibilidade do SVG; a autorização de acesso aos dados da instância continua sendo uma decisão de segurança separada.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Em pelo menos 95% das instâncias com diagrama disponível, o usuário consegue abrir o addon e ver o fluxo em até 2 segundos.
- **SC-002**: Em pelo menos 95% das instâncias com etapa atual identificável, o destaque visual e a indicação textual apontam para a mesma etapa.
- **SC-003**: Pelo menos 90% dos usuários que acompanham uma instância conseguem identificar a etapa atual do processo na primeira visualização, sem recorrer a outra área do console.
- **SC-004**: Em 100% dos casos sem SVG disponível ou sem etapa identificável, o usuário recebe feedback explícito sobre a limitação encontrada.
