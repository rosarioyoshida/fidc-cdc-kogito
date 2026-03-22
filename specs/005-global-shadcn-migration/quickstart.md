# Quickstart Validation: Migracao Global de Componentes Equivalentes para shadcn/ui

## Preconditions

- Aplicacao frontend iniciada localmente
- Usuario autenticado disponivel para validar menu, notificacoes e ajustes da conta
- Build sem erros apos a migracao das primitives e dos consumidores compostos

## Scenario 1: Validar primitives migradas

1. Abrir uma tela que renderize botoes, inputs, tabela e dialogo.
2. Confirmar que os componentes renderizam sem erro.
3. Confirmar foco visivel, estados hover/active/disabled e semantica basica.

## Scenario 2: Validar comunicacao visual e tema

1. Alternar entre tema claro e escuro.
2. Confirmar que o controle de tema permanece funcional.
3. Confirmar que feedback e contrastes continuam legiveis nos dois temas.

## Scenario 3: Validar menu do usuario e notificacoes

1. Abrir o menu do usuario autenticado.
2. Abrir notificacoes.
3. Confirmar preservacao de hierarquia visual, contadores e semantica de destaque.

## Scenario 4: Validar ajustes da conta

1. Abrir ajustes da conta.
2. Interagir com campos de email e senha.
3. Confirmar que dialogo, botoes, inputs e mensagens continuam claros.
4. Confirmar foco visivel e navegacao por teclado.

## Scenario 5: Validar rastreabilidade tecnica

1. Conferir a lista de componentes migrados e fora do escopo.
2. Executar as verificacoes tecnicas definidas para observabilidade e auditoria.
3. Confirmar que a migracao deixa evidencias suficientes para detectar regressao.
