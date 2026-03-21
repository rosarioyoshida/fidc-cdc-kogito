# Implementation Plan: Controle de Cessao de FIDC

**Branch**: `001-controle-cessao-fidc` | **Date**: 2026-03-20 | **Spec**: [spec.md](D:/desenv/fidc-cdc-kogito/specs/001-controle-cessao-fidc/spec.md)
**Input**: Feature specification from `/specs/001-controle-cessao-fidc/spec.md`

## Summary

Implementar uma plataforma de controle de cessao de FIDC orientada a BPMN, com backend
transacional em Kogito/Spring Boot, frontend operacional separado, integracao sincrona
com registradora via REST e uma camada de leitura consolidada para consulta
operacional, monitoramento e historico. A solucao deve distinguir claramente runtime
transacional, mensageria/eventos, servicos de apoio do Kogito e consoles operacionais.

## Technical Context

**Language/Version**: Java 21 no backend e TypeScript LTS no frontend  
**Primary Dependencies**: Spring Boot LTS, Spring Security, Spring HATEOAS,
Swagger/OpenAPI, Log4j2, SLF4J, JPA, Hibernate, Bean Validation, Flyway, Kogito,
React, Next.js, shadcn/ui, Tailwind CSS  
**Storage**: PostgreSQL LTS para dados transacionais do backend, indexacao operacional
do Kogito Data Index e persistencia do Kogito Jobs Service  
**Testing**: JUnit no backend, testes de integracao para fluxo BPMN, registradora,
autorizacao por etapa e auditoria, testes de contrato para APIs REST e RFC 9457, e
testes de interface e integracao para os fluxos operacionais criticos no frontend  
**Target Platform**: Aplicacao web containerizada para ambiente Linux com execucao
local via Docker Compose  
**Project Type**: Aplicacao web com frontend, backend rodando workflow BPMN,  e servicos de
apoio  
**Performance Goals**: Consulta de status e historico em ate 3 segundos; propagacao de
eventos para a visao consolidada em ate 10 segundos; suporte inicial a operacao com
ao menos 50 cessoes ativas simultaneamente, centenas de contratos por cessao e
milhares de parcelas por ciclo operacional sem perda de rastreabilidade  
**Constraints**: Basic Auth na primeira versao, integracao com registradora via REST
sincrona, remover Logback das dependencias do Spring Boot, modo dark/light no
frontend, compose com agrupador `fidc-cdc-kogit`  
**Scale/Scope**: Fluxo completo de 15 etapas, trilha de auditoria ponta a ponta,
consultas operacionais, tarefas humanas, timers BPMN, integracao externa e leitura
consolidada separada

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- Simplicity gate: aprovado. A solucao separa apenas os blocos que possuem
  responsabilidade distinta: UI operacional, runtime transacional, mensageria,
  leitura consolidada e consoles do ecossistema Kogito.
- Architecture gate: aprovado. O plano define limites claros entre backend
  transacional, frontend, Data Index, Jobs Service, Kafka e PostgreSQL, evitando
  acoplamento indevido entre escrita critica e leitura consolidada.
- API design gate: aprovado. APIs do backend devem seguir nomeacao REST baseada em
  substantivos, pluralizacao consistente, hierarquia por recurso e query parameters
  para filtros.
- API versioning gate: aprovado. A API sera publicada sob estrategia de versao
  explicita para breaking changes, preservando compatibilidade para consumidores
  existentes.
- Hypermedia gate: aprovado. HATEOAS sera usado nas APIs operacionais que exigem
  descoberta de proximas acoes e navegacao contextual do fluxo.
- API error contract gate: aprovado. Erros HTTP devem usar RFC 9457 com
  `application/problem+json`, sem vazamento de detalhes internos.
- Maintainability/scalability gate: aprovado. O runtime de processo e a camada de
  leitura foram desacoplados; Kafka e PostgreSQL suportam expansao da consulta
  consolidada, indexacao de processo e persistencia de jobs sem pressionar os fluxos
  operacionais do backend.
