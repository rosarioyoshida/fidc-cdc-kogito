# Feature Specification: Controle de Cessao de FIDC

**Feature Branch**: `001-controle-cessao-fidc`  
**Created**: 2026-03-20  
**Status**: Closed  
**Input**: User description: "Crie a especificacao com base no conteudo do arquivo
specs_001-controle-cessao-fidcspec.md"

## Clarifications

### Session 2026-03-20

- Q: Os requisitos regulatorios e de auditoria formal devem ser tratados como obrigatorios desde o inicio? → A: Sim, requisitos regulatorios e auditoria formal sao obrigatorios desde o inicio.
- Q: Como a integracao com a registradora deve ocorrer no fluxo? → A: A integracao com a registradora e sincrona e ocorre por API REST.
- Q: Como o sistema deve tratar falhas na API da registradora? → A: O sistema deve executar retry automatico com limite definido de tentativas antes de escalar.
- Q: Como a unicidade da cessao deve ser garantida? → A: Cada cessao deve ser unica por identificador de negocio externo da operacao.

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Conduzir a cessao ponta a ponta (Priority: P1)

Como operador responsavel pela cessao, quero conduzir cada cessao por um fluxo
controlado do inicio ao encerramento para garantir sequencia correta, rastreabilidade
e previsibilidade operacional.

**Why this priority**: O valor principal do produto e permitir que a cessao seja
governada de ponta a ponta sem perda de controle sobre etapas, dependencias e status.

**Independent Test**: O fluxo pode ser validado criando uma cessao, executando as
etapas obrigatorias na ordem prevista e confirmando o encerramento com historico
completo e status final coerente.

**Acceptance Scenarios**:

1. **Given** uma cessao apta a iniciar processamento, **When** a operacao for iniciada,
   **Then** o sistema registra a cessao no fluxo controlado e posiciona a operacao na
   primeira etapa pendente.
2. **Given** uma cessao em andamento, **When** uma etapa dependente for acionada antes
   da conclusao da etapa anterior, **Then** o sistema bloqueia o avanço e informa a
   dependencia pendente.
3. **Given** uma cessao com todas as confirmacoes obrigatorias concluidas, **When** a
   etapa final for executada, **Then** o sistema encerra a cessao com historico
   completo e status final auditavel.

---

### User Story 2 - Validar elegibilidade, valores e documentos (Priority: P1)

Como analista de negocio ou financeiro, quero validar elegibilidade, calcular o valor
da cessao, registrar documentos e confirmar consistencia dos lastros para reduzir erros
operacionais e garantir aderencia as regras do fundo.

**Why this priority**: Sem validacoes de negocio, calculo financeiro e controle de
documentos, o fluxo nao produz resultado confiavel nem apto a aprovacao.

**Independent Test**: A historia pode ser validada processando uma cessao com
contratos, parcelas e lastros, verificando aprovacao ou rejeicao das regras, valor a
pagar, evidencias registradas e bloqueios quando houver inconsistencias.

**Acceptance Scenarios**:

1. **Given** uma cessao importada, **When** as regras de elegibilidade forem aplicadas,
   **Then** o sistema registra o resultado de cada validacao e interrompe o fluxo para
   analise quando houver reprovacao impeditiva.
2. **Given** uma cessao elegivel, **When** o valor a pagar for apurado, **Then** o
   sistema registra o valor calculado, a base considerada e o resultado aprovado para
   continuidade do fluxo.
3. **Given** lastros recebidos com inconsistencias, **When** a validacao documental for
   concluida, **Then** o sistema impede a aceitacao final da cessao ate regularizacao.

---

### User Story 3 - Aplicar segregacao de funcao e auditoria (Priority: P1)

Como gestor, aprovador ou auditor, quero que cada etapa seja executada apenas por
perfis autorizados e que todo o historico fique disponivel para consulta para garantir
segregacao de funcao, governanca e conformidade.

**Why this priority**: O processo envolve aprovacoes, pagamento, aceite e intercambio
com terceiros; sem autorizacao e auditoria, o risco operacional e regulatorio se torna
inaceitavel.

**Independent Test**: A historia pode ser validada usando usuarios com perfis
distintos, confirmando permissoes corretas, bloqueio de acoes indevidas e consulta
completa do historico da cessao.

**Acceptance Scenarios**:

1. **Given** um usuario sem permissao para uma etapa critica, **When** ele tentar
   executa-la, **Then** o sistema nega a acao e registra a tentativa para auditoria.
