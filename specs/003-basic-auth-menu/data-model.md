# Data Model: Controle de Acesso e Menu do Usuario

## 1. SessaoUsuario

- **Purpose**: Representa o contexto autenticado atual da interface.
- **Core fields**:
  - `usuarioId`: identificador do usuario autenticado
  - `username`: credencial publica usada no login
  - `nomeExibicao`: nome mostrado no menu superior
  - `perfilAtivo`: perfil exibido no menu superior
  - `autenticadoEm`: data/hora de autenticacao
  - `sessaoValida`: indica se a sessao continua ativa
- **Validation rules**:
  - sessao valida exige usuario autenticado e perfil seedado associado
  - logout torna `sessaoValida` falsa imediatamente

## 2. PerfilUsuario

- **Purpose**: Representa o papel operacional associado ao usuario.
- **Allowed values**:
  - `OPERADOR`
  - `ANALISTA`
  - `APROVADOR`
  - `AUDITOR`
  - `INTEGRACAO`
- **Validation rules**:
  - apenas perfis seedados do projeto sao aceitos nesta feature
  - a interface nao cria nem altera perfis

## 3. ContaUsuario

- **Purpose**: Representa os dados gerenciaveis pelo proprio usuario autenticado.
- **Core fields**:
  - `usuarioId`: identificador do titular da conta
  - `nomeExibicao`: nome visivel no menu superior
  - `email`: email editavel pelo titular
  - `senhaAtual`: credencial atual exigida para alteracao segura
  - `novaSenha`: novo valor informado pelo titular
  - `perfilAtivo`: perfil seedado exibido na interface
- **Validation rules**:
  - email deve permanecer associado ao proprio titular
  - alteracao de senha exige validacao da identidade do proprio usuario

## 4. EventoAcesso

- **Purpose**: Registra eventos relevantes de seguranca e conta.
- **Core fields**:
  - `tipo`: login-sucesso, login-falha, conta-atualizada, senha-alterada, logout
  - `usuarioId`: usuario relacionado quando conhecido
  - `perfil`: perfil associado ao evento quando aplicavel
  - `ocorridoEm`: data/hora do evento
  - `resultado`: sucesso ou falha
  - `contexto`: metadados minimos para auditoria
- **Validation rules**:
  - login falho pode nao ter `usuarioId` confirmado
  - logout deve sempre gerar evento quando disparado por sessao valida

## Relationships Summary

- `SessaoUsuario` referencia um `PerfilUsuario`
- `ContaUsuario` pertence a um unico usuario autenticado por `SessaoUsuario`
- `EventoAcesso` pode referenciar `SessaoUsuario`, `ContaUsuario` e `PerfilUsuario`

## Lifecycle

1. Usuario informa credenciais
2. Sistema autentica e cria `SessaoUsuario`
3. Interface carrega `ContaUsuario` e `PerfilUsuario`
4. Usuario visualiza e opcionalmente atualiza a propria conta
5. Usuario executa logout
6. Sistema invalida `SessaoUsuario` e registra `EventoAcesso`
