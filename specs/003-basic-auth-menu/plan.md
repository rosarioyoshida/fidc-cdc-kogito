# Implementation Plan: Controle de Acesso e Menu do Usuario

**Branch**: `003-basic-auth-menu` | **Date**: 2026-03-22 | **Spec**: [spec.md](D:/desenv/fidc-cdc-kogito/specs/003-basic-auth-menu/spec.md)
**Input**: Feature specification from `/specs/003-basic-auth-menu/spec.md`

## Summary

Implementar controle de acesso na interface com autenticacao Basic Auth alinhada ao
backend atual, exibir no menu superior a identidade ativa do usuario e permitir
gestao da propria conta com alteracao de email, alteracao de senha e logout
imediato. A feature deve reutilizar os perfis operacionais ja definidos no seed do
projeto e nao criar novos perfis de acesso.

## Technical Context

**Language/Version**: Java 21 no backend e TypeScript 5 / React 19 / Next.js 15 no frontend  
**Primary Dependencies**: Spring Boot 3.3, Spring Security, Spring HATEOAS, JPA/Hibernate, Bean Validation, Flyway, Log4j2/SLF4J, React, Next.js, shadcn/ui, Tailwind CSS, Vitest, Testing Library  
**Storage**: PostgreSQL para dados de usuarios e perfis seedados; estado de sessao e configuracao da interface no frontend; arquivos Markdown em `specs/003-basic-auth-menu/` para artefatos da feature  
**Testing**: JUnit e testes de integracao/contrato no backend; Vitest e Testing Library no frontend; validacao manual guiada para autenticacao, menu superior e logout  
**Target Platform**: Aplicacao web containerizada para Linux com execucao local por Docker Compose e uso via navegador moderno  
**Project Type**: Aplicacao web com backend REST e frontend Next.js  
**Performance Goals**: Usuario autorizado chega a uma tela protegida em ate 1 minuto; menu superior renderiza identidade ativa sem atrasos perceptiveis; logout invalida a sessao atual imediatamente na interface  
**Constraints**: Manter a stack atual; nao adicionar dependencias sem consulta previa; reutilizar Basic Auth ja adotado pelo backend; usar apenas os perfis seedados (`OPERADOR`, `ANALISTA`, `APROVADOR`, `AUDITOR`, `INTEGRACAO`); nao criar novos perfis de acesso; manter alinhamento com o Design System e com RFC 9457 para erros de autenticacao/conta  
**Scale/Scope**: Cobrir autenticacao da interface, menu superior de usuario, exibicao de nome e perfil, edicao da propria conta, alteracao da propria senha e logout nas telas protegidas do produto

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- Simplicity gate: aprovado. A feature reutiliza autenticacao Basic Auth existente,
  perfis seedados e o layout atual, evitando novos provedores de identidade,
  frameworks ou servicos.
- Architecture gate: aprovado. O plano separa autenticao/sessao, consulta de dados do
  usuario, atualizacao da conta e apresentacao do menu superior com fronteiras claras
  entre backend e frontend.
- API design gate: aprovado. Caso novos endpoints sejam necessarios para conta do
  usuario, eles devem seguir URIs baseadas em recursos, pluralizacao consistente,
  lowercase com hifens e uso correto de metodos HTTP.
- API versioning gate: aprovado. Qualquer endpoint novo ou alterado permanece sob a
  superficie versionada atual e deve respeitar a politica de breaking change da API.
- Hypermedia gate: aprovado. HATEOAS permanece aplicavel quando a navegacao contextual
  de conta e proximas acoes fizer parte da resposta do backend.
- API error contract gate: aprovado. Erros de autenticacao, autorizacao e atualizacao
  de conta devem seguir RFC 9457 sem expor detalhes internos.
- Maintainability/scalability gate: aprovado. A feature reutiliza perfis seedados,
  evita duplicacao de regras de acesso e preserva fronteiras entre UI, sessao e
  dados de conta.
- Security/compliance gate: aprovado. O plano cobre Basic Auth, perfis seedados,
  protecao de dados de conta, auditoria de login/logout e alteracao de credenciais.
- Observability gate: aprovado. Eventos de login, falha, atualizacao de conta e
  logout exigem logs, metricas e correlacao.
- UX/performance gate: aprovado. O menu superior deve manter hierarquia visual clara,
  foco visivel, navegacao por teclado, feedback de erro e consistencia entre telas
  protegidas.
- Frontend implementation gate: aprovado. A feature preserva React, Next.js,
  TypeScript, shadcn/ui como base estrutural e Design System da Atlassian como fonte
  normativa de comportamento e aparencia.

## Project Structure

### Documentation (this feature)

```text
specs/003-basic-auth-menu/
├── plan.md
├── research.md
├── data-model.md
├── implementation-notes.md
├── quickstart.md
├── contracts/
│   ├── auth-account-api.md
│   └── topbar-user-menu.md
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
│   └── security/
├── src/main/resources/
│   ├── application.yml
│   └── db/migration/
└── src/test/java/com/fidc/cdc/kogito/
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
    └── integration/

infra/
└── compose/
```

