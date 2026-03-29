# Data Model: Revisao de Javadoc do Backend

## Overview

Esta feature nao introduz entidades persistidas. O modelo abaixo representa os
artefatos operacionais usados para planejar, executar e validar a conformidade
documental do backend.

## Entities

### InventarioDeConformidade

- **Purpose**: Representar o universo de classes Java de producao em escopo e o
  estado atual de aderencia documental.
- **Fields**:
  - `sourcePath`: caminho relativo da classe em `backend/src/main/java`
  - `packageName`: pacote Java ao qual a classe pertence
  - `className`: nome simples da classe
  - `originType`: `manual` ou `generated`
  - `currentStatus`: `missing`, `partial`, `compliant`
  - `issues`: lista de lacunas encontradas no contrato documental
  - `packageInfoStatus`: `missing`, `partial`, `compliant`
- **Validation Rules**:
  - Cada classe versionada em `src/main/java` deve aparecer exatamente uma vez.
  - `originType` nao altera obrigatoriedade de remediacao.
  - `currentStatus = compliant` exige ausencia de lacunas bloqueantes.
- **Final Snapshot**:
  - `totalClasses = 115`
  - `compliantClasses = 115`
  - `missingClasses = 0`
  - `partialClasses = 0`

### ContratoDeJavadoc

- **Purpose**: Definir o conjunto minimo que torna uma classe ou metodo conforme
  ao contrato de API exigido pela constituicao.
- **Fields**:
  - `summarySentencePresent`
  - `behaviorDescriptionPresent`
  - `nullabilityCovered`
  - `limitsAndCornerCasesCovered`
  - `sideEffectsCovered`
  - `threadSafetyCoveredWhenRelevant`
  - `tagsCovered`: subconjunto de `param`, `paramType`, `return`, `throws`, `deprecated`, `inheritDoc`
  - `internalDetailsLeaked`: boolean
- **Validation Rules**:
  - A primeira frase deve ser independente e coerente com o tipo documentado.
  - `internalDetailsLeaked` deve ser `false` para conformidade.
  - Tags so sao obrigatorias quando a assinatura/contrato as exigir.
- **Feature Interpretation**:
  - Nesta entrega, o gate automatizado valida obrigatoriamente Javadocs de tipo
    e a integridade sintatica dos comentarios gerados.
  - Tags adicionais continuam obrigatorias quando forem explicitamente usadas ou
    quando o contrato do tipo exigir complementacao manual futura.

### ContratoDePacote

- **Purpose**: Registrar a documentacao arquitetural obrigatoria em
  `package-info.java`.
- **Fields**:
  - `packageName`
  - `purposeSummary`
  - `architecturalRole`
  - `classRelationships`
  - `externalLinks`
- **Validation Rules**:
  - Cada pacote em escopo deve possuir exatamente um contrato de pacote.
  - `purposeSummary` e `architecturalRole` sao obrigatorios.
- **Final Snapshot**:
  - `totalPackages = 34`
  - `compliantPackages = 34`
  - `missingPackages = 0`

### EvidenciaDeValidacao

- **Purpose**: Consolidar a prova de geracao e verificacao da documentacao.
- **Fields**:
  - `inventorySnapshot`
  - `generationCommand`
  - `validationCommand`
  - `resultStatus`: `pass`, `fail`
  - `blockingFindings`
  - `reviewArtifactLocation`
- **Validation Rules**:
  - `resultStatus = pass` exige zero erros e zero avisos relevantes.
  - Deve apontar para artefato revisavel da documentacao gerada.
- **Final Snapshot**:
  - `generationCommand = mvn -DskipTests javadoc:javadoc`
  - `resultStatus = pass`
  - `reviewArtifactLocation = backend/target/site/apidocs/index.html`

## Relationships

- `InventarioDeConformidade` referencia um `ContratoDeJavadoc` por classe.
- `InventarioDeConformidade` referencia um `ContratoDePacote` pelo `packageName`.
- `EvidenciaDeValidacao` consolida o estado final de todos os itens do
  `InventarioDeConformidade`.

## State Transitions

### InventarioDeConformidade.currentStatus

`missing` → `partial` → `compliant`

- `missing`: classe sem Javadoc obrigatorio ou sem cobertura minima de contrato.
- `partial`: classe documentada, mas com lacunas, tags ausentes ou falhas de qualidade.
- `compliant`: classe aderente e sem findings bloqueantes.

### ContratoDePacote.packageInfoStatus

`missing` → `partial` → `compliant`

- `missing`: pacote sem `package-info.java`.
- `partial`: arquivo existe, mas sem contexto arquitetural suficiente.
- `compliant`: arquivo cobre resumo, papel arquitetural e relacoes relevantes.

## Review Matrix

| Item | Missing | Partial | Compliant |
|------|---------|---------|-----------|
| Classe Java | Sem Javadoc de tipo | Possui resumo, mas sem contexto contratual minimo | Possui resumo, descricao curta e nao expoe detalhes internos acidentais |
| `package-info.java` | Ausente no pacote | Existe, mas sem papel arquitetural claro | Resume o pacote e posiciona a responsabilidade da camada |
| Evidencia de validacao | Comando nao executado | Execucao sem consolidacao final | Comando executado com `BUILD SUCCESS` e HTML revisavel |
