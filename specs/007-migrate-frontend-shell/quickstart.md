# Quickstart: Validar a Migração para Novo Shell Horizontal

## Pré-requisitos

- Dependências do frontend instaladas em `frontend/`
- Backend e autenticação disponíveis conforme ambiente atual do projeto

## Execução

1. Inicie o frontend:

```powershell
cd D:\desenv\fidc-cdc-kogito\frontend
npm run dev
```

2. Abra a aplicação no navegador.

## Roteiro de validação

### 1. Login

1. Acesse `/`.
2. Verifique que a página usa o novo formato simplificado, sem header global.
3. Confirme presença do formulário, texto de apoio, mensagens de erro e rodapé simplificado.
4. Faça login com um usuário seedado válido.

### 2. Shell autenticado

1. Após autenticar, confirme header de largura total.
2. Verifique logo à esquerda, navegação principal central e ações do usuário à direita.
3. Confirme botão de destaque, notificações com contador quando houver pendência e avatar com menu.
4. Abra o menu do usuário e valide a opção de alteração de senha.

### 3. Barra secundária

1. Em cada rota autenticada do escopo, verifique a presença da barra secundária.
2. Confirme que ela combina contexto da rota com filtros, busca ou seletores quando aplicável.
3. Verifique estado ativo coerente com a rota atual.

### 4. Lista de cessões

1. Acesse `/cessoes`.
2. Confirme seção de criação da operação e seção de listagem.
3. Valide estados vazio e erro, se reproduzíveis no ambiente.
4. Abra um item da lista e verifique navegação para o detalhe.

### 5. Detalhe da cessão

1. Acesse `/cessoes/[businessKey]`.
2. Verifique resumo principal, ações locais e apresentação consistente das etapas.
3. Se houver permissão, execute a ação de avançar etapa.
4. Confirme manutenção dos estados de permissão e feedback.

### 6. Análise

1. Acesse `/cessoes/[businessKey]/analise`.
2. Confirme a reorganização do conteúdo em seções coerentes.
3. Valide mensagens de sucesso/erro.
4. Execute pelo menos uma ação disponível e confirme que o shell permanece consistente.

### 7. Auditoria

1. Acesse `/cessoes/[businessKey]/auditoria`.
2. Verifique contexto operacional e linha do tempo auditável.
3. Confirme preservação das ações de retorno e do estado vazio quando aplicável.

### 8. Responsividade e acessibilidade básica

1. Reduza a largura da viewport para tablet e mobile.
2. Confirme acesso contínuo à navegação, notificações e menu do usuário.
3. Navegue por teclado nos controles globais e locais.
4. Verifique foco visível e compreensão dos estados críticos.

### 9. Observabilidade e ações de conta

1. Force ou simule falha de carregamento de shell, resolução de usuário ou navegação contextual, se o ambiente permitir.
2. Confirme que o erro permanece compreensível e acionável sem perder o contexto da tela.
3. Abra notificações e menu do usuário e verifique que falhas nessas interações são detectáveis durante a validação técnica.
4. Execute logout e alteração de senha e confirme preservação da rastreabilidade dessas ações no fluxo validado pelo projeto.

### 10. Prontidão de Cutover

Confirme antes da ativação:

- `/` no novo formato simplificado
- `/cessoes` no novo shell
- `/cessoes/[businessKey]` no novo shell
- `/cessoes/[businessKey]/analise` no novo shell
- `/cessoes/[businessKey]/auditoria` no novo shell
- ações críticas continuam acessíveis
- menu do usuário e alteração de senha funcionam
- estados críticos permanecem visíveis
- não há dependência do shell antigo nas rotas do escopo
- `npm run lint` e `npm test` finalizam sem regressões bloqueadoras

Resultado registrado em 2026-03-25:

- `/` validado no shell simplificado sem header global
- `/cessoes` validado no shell autenticado
- `/cessoes/[businessKey]` validado no shell autenticado
- `/cessoes/[businessKey]/analise` validado no shell autenticado
- `/cessoes/[businessKey]/auditoria` validado no shell autenticado
- ações críticas preservadas nas rotas do escopo
- menu do usuário, logout e alteração de senha preservados
- estados críticos seguem visíveis no formato novo
- nenhuma rota do escopo depende do shell anterior

### 11. Cenários primários para SC-003

Registrar o resultado destes cenários ao validar o critério de 90%:

- login bem-sucedido de `/` para `/cessoes`
- navegação de `/cessoes` para `/cessoes/[businessKey]`
- navegação de `/cessoes/[businessKey]` para `/cessoes/[businessKey]/analise`
- navegação de `/cessoes/[businessKey]` para `/cessoes/[businessKey]/auditoria`
- retorno entre detalhe, análise e auditoria sem perda de contexto
- abertura do menu do usuário e acesso à ação de alteração de senha

Resultado registrado para SC-003 em 2026-03-25:

- 6 de 6 cenários primários cobertos nas verificações automatizadas e no roteiro técnico
- critério de 90% atendido

## Verificações técnicas

Execute os testes e lint do frontend:

```powershell
cd D:\desenv\fidc-cdc-kogito\frontend
npm run lint
npm test
```

## Resultado da execução atual

- Data: 2026-03-25
- Comando executado: `npm run lint`
- Resultado: aprovado sem erros
- Comando executado: `npm test`
- Resultado: `36` arquivos de teste aprovados e `46` testes aprovados

## Evidências automatizadas registradas

- Shell autenticado compartilhado validado em `frontend/tests/integration/shell-authenticated-navigation.spec.tsx`
- Login simplificado validado em `frontend/tests/integration/login-shell.spec.tsx`
- Barra secundária e contexto por rota validados em `frontend/tests/integration/secondary-nav-context.spec.tsx`
- Migração agregada do shell validada em `frontend/tests/integration/shell-migration.spec.tsx`
- Reorganização de análise e auditoria validada em `frontend/tests/integration/content-shell-sections.spec.tsx`
- Revisão final desta rodada: navegação contextual ativa, controles locais principais no secondary nav e estados de feedback preservados
