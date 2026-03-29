# Implementation Plan: Revisao de Javadoc UTF-8 do Backend

**Branch**: `010-javadoc-utf8-review` | **Date**: 2026-03-29 | **Spec**: [spec.md](D:/desenv/fidc-cdc/fidc-cdc-kogito/specs/010-javadoc-utf8-review/spec.md)
**Input**: Feature specification from `/specs/010-javadoc-utf8-review/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. See `.specify/templates/plan-template.md` for the execution workflow.

## Summary

Revisar integralmente a cobertura documental do backend Java para garantir que
100% das classes de producao em `backend/src/main/java` e 100% dos pacotes em
escopo possuam Javadoc como contrato de API, preservando UTF-8 nos fontes e na
documentacao gerada, com gate repetivel de geracao e validacao via DocLint.

## Technical Context

**Language/Version**: Java 21  
**Primary Dependencies**: Spring Boot 3.3.9, Spring Security, Spring HATEOAS, Spring Data JPA, Bean Validation, Flyway, Springdoc OpenAPI, Kogito 10.1.0, Log4j2, Testcontainers  
**Storage**: PostgreSQL para runtime; arquivos Markdown em `specs/010-javadoc-utf8-review/` para artefatos de planejamento  
**Testing**: JUnit 5, Spring Boot Test, Spring Security Test, Testcontainers, validacao Maven para Javadoc/DocLint  
**Target Platform**: Servico backend JVM executado via Spring Boot em ambiente Linux/container  
**Project Type**: web-service  
**Performance Goals**: Geracao e validacao documental repetiveis no build sem degradacao operacional critica do ciclo de entrega; preservacao de caracteres UTF-8 no HTML gerado  
**Constraints**: Escopo obrigatorio restrito a `backend/src/main/java`; classes de teste em `src/test/java` ficam fora; classes geradas automaticamente materializadas em `src/main/java` entram no inventario; erros e avisos relevantes de validacao bloqueiam a entrega; `package-info.java` e obrigatorio para todos os pacotes em escopo; fontes e comentarios remediados devem permanecer em UTF-8  
**Scale/Scope**: 115 classes Java de producao mapeadas atualmente em `backend/src/main/java/com/fidc/cdc/kogito` e 34 pacotes em escopo sob `com.fidc.cdc.kogito`  
**Documentation/Compliance Gates**: Javadoc de contrato para todas as classes de producao em escopo, `package-info.java` por pacote, geracao de HTML revisavel em UTF-8 e validacao DocLint sem erros ou avisos relevantes

Para esta feature, `package-info.java` e tratado como obrigatorio em todos os
pacotes em escopo, mesmo que a constituicao o posicione como baseline
recomendado no nivel geral do projeto.
O escopo desta entrega fica limitado ao backend de producao, embora a regra
constitucional permaneça global para classes Java do repositorio.

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- Simplicity: a feature nao introduz novas camadas de negocio; o desenho adiciona apenas inventario, contrato documental, garantia de UTF-8 e gate de build/documentacao estritamente necessarios para a obrigacao normativa.
- Architecture: o plano preserva a divisao atual `api` / `application` / `domain` / `infrastructure` / `observability` / `process` / `security`, tratando Javadoc e `package-info.java` como contratos aderentes ao papel de cada classe e pacote.
- Security/Operations: a validacao automatizada passa a bloquear merge com nao conformidades documentais, de markup e de encoding, gerando evidencia rastreavel; nao altera comportamento funcional nem superficie de seguranca do runtime.
- UX/Design System: nao ha interface nova; item nao aplicavel para a implementacao desta feature.
- Java documentation: o plano cobre 100% das classes de producao em `src/main/java`, inclui `package-info.java` por pacote, explicita UTF-8 como encoding canonico e define geracao de Javadoc com DocLint bloqueante.

**Gate Result (Pre-Research)**: PASS

## Project Structure

### Documentation (this feature)

```text
specs/010-javadoc-utf8-review/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── javadoc-utf8-compliance.md
└── tasks.md
```

### Source Code (repository root)

```text
backend/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/fidc/cdc/kogito/
│   │   │   ├── api/
│   │   │   ├── application/
│   │   │   ├── domain/
│   │   │   ├── infrastructure/
│   │   │   ├── observability/
│   │   │   ├── process/
│   │   │   ├── security/
│   │   │   └── FidcCdcKogitoApplication.java
│   │   └── resources/
│   └── test/
│       └── java/
frontend/
specs/
```

**Structure Decision**: a feature atuara principalmente em `backend/pom.xml` e
em `backend/src/main/java/com/fidc/cdc/kogito/**`, com artefatos de suporte em
`specs/010-javadoc-utf8-review/`. `src/test/java` permanece fora do escopo
normativo, mas pode ser usado apenas para verificacao operacional do gate.

## Phase 0: Research Summary

- Validacao documental sera centralizada no Maven do backend para manter um unico
  ponto de execucao local e em CI.
- O inventario deve usar arquivos versionados em `backend/src/main/java`; saidas
  temporarias de `target/` nao entram no backlog de remediacao.
- UTF-8 sera tratado como codificacao canonica para fontes Java, comentarios
  Javadoc e documentacao HTML gerada.
- `package-info.java` sera adotado como contrato de pacote obrigatorio em todos os
  pacotes de producao, evitando duplicacao de contexto arquitetural em cada classe.

## Phase 1: Design Summary

- O modelo de dados da feature gira em torno de inventario de conformidade,
  contrato de Javadoc por classe, contrato de pacote, politica de encoding e
  evidencia de validacao.
- O contrato operacional da feature sera documentado como especificacao de
  conformidade documental e de UTF-8 em `contracts/javadoc-utf8-compliance.md`.
- O quickstart define o fluxo minimo para inventariar, remediar, gerar e validar
  a documentacao antes de avancar para tarefas.

## Post-Design Constitution Check

- Simplicity: mantido; nenhum componente novo fora do necessario para documentacao, UTF-8 e gate.
- Architecture: mantido; documentacao respeita ownership por pacote e papel de cada classe.
- Security/Operations: mantido; gate bloqueante e evidencias de validacao ficaram explicitados.
- UX/Design System: continua nao aplicavel.
- Java documentation: mantido; estrategia de Javadoc, `package-info.java`, UTF-8, geracao HTML e DocLint ficaram explicitamente definidos.

**Gate Result (Post-Design)**: PASS

## Complexity Tracking

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| None | N/A | N/A |
