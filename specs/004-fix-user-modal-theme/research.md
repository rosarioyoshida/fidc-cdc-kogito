# Research: Fechamento de Ajustes e Persistencia de Tema

## Decision 1: Reutilizar a janela de ajustes existente

**Decision**: Corrigir a janela de ajustes de conta existente, adicionando uma acao
de fechamento explicita e consistente para todo o fluxo do dialogo.

**Rationale**: O problema relatado e comportamental, nao estrutural. Reutilizar o
dialogo atual preserva KISS, evita refatoracao ampla do menu superior e reduz risco
de regressao visual.

**Alternatives considered**:

- Criar uma nova tela dedicada para ajustes de conta: rejeitado por ampliar escopo e
  quebrar o fluxo atual sem necessidade.
- Reutilizar apenas fechamento por clique fora da janela: rejeitado por nao oferecer
  controle explicito suficiente nem atender o requisito de visibilidade.

## Decision 2: Preservar o tema a partir da fonte de verdade ja adotada

**Decision**: Manter o modo visual ativo a partir do estado ja persistido da sessao
do frontend, sem redefinir a interface para light apos salvar email ou senha.

**Rationale**: O projeto ja adota uma fonte de verdade para o tema na camada de UI.
Reinicializar o tema no pos-salvamento cria regressao visivel e quebra a expectativa
de persistencia da preferencia do usuario.

**Alternatives considered**:

- Reaplicar sempre o tema light apos salvamento: rejeitado por contradizer o requisito
  da feature.
- Duplicar a persistencia de tema dentro de cada formulario: rejeitado por gerar DRY
  violation e fragilidade de manutencao.

## Decision 3: Retornar para a rota protegida de origem

**Decision**: Salvar email ou senha deve concluir o fluxo retornando o usuario para a
rota protegida de origem, preservando o contexto funcional anterior.

**Rationale**: O redirecionamento para a tela inicial quebra a tarefa em andamento e
gera custo operacional desnecessario. A rota de origem e a referencia mais simples e
coesa para o pos-salvamento.

**Alternatives considered**:

- Redirecionar sempre para `/`: rejeitado por quebrar contexto e por reproduzir o
  defeito atual.
- Redirecionar sempre para `/cessoes`: rejeitado por ainda ignorar a origem real do
  usuario em telas internas.

## Decision 4: Concentrar a correcao no frontend, com backend estavel

**Decision**: Planejar a feature como correcao predominantemente de frontend, tocando
o backend apenas se houver necessidade minima para preservar contratos existentes.

**Rationale**: O problema central relatado esta na experiencia da janela, na
persistencia de tema e no redirecionamento. Alterar o backend sem necessidade
concreta aumentaria acoplamento e risco sem ganho proporcional.

**Alternatives considered**:

- Introduzir novo endpoint apenas para pos-salvamento: rejeitado por YAGNI.
- Reestruturar o fluxo de autenticacao para resolver o tema: rejeitado por fugir do
  escopo e ampliar risco operacional.

## Operational Notes

**Audit expectations**:

- registrar evidencias de fechamento explicito da janela como comportamento de UX
  esperado e verificavel no fluxo corrigido;
- manter rastreabilidade de sucesso e falha nas operacoes de email e senha sem criar
  novo evento de negocio fora da superficie atual de conta.

**Observable signals**:

- o fluxo deve permitir identificar quando o salvamento conclui sem redirecionamento
  indevido;
- o fluxo deve permitir verificar que o modo visual ativo foi preservado apos salvar;
- falhas de salvamento devem permanecer perceptiveis no proprio contexto protegido do
  usuario.
