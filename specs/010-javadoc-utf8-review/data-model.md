# Data Model: Revisao de Javadoc UTF-8 do Backend

## Overview

Esta feature nao introduz entidades persistidas. O modelo abaixo representa os
artefatos operacionais usados para planejar, executar e validar a conformidade
documental e de encoding do backend.

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
  - `encodingStatus`: `unknown`, `invalid`, `utf8`
- **Validation Rules**:
  - Cada classe versionada em `src/main/java` deve aparecer exatamente uma vez.
  - `originType` nao altera obrigatoriedade de remediacao.
  - `currentStatus = compliant` exige ausencia de lacunas bloqueantes.
  - `encodingStatus = utf8` e obrigatorio para conformidade final.
- **Final Snapshot Target**:
  - `totalClasses = 115`
  - `totalPackages = 34`

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
  - Tags so sao obrigatorias quando a assinatura ou o contrato as exigir.

### ContratoDePacote

- **Purpose**: Registrar a documentacao arquitetural obrigatoria em
  `package-info.java`.
- **Fields**:
  - `packageName`
  - `purposeSummary`
  - `architecturalRole`
  - `classRelationships`
  - `externalLinks`
  - `encodingStatus`
- **Validation Rules**:
  - Cada pacote em escopo deve possuir exatamente um contrato de pacote.
  - `purposeSummary` e `architecturalRole` sao obrigatorios.
  - `encodingStatus = utf8` e obrigatorio para conformidade final.

### PoliticaDeEncoding

- **Purpose**: Definir a codificacao canonica exigida para fontes, comentarios e
  artefatos gerados pelo fluxo documental.
- **Fields**:
  - `sourceEncoding`
  - `docEncoding`
  - `htmlCharset`
  - `validationStatus`
- **Validation Rules**:
  - Todos os campos devem apontar para UTF-8.
  - `validationStatus = pass` exige ausencia de corrupcao de caracteres no HTML revisavel.

### EvidenciaDeValidacao

- **Purpose**: Consolidar a prova de geracao e verificacao da documentacao.
- **Fields**:
  - `inventorySnapshot`
  - `generationCommand`
  - `validationCommand`
  - `resultStatus`: `pass`, `fail`
  - `blockingFindings`
  - `reviewArtifactLocation`
  - `encodingReviewStatus`
- **Validation Rules**:
  - `resultStatus = pass` exige zero erros e zero avisos relevantes.
  - `encodingReviewStatus = pass` exige caracteres legiveis no HTML gerado.
  - Deve apontar para artefato revisavel da documentacao gerada.

## Relationships

- `InventarioDeConformidade` referencia um `ContratoDeJavadoc` por classe.
- `InventarioDeConformidade` referencia um `ContratoDePacote` pelo `packageName`.
- `EvidenciaDeValidacao` consolida o estado final de todos os itens do
  `InventarioDeConformidade` e da `PoliticaDeEncoding`.

## State Transitions

### InventarioDeConformidade.currentStatus

`missing` → `partial` → `compliant`

- `missing`: classe sem Javadoc obrigatorio ou sem cobertura minima de contrato.
- `partial`: classe documentada, mas com lacunas, tags ausentes ou falhas de qualidade.
- `compliant`: classe aderente e sem findings bloqueantes.

### InventarioDeConformidade.encodingStatus

`unknown` → `invalid` | `utf8`

- `unknown`: encoding ainda nao verificado.
- `invalid`: arquivo ou comentario com indicio de codificacao divergente.
- `utf8`: fonte e comentario confirmados como UTF-8.

## Review Matrix

| Item | Missing | Partial | Compliant |
|------|---------|---------|-----------|
| Classe Java | Sem Javadoc de tipo | Possui resumo, mas sem contexto contratual minimo | Possui resumo, descricao curta e encoding UTF-8 valido |
| `package-info.java` | Ausente no pacote | Existe, mas sem papel arquitetural claro ou com encoding duvidoso | Resume o pacote, posiciona a responsabilidade da camada e preserva UTF-8 |
| Evidencia de validacao | Comando nao executado | Execucao sem consolidacao final ou sem revisao de encoding | Comando executado com sucesso, HTML revisavel e caracteres legiveis |

## Final Compliance Snapshot

- `InventarioDeConformidade.totalClasses = 115`
- `InventarioDeConformidade.totalPackages = 34`
- `InventarioDeConformidade.currentStatus = compliant` para `115/115` classes
- `InventarioDeConformidade.packageInfoStatus = compliant` para `34/34` pacotes
- `InventarioDeConformidade.encodingStatus = utf8` para `149/149` arquivos Java em escopo
- `PoliticaDeEncoding.sourceEncoding = UTF-8`
- `PoliticaDeEncoding.docEncoding = UTF-8`
- `PoliticaDeEncoding.htmlCharset = UTF-8`
- `PoliticaDeEncoding.validationStatus = pass`
- `EvidenciaDeValidacao.resultStatus = pass`
- `EvidenciaDeValidacao.reviewArtifactLocation = backend/target/site/apidocs/index.html`
