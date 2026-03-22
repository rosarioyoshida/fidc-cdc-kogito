# Quickstart: Fechamento de Ajustes e Persistencia de Tema

## Preconditions

- Aplicacao backend e frontend em execucao
- Usuario autenticado em uma tela protegida
- Janela de ajustes da conta acessivel pelo menu superior
- Tema alternavel por `ThemeToggle` ou mecanismo equivalente ja disponivel

## Scenario 1: Fechar a janela sem alteracoes pendentes

1. Abrir a janela de ajustes da conta.
2. Sem alterar nenhum campo, acionar o botao de fechar.
3. Confirmar que a janela e encerrada imediatamente.
4. Confirmar que a tela protegida anterior continua visivel.

## Scenario 2: Fechar a janela com alteracoes pendentes e cancelar descarte

1. Abrir a janela de ajustes da conta.
2. Alterar um campo sem submeter.
3. Acionar o botao de fechar.
4. Confirmar que o sistema apresenta confirmacao de descarte.
5. Cancelar o descarte.
6. Confirmar que a janela permanece aberta.
7. Confirmar que o valor editado continua presente no formulario.

## Scenario 3: Salvar email preservando tema e contexto

1. Selecionar um modo visual diferente do light padrao.
2. Abrir a janela de ajustes da conta.
3. Alterar o email.
4. Salvar a alteracao.
5. Confirmar que a janela fecha automaticamente.
6. Confirmar mensagem de sucesso na tela protegida.
7. Confirmar que o modo visual ativo permanece o mesmo de antes da acao.
8. Confirmar que nao houve redirecionamento para a tela inicial.

## Scenario 4: Salvar senha preservando tema e contexto

1. Selecionar um modo visual diferente do light padrao.
2. Abrir a janela de ajustes da conta.
3. Informar senha atual e nova senha.
4. Salvar a alteracao.
5. Confirmar que a janela fecha automaticamente.
6. Confirmar mensagem de sucesso na tela protegida.
7. Confirmar que o modo visual ativo permanece o mesmo de antes da acao.
8. Confirmar que nao houve retorno para a tela inicial.

## Scenario 5: Falha de salvamento sem perda de contexto

1. Abrir a janela de ajustes da conta em uma tela protegida interna.
2. Provocar uma falha de validacao ou salvamento.
3. Confirmar que a janela continua aberta.
4. Confirmar que o usuario permanece no mesmo contexto protegido.
5. Confirmar que o tema visual nao foi redefinido.
6. Confirmar que a mensagem de erro permanece visivel na propria janela, sem
   redirecionamento e sem perda do contexto protegido de origem.

## Validation Notes

- O fluxo deve substituir, dentro do escopo da feature, componentes nao aderentes por
  componentes alinhados ao padrao tecnico exigido pela constituicao.
- O bootstrap de tema deve continuar coerente entre `localStorage`, cookie e
  atributo `data-theme` do documento apos reload da pagina.
