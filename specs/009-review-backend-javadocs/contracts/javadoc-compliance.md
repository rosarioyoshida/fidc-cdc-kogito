# Contract: Javadoc Compliance Interface

## Purpose

Definir o contrato verificavel da feature de conformidade documental do backend,
incluindo escopo, entradas, saidas e criterios de falha aceitos pelo gate.

## Scope Contract

- O contrato cobre 100% das classes Java de producao em
  `backend/src/main/java`.
- Classes em `backend/src/test/java` nao fazem parte da obrigacao normativa
  desta feature.
- Classes geradas automaticamente que estejam materializadas em
  `backend/src/main/java` fazem parte do inventario obrigatorio.
- Cada pacote em escopo deve possuir `package-info.java`.

## Inputs

- Codigo-fonte Java versionado no backend.
- Regras de governanca descritas em `.specify/memory/constitution.md`.
- Configuracao de build/documentacao do modulo `backend`.
- Referencia Oracle adotada pelo projeto:
  `https://www.oracle.com/technical-resources/articles/java/javadoc-tool.html`

## Required Outputs

- Inventario completo das classes em escopo com status de aderencia.
- Documentacao Javadoc gerada em formato revisavel.
- Resultado de validacao automatizada com status `pass` ou `fail`.
- Evidencia unica consolidada em
  `specs/009-review-backend-javadocs/research.md`.

## Inventory Verification Checklist

- `115` classes Java de producao inventariadas.
- `34` pacotes em escopo com `package-info.java`.
- Nenhuma classe em `backend/src/main/java/com/fidc/cdc/kogito` fica fora da
  contagem final.

## Quality Review Criteria

- Toda classe possui frase-resumo independente e descricao curta de contrato.
- Nenhum comentario inventa comportamento nao sustentado pelo codigo.
- Comentarios de pacote descrevem papel arquitetural e fronteira de
  responsabilidade.
- Notas de implementacao e detalhes internos acidentais nao aparecem como parte
  do contrato publico do tipo.

## Execution Contract

- **Command**: `mvn -DskipTests javadoc:javadoc`
- **Module**: `backend`
- **HTML output**: `backend/target/site/apidocs/index.html`
- **DocLint profile**: `all,-missing`

## Pass Criteria

- Toda classe em escopo possui Javadoc de classe aderente ao contrato de API.
- Pacotes em escopo possuem `package-info.java` coerente com a arquitetura.
- O fluxo de geracao e validacao termina sem erros de DocLint, markup ou links.
- A evidencia de cobertura e de validacao pode ser localizada em um unico ponto
  de revisao da entrega.

## Fail Criteria

- Qualquer classe de producao em escopo ausente do inventario.
- Qualquer classe sem Javadoc obrigatorio ou com contrato incompleto.
- Ausencia de `package-info.java` em pacote em escopo.
- Qualquer erro retornado pelo fluxo de validacao.
- Qualquer HTML malformado, referencia quebrada ou comentario invalido no
  resultado do Javadoc.

## Operational Notes

- O contrato nao exige consolidacao formal por pacote ou camada para aprovar a
  entrega, desde que a evidencia cubra 100% do escopo.
- O gate foi implementado de forma repetivel no `backend/pom.xml`.
- A ausencia de comentarios em membros nao e usada como criterio de falha nesta
  feature; a obrigacao normativa implementada aqui cobre Javadocs de tipo e
  contratos de pacote, com DocLint bloqueando problemas sintaticos e de
  renderizacao.
