# Implementation Plan: Addon Process SVG no Management Console

**Branch**: `008-management-process-svg` | **Date**: 2026-03-25 | **Spec**: [spec.md](D:/desenv/fidc-cdc-kogito/specs/008-management-process-svg/spec.md)
**Input**: Feature specification from `/specs/008-management-process-svg/spec.md`

## Summary

Habilitar o addon oficial `Process SVG` do Kogito 10.1 no backend Spring Boot usado pelo Management Console, para que instancias elegiveis exibam o fluxo em SVG com caminho executado, enquanto a etapa textual destacada continue vindo do contexto operacional ja calculado pelo projeto. A entrega deve convergir a exibicao de diagrama para o addon oficial, manter a regra de destaque da etapa atual ou da ultima etapa concluida quando o processo estiver encerrado, e eliminar ambiguidade entre fonte visual e fonte textual sem criar uma segunda implementacao paralela de SVG.

## Technical Context

**Language/Version**: Java 21 no backend  
**Primary Dependencies**: Spring Boot 3.3, Spring Security, Spring HATEOAS, JPA/Hibernate, Flyway, Log4j2/SLF4J, Kogito 10.1.0, `kie-addons-springboot-process-management`, `jbpm-addons-springboot-task-management`, `kogito-addons-springboot-jobs-management`, `kie-addons-springboot-monitoring-prometheus`, addon oficial `org.kie:kie-addons-springboot-process-svg`  
**Storage**: PostgreSQL para estado operacional; Data Index para consulta do runtime; BPMN em `backend/src/main/resources/processes`; SVG do processo em `META-INF/processSVG/{processId}.svg` no classpath, com `kogito.svg.folder.path` reservado apenas para excecao operacional  
**Testing**: JUnit 5, Spring Boot Test, MockMvc, testes de integracao de console existentes, testes opcionais contra stack externa com Data Index / Management Console / Task Console  
**Target Platform**: Servico backend Spring Boot executado localmente ou via `infra/compose/docker-compose.yml`, consumido pelo Kogito Management Console 10.1.x  
**Project Type**: Web service de runtime Kogito com integracao a consoles externos  
**Authentication Strategy**: O Management Console externo continuara acessando os endpoints do runtime por meio do mesmo mecanismo aceito hoje pelo backend para integracoes de console. Esta feature nao introduz novo protocolo de autenticacao; ela revisa a exposicao atual para restringir acesso indevido sem quebrar o consumo legitimo do console no ambiente `infra/compose`  
**Performance Goals**: Diagrama inicial disponivel ao console em ate 2 segundos para instancias elegiveis; ausencia de SVG, ausencia de etapa ativa e divergencias devem retornar feedback observavel sem degradacao perceptivel  
**Constraints**: Seguir a documentacao oficial do Kogito 10.1 e o addon `process-svg`; evitar manter um gerador manual paralelo de SVG; preservar `ManagementConsoleSupport` como fonte textual da etapa atual; restringir a exibicao a processos com SVG disponivel; alinhar controles de acesso e sinais de auditoria/observabilidade aos fluxos do console  
**Scale/Scope**: Abrange `backend/pom.xml`, `backend/src/main/resources/processes/`, eventual `META-INF/processSVG/`, configuracao em `application.yml`, suporte de contexto em `backend/src/main/java/com/fidc/cdc/kogito/application/process/`, endpoints de console em `backend/src/main/java/com/fidc/cdc/kogito/api/process/` e testes em `backend/src/test/java/com/fidc/cdc/kogito/integration/console/`

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- Simplicity gate: aprovado. O plano adota o addon oficial e evita manter uma segunda solucao proprietaria de renderizacao SVG.
- Architecture gate: aprovado. O backend continua responsavel por runtime, autorizacao e contexto textual; a exibicao do diagrama passa a seguir a extensao oficial do Kogito.
- API design gate: aprovado com escopo limitado. A feature deve privilegiar as operacoes REST oficiais do addon `process-svg` e evitar expandir endpoints customizados para o mesmo papel.
- API versioning gate: nao aplicavel. Nao ha nova API publica versionada do dominio; o foco e integrar um addon oficial do runtime.
- Hypermedia gate: nao aplicavel. Nao ha descoberta hipermidia nova no dominio do projeto.
- API error contract gate: aprovado. Endpoints customizados remanescentes devem continuar usando os contratos de erro existentes do projeto; a feature nao deve mascarar falhas do addon com respostas opacas.
- Maintainability/scalability gate: aprovado. O plano reduz duplicacao entre o SVG manual atual e a capacidade nativa do Kogito, deixando uma fonte principal para o diagrama.
- Security/compliance gate: aprovado com atencao explicita. O desenho precisa alinhar acesso ao addon e ao contexto textual aos mesmos controles de visualizacao do console e registrar falhas relevantes de acesso/carregamento.
- Observability gate: aprovado. O design exige sinais para carregamento bem-sucedido, SVG ausente, etapa nao resolvida, divergencia entre estado textual e destaque visual e falhas de consulta do runtime.
- UX/performance gate: aprovado. O addon deve manter hierarquia simples: titulo, diagrama, destaque e indicacao textual; o fallback de processo encerrado permanece explicito e verificavel.
- Frontend implementation gate: nao aplicavel diretamente ao codigo do repositorio. O Management Console e externo; esta entrega integra o runtime backend para habilitar o addon oficial em vez de construir uma UI React local para o mesmo fluxo.
- Component governance gate: nao aplicavel ao frontend local. O contrato de interface desta feature trata a integracao com o addon do Management Console, sem introduzir novos componentes em `frontend/src/components/ui`.

