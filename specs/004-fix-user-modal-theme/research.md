# Research: Fechamento de Ajustes e Persistencia de Tema

## Decision 1: Migrar o fluxo de ajustes para componentes aderentes ao padrao tecnico exigido

**Decision**: Corrigir a janela de ajustes existente migrando o fluxo para
componentes aderentes ao padrao tecnico exigido pela constituicao, sem aceitar o
reuso de componentes locais nao conformes como base da feature.

**Rationale**: A constituicao 1.9.0 tornou o requisito de base tecnica
nao-negociavel. Mesmo com retrabalho, a feature nao pode consolidar dependencias
em componentes fora do padrao definido.

**Alternatives considered**:

- Reutilizar o dialogo local atual como base da feature: rejeitado por conflito com
  a constituicao.
- Criar nova tela dedicada para ajustes de conta: rejeitado por ampliar escopo e
  quebrar o fluxo atual sem necessidade.

## Decision 2: Confirmar descarte apenas quando houver alteracoes nao salvas

**Decision**: Ao fechar a janela com alteracoes pendentes, o sistema deve solicitar
confirmacao de descarte; sem alteracoes pendentes, o fechamento ocorre
imediatamente.

**Rationale**: Essa abordagem reduz perda acidental de dados sem introduzir atrito
desnecessario no fechamento normal da janela.

**Alternatives considered**:

- Fechar sempre imediatamente: rejeitado por risco de descarte silencioso.
- Bloquear fechamento enquanto houver alteracoes: rejeitado por piorar UX e fugir
  do fluxo esperado para dialogos de conta.

## Decision 3: Tratar `localStorage` como fonte primaria do tema

**Decision**: A preferencia visual do usuario deve usar `localStorage` como fonte
primaria de verdade, sincronizando o atributo `data-theme` do documento e o cookie
de tema durante bootstrap e interacoes.

**Rationale**: O frontend atual ja le e escreve o tema por esse eixo. Formalizar
essa fonte primaria evita divergencia entre bootstrap, toggle, salvamento e reload.

**Alternatives considered**:

- Tratar cookie como fonte primaria: rejeitado por contrariar o comportamento atual
  do frontend e aumentar chance de drift entre cliente e documento.
- Duplicar a regra de persistencia dentro de cada formulario: rejeitado por violar
  DRY e espalhar responsabilidade por varios pontos da UI.

## Decision 4: Fechar a janela automaticamente apos sucesso e exibir feedback no contexto protegido

**Decision**: Salvamentos bem-sucedidos de email e senha devem fechar a janela
automaticamente e exibir feedback de sucesso na tela protegida de origem.

**Rationale**: A janela deixa de capturar o foco depois que a acao conclui e o
usuario recebe confirmacao no proprio contexto funcional, sem precisar permanecer no
dialogo.

**Alternatives considered**:

- Manter a janela aberta apos sucesso: rejeitado por manter UI transitiva aberta sem
  necessidade e conflitar com a expectativa de conclusao.
- Exibir sucesso apenas dentro da janela antes de fechar: rejeitado por introduzir
  estado intermediario desnecessario e mais fragil para teste.

## Decision 5: Preservar a rota protegida de origem sem tocar no backend

**Decision**: O retorno apos salvar ou falhar deve permanecer na rota protegida de
origem, sem redirecionamento para a tela inicial, e a correcao deve ficar
predominantemente no frontend.

**Rationale**: O problema relatado esta no fluxo da UI e nao exige nova API nem
mudanca contratual do backend para ser resolvido.

**Alternatives considered**:

- Redirecionar sempre para `/`: rejeitado por reproduzir o defeito atual.
- Introduzir novo endpoint so para controlar pos-salvamento: rejeitado por YAGNI.

## Operational Notes

**Audit expectations**:

- manter rastreabilidade dos eventos ja existentes de sucesso e falha em alteracao de
  email e senha, sem criar novo evento de negocio;
- tratar cancelamento da confirmacao de descarte como comportamento de UX verificavel,
  nao como novo evento de auditoria obrigatorio.

**Security notes**:

- o fluxo nao deve expor senha atual, nova senha ou credenciais em logs, mensagens de
  erro ou feedback visual;
- a sessao autenticada deve permanecer valida durante atualizacao de email;
- apos alteracao de senha, o mecanismo de sessao deve refletir a nova credencial sem
  romper o contexto protegido;
- a preservacao de contexto protegido nao pode redirecionar o usuario para rotas
  publicas enquanto a sessao permanecer autentica.

**Observable signals**:

- o fluxo deve permitir identificar quando o salvamento conclui sem redirecionamento
  indevido;
- o fluxo deve permitir verificar que o modo visual ativo foi preservado apos salvar;
- falhas de salvamento devem permanecer perceptiveis no proprio contexto protegido do
  usuario;
- o cancelamento do descarte deve preservar a janela aberta e os valores em edicao.
