# Research: Addon Process SVG no Management Console

## Decision 1: Usar o addon oficial `org.kie:kie-addons-springboot-process-svg`

**Decision**: Habilitar o addon oficial `Process SVG` para Spring Boot no backend Kogito do projeto.

**Rationale**: A documentacao oficial do Kogito 10.1 lista `org.kie:kie-addons-springboot-process-svg` como o addon responsavel por expor as operacoes REST suplementares para diagrama SVG e caminho executado da instancia. Como o projeto ja roda Kogito 10.1.0 com Spring Boot e Management Console externo, esta e a integracao de menor risco e maior compatibilidade.

**Alternatives considered**:
- Manter apenas o endpoint manual atual que monta um SVG estatico. Rejeitado porque duplica responsabilidade ja coberta pelo addon oficial e nao escala para o fluxo real.
- Criar um addon proprietario paralelo. Rejeitado porque adiciona custo de manutencao e foge da documentacao oficial pedida para a entrega.

## Decision 2: Preferir `META-INF/processSVG/{processId}.svg` no classpath

**Decision**: Disponibilizar o diagrama SVG pelo caminho padrao `META-INF/processSVG/{processId}.svg` no classpath e deixar `kogito.svg.folder.path` apenas como opcao operacional de contingencia.

**Rationale**: A documentacao oficial do Kogito 10.1 informa que o addon busca os arquivos SVG primeiro em um diretorio definido por `kogito.svg.folder.path`; se a propriedade nao for usada, a busca ocorre em `META-INF/processSVG/` no classpath. A mesma documentacao descreve que a exportacao do BPMN para SVG pode ser copiada e renomeada para `META-INF/processSVG/{processId}.svg` durante o build, o que reduz dependencia de volume externo e simplifica reproducao local e em CI.

**Alternatives considered**:
- Configurar apenas `kogito.svg.folder.path` apontando para um volume do host. Rejeitado porque aumenta acoplamento operacional e fragilidade entre ambientes.
- Buscar SVG em local customizado fora do contrato do addon. Rejeitado porque desvia do padrao oficial e dificulta portabilidade.

## Decision 3: Convergir o diagrama para uma unica fonte oficial

**Decision**: Descontinuar o uso do SVG manual gerado por `ConsoleRuntimeEndpointService.processDiagram(...)` como fonte principal e alinhar o runtime para que o Management Console consuma o addon oficial `process-svg`.

**Rationale**: O repositorio ja possui um endpoint custom `/svg/processes/{processId}/instances/{processInstanceId}` que renderiza um SVG montado manualmente. Isso cria uma segunda fonte de verdade para o diagrama, em paralelo ao addon oficial e ao `diagram` retornado pelo ecossistema Kogito/Data Index. A feature pede o addon `Process SVG` no Management Console; portanto, o desenho correto e remover a duplicacao funcional e manter a customizacao apenas onde o addon nao cobre o caso.

**Alternatives considered**:
- Manter o endpoint manual e adicionar o addon oficial em paralelo. Rejeitado porque preserva ambiguidade operacional e risco de divergencia entre visoes.
- Migrar todo o contexto textual para dentro do endpoint manual. Rejeitado porque mistura regras de negocio do projeto com responsabilidade nativa do addon.

## Decision 4: Usar `ManagementConsoleSupport.currentStage` como fonte textual da etapa atual

**Decision**: Reutilizar a logica atual de `ManagementConsoleSupport` para definir a etapa textual do addon, incluindo o fallback para a ultima etapa concluida quando o processo estiver encerrado.

**Rationale**: O servico ja calcula `currentStage` com a regra certa para o projeto: etapa em execucao quando existir, senao ultima etapa concluida, senao `SEM_ETAPA_ATIVA`. Isso coincide com as clarificacoes do spec para processo encerrado e divergencia temporaria. Reusar essa fonte evita reimplementar a regra dentro do fluxo SVG.

**Alternatives considered**:
- Derivar a etapa textual apenas do estado visual do SVG. Rejeitado porque, em caso de divergencia temporaria, o spec definiu que o estado atual da instancia prevalece.
- Derivar a etapa textual apenas do Data Index. Rejeitado porque a regra de fallback ja existe no dominio local e e mais diretamente controlada pelo projeto.

## Decision 5: Endurecer a governanca de acesso e observabilidade da integracao

**Decision**: Revisar a exposicao atual dos caminhos SVG/management runtime e manter `GET` publico apenas para os endpoints tecnicamente exigidos pelo ecossistema do Management Console, adicionando sinais observaveis para sucesso, ausencia de SVG, ausencia de etapa e falhas de carregamento.

**Rationale**: O spec exige protecao contra exibicao indevida e evidencias para auditoria de falhas e divergencias. Hoje `SecurityConfig` permite acesso anonimo a `/svg/processes/**` e `/management/processes/**`. Como o console externo depende desses `GET` no ambiente atual, a estrategia de menor risco e manter apenas esses caminhos publicos para leitura tecnica do runtime e deixar os demais endpoints sob autenticacao normal do backend.

**Alternatives considered**:
- Preservar `permitAll` indefinidamente. Rejeitado porque conflita com `FR-009` e `NFR-001`.
- Bloquear tudo sem verificar como o Management Console consome o runtime. Rejeitado porque pode quebrar a integracao legitima sem desenho de compatibilidade.

## Decision 6: Validar a entrega com testes locais e stack externa

**Decision**: Cobrir a feature com testes de integracao no backend e com um roteiro manual/tecnico usando `infra/compose/docker-compose.yml` para validar Data Index, Task Console e Management Console.

**Rationale**: O projeto ja possui testes `KogitoConsoleIntegrationTest` e `ConsoleRuntimeEndpointsIntegrationTest`, alem de stack externa padronizada no compose. Isso e suficiente para validar elegibilidade do addon, disponibilidade do SVG, compatibilidade com o console e comportamento de fluxo encerrado sem inventar um harness novo.

**Alternatives considered**:
- Validar apenas por inspeção manual no browser. Rejeitado porque nao protege regressao de integracao.
- Criar uma nova suite E2E do zero. Rejeitado porque o repositorio ja possui base de validacao proporcional ao risco.
