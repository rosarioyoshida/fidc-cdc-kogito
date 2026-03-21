# Research: Controle de Cessao de FIDC

## Decision 1: Backend transacional em Spring Boot LTS com Java 21

**Decision**: Usar Java 21 com Spring Boot LTS como plataforma principal do backend.

**Rationale**: A stack atende ao requisito corporativo informado, possui maturidade para
seguranca, persistencia, APIs REST, observabilidade e integra bem com o ecossistema
Kogito.

**Alternatives considered**:
- Quarkus com Kogito nativo
- Spring Boot em versao nao LTS

## Decision 2: Kogito como motor BPMN e orquestrador do fluxo

**Decision**: Modelar e executar o fluxo das 15 etapas em BPMN com Kogito, usando Jobs
Service para timers e consoles oficiais para operacao humana e monitoramento.

**Rationale**: O processo e explicitamente orientado a BPMN, com tarefas humanas,
integracoes e estados auditaveis. Kogito atende diretamente a esse desenho.

**Alternatives considered**:
- Orquestracao manual sem BPMN
- Motor BPMN diferente do Kogito

## Decision 3: Persistencia transacional e leitura consolidada separadas

**Decision**: PostgreSQL LTS sera a base de persistencia do backend, do Kogito Data
Index e do Kogito Jobs Service, enquanto Kafka seguira como barramento de eventos para
indexacao, historico consultavel e timers operacionais.

**Rationale**: A estrategia preserva separacao logica entre escrita critica do fluxo,
indexacao operacional e persistencia de jobs, reduz a superficie de operacao local e
mantem consultas de processo, auditoria e monitoramento integradas ao ecossistema
Kogito.

**Alternatives considered**:
- Persistencias distintas por servico
- Consulta direta no runtime transacional sem Data Index

## Decision 4: Integracao sincrona REST com a registradora

**Decision**: Integrar com a registradora via API REST sincrona, com retries
automaticos limitados, observabilidade, persistencia de evidencias e escalonamento
operacional apos esgotamento das tentativas.

**Rationale**: Essa decisao veio das clarificacoes da spec e impacta diretamente
contratos, tratamento de erro, UX operacional e modelagem do workflow.

**Alternatives considered**:
- Integracao assincrona
- Interacao manual

## Decision 5: Contratos REST com HATEOAS, RFC 9457 e versionamento formal

**Decision**: APIs expostas pelo backend adotarao OpenAPI, resource naming consistente,
versionamento explicito para breaking changes, HATEOAS quando houver descoberta de
acoes e RFC 9457 para erros HTTP.

**Rationale**: Isso alinha o plano a constituicao do projeto e torna o contrato de API
mais previsivel para integracoes e clientes internos.

**Alternatives considered**:
- APIs sem padronizacao de erro
- Navegacao sem hipermidia
- Versionamento ad hoc

## Decision 6: Logging com Log4j2 + SLF4J e remocao de Logback

**Decision**: Padronizar o backend em Log4j2 + SLF4J e remover Logback de todas as
dependencias do Spring Boot.

**Rationale**: A stack foi definida explicitamente e precisa ser tratada cedo para
evitar conflito de bindings e padroes de logging inconsistentes.

**Alternatives considered**:
- Manter Logback padrao do Spring Boot
- Usar outro facade de logging

## Decision 7: Frontend em React + Next.js + TypeScript com shadcn/ui e Atlassian

**Decision**: O frontend sera implementado com React, Next.js e TypeScript, usando
shadcn/ui como base estrutural e o Design System da Atlassian como contrato visual e
comportamental.

**Rationale**: Atende integralmente a constituicao e a stack exigida para operacao
humana, monitoramento e acessibilidade.

**Alternatives considered**:
- UI sem Design System formal
- Biblioteca de componentes fechada como fonte de verdade visual

## Decision 8: Docker Compose minimo com agrupador `fidc-cdc-kogit`

**Decision**: Fornecer ambiente local com backend, frontend, PostgreSQL, Kafka, Jobs
Service, Task Console, Management Console e Data Index no compose principal, usando as
variantes PostgreSQL dos servicos Kogito.

**Rationale**: O fluxo so pode ser validado fim a fim com BPMN, persistencia,
integracoes, consulta consolidada e operacao humana disponiveis localmente.

**Alternatives considered**:
- Compose parcial sem servicos Kogito
- Ambiente manual sem orquestracao local
