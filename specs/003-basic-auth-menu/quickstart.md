# Quickstart: Controle de Acesso e Menu do Usuario

## Objetivo

Validar localmente a autenticacao da interface, a exibicao do menu superior do usuario
e as acoes de conta usando os usuarios e perfis seedados do projeto.

## Pre-requisitos

- backend e frontend executando localmente
- usuarios seedados disponiveis no ambiente
- nenhuma nova dependencia adicionada sem consulta previa

## Perfis e usuarios seedados

Use os perfis ja definidos em
[V5__seed_roles_permissions.sql](D:/desenv/fidc-cdc-kogito/backend/src/main/resources/db/migration/V5__seed_roles_permissions.sql):

- `OPERADOR`
- `ANALISTA`
- `APROVADOR`
- `AUDITOR`
- `INTEGRACAO`

Usuarios seedados de referencia:

- `operador`
- `analista`
- `aprovador`
- `auditor`
- `integracao`

## Fluxo minimo de validacao

1. Abrir a interface sem sessao autenticada e confirmar que a area protegida exige login.
2. Autenticar com um usuario seedado valido.
3. Confirmar que o menu superior exibe nome e perfil corretos.
4. Abrir as opcoes de conta e atualizar o proprio email.
5. Executar alteracao da propria senha.
6. Reautenticar usando a nova senha para confirmar que a alteracao entrou em vigor.
7. Executar logout e confirmar que qualquer retorno a area protegida exige novo login.

## Evidencias esperadas

- autenticacao aceita apenas com credenciais validas
- nome e perfil exibidos no menu superior
- perfil exibido corresponde a um perfil seedado do projeto
- atualizacao do proprio email concluida com feedback claro
- alteracao da propria senha concluida com feedback claro
- novo login com a senha alterada concluido com sucesso
- logout invalida a sessao imediatamente
