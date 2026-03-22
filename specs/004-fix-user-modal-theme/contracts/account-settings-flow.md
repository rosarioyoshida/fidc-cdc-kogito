# Contract: Account Settings Flow

## Objective

Definir o comportamento esperado da janela de ajustes da conta para fechamento,
salvamento de email, salvamento de senha, preservacao de tema e retorno a navegacao
protegida.

## Actors

- Usuario autenticado

## Entry Points

- Botao ou acao do menu superior que abre a janela de ajustes da conta

## States

- `closed`: janela nao visivel
- `open-idle`: janela aberta sem alteracao pendente
- `open-dirty`: janela aberta com alteracao ainda nao salva
- `submitting`: operacao de email ou senha em andamento
- `success`: operacao concluida com sucesso
- `error`: operacao falhou e requer correcao do usuario

## Required Behaviors

1. A janela deve oferecer acao explicita de fechamento perceptivel ao usuario.
2. A acao explicita de fechamento deve estar disponivel no cabecalho do dialogo.
3. Fechar a janela nao deve salvar alteracoes implicitamente.
4. Salvar email deve manter a rota protegida de origem sem redirecionamento para a
   tela inicial.
5. Salvar senha deve manter a rota protegida de origem sem redirecionamento para a
   tela inicial.
6. O tema visual ativo antes da acao deve permanecer ativo apos sucesso.
7. O tema visual ativo antes da acao deve permanecer ativo apos falha.
8. Feedback de sucesso ou erro deve permanecer dentro do contexto atual sem enviar o
   usuario para a tela inicial.

## Accessibility Expectations

- O controle de fechamento deve ser focavel por teclado
- O foco deve permanecer visivel durante toda a interacao
- O dialogo deve permitir fechamento por acao explicita sem exigir apontador
- Estados de erro e sucesso devem ser perceptiveis por texto e nao apenas por cor

## Failure Handling

- Falha de validacao mantem a janela aberta
- Falha de salvamento mantem a rota protegida de origem
- Falha de salvamento nao redefine o modo visual do usuario
- Sucesso de salvamento nao desmonta o contexto protegido do usuario

## Notes

- Este contrato corrige fluxo existente e nao introduz novo endpoint obrigatorio.
- O backend existente de conta permanece a referencia para persistencia de email e
  senha, salvo ajuste minimo estritamente necessario.
