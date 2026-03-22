# Research: Controle de Acesso e Menu do Usuario

## Decision 1: Reutilizar Basic Auth ja adotado pelo backend

- **Decision**: A interface deve autenticar usando o modelo Basic Auth ja aceito pelo
  backend atual.
- **Rationale**: O repositorio ja possui autenticacao Basic Auth funcional e usuarios
  padrao definidos. Reutilizar esse modelo reduz complexidade e mantem alinhamento com
  a restricao de nao adicionar dependencias sem consulta.
- **Alternatives considered**:
  - Adotar um novo provedor de sessao no frontend: rejeitado por ampliar escopo.
  - Migrar a feature para outro mecanismo de autenticacao nesta fase: rejeitado por
    introduzir rework e dependencias fora do pedido atual.

## Decision 2: Usar somente perfis seedados do projeto

- **Decision**: Os perfis permitidos para esta feature sao somente `OPERADOR`,
  `ANALISTA`, `APROVADOR`, `AUDITOR` e `INTEGRACAO`, conforme seed atual.
- **Rationale**: O usuario explicitou essa restricao e o projeto ja materializa esses
  perfis em `V5__seed_roles_permissions.sql`.
- **Alternatives considered**:
  - Criar novo perfil para gestao de conta: rejeitado por ser desnecessario.
  - Ignorar perfis seedados e derivar o perfil apenas de credenciais do frontend:
    rejeitado por causar divergencia com o backend.

## Decision 3: Gestao de conta e self-service

- **Decision**: A feature cobre alteracao do proprio email e da propria senha pelo
  usuario autenticado, sem incluir administracao de usuarios de terceiros.
- **Rationale**: Isso atende ao objetivo do menu superior e mantem o escopo restrito a
  self-service.
- **Alternatives considered**:
  - Apenas atalhos de navegacao sem alteracao real: rejeitado pela clarificacao.
  - Gestao administrativa de usuarios: rejeitado como fora de escopo.

## Decision 4: Logout invalida a sessao imediatamente

- **Decision**: O logout deve invalidar imediatamente a sessao atual e exigir novo
  login para qualquer retorno a area protegida.
- **Rationale**: Isso reduz risco em estacoes compartilhadas e melhora previsibilidade
  da seguranca.
- **Alternatives considered**:
  - Manter sessao ate expirar: rejeitado por ambiguidade de acesso.
  - Encerrar apenas ao fechar navegador: rejeitado por fragilidade operacional.

## Decision 5: Erros e observabilidade seguem contratos existentes

- **Decision**: Falhas de autenticacao, atualizacao de conta e logout devem seguir
  RFC 9457 e gerar trilha de auditoria e sinais operacionais.
- **Rationale**: O projeto ja adota esse padrao para APIs e exige observabilidade por
  constituicao.
- **Alternatives considered**:
  - Retornar erros ad hoc para a feature: rejeitado por inconsistencia.
