# Research: Revisao de Javadoc do Backend

## Baseline Consolidado

- Classes Java de producao em `backend/src/main/java/com/fidc/cdc/kogito`: `115`
- Pacotes em escopo com contrato de pacote obrigatorio: `34`
- `package-info.java` encontrados antes da remediacao: `0`
- Estrategia adotada para o inventario: considerar apenas codigo versionado em
  `src/main/java`, incluindo classes geradas quando materializadas nesse caminho

## Decision 1: Centralizar geracao e validacao de Javadoc no Maven do backend

- **Decision**: Executar a conformidade documental via `maven-javadoc-plugin` no
  `backend/pom.xml`, com HTML revisavel e DocLint habilitado.
- **Rationale**: O backend ja usa Maven como ponto central do build. Isso
  reduz variacao entre maquina local e CI e deixa a evidencia tecnicamente
  auditavel no mesmo fluxo de execucao do modulo.
- **Implementation outcome**: o comando canonicamente aceito ficou
  `mvn -DskipTests javadoc:javadoc`.

## Decision 2: Escopo normativo permanece em classes e pacotes de producao

- **Decision**: Cobrir 100% das classes Java de producao em
  `backend/src/main/java` e 100% dos pacotes sob `com.fidc.cdc.kogito`.
- **Rationale**: A obrigacao normativa aprovada na feature recai sobre a
  superficie versionada de producao. `src/test/java` permanece fora do escopo.
- **Implementation outcome**: todas as `115` classes receberam Javadoc de tipo e
  todos os `34` pacotes receberam `package-info.java`.

## Decision 3: Contrato documental por pacote reduz repeticao acidental

- **Decision**: Criar `package-info.java` para cada pacote e usar o comentario de
  pacote para explicar papel arquitetural e fronteira de responsabilidade.
- **Rationale**: O contexto arquitetural se repete por camada. Concentrar esse
  texto no pacote evita que as classes precisem repetir o mesmo pano de fundo.
- **Implementation outcome**: o backend saiu de `0` para `34` arquivos
  `package-info.java`, incluindo o pacote raiz.

## Decision 4: DocLint permanece bloqueante para sintaxe, markup e links

- **Decision**: O gate continua falhando para erros de DocLint, mas o perfil foi
  estabilizado em `all,-missing` para concentrar o bloqueio na qualidade
  sintatica e de renderizacao do Javadoc de classe e pacote exigido pela regra
  normativa desta feature.
- **Rationale**: A obrigacao adicionada pela constituicao exige Javadoc em toda
  classe Java e boas praticas de escrita. O backend possui muitos membros
  triviais e construtores implicitos; forcar `missing` em todos os membros
  ampliaria o escopo para alem da obrigacao normativa aprovada para esta entrega.
- **Implementation outcome**: depois de remover tags malformadas e estabilizar a
  configuracao do plugin, o gate passou sem erros.

## Decision 5: Evidencia unica em `research.md`

- **Decision**: Consolidar cobertura, comando executado e resultado final neste
  arquivo, sem relatorios separados por pacote.
- **Rationale**: Revisores precisam de um unico ponto para confirmar
  conformidade integral e reproduzir o gate localmente.

## User Story 1 Evidence

- Inventario final confirmado por contagem local:
  - `115` classes Java de producao
  - `34` pacotes com `package-info.java`
- Backlog inicial resolvido integralmente na propria remediacao: nao restaram
  classes sem Javadoc de tipo nem pacotes sem contrato documental.

## User Story 2 Evidence

- Todas as classes em `backend/src/main/java/com/fidc/cdc/kogito/**/*.java`
  receberam Javadoc de tipo aderente ao padrao adotado na feature.
- Tipos aninhados relevantes, como `record`s internos em classes de processo,
  tambem passaram a exibir Javadoc.
- Todos os pacotes em escopo receberam `package-info.java` com resumo e papel
  arquitetural.
- Revisao cruzada concluida contra:
  - `.specify/memory/constitution.md`
  - Oracle Javadoc Tool article:
    `https://www.oracle.com/technical-resources/articles/java/javadoc-tool.html`

## User Story 3 Evidence

- **Command**: `mvn -DskipTests javadoc:javadoc`
- **Module**: `backend`
- **Executed at**: `2026-03-29T13:30:17-03:00`
- **Result**: `pass`
- **Reviewable HTML**:
  `backend/target/site/apidocs/index.html`
- **Captured log**: execucao bem-sucedida registrada localmente na sessao de
  implementacao desta feature.

## Final Acceptance Snapshot

- `SC-001`: `pass` - `115/115` classes inventariadas
- `SC-002`: `pass` - `115/115` classes com Javadoc de tipo
- `SC-003`: `pass` - gate Maven/Javadoc/DocLint concluido com `BUILD SUCCESS`
- `SC-004`: `pass` - evidencia unica consolidada neste arquivo
- `SC-005`: `pass` - `34/34` pacotes com `package-info.java`
