# Contract: Auth and Account API

## Purpose

Definir o comportamento esperado da superficie de backend necessaria para a interface
obter dados do usuario autenticado e atualizar a propria conta.

## Resources

### `GET /api/v1/usuarios/me`

- **Purpose**: Retorna os dados do usuario autenticado para o menu superior e a area de conta.
- **Response fields**:
  - `username`
  - `nomeExibicao`
  - `email`
  - `perfilAtivo`
  - links ou acoes relevantes quando aplicavel

### `PATCH /api/v1/usuarios/me`

- **Purpose**: Atualiza dados editaveis da propria conta do usuario autenticado.
- **Allowed updates**:
  - `email`

### `POST /api/v1/usuarios/me/alteracao-senha`

- **Purpose**: Permite ao proprio usuario alterar sua senha.
- **Request expectations**:
  - validacao da identidade do proprio titular
  - feedback claro para sucesso e falha

### `DELETE /api/v1/usuarios/me/sessao`

- **Purpose**: Registra o logout explicito e encerra a sessao mantida pela interface.
- **Response**:
  - `204 No Content`

## Security Rules

- toda a superficie exige autenticacao Basic Auth valida;
- o usuario so pode consultar e alterar a propria conta;
- o perfil exibido e derivado dos perfis seedados do projeto.

## Error Contract

- erros de autenticacao, autorizacao e validacao usam RFC 9457;
- payloads nao devem vazar detalhes internos;
- falhas de atualizacao de conta devem permitir correcao pelo usuario.
