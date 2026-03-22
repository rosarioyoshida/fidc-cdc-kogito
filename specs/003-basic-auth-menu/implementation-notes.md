# Implementation Notes: Controle de Acesso e Menu do Usuario

## API mapping

- `GET /api/v1/usuarios/me`: resolve identidade autenticada, email e perfil seedado
- `PATCH /api/v1/usuarios/me`: atualiza o proprio email
- `POST /api/v1/usuarios/me/alteracao-senha`: atualiza a propria senha
- `DELETE /api/v1/usuarios/me/sessao`: registra logout e permite invalidacao imediata da sessao da interface

## Audit expectations

- login bem-sucedido gera log estruturado com ator, perfil e correlation id
- falha de autenticacao gera log estruturado sem vazar detalhes sensiveis
- alteracao de email e senha gera log estruturado do proprio titular
- logout explicito gera log estruturado com correlation id

## Observability expectations

- contador `fidc.auth.events` para login-sucesso, login-falha e logout
- contador `fidc.account.events` para alteracao de email e senha
- erros de autenticacao e conta mantem contrato RFC 9457
- correlation id preservado no ciclo de autenticacao e conta
