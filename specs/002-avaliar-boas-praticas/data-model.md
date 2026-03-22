# Data Model: Avaliacao de Boas Praticas do Projeto

## 1. CriterioAvaliacao

- **Purpose**: Representa uma regra objetiva usada para verificar aderencia.
- **Core fields**:
  - `id`: identificador unico do criterio
  - `area`: implementacao, configuracao, seguranca, observabilidade, testes,
    documentacao operacional ou UX
  - `titulo`: nome curto do criterio
  - `descricao`: expectativa normativa do criterio
  - `fonteReferencia`: diretriz do projeto, constituicao ou boa pratica complementar
  - `obrigatoriedade`: obrigatorio ou recomendavel
  - `gatilhoBloqueio`: define se o descumprimento pode gerar bloqueio critico
- **Validation rules**:
  - cada criterio deve apontar para uma fonte de referencia explicita
  - criterios obrigatorios nao podem ficar sem regra de classificacao

## 2. ItemAvaliado

- **Purpose**: Representa o alvo concreto que sera inspecionado na avaliacao.
- **Core fields**:
  - `id`: identificador unico do item
  - `tipo`: codigo, configuracao, teste, contrato, documentacao ou infraestrutura
  - `localizacao`: caminho, modulo, artefato ou contexto observado
  - `descricao`: resumo do que esta sendo avaliado
  - `responsabilidade`: area ou ownership tecnico associado
- **Relationships**:
  - um `ItemAvaliado` pode ser associado a varios `CriterioAvaliacao`
  - um `ItemAvaliado` pode produzir varias `Evidencia`

## 3. Evidencia

- **Purpose**: Registra a base objetiva da conclusao.
- **Core fields**:
  - `id`: identificador unico da evidencia
  - `tipo`: trecho de codigo, arquivo, saida de comando, teste, configuracao ou
    documentacao
  - `origem`: referencia ao artefato ou comando de origem
  - `resumo`: descricao curta do que a evidencia comprova
  - `coletadaEm`: data do ciclo de avaliacao
  - `confiabilidade`: alta, media ou baixa
- **Validation rules**:
  - toda evidencia deve apontar para origem verificavel
  - evidencias com confiabilidade baixa exigem observacao explicita no parecer

## 4. Achado

- **Purpose**: Descreve o resultado consolidado da verificacao de um ou mais criterios.
- **Core fields**:
  - `id`: identificador unico do achado
  - `status`: conforme, parcialmente conforme, nao conforme ou nao verificavel
  - `severidade`: baixa, media, alta ou critica
  - `descricao`: declaracao objetiva do problema ou conformidade
  - `impacto`: consequencia esperada para negocio, operacao ou evolucao
  - `recomendacao`: acao minima esperada para adequacao
  - `bloqueiaProximaFase`: verdadeiro ou falso
- **Relationships**:
  - um `Achado` referencia um ou mais `CriterioAvaliacao`
  - um `Achado` consolida uma ou mais `Evidencia`

## 5. PlanoAdequacao

- **Purpose**: Representa a resposta priorizada aos achados.
- **Core fields**:
  - `id`: identificador unico
  - `prioridade`: imediata, curta, media ou longa
  - `acao`: correcao ou medida recomendada
  - `criterioAceite`: condicao objetiva para considerar o item resolvido
  - `dependencias`: precondicoes conhecidas para executar a acao
  - `prazoIndicativo`: expectativa de janela de tratamento
- **Validation rules**:
  - achados altos ou criticos devem possuir item correspondente no plano

## 6. ParecerProntidao

- **Purpose**: Expressa a classificacao final do ciclo de avaliacao.
- **Core fields**:
  - `id`: identificador unico do parecer
  - `ciclo`: fase ou marco do projeto avaliado
  - `classificacao`: apto, apto com ressalvas ou inapto
  - `resumoExecutivo`: sintese do estado geral
  - `bloqueiosCriticos`: lista de achados que impedem evolucao
  - `ressalvas`: lista de itens altos controlados por plano explicito
  - `emitidoEm`: data de conclusao do parecer
- **Validation rules**:
  - `Apto` nao admite achado alto ou critico em aberto
  - `Apto com ressalvas` admite achado alto apenas com plano explicito
  - `Inapto` e obrigatorio quando existir qualquer bloqueio critico

## Relationships Summary

- `CriterioAvaliacao` 1:N `Achado`
- `ItemAvaliado` 1:N `Evidencia`
- `Achado` N:N `Evidencia`
- `Achado` 1:N `PlanoAdequacao`
- `ParecerProntidao` agrega `Achado` e `PlanoAdequacao`

## Lifecycle

1. Definir ou revisar `CriterioAvaliacao`
2. Selecionar `ItemAvaliado`
3. Coletar `Evidencia`
4. Consolidar `Achado`
5. Montar `PlanoAdequacao`
6. Emitir `ParecerProntidao`
7. Repetir no proximo ciclo e comparar com o anterior
