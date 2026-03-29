# Research: Revisao de Javadoc UTF-8 do Backend

## Decision 1: Centralizar geracao e validacao de Javadoc no Maven do backend

- **Decision**: Executar a conformidade documental via `maven-javadoc-plugin` no
  `backend/pom.xml`, com HTML revisavel e DocLint habilitado no mesmo fluxo.
- **Rationale**: O backend ja usa Maven como ponto central do build. Isso reduz
  variacao entre maquina local e CI, simplifica a reproducibilidade do gate e
  deixa a evidencia tecnicamente auditavel.
- **Alternatives considered**:
  - Script shell avulso para rodar `javadoc`: rejeitado por fragilizar
    reproducibilidade e descoberta dos parametros corretos de encoding.
  - Revisao apenas manual do HTML: rejeitada por nao cumprir a necessidade de
    gate automatizado e bloqueante.

## Decision 2: Tratar UTF-8 como codificacao canonica de ponta a ponta

- **Decision**: Usar UTF-8 como encoding obrigatorio dos fontes Java remediados,
  dos comentarios Javadoc e da documentacao gerada.
- **Rationale**: A feature introduz uma obrigacao normativa explicita de UTF-8.
  Sem padrao unico, acentuacao, nomes de dominio e caracteres especiais podem se
  degradar entre edicao, build e HTML final.
- **Alternatives considered**:
  - Confiar no encoding padrao do sistema operacional: rejeitado por ser
    instavel entre ambientes.
  - Validar encoding apenas no HTML final: rejeitado por deixar a origem do
    problema invisivel nos fontes.

## Decision 3: Inventario usa codigo versionado em `src/main/java`

- **Decision**: O inventario de conformidade cobre 100% das classes Java de
  producao versionadas em `backend/src/main/java`, incluindo classes geradas
  automaticamente quando materializadas nesse caminho.
- **Rationale**: O requisito da feature e remediar o codigo-fonte mantido e
  auditavel no repositorio. Artefatos efemeros de `target/` nao sao backlog
  sustentavel de documentacao.
- **Alternatives considered**:
  - Incluir `src/test/java`: rejeitado por decisao de clarificacao da feature.
  - Incluir tudo que aparecer em `target/generated-sources`: rejeitado porque
    tornaria o inventario instavel entre execucoes.

## Decision 4: `package-info.java` obrigatorio por pacote

- **Decision**: Criar ou revisar `package-info.java` para todos os pacotes em
  escopo sob `com.fidc.cdc.kogito`.
- **Rationale**: Muitas regras da feature sao arquiteturais por pacote.
  Documentar isso no nivel de pacote reduz repeticao e melhora navegabilidade do
  HTML gerado.
- **Alternatives considered**:
  - Documentar contexto arquitetural repetidamente em cada classe: rejeitado por
    gerar ruido e inconsistencias.
  - Tornar `package-info.java` opcional: rejeitado pelas clarificacoes da feature.

## Decision 5: Erros e avisos relevantes bloqueiam a entrega

- **Decision**: O gate de documentacao tratara erros e avisos relevantes de
  DocLint, markup e encoding como falha bloqueante ate correcao.
- **Rationale**: A obrigacao normativa atual exige evidencia objetiva de
  conformidade. Permitir avisos relevantes enfraqueceria o novo requisito e
  abriria margem para degradacao gradual da legibilidade.
- **Alternatives considered**:
  - Bloquear apenas erros: rejeitado por tolerar nao conformidades recorrentes.
  - Bloquear apenas problemas de UTF-8: rejeitado por nao validar integridade do
    comentario como contrato de API.

## Baseline Consolidado

- Classes Java de producao em `backend/src/main/java/com/fidc/cdc/kogito`: `115`
- Pacotes em escopo com contrato de pacote obrigatorio: `34`
- `package-info.java` detectados no baseline do repositorio: `34`
- Estrategia adotada para o inventario: considerar apenas codigo versionado em
  `src/main/java`, incluindo classes geradas quando materializadas nesse caminho

## Output esperado da fase de implementacao

- Inventario final de `115` classes Java e `34` pacotes em escopo
- Javadocs de contrato de API preservados em UTF-8
- Gate oficial reproduzivel via Maven com HTML revisavel e DocLint bloqueante
- Evidencia unica consolidada em `specs/010-javadoc-utf8-review/research.md`

## User Story 1: Inventario e UTF-8

- Inventario validado em `2026-03-29` contra
  `backend/src/main/java/com/fidc/cdc/kogito`.
- Classes Java de producao confirmadas: `115`.
- Pacotes em escopo confirmados com `package-info.java`: `34`.
- Auditoria de encoding com `System.Text.UTF8Encoding(..., throwOnInvalidBytes=true)`:
  `0` arquivos invalidos.
- Resultado da historia: `pass`.

## User Story 2: Remediacao documental

- Todos os `115` tipos em escopo possuem Javadoc de classe detectado no preambulo
  do tipo.
- Todos os `34` arquivos `package-info.java` possuem comentario Javadoc.
- Ajustes reais aplicados nesta implementacao para aderencia do Javadoc ao tipo:
  - `backend/src/main/java/com/fidc/cdc/kogito/api/process/KogitoConsoleRuntimeController.java`
  - `backend/src/main/java/com/fidc/cdc/kogito/domain/analise/Contrato.java`
  - `backend/src/main/java/com/fidc/cdc/kogito/domain/analise/Parcela.java`
  - `backend/src/main/java/com/fidc/cdc/kogito/domain/analise/TermoAceite.java`
  - `backend/src/main/java/com/fidc/cdc/kogito/domain/security/PermissaoEtapa.java`
- Correcoes aplicadas: mover o bloco Javadoc para antes das anotacoes de classe,
  preservando o texto contratual existente e tornando a documentacao efetiva para
  o tipo documentado.
- Revisao cruzada concluida contra `.specify/memory/constitution.md` e a
  referencia Oracle:
  `https://www.oracle.com/technical-resources/articles/java/javadoc-tool.html`
- Resultado da historia: `pass`.

## User Story 3: Gate Maven, DocLint e HTML

- Comando oficial validado no modulo `backend`:
  `mvn -DskipTests javadoc:javadoc`.
- Para forcar geracao nova apos remediacao, a evidencia final foi capturada com:
  `mvn -DskipTests clean javadoc:javadoc`.
- Resultado final do gate: `BUILD SUCCESS`.
- HTML revisavel gerado em:
  `backend/target/site/apidocs/index.html`.
- Evidencia de UTF-8 no HTML:
  - cabecalho contem `charset=UTF-8`;
  - `index.html` gerado em `2026-03-29 14:04:17 -03:00`;
  - a pagina de `Contrato` renderiza o resumo
    `Representa contrato no backend de cessao.` sem corrupcao.
- Resultado da historia: `pass`.

## Pacote Final de Evidencias

- Escopo validado: `115` classes e `34` pacotes.
- Encoding final: `utf8` para todos os fontes e para o HTML gerado.
- Findings bloqueantes restantes: `0`.
- Status final da feature: `pass`.