- Security/compliance gate: aprovado. O plano contempla Basic Auth, Spring Security,
  segregacao de funcao, trilha de auditoria e tratamento de evidencias desde a
  primeira versao.
- Observability gate: aprovado. Logs, metricas, correlation IDs, eventos de processo,
  retries de registradora e sinais de jobs/tarefas devem ser observaveis.
- UX/performance gate: aprovado. O frontend usara Design System da Atlassian,
  acessibilidade, estados completos de componentes, semantica de cores e metas
  operacionais de consulta.
- Frontend implementation gate: aprovado. React + Next.js + TypeScript sera a base do
  frontend; shadcn/ui e apenas fundacao estrutural; a referencia visual e
  comportamental e o Design System da Atlassian.

## Component Interaction Model

### Logical relationship between components

O diagrama de [componentes.mmd](D:/desenv/fidc-cdc-kogito/docs/componentes.mmd)
estabelece a relacao normativa entre os blocos da solucao:

1. O `frontend` e o canal de negocio da aplicacao. Ele inicia cessoes, consulta
   status, apresenta historico, evidencia pendencias e chama exclusivamente as APIs do
   `backend`.
2. O `backend` e o nucleo funcional. Ele hospeda as APIs REST, executa o BPMN no
   Kogito, aplica regras, persiste dados transacionais no `PostgreSQL`, integra com a
   registradora e publica eventos de processo no `Kafka`.
3. O `Kafka` e o barramento de eventos internos. Ele desacopla o runtime transacional
   da camada de leitura e propaga eventos relevantes de processo, tarefas e jobs.
4. O `Kogito Data Index` consome os eventos do `Kafka` para indexar instancias,
   tarefas, status e historico do processo. Ele nao substitui o backend transacional;
   ele materializa consultas operacionais e nao deve ser tratado como storage
   transacional primario.
5. O `PostgreSQL` tambem sustenta a persistencia operacional do `Kogito Data Index`
   e do `Kogito Jobs Service`, consolidando indexacao de processos, tarefas e jobs em
   esquemas dedicados sem transferir ownership do processo transacional para fora do
   backend.
6. O `Kogito Jobs Service` gerencia timers BPMN. O `backend` agenda jobs e recebe de
   volta callbacks/eventos temporizados para retomar o fluxo quando houver espera por
   prazo, expiracao ou escalonamento.
7. O `Task Console` e um canal operacional especializado para tarefas humanas. Ele
   consulta tarefas e contexto via `Data Index` e envia acoes de execucao/conclusao ao
   runtime do `backend`. Seu funcionamento depende de human tasks corretamente
   modeladas, atribuicoes por perfil e metadados de tarefa consistentes no BPMN.
8. O `Management Console` e um canal especializado de gestao e monitoramento de
   instancias. Ele consulta processos, tarefas e jobs via `Data Index` e executa
   comandos de administracao no runtime do `backend`. Seu funcionamento depende de
   visibilidade adequada de instancias, estados e jobs expostos pelo ecossistema Kogito.

### Interaction rules

- `frontend`, `Task Console` e `Management Console` sao canais distintos. O frontend
  nao substitui os consoles oficiais do Kogito; ele cobre a experiencia de negocio da
  aplicacao.
- Todo comando que altera estado do processo passa pelo `backend`.
- Toda consulta operacional consolidada deve priorizar `Data Index` e as estruturas de
  indexacao e jobs persistidas em `PostgreSQL`, evitando sobrecarga nas operacoes
  transacionais do backend.
- `Kafka` nao e canal de comando de usuario; ele e canal de propagacao de eventos
  tecnicos e de negocio.
- `Jobs Service` participa apenas de timers e eventos temporizados; ele nao executa
  regra de negocio fora do contexto do processo.
- `Data Index` indexa o estado observavel do processo e persiste essa indexacao em
  `PostgreSQL`, sem substituir a fonte transacional do workflow no backend.
