# Contract: Javadoc UTF-8 Compliance Interface

## Purpose

Definir o contrato verificavel da feature de conformidade documental e de UTF-8
do backend, incluindo escopo, entradas, saidas e criterios de falha aceitos
pelo gate.

## Scope Contract

- O contrato cobre 100% das classes Java de producao em
  `backend/src/main/java`.
- Classes em `backend/src/test/java` nao fazem parte da obrigacao normativa
  desta feature.
- Classes geradas automaticamente que estejam materializadas em
  `backend/src/main/java` fazem parte do inventario obrigatorio.
- Cada pacote em escopo deve possuir `package-info.java`.
- Fontes Java, comentarios Javadoc e artefatos gerados por esta feature devem
  preservar UTF-8.

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
  `specs/010-javadoc-utf8-review/research.md`.

## Inventory Verification Checklist

- `115` classes Java de producao inventariadas.
- `34` pacotes em escopo com `package-info.java`.
- Nenhuma classe em `backend/src/main/java/com/fidc/cdc/kogito` fica fora da
  contagem final.
- Nenhum arquivo remediado permanece fora do padrao UTF-8.

## Quality Review Criteria

- Toda classe possui frase-resumo independente e descricao curta de contrato.
- Nenhum comentario inventa comportamento nao sustentado pelo codigo.
- Comentarios de pacote descrevem papel arquitetural e fronteira de
  responsabilidade.
- Notas de implementacao e detalhes internos acidentais nao aparecem como parte
  do contrato publico do tipo.
- A acentuacao e os caracteres especiais permanecem legiveis nos fontes e no
  HTML revisavel.

## Execution Contract

- **Command**: `mvn -DskipTests javadoc:javadoc`
- **Module**: `backend`
- **HTML output**: `backend/target/site/apidocs/index.html`
- **Encoding target**: UTF-8 para fontes, comentarios e HTML
- **DocLint profile**: sem erros ou avisos relevantes bloqueantes
- **Forced refresh for evidence capture**: `mvn -DskipTests clean javadoc:javadoc`

## Pass Criteria

- Toda classe em escopo possui Javadoc de classe aderente ao contrato de API.
- Pacotes em escopo possuem `package-info.java` coerente com a arquitetura.
- O fluxo de geracao e validacao termina sem erros de DocLint, markup, links ou
  problemas relevantes de encoding.
- O HTML gerado preserva caracteres legiveis em UTF-8.
- A evidencia de cobertura e de validacao pode ser localizada em um unico ponto
  de revisao da entrega.

## Fail Criteria

- Qualquer classe de producao em escopo ausente do inventario.
- Qualquer classe sem Javadoc obrigatorio ou com contrato incompleto.
- Ausencia de `package-info.java` em pacote em escopo.
- Qualquer erro ou aviso relevante retornado pelo fluxo de validacao.
- Qualquer HTML malformado, referencia quebrada, comentario invalido ou
  corrupcao de caracteres no resultado do Javadoc.

## Operational Notes

- O contrato nao exige consolidacao formal por pacote ou camada para aprovar a
  entrega, desde que a evidencia cubra 100% do escopo.
- O gate sera implementado de forma repetivel no `backend/pom.xml`.
- A verificacao de UTF-8 faz parte da conformidade final desta feature, nao um
  ajuste opcional posterior.
- A evidencia canônica desta implementacao registra `115` classes,
  `34` pacotes e status final `pass` em
  `specs/010-javadoc-utf8-review/research.md`.