**Structure Decision**: A feature sera implementada na aplicacao web existente,
reutilizando o backend REST e o frontend Next.js ja presentes no repositorio. Os
artefatos de planejamento ficam isolados em `specs/003-basic-auth-menu/`.

## Phase 0: Research Outcomes

As decisoes consolidadas em
[research.md](D:/desenv/fidc-cdc-kogito/specs/003-basic-auth-menu/research.md)
resolvem os pontos de pesquisa necessarios:

- reutilizar Basic Auth e credenciais ja adotadas pelo backend;
- usar somente os perfis seedados em `V5__seed_roles_permissions.sql`;
- expor dados de conta e perfil no menu superior sem criar novos perfis;
- modelar atualizacao da propria conta do usuario como capacidade do proprio ator,
  nunca como administracao de terceiros;
- preservar RFC 9457, observabilidade e contratos REST existentes.

Nao ha `NEEDS CLARIFICATION` remanescente para a fase de desenho.

## Phase 1: Design Artifacts

### Data model

O modelo de dados detalhado esta em
[data-model.md](D:/desenv/fidc-cdc-kogito/specs/003-basic-auth-menu/data-model.md)
e define:

- `SessaoUsuario` para contexto autenticado;
- `ContaUsuario` para nome, email e credenciais do proprio usuario;
- `PerfilUsuario` limitado aos perfis seedados do projeto;
- `EventoAcesso` para login, falha, alteracao de conta e logout.

### Contracts

Os contratos definidos para a fase atual sao:

- [auth-account-api.md](D:/desenv/fidc-cdc-kogito/specs/003-basic-auth-menu/contracts/auth-account-api.md):
  contrato funcional dos endpoints de autenticacao e conta do proprio usuario
- [topbar-user-menu.md](D:/desenv/fidc-cdc-kogito/specs/003-basic-auth-menu/contracts/topbar-user-menu.md):
  contrato da experiencia do menu superior, estados e acoes disponiveis

### Operational bootstrap

O fluxo minimo de inicializacao e validacao esta em
[quickstart.md](D:/desenv/fidc-cdc-kogito/specs/003-basic-auth-menu/quickstart.md)
e contempla autenticacao com usuarios seedados, exibicao do menu superior, alteracao
de email, alteracao de senha e logout.

As notas auxiliares de implementacao e rastreabilidade de requisitos nao funcionais
ficam em
[implementation-notes.md](D:/desenv/fidc-cdc-kogito/specs/003-basic-auth-menu/implementation-notes.md).

## Phase 2: Implementation Direction

### Backend

- Reutilizar o modelo de autenticacao Basic Auth ja presente no backend para proteger
  as rotas necessarias pela interface.
- Reutilizar os usuarios e perfis seedados existentes como fonte de identidade e
  contexto de perfil visivel na interface.
- Expor ou ajustar endpoints para obter a conta do proprio usuario autenticado,
  alterar o proprio email e alterar a propria senha sem criar administracao de
  terceiros.
- Garantir que erros de autenticacao, autorizacao e validacao de conta retornem em
  formato RFC 9457.
- Registrar eventos auditaveis para login, falha de autenticacao, alteracao de conta
  e logout.

### Frontend

- Introduzir fluxo de autenticacao e persistencia controlada da sessao compatível com
  Basic Auth adotado pelo backend.
- Exibir nome e perfil do usuario autenticado no menu superior em todas as telas
  protegidas relevantes.
- Disponibilizar acoes de conta no menu superior para alteracao do proprio email,
  alteracao da propria senha e logout.
- Garantir foco visivel, navegacao por teclado, estados de erro, loading e feedback
  coerentes com o Design System do projeto.

### Access model and seeded profiles

- Reutilizar exclusivamente os perfis seedados `OPERADOR`, `ANALISTA`, `APROVADOR`,
  `AUDITOR` e `INTEGRACAO`.
- Nao criar novos perfis ou novas combinacoes de permissao fora do seed desta fase.
- Exibir no menu superior o perfil efetivo associado ao usuario autenticado conforme
  seed e contexto de sessao.

## Post-Design Constitution Re-Check

- Simplicidade: mantida. Nenhum novo provedor de identidade ou perfil foi introduzido.
- Arquitetura e coesao: mantidas. Sessao, conta e menu superior seguem fronteiras
  explicitas.
- Seguranca e compliance: fortalecidas. A feature trata login, logout, alteracao de
  credenciais e auditoria desde o desenho.
- Observabilidade: fortalecida. Eventos de autenticacao e conta ficam explicitamente
  cobertos.
- UX e frontend: mantidos. O menu superior e as acoes da conta seguem criterios de
  acessibilidade, consistencia e feedback.
- Restricao de perfis: mantida. Apenas perfis seedados do projeto sao considerados.

## Complexity Tracking

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| None | N/A | N/A |
