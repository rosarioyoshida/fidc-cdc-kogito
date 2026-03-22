# Feature Specification: Fechamento de Ajustes e Persistencia de Tema

**Feature Branch**: `004-fix-user-modal-theme`  
**Created**: 2026-03-22  
**Status**: Draft  
**Input**: User description: "A tela de ajuste de dados do usuario loga precisa de um botao para fechar a janela. Ao clicar em salvar email ou senha o usuario é redirecionado para a tela inicial com o modo light selecionado. o comportamente deveria manter o modo selecionado anteriormente."

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Fechar a janela de ajustes (Priority: P1)

Como usuario autenticado, quero fechar explicitamente a janela de ajustes da minha
conta para sair desse fluxo sem ser obrigado a salvar alteracoes.

**Why this priority**: Sem uma acao clara de fechamento, o fluxo fica confuso e
parece preso, o que compromete uma operacao basica do usuario.

**Independent Test**: A historia pode ser validada abrindo a janela de ajustes da
conta, acionando o botao de fechar e confirmando que a janela e encerrada sem salvar
mudancas nao confirmadas.

**Acceptance Scenarios**:

1. **Given** um usuario autenticado com a janela de ajustes aberta, **When** ele
   acionar o botao de fechar, **Then** a janela e encerrada e a tela protegida
   anterior permanece visivel.
2. **Given** um usuario autenticado com campos alterados e ainda nao salvos, **When**
   ele fechar a janela, **Then** nenhuma alteracao e aplicada automaticamente.

---

### User Story 2 - Preservar tema apos salvar dados (Priority: P1)

Como usuario autenticado, quero manter o modo visual que escolhi apos salvar email ou
senha para nao perder meu contexto visual nem voltar a uma aparencia inesperada.

**Why this priority**: A troca involuntaria para modo light depois do salvamento causa
quebra de experiencia perceptivel e gera sensacao de erro no produto.

**Independent Test**: A historia pode ser validada selecionando um modo visual
diferente do padrao, salvando email ou senha e confirmando que a interface continua
no mesmo modo visual apos o fluxo.

**Acceptance Scenarios**:

1. **Given** um usuario autenticado com um modo visual ja selecionado, **When** ele
   salvar a alteracao do proprio email, **Then** o sistema preserva o modo visual
   anteriormente ativo.
2. **Given** um usuario autenticado com um modo visual ja selecionado, **When** ele
   salvar a alteracao da propria senha, **Then** o sistema preserva o modo visual
   anteriormente ativo.

---

### User Story 3 - Manter contexto apos salvar (Priority: P2)

Como usuario autenticado, quero continuar no contexto em que eu estava apos salvar
meus dados para nao ser redirecionado de forma inesperada para a tela inicial.

**Why this priority**: O redirecionamento indevido apos salvar interrompe a tarefa do
usuario e aumenta o custo de retomada do fluxo protegido.

**Independent Test**: A historia pode ser validada abrindo a janela de ajustes a
partir de uma tela protegida, salvando email ou senha e confirmando que o usuario
permanece na mesma area funcional, com o mesmo tema visual ativo.

**Acceptance Scenarios**:

1. **Given** um usuario autenticado em uma tela protegida, **When** ele salvar a
   alteracao do proprio email, **Then** o sistema conclui a acao sem redireciona-lo
   para a tela inicial.
2. **Given** um usuario autenticado em uma tela protegida, **When** ele salvar a
   alteracao da propria senha, **Then** o sistema conclui a acao sem resetar o tema
   visual e sem perder o contexto protegido anterior.

---

### Edge Cases

- Usuario fecha a janela de ajustes logo apos abrir, sem interagir com nenhum campo
- Usuario tenta fechar a janela apos editar campos nao salvos
- Usuario salva email ou senha enquanto o modo light esta ativo
- Usuario salva email ou senha enquanto um modo visual diferente do light esta ativo
- Salvamento falha e o sistema deve manter a janela aberta, o contexto atual e o modo
  visual selecionado anteriormente
- Usuario conclui salvamento a partir de uma tela protegida interna e nao deve ser
  levado para a tela inicial por efeito colateral do fluxo

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: A janela de ajustes da conta MUST disponibilizar uma acao explicita de
  fechamento visivel ao usuario autenticado.
- **FR-002**: O acionamento da acao de fechamento MUST encerrar a janela de ajustes
  sem aplicar automaticamente alteracoes ainda nao salvas.