- `Task Console` e `Management Console` dependem de integracao funcional real com
  tarefas humanas, instancias e jobs; subir containers sem metadados e eventos
  corretos nao atende ao objetivo operacional.

### Runtime flow by component

1. O usuario atua no `frontend` e envia uma acao operacional.
2. O `backend` autentica, autoriza, executa a etapa BPMN e persiste o estado
   transacional no `PostgreSQL`.
3. Se houver integracao com registradora, o `backend` chama a API REST sincrona,
   registra request/response, retries e evidencias.
4. O `backend` publica eventos de processo e execucao no `Kafka`.
5. O `Kogito Data Index` consome esses eventos e atualiza a indexacao de processo em
   `PostgreSQL`.
6. O `Kogito Jobs Service` persiste jobs, agendas e estados temporizados em
   `PostgreSQL`, enquanto a camada de leitura consolidada do backend usa o mesmo banco
   com tabelas dedicadas para consultas operacionais.
7. `Task Console` e `Management Console` consultam essa visao indexada e disparam
   acoes administrativas ou humanas contra o `backend`.
8. Quando o processo agenda prazo ou temporizador, o `backend` registra o job no
   `Jobs Service`, que devolve o callback no vencimento e reativa a execucao do fluxo.

## Project Structure

### Documentation (this feature)

```text
specs/001-controle-cessao-fidc/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   ├── backend-openapi.yaml
│   └── registradora-integration.md
└── tasks.md
```

### Source Code (repository root)

```text
backend/
├── src/main/java/
│   ├── api/
│   ├── application/
│   ├── domain/
│   ├── infrastructure/
│   ├── process/
│   ├── security/
│   └── observability/
├── src/main/resources/
│   ├── db/migration/
│   ├── processes/
│   └── application.yml
└── src/test/
    ├── contract/
    ├── integration/
    └── unit/

frontend/
├── src/
│   ├── app/
│   ├── components/
│   ├── design-system/
│   ├── features/
│   ├── lib/
│   └── services/
└── tests/
    ├── e2e/
    ├── integration/
    └── unit/

infra/
├── docker/
├── compose/
└── observability/
```

**Structure Decision**: Aplicacao web com separacao explicita entre backend
transacional, frontend operacional e infraestrutura/containerizacao. O ecossistema
Kogito, Kafka e PostgreSQL sera orquestrado via `infra/compose`.

## Phase 0: Research Outcomes

As decisoes de pesquisa registradas em
[research.md](D:/desenv/fidc-cdc-kogito/specs/001-controle-cessao-fidc/research.md)
fecham os pontos necessarios para o plano:

- Java 21 + Spring Boot LTS no backend
- Kogito como motor BPMN
- PostgreSQL LTS como persistencia do backend, do Data Index e do Jobs Service
- Kafka + Data Index + Jobs Service para leitura consolidada, indexacao de processos
  e timers operacionais
- REST sincrono para a registradora com retry limitado
- OpenAPI + HATEOAS + RFC 9457 nas APIs
- React + Next.js + TypeScript no frontend
- Compose minimo com todos os servicos essenciais

Nao ha `NEEDS CLARIFICATION` remanescente para a fase de desenho.

## Phase 1: Design Artifacts

### Data model

O modelo de dados detalhado esta em
[data-model.md](D:/desenv/fidc-cdc-kogito/specs/001-controle-cessao-fidc/data-model.md)
e define:

- `Cessao` como raiz transacional da operacao
- entidades operacionais (`EtapaCessao`, `Pagamento`, `Lastro`, `OfertaRegistradora`)
- entidades de seguranca e trilha (`PerfilAcesso`, `Usuario`, `EventoAuditoria`)
- `Read Model Consolidado` para leitura operacional separada

### Contracts

Os contratos definidos para a fase atual sao:

- [backend-openapi.yaml](D:/desenv/fidc-cdc-kogito/specs/001-controle-cessao-fidc/contracts/backend-openapi.yaml):
  contrato inicial das APIs do backend
