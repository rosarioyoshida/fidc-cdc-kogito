# Data Model: Fechamento de Ajustes e Persistencia de Tema

## JanelaAjustesConta

**Description**: Estado funcional da janela de ajustes do proprio usuario.

**Fields**:

- `aberta`: indica se a janela esta visivel
- `origemDeRetorno`: identifica a tela protegida de onde o fluxo foi iniciado
- `alteracoesPendentes`: indica se houve edicao ainda nao salva
- `estadoDeFeedback`: representa ausencia de mensagem, sucesso ou falha

**Relationships**:

- referencia a `PreferenciaTemaVisual` para manter consistencia da experiencia
- referencia a `RetornoPosSalvamento` para decidir navegacao apos salvar ou fechar

**Validation Rules**:

- o fechamento nao pode aplicar alteracoes pendentes automaticamente
- falha de salvamento nao pode descartar o contexto atual da janela

## PreferenciaTemaVisual

**Description**: Preferencia visual ativa durante a sessao do usuario.

**Fields**:

- `modoAtivo`: modo visual atualmente selecionado
- `origemPersistida`: referencia da fonte de verdade ja usada pela interface
- `restauravelAposAcao`: indica se o modo deve ser reaplicado apos navegar ou salvar

**Relationships**:

- e lida e preservada durante operacoes da `JanelaAjustesConta`
- influencia a experiencia percebida em `RetornoPosSalvamento`

**Validation Rules**:

- o modo ativo apos salvar deve ser igual ao modo ativo antes de iniciar a acao
- falhas operacionais nao podem redefinir a preferencia para light por padrao

## RetornoPosSalvamento

**Description**: Contexto de navegacao que define para onde o usuario volta apos
salvar ou fechar a janela de ajustes.

**Fields**:

- `rotaProtegidaAtual`: tela autenticada de origem
- `resultadoDaAcao`: sucesso, falha ou cancelamento
- `redirecionamentoPermitido`: identifica se ha navegacao legitima fora da origem

**Relationships**:

- recebe informacao de `JanelaAjustesConta`
- deve respeitar a `PreferenciaTemaVisual` durante a transicao

**Validation Rules**:

- sucesso no salvamento nao pode enviar o usuario para a tela inicial por padrao
- cancelamento deve retornar o usuario ao contexto protegido anterior sem efeitos
  colaterais
