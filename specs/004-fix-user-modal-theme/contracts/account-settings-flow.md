# Contract: Account Settings Flow

## Objective

Definir o comportamento esperado da janela de ajustes da conta para fechamento,
confirmacao de descarte, salvamento de email, salvamento de senha, preservacao de
tema e retorno a navegacao protegida.

## Actors

- Usuario autenticado

## Entry Points

- Botao ou acao do menu superior que abre a janela de ajustes da conta

## States

- `closed`: janela nao visivel
- `open-idle`: janela aberta sem alteracao pendente
- `open-dirty`: janela aberta com alteracao ainda nao salva
- `discard-confirmation`: confirmacao de descarte aberta
- `submitting`: operacao de email ou senha em andamento
- `success`: operacao concluida com sucesso, com fechamento automatico da janela
- `error`: operacao falhou e requer correcao do usuario

## Required Behaviors

1. A janela deve oferecer acao explicita de fechamento perceptivel ao usuario.
2. A acao explicita de fechamento deve estar disponivel no cabecalho do dialogo.
3. Fechar a janela sem alteracoes pendentes deve encerrar o dialogo imediatamente.
4. Fechar a janela com alteracoes pendentes deve abrir confirmacao de descarte.
5. Cancelar a confirmacao de descarte deve manter a janela aberta e preservar os
   valores em edicao.
6. Confirmar o descarte deve fechar a janela sem salvar alteracoes.
7. Salvar email deve manter a rota protegida de origem, fechar a janela
   automaticamente e exibir feedback de sucesso na tela protegida.
8. Salvar senha deve manter a rota protegida de origem, fechar a janela
   automaticamente e exibir feedback de sucesso na tela protegida.
9. O tema visual ativo antes da acao deve permanecer ativo apos sucesso.
10. O tema visual ativo antes da acao deve permanecer ativo apos falha.
11. A preferencia de tema deve usar `localStorage` como fonte primaria, com
    sincronizacao para cookie e atributo `data-theme` do documento.
12. Feedback de falha deve permanecer visivel na propria janela sem perda do
    contexto protegido.
13. O fluxo deve migrar os componentes nao aderentes do ajuste de conta para
    componentes alinhados ao padrao tecnico exigido pela constituicao.

## Accessibility Expectations

- O controle de fechamento deve ser focavel por teclado
- O foco deve permanecer visivel durante toda a interacao
- O dialogo deve permitir fechamento por acao explicita sem exigir apontador
- A confirmacao de descarte deve ser operavel por teclado
- Estados de erro e sucesso devem ser perceptiveis por texto e nao apenas por cor

## Failure Handling

- Falha de validacao mantem a janela aberta
- Falha de salvamento mantem a rota protegida de origem
- Falha de salvamento nao redefine o modo visual do usuario
- Sucesso de salvamento nao desmonta o contexto protegido do usuario
- Cancelamento da confirmacao de descarte nao descarta valores em edicao

## Notes

- Este contrato corrige fluxo existente e nao introduz novo endpoint obrigatorio.
- O backend existente de conta permanece a referencia para persistencia de email e
  senha, salvo ajuste minimo estritamente necessario.