2. **Given** uma etapa com aprovacao humana obrigatoria, **When** o usuario autorizado
   concluir sua decisao, **Then** o sistema registra identidade, horario, decisao e
   justificativa quando exigida.
3. **Given** uma cessao encerrada, **When** um auditor autorizado consultar o caso,
   **Then** o sistema apresenta o historico integral das etapas, evidencias, decisoes e
   eventos relevantes.

---

### Edge Cases

- Importacao de cessao duplicada, incompleta ou com dados invalidos
- Falha de comunicacao com a registradora durante etapas obrigatorias
- Esgotamento do limite de retries automaticos na registradora
- Confirmacao parcial de contratos, parcelas ou aceite por parte de terceiros
- Divergencia entre valor calculado, valor aprovado e valor efetivamente pago
- Recebimento tardio, incompleto ou inconsistente de lastros
- Necessidade de reprocessar etapa apos falha tecnica ou rejeicao de negocio
- Tentativa de executar etapa sem perfil autorizado

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: O sistema MUST permitir registrar uma nova cessao para processamento
  controlado.
- **FR-001A**: O sistema MUST garantir unicidade da cessao pelo identificador de
  negocio externo da operacao e bloquear duplicidades.
- **FR-002**: O sistema MUST controlar o fluxo obrigatorio da cessao nas seguintes
  etapas: importar cessao, executar regras de elegibilidade, calcular valor a pagar,
  obter aceite do cedente, criar carteira na registradora, registrar contratos e
  parcelas, realizar oferta na registradora, emitir termo de aceite, liberar
  pagamento, confirmar pagamento, receber lastros, validar lastros, aceitar oferta da
  cessionaria, confirmar aceite pela registradora e encerrar cessao.
- **FR-003**: O sistema MUST impedir avanço para etapas dependentes enquanto
  precondicoes obrigatorias nao forem atendidas.
- **FR-004**: O sistema MUST registrar o status atual da cessao e o historico de
  transicao entre etapas.
- **FR-005**: O sistema MUST aplicar regras de elegibilidade e registrar o resultado de
  cada validacao.
- **FR-006**: O sistema MUST calcular e registrar o valor a pagar da cessao com base
  nas informacoes aceitas para o caso.
- **FR-007**: O sistema MUST registrar aceite do cedente e demais confirmacoes
  obrigatorias com evidencia associada.
- **FR-008**: O sistema MUST registrar contratos, parcelas, ofertas, pagamentos e
  lastros vinculados a cada cessao.
- **FR-008A**: O sistema MUST executar as interacoes com a registradora por API REST
  sincrona nas etapas integradas do fluxo e registrar requisicoes, respostas e
  resultados dessas chamadas.
- **FR-009**: O sistema MUST controlar liberacao e confirmacao de pagamento como
  eventos distintos do fluxo.
- **FR-010**: O sistema MUST validar lastros antes da aceitacao final da cessao quando
  essa validacao for obrigatoria para o caso.
- **FR-011**: O sistema MUST permitir tratar excecoes operacionais com retorno para
  analise, correcao, nova tentativa ou cancelamento controlado.
- **FR-011A**: O sistema MUST executar retries automaticos com limite definido nas
  falhas de chamada a API REST da registradora antes de escalar a ocorrencia para
  tratamento operacional.
- **FR-012**: O sistema MUST restringir a execucao de cada etapa aos perfis autorizados
  para aquela acao.
- **FR-013**: O sistema MUST manter trilha de auditoria de acoes relevantes,
  incluindo ator, etapa, data, decisao e resultado.
- **FR-014**: O sistema MUST permitir consulta do historico completo da cessao,
  incluindo etapas, evidencias, aprovacoes, inconsistencias e interacoes externas.
- **FR-015**: O sistema MUST emitir termo de aceite associado a cessao quando a etapa
  correspondente for concluida.

### Non-Functional Requirements *(mandatory)*

- **NFR-001**: A solucao MUST garantir autenticacao, autorizacao e segregacao de
  funcao compatíveis com a criticidade operacional do processo.
- **NFR-002**: A solucao MUST manter trilha de auditoria suficiente para suporte,
  fiscalizacao e investigacao de eventos relevantes.
- **NFR-003**: A solucao MUST permitir observabilidade operacional do fluxo, incluindo
  identificacao de etapa atual, falhas, reprocessamentos e pendencias.
- **NFR-003A**: A solucao MUST monitorar chamadas sincronas a API REST da registradora,
  incluindo tempo de resposta, falhas, indisponibilidade e evidencias das transacoes.
