# Quickstart Validation: Migracao Visual da Interface por Referencia

## Preconditions

- Frontend iniciado localmente
- Backend disponivel para autenticacao e consulta das telas ja existentes
- Usuario autenticado de teste disponivel
- Build e testes do frontend sem erros antes da validacao manual
- Pelo menos 5 avaliadores internos disponiveis para a validacao guiada
- Checklist de evidencias por tela preparado com campos de tempo e resultado

## Scenario 1: Validar preservacao de acesso e identidade visual base

1. Abrir a rota `/`.
2. Confirmar que a tela de login apresenta nova hierarquia visual sem mudar campos,
   mensagem de erro ou acao principal.
3. Confirmar que as cores e a tipografia permanecem consistentes com o restante do
   produto atual.

## Scenario 2: Validar topbar autenticada e menu de conta

1. Autenticar no produto.
2. Confirmar que `topbar-user-menu` continua exibindo usuario, perfil, notificacoes,
   alternancia de tema e acesso aos ajustes de conta.
3. Confirmar que o redesign melhora agrupamento e clareza sem criar novas acoes.

## Scenario 3: Validar listagem de cessoes

1. Abrir `/cessoes`.
2. Confirmar que header, formulario de criacao, lista e estado vazio seguem o novo
   padrao visual.
3. Confirmar que a acao principal da tela continua evidente e operavel.

## Scenario 4: Validar detalhe da cessao

1. Abrir `/cessoes/[businessKey]`.
2. Confirmar que header contextual, botoes de navegacao, painel de status e tabela de
   etapas foram reorganizados visualmente sem mudar o fluxo.
3. Confirmar foco visivel, legibilidade e responsividade.

## Scenario 5: Validar dashboard de analise

1. Abrir `/cessoes/[businessKey]/analise`.
2. Confirmar consistencia entre header, mensagens de feedback, paineis e agrupamentos.
3. Confirmar que nenhuma informacao, filtro ou acao nova foi introduzida por causa da
   referencia.

## Scenario 6: Validar auditoria e responsividade

1. Abrir `/cessoes/[businessKey]/auditoria`.
2. Confirmar que a tela segue a linguagem visual nova sem perder acesso ao conteudo.
3. Repetir a verificacao nas larguras de tela suportadas pelo produto.

## Scenario 7: Medir clareza operacional em ate 5 segundos

1. Executar uma tentativa por tela da primeira onda obrigatoria com cada um dos 5
   avaliadores internos.
2. Um observador externo deve cronometrar cada tentativa.
3. Considerar a tela aprovada quando o avaliador identifica pagina, conteudo principal
   e acao primaria em ate 5 segundos.
4. Registrar tempo, resultado (`aprovado` ou `reprovado`) e observacoes por tela em
   checklist de validacao.

## Scenario 8: Validar governanca de componentes

1. Revisar todas as superficies novas ou substancialmente alteradas.
2. Confirmar que cada uma reutiliza `frontend/src/components/ui`, adiciona componente do
   `shadcn/ui`, resolve por composicao/variants ou possui excecao formal aprovada.
3. Confirmar que nenhuma criacao local ocorreu apenas para trocar classes ou cosmetica.
4. Confirmar que os sinais de feedback criticos de sucesso, erro e estado vazio seguem
   detectaveis na validacao manual e nos testes atualizados.

## Current Evidence Snapshot

- Automacao validada localmente para:
  - `topbar-user-menu.test.tsx`
  - `theme-toggle.test.tsx`
  - `login-panel.test.tsx`
  - `cessao-list.test.tsx`
  - `cessao-detail.test.tsx`
  - `analise-cessao.spec.tsx`
  - `auditoria-permissoes.spec.tsx`
  - paineis de `analise/*`
- Revisao de governanca concluida sem excecoes de componente novo.
- Validacao manual de 5 avaliadores: pendente de execucao humana fora deste rollout.
