# Implementation Plan: Avaliacao de Boas Praticas do Projeto

**Branch**: `002-avaliar-boas-praticas` | **Date**: 2026-03-22 | **Spec**: [spec.md](D:/desenv/fidc-cdc-kogito/specs/002-avaliar-boas-praticas/spec.md)
**Input**: Feature specification from `/specs/002-avaliar-boas-praticas/spec.md`

## Summary

Estruturar uma capacidade recorrente de avaliacao de aderencia do projeto usando os
artefatos, a stack e os padroes ja adotados no repositorio. A entrega desta feature
deve formalizar criterios, evidencias, classificacao de prontidao e forma de registro
dos achados sem introduzir nova stack ou dependencias sem consulta previa.

## Technical Context

**Language/Version**: Java 21 no backend e TypeScript 5 / React 19 / Next.js 15 no frontend  
**Primary Dependencies**: Spring Boot 3.3, Spring Security, Spring HATEOAS, JPA/Hibernate, Bean Validation, Flyway, Log4j2/SLF4J, Kogito 10.1, React, Next.js, shadcn/ui, Tailwind CSS, Vitest, Testing Library  
**Storage**: PostgreSQL para persistencia operacional; arquivos Markdown em `specs/` para artefatos de avaliacao  
**Testing**: JUnit + testes de integracao/contrato no backend; Vitest + Testing Library no frontend; validacao documental dos artefatos da feature  
**Target Platform**: Aplicacao web containerizada para Linux com execucao local por Docker Compose  
**Project Type**: Aplicacao web com backend, frontend e artefatos documentais de governanca  
**Performance Goals**: Parecer final disponivel em ate 2 dias uteis; leitura do resumo executivo em ate 10 minutos; reutilizar os comandos atuais de validacao sem degradar o fluxo existente  
**Constraints**: Manter a stack atual do projeto; nao adicionar dependencias sem consulta previa; reutilizar padroes, modulos e ferramentas ja existentes; preservar formato documental em `specs/`; evitar automacoes que exijam nova infraestrutura  
**Scale/Scope**: Avaliacao recorrente por fase relevante do projeto cobrindo implementacoes, configuracoes, seguranca, observabilidade, testes, documentacao operacional e prontidao geral

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- Simplicity gate: aprovado. A feature se limita a artefatos documentais, criterios e
  fluxo de avaliacao; nao introduz novos servicos, frameworks ou automacoes pesadas.
- Architecture gate: aprovado. O plano reutiliza a estrutura atual (`backend`,
  `frontend`, `infra`, `specs`) e separa claramente evidencia, achado, parecer e plano
  de adequacao.
- API design gate: aprovado por nao haver nova API nesta feature. Qualquer eventual
  extensao futura deve seguir os contratos REST ja adotados pelo projeto.
- API versioning gate: aprovado por nao haver superficie REST nova nesta feature.
- Hypermedia gate: nao aplicavel nesta fase, pois nao ha interface REST nova.
- API error contract gate: nao aplicavel nesta fase, pois nao ha contrato HTTP novo.
- Maintainability/scalability gate: aprovado. A avaliacao sera repetivel por fase,
  comparavel entre ciclos e baseada em artefatos existentes, sem ampliar acoplamento.
- Security/compliance gate: aprovado. O plano inclui verificacao explicita de controles
  de acesso, dados sensiveis, auditoria, retencao e conformidade.
- Observability gate: aprovado. A avaliacao inclui logs, metricas, correlacao,
  health checks e sinais operacionais como areas obrigatorias de verificacao.
- UX/performance gate: aprovado. O relatorio precisa ser compreensivel, acionavel e
  rapido de consumir; a avaliacao cobre acessibilidade, hierarquia, semantica visual e
  mensagens ao usuario para as areas com interface.
- Frontend implementation gate: aprovado. A feature preserva React, Next.js,
  TypeScript, shadcn/ui e a referencia normativa do Design System da Atlassian; nao ha
  adicao de dependencias sem consulta previa.

## Project Structure

### Documentation (this feature)

```text
specs/002-avaliar-boas-praticas/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── assessment-report.md
├── checklists/
│   └── requirements.md
└── tasks.md
```

### Source Code (repository root)

```text
backend/
├── src/main/java/com/fidc/cdc/kogito/
│   ├── api/
│   ├── application/
│   ├── domain/
│   ├── infrastructure/
│   ├── observability/
│   ├── process/
│   └── security/
├── src/main/resources/
│   ├── application.yml
│   ├── db/migration/
│   └── processes/
└── src/test/java/com/fidc/cdc/kogito/
    ├── architecture/
    ├── contract/
    ├── integration/
    └── support/

frontend/
├── src/
│   ├── app/
│   ├── components/
│   ├── design-system/
│   ├── features/
│   └── lib/
└── tests/
    ├── integration/
    └── setup.ts

infra/
├── compose/
├── keycloak/
├── observability/
└── postgres/

specs/
├── 001-controle-cessao-fidc/
└── 002-avaliar-boas-praticas/
```

