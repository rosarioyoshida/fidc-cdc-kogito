# Atendimento N1 para incidentes de fluxo de cessao

## Objetivo

Padronizar o atendimento N1 para ocorrencias no fluxo operacional de cessao com foco em qualidade, assertividade e escalonamento rapido quando houver falha tecnica.

## Resultado esperado para o N1

Ao final do atendimento, o analista N1 deve conseguir:

- confirmar se a cessao existe e qual `businessKey` esta sendo tratada
- identificar se a instancia do processo esta `ACTIVE`, `COMPLETED` ou `ERROR`
- localizar a etapa atual ou a etapa em que houve falha
- distinguir se o caso e de fila operacional, espera de timer, falha tecnica ou erro de modelagem/processo
- registrar uma descricao objetiva do incidente para N2 sem depender de interpretacao subjetiva

## Dados minimos que devem ser coletados

- `businessKey`
- `processInstanceId`
- usuario que executou a ultima acao
- data/hora aproximada da falha
- etapa informada pelo usuario
- tela ou console em que o erro apareceu
- mensagem exibida na interface, se houver

## Roteiro de troubleshooting N1

### 1. Confirmar a cessao

- consultar a cessao pelo `businessKey`
- confirmar se o `workflowInstanceId` esta preenchido
- confirmar se a cessao ainda esta em andamento, concluida ou em erro

### 2. Consultar a instancia no Management Console

- abrir a instancia pelo `processInstanceId`
- verificar o `status` da instancia
- verificar se o diagrama carrega
- verificar se ha indicacao de etapa atual ou de erro

Classificacao inicial:

- `ACTIVE` com task pendente: fluxo operacional em andamento
- `ACTIVE` sem task pendente: pode ser espera de timer/job ou falha de materializacao
- `ERROR`: tratar como incidente tecnico ate que a causa seja classificada
- `COMPLETED`: fluxo finalizado; validar se a expectativa do usuario era realmente uma nova task

### 3. Consultar o Task Console

- abrir o inbox do usuario operacional esperado
- validar se existe task `Ready`
- validar grupo potencial e usuario potencial quando a task existir

Interpretacao:

- task `Ready` visivel: encaminhar para execucao operacional
- sem task e processo `ACTIVE`: verificar se a etapa atual e automatica ou espera de timer
- sem task e processo `ERROR`: escalar com detalhes do erro

### 4. Validar o Data Index

- consultar `ProcessInstances` por `businessKey`
- consultar `UserTaskInstances` por `processInstanceId`
- confirmar se o processo existe no GraphQL
- confirmar se existe task `Ready`, `Completed` ou nenhuma task nova

Perguntas que o N1 precisa responder:

- a instancia ainda existe no Data Index?
- o processo mudou para `ERROR`?
- a ultima human task foi concluida?
- a proxima task humana foi materializada?

### 5. Validar se o problema e operacional ou tecnico

Sinais de problema operacional:

- task existe no inbox correto
- processo esta `ACTIVE`
- nao ha erro tecnico no console
- usuario esta sem permissao ou executando na etapa errada

Sinais de problema tecnico:

- processo em `ERROR`
- detalhe da pagina nao carrega
- task nao materializa no Data Index apos uma transicao valida
- endpoint do processo ou da task nao responde
- GraphQL retorna a instancia sem proxima task quando deveria existir uma

### 6. Classificar a causa provavel

Usar uma classificacao simples no registro do incidente:

- `PERMISSAO_OPERACIONAL`
- `DADO_INVALIDO`
- `INTEGRACAO_EXTERNA`
- `TIMER_JOB`
- `DATA_INDEX_MATERIALIZACAO`
- `RUNTIME_BPMN`
- `ERRO_DESCONHECIDO`

### 7. Escalonar para N2 com registro objetivo

O repasse para N2 deve conter:

- `businessKey`
- `processInstanceId`
- etapa em que falhou
- status da instancia no Management Console
- status da task no Task Console
- evidencias do GraphQL do Data Index
- mensagem exibida ao usuario
- classificacao da causa provavel
- impacto operacional

## Mensagem padrao de repasse para N2

```text
Incidente no fluxo de cessao.
BusinessKey: <businessKey>
ProcessInstanceId: <processInstanceId>
Etapa observada: <etapa>
Status no Management Console: <status>
Status no Task Console: <status ou sem task>
Evidencia no Data Index: <resumo objetivo>
Mensagem exibida: <mensagem>
Classificacao inicial: <categoria>
Impacto: <o usuario nao consegue concluir a operacao / fila parada / processo em erro>
```

## Campos desejados para evolucao futura da observabilidade

Para melhorar a autonomia do N1, a tela de detalhe do processo deve futuramente exibir:

- `errorCategory`
- `errorCode`
- `errorMessage`
- `failedNodeId`
- `failedNodeName`
- `failedAt`
- `retryable`
- `supportAction`

Exemplo desejado:

- etapa com falha: `CONSOLIDAR_CONTRATOS`
- categoria: `RUNTIME_BPMN`
- mensagem: `acao automatica nao configurada no runtime`
- acao recomendada: `escalar para N2/BPM`

## Critério de qualidade para o atendimento N1

Um atendimento N1 e considerado assertivo quando:

- identifica corretamente se o fluxo esta em andamento, aguardando timer ou em erro
- registra `businessKey` e `processInstanceId`
- informa a etapa exata da falha
- separa problema operacional de problema tecnico
- escala para N2 com evidencias objetivas e sem narrativa vaga