## Project Structure

### Documentation (this feature)

```text
specs/008-management-process-svg/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── ui-process-svg-contract.md
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
│   │   │   │   ├── process/
│   │   │   │   └── security/
│   │   │   ├── application/
│   │   │   │   └── process/
│   │   │   ├── infrastructure/
│   │   │   └── security/
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── processes/
│   │       └── META-INF/
│   │           └── processSVG/
│   └── test/
│       └── java/com/fidc/cdc/kogito/
│           ├── architecture/
│           └── integration/console/
├── target/
│   └── classes/
└── Dockerfile

infra/
└── compose/
    └── docker-compose.yml
```

**Structure Decision**: A implementacao fica concentrada no backend porque o repositorio ja expoe runtime e contexto para o Kogito Management Console, enquanto o console em si roda como servico externo do compose. O nucleo da mudanca fica em `backend/pom.xml` para ativar o addon oficial, em `backend/src/main/resources/` para garantir o SVG esperado pelo addon, em `backend/src/main/java/com/fidc/cdc/kogito/application/process/` para preservar a fonte textual da etapa atual e em `backend/src/test/java/com/fidc/cdc/kogito/integration/console/` para validar a integracao ponta a ponta com o console.

## Phase 0: Outline & Research

As decisoes de pesquisa desta feature foram consolidadas em [research.md](D:/desenv/fidc-cdc-kogito/specs/008-management-process-svg/research.md) e resolvem os pontos criticos:

- qual addon oficial do Kogito 10.1 deve ser usado no backend Spring Boot;
- como disponibilizar o SVG ao addon usando o padrao oficial `META-INF/processSVG/{processId}.svg`;
- como eliminar a duplicacao entre o endpoint SVG manual existente e o addon oficial;
- como preservar a etapa textual atual ou a ultima etapa concluida como fonte de verdade operacional;
- como alinhar acesso, observabilidade e validacao com o Management Console e a stack externa ja existente no projeto.

## Phase 1: Design & Contracts

### Data Model

O modelo operacional da feature esta descrito em [data-model.md](D:/desenv/fidc-cdc-kogito/specs/008-management-process-svg/data-model.md) e cobre:

- elegibilidade de processos para o addon Process SVG;
- artefato SVG no classpath e suas regras de resolucao;
- indicador textual de etapa atual e fallback para processo encerrado;
- estados de exibicao, erro e divergencia usados pelo addon no Management Console.

### Interface Contracts

O contrato funcional da integracao esta documentado em [ui-process-svg-contract.md](D:/desenv/fidc-cdc-kogito/specs/008-management-process-svg/contracts/ui-process-svg-contract.md) e explicita:

- quando o addon deve ou nao aparecer;
- quais sao as fontes de dados visuais e textuais;
- como o fluxo encerrado, a ausencia de SVG e a ausencia de etapa atual devem ser tratados;
- quais evidencias tecnicas precisam existir para validar seguranca, observabilidade e comportamento.

### Quickstart

O roteiro de validacao manual e tecnica esta em [quickstart.md](D:/desenv/fidc-cdc-kogito/specs/008-management-process-svg/quickstart.md) e cobre stack externa, carga do SVG, renderizacao do addon, etapa ativa, processo encerrado, ausencia de SVG e falhas de acesso/carregamento.

### Agent Context Update

O contexto do agente deve ser atualizado apos a geracao destes artefatos para registrar o uso do addon oficial `kie-addons-springboot-process-svg` no backend Kogito do projeto.

### Authentication and Authorization Compatibility

O design tambem precisa validar explicitamente:

- a estrategia de autenticacao/autorizacao entre Management Console e backend para os endpoints do addon;
- o que pode permanecer exposto para compatibilidade tecnica do console;
- o que deve exigir credencial ou contexto autorizado;
- como provar que o endurecimento de acesso nao quebra a integracao legitima no ambiente `infra/compose`.

## Post-Design Constitution Check

- Simplicity gate: mantido. A solucao final continua centrada no addon oficial, sem gerador SVG concorrente.
- Architecture gate: mantido. O runtime do processo, a origem do SVG e a logica textual da etapa atual permanecem em fronteiras claras.
- API design gate: mantido. O plano evita criar novas rotas customizadas para papel ja coberto pelo addon oficial.
- Security/compliance gate: mantido. O design exige alinhamento dos controles de acesso do addon com o contexto autorizado do console e auditoria de falhas relevantes.
- Observability gate: mantido. O contrato inclui sinais para sucesso, ausencia de SVG, ausencia de etapa, divergencia e falha de consulta.
- UX/performance gate: mantido. O addon segue a hierarquia definida no spec e a meta de renderizacao inicial em ate 2 segundos.
- Frontend implementation gate: permanece nao aplicavel ao codigo local. A entrega nao cria uma interface React propria para substituir o Management Console.
- Component governance gate: permanece nao aplicavel ao frontend local. Nenhum componente novo do frontend do repositorio e introduzido por este plano.

## Complexity Tracking

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| Nenhuma | N/A | N/A |
