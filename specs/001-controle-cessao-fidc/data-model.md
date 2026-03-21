# Data Model: Controle de Cessao de FIDC

## Cessao

**Description**: Entidade raiz do processo de cessao.

**Fields**:
- `id`: identificador interno
- `businessKey`: identificador de negocio externo unico
- `status`: status atual da cessao
- `workflowInstanceId`: identificador da instancia BPMN
- `cedenteId`: identificador do cedente
- `cessionariaId`: identificador da cessionaria
- `valorCalculado`: valor apurado da operacao
- `valorAprovado`: valor aprovado para continuidade
- `dataImportacao`: data de entrada no sistema
- `dataEncerramento`: data final do fluxo, quando aplicavel

**Validation Rules**:
- `businessKey` MUST ser unico
- `status` MUST refletir etapa coerente do workflow

## EtapaCessao

**Description**: Registro de cada etapa executada ou pendente no fluxo.

**Fields**:
- `id`
- `cessaoId`
- `nomeEtapa`
- `statusEtapa`
- `ordem`
- `responsavelId`
- `inicioEm`
- `concluidaEm`
- `resultado`
- `justificativa`

**Relationships**:
- muitas etapas pertencem a uma cessao

**State Transitions**:
- `PENDENTE -> EM_EXECUCAO -> CONCLUIDA`
- `EM_EXECUCAO -> FALHA`
- `FALHA -> REPROCESSAMENTO`
- `PENDENTE/EM_EXECUCAO -> CANCELADA` quando permitido pelo fluxo

## RegraElegibilidade

**Description**: Resultado de cada validacao de elegibilidade aplicada a cessao.

**Fields**:
- `id`
- `cessaoId`
- `codigoRegra`
- `descricao`
- `resultado`
- `severidade`
- `mensagem`
- `avaliadaEm`

## Contrato

**Description**: Contrato financeiro vinculado a cessao.

**Fields**:
- `id`
- `cessaoId`
- `identificadorExterno`
- `sacadoId`
- `valorNominal`
- `dataOrigem`
- `statusRegistro`

**Validation Rules**:
- `identificadorExterno` MUST ser unico dentro da cessao

## Parcela

**Description**: Parcela vinculada a um contrato da cessao.

**Fields**:
- `id`
- `contratoId`
- `identificadorExterno`
- `numeroParcela`
- `vencimento`
- `valor`
- `statusRegistro`

**Validation Rules**:
- combinacao `contratoId + numeroParcela` MUST ser unica

## Pagamento

**Description**: Registro de liberacao e confirmacao financeira.

**Fields**:
- `id`
- `cessaoId`
- `valor`
- `statusPagamento`
- `autorizadoPor`
- `autorizadoEm`
- `confirmadoEm`
- `comprovanteReferencia`

**State Transitions**:
- `PENDENTE -> LIBERADO -> CONFIRMADO`
- `LIBERADO -> FALHA`

## Lastro

**Description**: Evidencia documental vinculada a cessao, contrato ou parcela.

**Fields**:
- `id`
- `cessaoId`
- `contratoId`
- `parcelaId`
- `tipoDocumento`
- `origem`
- `statusValidacao`
- `recebidoEm`
- `validadoEm`

## OfertaRegistradora

**Description**: Registro das interacoes formais com a registradora.

**Fields**:
- `id`
- `cessaoId`
- `tipoOperacao`
- `requestId`
- `httpStatus`
- `statusNegocio`
- `tentativa`
- `requestPayloadRef`
- `responsePayloadRef`
- `executadaEm`

## TermoAceite

**Description**: Documento formal de aceite gerado no processo.

**Fields**:
- `id`
- `cessaoId`
- `versao`
- `emitidoEm`
- `referenciaDocumento`
- `statusDocumento`

## PerfilAcesso

**Description**: Perfil funcional de autorizacao por etapa.

**Fields**:
- `id`
- `nome`
- `descricao`

## Usuario

**Description**: Ator autenticado do sistema.

**Fields**:
- `id`
- `username`
- `nomeExibicao`
- `ativo`

**Relationships**:
- usuario possui um ou mais perfis

## EventoAuditoria

**Description**: Registro imutavel de acoes relevantes do processo.

**Fields**:
- `id`
- `cessaoId`
- `etapaId`
- `atorId`
- `perfil`
- `tipoEvento`
- `resultado`
- `correlationId`
- `ocorridoEm`
- `detalheRef`

## Read Model Consolidado

**Description**: Projecao consolidada de leitura mantida para operacao e monitoramento.

**Fields**:
- `cessaoBusinessKey`
- `statusAtual`
- `etapaAtual`
- `pendencias`
- `ultimoEvento`
- `ultimaAtualizacao`
- `resumoFinanceiro`
- `resumoDocumental`

## Relationships Summary

- `Cessao 1:N EtapaCessao`
- `Cessao 1:N RegraElegibilidade`
- `Cessao 1:N Contrato`
- `Contrato 1:N Parcela`
- `Cessao 1:N Lastro`
- `Cessao 1:N OfertaRegistradora`
- `Cessao 1:1..N Pagamento`
- `Cessao 1:1..N TermoAceite`
- `Cessao 1:N EventoAuditoria`