- **NFR-003B**: A solucao MUST registrar a quantidade de retries automaticos da
  registradora, o esgotamento do limite de tentativas e o resultado final da
  integracao.
- **NFR-004**: A solucao MUST suportar crescimento do volume de cessoes, contratos,
  parcelas e eventos sem perda de rastreabilidade, considerando ao menos o volume
  operacional inicial de implantacao com centenas de contratos por cessao e milhares
  de parcelas agregadas por ciclo operacional.
- **NFR-005**: A solucao MUST permitir consulta do estado atual e do historico da
  cessao em ate 3 segundos e atualizacao da visao consolidada em ate 10 segundos,
  considerando a operacao nominal inicial de implantacao com ao menos 50 cessoes
  ativas simultaneamente e propagacao regular de eventos de processo no ambiente alvo.
- **NFR-006**: A solucao MUST apresentar mensagens de erro e bloqueio compreensiveis
  para usuarios operacionais e de auditoria.
- **NFR-007**: A solucao MUST atender requisitos de conformidade, retencao de
  evidencias e justificativas de aprovacao ou rejeicao aplicaveis ao processo.
- **NFR-008**: A solucao MUST tratar compliance regulatorio e auditoria formal como
  requisitos obrigatorios desde o inicio da feature, incluindo controles, evidencias e
  registros suficientes para fiscalizacao.

### Key Entities *(include if feature involves data)*

- **Cessao**: Operacao de cessao submetida ao fluxo, com identificacao, partes
  envolvidas, status, historico e identificador de negocio externo unico.
- **Etapa de Cessao**: Registro da fase atual ou concluida do fluxo, com resultado,
  responsavel, datas e evidencias.
- **Regra de Elegibilidade**: Regra aplicada a cessao para determinar aptidao,
  pendencia ou rejeicao.
- **Contrato**: Unidade contratual vinculada a cessao e usada no processamento.
- **Parcela**: Unidade financeira derivada de contrato, com valor, vencimento e
  situacao operacional.
- **Pagamento**: Registro de liberacao e confirmacao financeira associado a cessao.
- **Lastro**: Documento ou evidencia que suporta a cessao e sua validacao.
- **OfertaRegistradora**: Manifestacao formal enviada ou recebida nas interacoes da
  cessao com a registradora.
- **Termo de Aceite**: Documento formal emitido para registrar aceite da operacao.
- **Perfil de Acesso**: Conjunto de permissoes que define quais etapas cada usuario
  pode consultar, aprovar ou executar.
- **Evento de Auditoria**: Registro imutavel de acao relevante do fluxo.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: 100% das cessoes processadas pelo sistema possuem historico completo das
  etapas executadas, com status e responsavel auditaveis.
- **SC-002**: 100% das tentativas de executar etapas sem autorizacao sao bloqueadas e
  registradas para auditoria.
- **SC-003**: Usuarios autorizados conseguem consultar o estado atual e o historico de
  uma cessao em ate 3 segundos no volume operacional esperado.
- **SC-004**: 100% das cessoes com inconsistencias impeditivas sao bloqueadas antes da
  aceitacao final ou encerramento.
- **SC-005**: O sistema permite reprocessar etapas elegiveis apos falha tecnica sem
  perda de historico nem inconsistencias de rastreabilidade.
- **SC-006**: Pelo menos 95% das cessoes sem pendencias externas concluem o fluxo sem
  intervencao manual fora das etapas de aprovacao previstas.

## Assumptions

- O processo contempla uma operacao de cessao com quinze etapas obrigatorias e ordem
  controlada.
- Cada cessao possui identificador de negocio externo proprio e unico.
- Existem perfis distintos para operacao, analise, aprovacao, auditoria e integracoes
  externas.
- A integracao com a registradora ocorre de forma sincrona por API REST.
- Evidencias, aprovacoes e documentos relevantes precisam permanecer vinculados ao
  historico da cessao.
- Requisitos regulatorios e auditoria formal fazem parte do escopo obrigatorio da
  solucao desde a primeira versao.

## Out of Scope

- Definicao de arquitetura tecnica, linguagem, framework, banco de dados ou motor
  especifico de orquestracao
- Regras detalhadas de calculo financeiro alem da necessidade de apuracao do valor
- Integracoes com sistemas nao mencionados no fluxo base
- Redesign visual amplo do produto alem da aplicacao dos padroes obrigatorios de UX,
  acessibilidade, semantica de cores e Design System definidos pela constituicao
