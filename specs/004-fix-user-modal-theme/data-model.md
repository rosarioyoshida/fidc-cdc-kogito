# Data Model: Fechamento de Ajustes e Persistencia de Tema

## JanelaAjustesConta

**Description**: Estado funcional da janela de ajustes do proprio usuario.

**Fields**:

- `aberta`: indica se a janela esta visivel
- `origemDeRetorno`: identifica a rota protegida de onde o fluxo foi iniciado
- `alteracoesPendentes`: indica se houve edicao ainda nao salva
- `confirmacaoDescarteAberta`: indica se o usuario esta decidindo descartar ou nao
- `estadoSubmissao`: representa ocioso, submetendo, sucesso ou falha
- `destinoFeedback`: identifica que o feedback de sucesso deve ser renderizado na
  tela protegida apos fechamento automatico

**Relationships**:

- referencia a `PreferenciaTemaVisual` para manter consistencia da experiencia
- referencia a `RetornoPosSalvamento` para decidir navegacao apos salvar ou fechar

**Validation Rules**:

- o fechamento sem alteracoes pendentes pode ocorrer imediatamente
- o fechamento com alteracoes pendentes deve exigir confirmacao de descarte
- cancelar a confirmacao de descarte deve manter a janela aberta e preservar os
  valores em edicao
- sucesso de salvamento deve fechar a janela automaticamente
- falha de salvamento deve manter a janela aberta e o contexto atual

## PreferenciaTemaVisual

**Description**: Preferencia visual ativa durante a sessao do usuario.

**Fields**:

- `modoAtivo`: modo visual atualmente selecionado
- `fontePrimaria`: `localStorage`
- `cookieSincronizado`: indica se o cookie de tema acompanha o modo ativo
- `atributoDocumentoSincronizado`: indica se `document.documentElement.dataset.theme`
  acompanha o modo ativo

**Relationships**:

- e lida e preservada durante operacoes da `JanelaAjustesConta`
- influencia a experiencia percebida em `RetornoPosSalvamento`

**Validation Rules**:

- o modo ativo apos salvar deve ser igual ao modo ativo antes de iniciar a acao
- falhas operacionais nao podem redefinir a preferencia para light por padrao
- `localStorage` deve prevalecer sobre cookie e atributo do documento em caso de
  restauracao do estado

## RetornoPosSalvamento

**Description**: Contexto de navegacao que define para onde o usuario volta apos
salvar, falhar ou fechar a janela de ajustes.

**Fields**:

- `rotaProtegidaAtual`: tela autenticada de origem
- `resultadoDaAcao`: sucesso, falha, cancelamento ou descarte-confirmado
- `redirecionamentoPermitido`: identifica se ha navegacao legitima fora da origem
- `feedbackSucessoVisivel`: indica se a mensagem de sucesso foi entregue no contexto
  protegido

**Relationships**:

- recebe informacao de `JanelaAjustesConta`
- deve respeitar a `PreferenciaTemaVisual` durante a transicao

**Validation Rules**:

- sucesso no salvamento nao pode enviar o usuario para a tela inicial por padrao
- sucesso deve manter a rota protegida de origem e exibir feedback no mesmo contexto
- falha deve manter a rota protegida de origem e a janela aberta
- cancelamento deve retornar o usuario ao contexto protegido anterior sem efeitos
  colaterais
