# Quickstart: Fechamento de Ajustes e Persistencia de Tema

## Preconditions

- Aplicacao backend e frontend em execucao
- Usuario autenticado em uma tela protegida
- Janela de ajustes da conta acessivel pelo menu superior

## Scenario 1: Fechar a janela sem salvar

1. Abrir a janela de ajustes da conta.
2. Alterar um campo sem submeter.
3. Acionar o botao de fechar.
4. Confirmar que a janela e encerrada.
5. Confirmar que nenhuma alteracao foi aplicada automaticamente.
6. Confirmar que a tela protegida anterior continua visivel.

## Scenario 2: Salvar email preservando tema

1. Selecionar um modo visual diferente do light padrao.
2. Abrir a janela de ajustes da conta.
3. Alterar o email.
4. Salvar a alteracao.
5. Confirmar mensagem de sucesso no contexto protegido.
6. Confirmar que o modo visual ativo permanece o mesmo de antes da acao.
7. Confirmar que nao houve redirecionamento para a tela inicial.
8. Confirmar que o cabecalho da pagina protegida continua o mesmo apos a operacao.

## Scenario 3: Salvar senha preservando tema

1. Selecionar um modo visual diferente do light padrao.
2. Abrir a janela de ajustes da conta.
3. Informar senha atual e nova senha.
4. Salvar a alteracao.
5. Confirmar mensagem de sucesso no contexto protegido.
6. Confirmar que o modo visual ativo permanece o mesmo de antes da acao.
7. Confirmar que nao houve retorno para a tela inicial.
8. Confirmar que a janela de ajustes permanece sob controle do usuario apos a acao.

## Scenario 4: Falha de salvamento sem perda de contexto

1. Abrir a janela de ajustes da conta em uma tela protegida interna.
2. Provocar uma falha de validacao ou salvamento.
3. Confirmar que a janela continua aberta ou que o contexto funcional atual e mantido.
4. Confirmar que o tema visual nao foi redefinido.
5. Confirmar que o usuario nao foi redirecionado para a tela inicial.
6. Confirmar que a mensagem de erro permanece visivel no proprio contexto protegido.