**Structure Decision**: A feature sera implementada como capacidade documental e de
governanca sobre a estrutura existente do produto. Nao ha necessidade de novos modulos
ou repositorios; os artefatos desta feature ficam isolados em `specs/002-...` e
consomem evidencias das camadas atuais do projeto.

## Phase 0: Research Outcomes

As decisoes consolidadas em
[research.md](D:/desenv/fidc-cdc-kogito/specs/002-avaliar-boas-praticas/research.md)
resolvem os pontos de pesquisa necessarios:

- preservar integralmente a stack atual do projeto;
- proibir adicao de dependencias sem consulta previa;
- usar como baseline as diretrizes do projeto e a constituicao vigente;
- tratar a avaliacao como ciclo recorrente por fase relevante;
- usar contrato documental para parecer, achados e plano de adequacao;
- reutilizar os comandos e testes existentes como fonte de evidencia, sem nova
  infraestrutura.

Nao ha `NEEDS CLARIFICATION` remanescente para a fase de desenho.

## Phase 1: Design Artifacts

### Data model

O modelo de dados da avaliacao esta em
[data-model.md](D:/desenv/fidc-cdc-kogito/specs/002-avaliar-boas-praticas/data-model.md)
e define:

- `CriterioAvaliacao` como unidade normativa de verificacao;
- `ItemAvaliado` como alvo observado no repositorio ou configuracao;
- `Evidencia` como suporte objetivo da conclusao;
- `Achado` como resultado priorizado da avaliacao;
- `ParecerProntidao` como classificacao final do ciclo;
- `PlanoAdequacao` como resposta priorizada aos desvios.

### Contracts

O contrato definido para a fase atual e:

- [assessment-report.md](D:/desenv/fidc-cdc-kogito/specs/002-avaliar-boas-praticas/contracts/assessment-report.md):
  estrutura obrigatoria do relatorio de avaliacao, incluindo status, severidade,
  rastreabilidade, criterios de prontidao e formato minimo do plano de adequacao.

### Operational bootstrap

O fluxo minimo de uso e validacao esta em
[quickstart.md](D:/desenv/fidc-cdc-kogito/specs/002-avaliar-boas-praticas/quickstart.md)
e contempla:

- preparacao do ambiente atual do projeto;
- execucao das validacoes ja existentes de backend e frontend;
- consolidacao do relatorio de avaliacao;
- reexecucao da avaliacao em fases futuras.

## Phase 2: Implementation Direction

### Governance and criteria

- Consolidar criterios de avaliacao por area: implementacao, configuracao, seguranca,
  observabilidade, testes, documentacao operacional e UX quando aplicavel.
- Formalizar a classificacao `Apto`, `Apto com ressalvas` e `Inapto` com base nos
  gatilhos objetivos definidos no spec.
- Exigir que cada achado tenha evidencia, impacto, severidade e recomendacao
  rastreavel ao criterio de origem.

### Evidence collection

- Reutilizar artefatos existentes do repositorio como fonte primaria de evidencia:
  codigo, configuracoes, testes, contratos, compose, observabilidade e specs.
- Tratar comandos atuais (`mvn test`, `npm run lint`, `npm test`) como fontes de
  validacao, sem criar novo ferramental por padrao.
- Registrar limitacoes quando uma area nao for verificavel apenas com os artefatos
  disponiveis.

### Delivery constraints

- Manter a stack atual do projeto em qualquer implementacao decorrente deste plano.
- Consultar previamente antes de qualquer adicao de dependencia no backend, frontend
  ou infraestrutura.
- Priorizar ajustes documentais, configuracionais e de organizacao antes de propor
  novas abstrações, bibliotecas ou servicos.

## Post-Design Constitution Re-Check

- Simplicidade: mantida. O desenho continua documental e orientado a governanca.
- Arquitetura e coesao: mantidas. Nenhuma nova fronteira tecnica foi introduzida.
- Seguranca e compliance: fortalecidas. A avaliacao torna explicito o gate de
  controles, dados sensiveis, autorizacao e auditoria.
- Observabilidade: fortalecida. Logs, metricas, correlacao e health checks foram
  mantidos como area obrigatoria da avaliacao.
- UX e frontend: mantidos. A avaliacao continua exigindo aderencia aos principios de
  acessibilidade, tokens, componentes e comportamento do frontend atual.
- Restricao de stack: mantida. O plano proibe nova stack e condiciona qualquer
  dependencia adicional a consulta previa.

## Complexity Tracking

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| None | N/A | N/A |
