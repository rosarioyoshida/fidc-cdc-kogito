# Research: Avaliacao de Boas Praticas do Projeto

## Decision 1: Preservar integralmente a stack atual do projeto

- **Decision**: Toda implementacao derivada desta feature deve permanecer sobre a
  stack atual do repositorio: Java 21 + Spring Boot/Kogito no backend e TypeScript +
  React + Next.js no frontend.
- **Rationale**: A feature trata de avaliacao de aderencia do projeto. Introduzir nova
  stack aumentaria variacao tecnica sem melhorar o objetivo principal, que e medir e
  fortalecer a qualidade do que ja existe.
- **Alternatives considered**:
  - Adotar ferramenta dedicada de auditoria ou quality platform externa: rejeitado por
    ampliar complexidade e dependencia operacional.
  - Implementar pipeline separado com nova stack de scripting: rejeitado por quebrar a
    restricao explicita do usuario e do plano.

## Decision 2: Nao adicionar dependencias sem consulta previa

- **Decision**: Nenhuma nova dependencia de backend, frontend ou infraestrutura deve
  ser adicionada automaticamente como parte desta feature sem consulta previa.
- **Rationale**: O objetivo imediato e formalizar criterios, relatorio e fluxo de
  avaliacao. O repositorio ja possui bibliotecas e ferramentas suficientes para isso.
- **Alternatives considered**:
  - Adicionar bibliotecas de linting documental ou geracao automatica de relatorios:
    rejeitado por nao ser necessario nesta fase.
  - Adicionar framework de policy-as-code: rejeitado por extrapolar o escopo atual.

## Decision 3: Baseline de avaliacao usa diretrizes do projeto e constituicao

- **Decision**: Os criterios de avaliacao devem partir das diretrizes do projeto
  existentes no repositorio e da constituicao vigente, complementando lacunas com boas
  praticas amplamente aceitas de engenharia.
- **Rationale**: Isso preserva a identidade tecnica do projeto, evita subjetividade e
  ainda permite decidir quando uma diretriz local for insuficiente.
- **Alternatives considered**:
  - Usar apenas diretrizes locais: rejeitado por deixar lacunas nao verificaveis.
  - Usar somente referencia externa generica: rejeitado por ignorar padroes ja
    assumidos pela equipe.

## Decision 4: Avaliacao recorrente por fase relevante

- **Decision**: A feature deve suportar ciclos repetidos de avaliacao por fase
  relevante do projeto, com comparabilidade entre pareceres.
- **Rationale**: Uma avaliacao pontual perde valor rapidamente e nao ajuda a controlar
  regressao ou evolucao da aderencia.
- **Alternatives considered**:
  - Fazer avaliacao unica do estado atual: rejeitado por gerar diagnostico sem
    mecanismo claro de acompanhamento.
  - Executar avaliacao apenas para liberar a fase imediata: rejeitado por limitar a
    utilidade do investimento documental.

## Decision 5: O contrato principal da feature e documental

- **Decision**: A entrega principal desta feature e um contrato documental para o
  relatorio de avaliacao, nao uma nova API ou servico.
- **Rationale**: O consumidor principal e o stakeholder tecnico/gestor que precisa de
  um parecer rastreavel e acionavel, e nao uma integracao sistêmica nova.
- **Alternatives considered**:
  - Expor endpoint de avaliacao: rejeitado por nao ser exigido pelo spec atual.
  - Criar armazenamento dedicado para historico de avaliacoes: rejeitado nesta fase,
    pois o escopo pode ser atendido com artefatos versionados no repositorio.

## Decision 6: Evidencias devem vir dos artefatos e validacoes atuais

- **Decision**: O processo deve usar como evidencia principal codigo, configuracoes,
  testes, contratos, compose, observabilidade e specs ja presentes no repositorio.
- **Rationale**: Isso reduz custo de adocao, aumenta repetibilidade e mantem a
  avaliacao auditavel no mesmo contexto de desenvolvimento.
- **Alternatives considered**:
  - Exigir coleta manual fora do repositorio como padrao: rejeitado por reduzir
    repetibilidade.
  - Criar nova telemetria apenas para a feature: rejeitado por desnecessario no
    contexto atual.