- [registradora-integration.md](D:/desenv/fidc-cdc-kogito/specs/001-controle-cessao-fidc/contracts/registradora-integration.md):
  padrao da integracao REST sincrona com a registradora

### Operational bootstrap

O fluxo minimo de inicializacao esta em
[quickstart.md](D:/desenv/fidc-cdc-kogito/specs/001-controle-cessao-fidc/quickstart.md)
e contempla backend, frontend, PostgreSQL, Kafka, Data Index, Jobs Service, Task
Console e Management Console.

## Phase 2: Implementation Direction

### Backend

- Expor APIs REST versionadas com OpenAPI, HATEOAS contextual e erros padronizados por
  RFC 9457.
- Implementar autenticacao Basic Auth com Spring Security e autorizacao por perfil e
  etapa.
- Modelar o fluxo BPMN de 15 etapas no Kogito, incluindo tarefas humanas e timers.
- Persistir estado transacional, auditoria e read models operacionais no PostgreSQL
  com Flyway.
- Remover Logback e padronizar logging em Log4j2 + SLF4J.
- Publicar eventos de processo e de integracao no Kafka com correlation ID.

### Frontend

- Construir experiencia operacional em React + Next.js + TypeScript.
- Usar shadcn/ui como base estrutural e tokens/estados/variantes segundo o Design
  System da Atlassian.
- Separar componentes de apresentacao de chamadas de API e de regra de negocio.
- Implementar dark mode e light mode sem quebrar contraste, foco visivel e semantica
  de cores.

### Query and monitoring stack

- Usar Kogito Data Index para consulta de instancias, tarefas, jobs e historico do
  processo.
- Persistir Data Index, Jobs Service e leituras operacionais em PostgreSQL com
  esquemas e tabelas dedicados para consultas, indexacao e timers.
- Garantir que Task Console e Management Console funcionem sobre a camada de indexacao
  e ainda disparem comandos no runtime do backend quando necessario.
- Configurar Kafka, Data Index e projetores de leitura de forma que a cadeia
  `backend -> Kafka -> Data Index -> PostgreSQL` seja observavel, consistente e
  validavel.
- Tratar Jobs Service como dependencia funcional obrigatoria para timers BPMN,
  expiracoes e escalonamentos temporizados.
- Tratar Task Console e Management Console como capacidades operacionais do produto,
  nao apenas componentes de infraestrutura do compose.

### Compose and local environment

- O compose deve subir apenas o minimo necessario: `backend`, `frontend`, `postgres`,
  `kafka`, `data-index`, `jobs-service`, `task-console`, `management-console`.
- O nome do agrupador deve ser `fidc-cdc-kogit`.
- A ordem de bootstrap deve preservar primeiro infraestrutura de dados/eventos,
  depois servicos Kogito, depois backend e frontend.

## Post-Design Constitution Re-Check

- Simplicidade: mantida. Cada componente no plano possui papel operacional claro.
- Arquitetura e coesao: mantidas. Nenhum componente de leitura recebe responsabilidade
  de escrita transacional.
- Seguranca e compliance: mantidas. Todo comando de negocio continua centralizado no
  backend autenticado e auditavel.
- Observabilidade: fortalecida. A relacao entre backend, Kafka, Jobs Service e Data
  Index agora esta explicita e testavel.
- UX e frontend: mantidos. O frontend permanece como experiencia de negocio distinta
  dos consoles tecnicos.

## Complexity Tracking

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| Compartilhamento de PostgreSQL entre backend, Data Index e Jobs Service com esquemas e bancos dedicados | Centraliza persistencia operacional sem perder separacao logica entre escrita transacional, indexacao e timers | Introduzir persistencias distintas por servico ampliaria operacao, observabilidade e custo de manutencao sem ganho proporcional neste escopo |
| Uso de consoles oficiais junto com frontend proprio | Consoles atendem operacao tecnica e humana do Kogito, enquanto o frontend atende fluxo de negocio do produto | Unificar tudo no frontend aumentaria escopo e duplicaria capacidades nativas do ecossistema Kogito |