- **FR-003**: O sistema MUST permitir que o usuario autenticado permaneca na mesma
  area protegida apos salvar a alteracao do proprio email.
- **FR-004**: O sistema MUST permitir que o usuario autenticado permaneca na mesma
  area protegida apos salvar a alteracao da propria senha.
- **FR-005**: O sistema MUST preservar o modo visual previamente selecionado pelo
  usuario apos o salvamento bem-sucedido de email.
- **FR-006**: O sistema MUST preservar o modo visual previamente selecionado pelo
  usuario apos o salvamento bem-sucedido de senha.
- **FR-007**: O sistema MUST evitar redirecionamento automatico para a tela inicial
  como efeito colateral do salvamento de dados da conta.
- **FR-008**: O sistema MUST exibir feedback claro de sucesso ou falha na propria
  experiencia protegida do usuario, sem redefinir o modo visual anteriormente ativo.
- **FR-009**: O sistema MUST manter a janela de ajustes e o contexto atual quando o
  salvamento nao for concluido com sucesso.

### Non-Functional Requirements *(mandatory)*

- **NFR-001**: A solucao MUST documentar como o fluxo de atualizacao de conta preserva
  autenticacao, contexto protegido e preferencias visuais sem expor dados sensiveis.
- **NFR-002**: A solucao MUST registrar requisitos de auditoria apenas para os eventos
  ja aplicaveis ao salvamento de email e senha, sem ampliar escopo funcional desta
  correcao.
- **NFR-003**: A solucao MUST definir sinais minimos de observabilidade para detectar
  sucesso, falha e eventual redirecionamento indevido apos o salvamento de dados da
  conta.
- **NFR-004**: O fluxo corrigido MUST manter separacao clara entre comportamento da
  janela de ajustes, persistencia de preferencia visual e navegacao protegida.
- **NFR-005**: O fechamento da janela e o retorno apos salvamento MUST ocorrer de
  forma imediata em condicoes normais de uso, sem exigir repeticao manual do fluxo.
- **NFR-006**: A janela de ajustes MUST oferecer operacao por teclado, foco visivel e
  acao de fechamento perceptivel e consistente com o restante da interface.
- **NFR-007**: A experiencia corrigida MUST manter consistencia visual e semantica de
  feedback, preservando o modo visual escolhido anteriormente apos sucesso ou falha.
- **NFR-008**: A correcao MUST manter a capacidade de evolucao do fluxo de conta sem
  duplicar regras de navegacao ou persistencia de preferencia visual em pontos
  dispersos da interface.

### Key Entities *(include if feature involves data)*

- **Janela de Ajustes da Conta**: Superficie de interacao onde o usuario autenticado
  altera email, altera senha ou encerra o fluxo sem salvar.
- **Preferencia de Tema Visual**: Estado de apresentacao previamente selecionado pelo
  usuario e que deve permanecer consistente durante e apos o fluxo de ajuste.
- **Contexto Protegido Atual**: Tela autenticada de origem a partir da qual o usuario
  abriu a janela de ajustes e para a qual deve retornar apos fechar ou salvar.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: 100% dos usuarios autenticados conseguem fechar explicitamente a janela
  de ajustes sem salvar alteracoes.
- **SC-002**: 100% dos salvamentos bem-sucedidos de email preservam o modo visual que
  estava ativo antes da acao.
- **SC-003**: 100% dos salvamentos bem-sucedidos de senha preservam o modo visual que
  estava ativo antes da acao.
- **SC-004**: 100% dos salvamentos bem-sucedidos de email ou senha concluem o fluxo
  sem redirecionar o usuario para a tela inicial.
- **SC-005**: Pelo menos 95% dos usuarios conseguem concluir o ajuste desejado ou
  fechar a janela na primeira tentativa sem perder o contexto visual anterior.

## Assumptions

- A tela de ajustes de conta ja existe e ja permite alterar email e senha do proprio
  usuario autenticado.
- O produto ja possui um mecanismo de selecao de modo visual cujo estado e esperado
  como persistente durante a sessao do usuario.
- O comportamento esperado para esta correcao e manter o usuario no contexto
  protegido em que ele iniciou o ajuste, em vez de reinicia-lo no fluxo da tela
  inicial.

## Out of Scope

- Criacao de novos campos de perfil alem de email e senha
- Redesenho completo do fluxo de autenticacao ou do menu superior
- Alteracao das regras de permissao, perfis seedados ou politicas de acesso
- Introducao de novos modos visuais ou revisao ampla do sistema de temas
