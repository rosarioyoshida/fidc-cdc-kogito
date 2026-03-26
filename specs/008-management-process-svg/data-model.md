# Data Model: Addon Process SVG no Management Console

## 1. ProcessSvgAsset

**Purpose**: Representa o artefato SVG elegivel para um processo do Kogito.

**Fields**:
- `processId`: identificador tecnico do processo, por exemplo `controle-cessao`.
- `classpathLocation`: caminho esperado no build final, `META-INF/processSVG/{processId}.svg`.
- `externalFolderLocation`: caminho opcional apontado por `kogito.svg.folder.path`.
- `available`: indica se o SVG foi resolvido pelo addon.
- `source`: `classpath` ou `filesystem`.

**Rules**:
- O addon so e elegivel para processos cujo `ProcessSvgAsset.available` seja `true`.
- O arquivo precisa seguir o nome `{processId}.svg`.
- `externalFolderLocation` so deve ser usado quando houver necessidade operacional explicita.

## 2. ProcessSvgAddonAvailability

**Purpose**: Define se a instancia aberta no Management Console pode mostrar o addon `Process SVG`.

**Fields**:
- `businessKey`: identificador de negocio da cessao.
- `processId`: identificador do processo associado.
- `processInstanceId`: identificador da instancia no runtime.
- `svgAvailable`: indica se ha SVG resolvido para o `processId`.
- `authorized`: indica se o usuario tem acesso para visualizar o contexto no console.
- `availabilityReason`: motivo tecnico da disponibilidade ou indisponibilidade.

**Rules**:
- O addon deve aparecer apenas quando `svgAvailable=true`.
- Se `authorized=false`, a visualizacao nao pode expor detalhes da instancia.
- `availabilityReason` deve distinguir ao menos `available`, `svg-missing`, `unauthorized` e `instance-not-found`.

## 3. ProcessStageIndicator

**Purpose**: Representa a etapa textual usada como fonte de verdade para o destaque da instancia.

**Fields**:
- `currentStage`: nome tecnico da etapa calculada.
- `displayLabel`: rotulo legivel para exibicao textual.
- `source`: `active-stage`, `last-completed-stage` ou `unresolved`.
- `hasActiveStage`: indica se existe etapa ativa no runtime.
- `processStatus`: estado atual da instancia, por exemplo `EM_ANDAMENTO` ou `CONCLUIDA`.

**Rules**:
- Quando houver etapa em execucao, `source` deve ser `active-stage`.
- Quando o processo estiver encerrado sem etapa ativa, `source` deve ser `last-completed-stage`.
- Quando nao houver etapa determinavel, a interface deve manter feedback textual explicito.

## 4. SvgHighlightProjection

**Purpose**: Representa a projecao visual esperada do caminho executado e do ponto atual no diagrama.

**Fields**:
- `processInstanceId`: instancia usada para calcular o caminho executado.
- `executedPathAvailable`: indica se o addon conseguiu resolver o caminho executado.
- `highlightedStage`: etapa visual que deve ficar em evidencia.
- `divergenceDetected`: indica divergencia entre metadado textual e correspondencia visual do SVG.
- `fallbackApplied`: indica se houve fallback para ultima etapa concluida.

**Rules**:
- Em divergencia temporaria, `highlightedStage` deve seguir a etapa textual mais recente da instancia.
- `fallbackApplied=true` quando o processo estiver encerrado e a ultima etapa concluida for usada como destaque.

## 5. ProcessSvgViewState

**Purpose**: Modela os estados de exibicao do addon no Management Console.

**States**:
- `loading`
- `diagram-ready`
- `no-svg`
- `no-current-stage`
- `unauthorized`
- `error`

**Rules**:
- `diagram-ready` exige `ProcessSvgAsset.available=true`.
- `no-svg` deve informar claramente que o diagrama nao esta disponivel naquele momento.
- `no-current-stage` pode coexistir com o SVG quando o diagrama existe mas a etapa nao e determinavel.
- `error` deve preservar diagnostico observavel sem bloquear a navegacao principal do console.

## 6. ManagementConsoleProcessSvgContext

**Purpose**: Agrega os dados que o Management Console precisa para reconciliar diagrama, etapa textual e elegibilidade.

**Fields**:
- `availability`: instancia de `ProcessSvgAddonAvailability`.
- `stageIndicator`: instancia de `ProcessStageIndicator`.
- `highlightProjection`: instancia de `SvgHighlightProjection`.
- `managementConsoleUrl`: URL base do console de gestao, quando necessaria para diagnostico.
- `dataIndexUrl`: URL do Data Index usada pelo ecossistema do console.

**Rules**:
- O contexto textual local deve prevalecer sobre qualquer interpretacao visual divergente.
- O contexto deve ser auditavel o suficiente para explicar `svg-missing`, `no-current-stage` e `error`.
