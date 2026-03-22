# Contract: Topbar User Menu

## Purpose

Definir o comportamento esperado do menu superior do usuario nas telas protegidas.

## Required Elements

- nome do usuario autenticado
- perfil ativo derivado do seed do projeto
- entrada para ajustes da propria conta
- entrada para alteracao da propria senha
- botao ou acao explicita de logout

## Required States

- carregando dados do usuario
- autenticado com dados completos
- autenticado com dados parciais indisponiveis
- erro ao carregar conta
- sessao invalidada

## Accessibility and UX Rules

- foco visivel em todos os controles
- operacao completa por teclado
- semantica clara para feedback de sucesso, aviso e erro
- consistencia visual entre telas protegidas

## Behavioral Rules

- o menu superior aparece apenas em areas protegidas relevantes;
- a identidade exibida permanece consistente durante a sessao;
- logout invalida a sessao imediatamente e remove acesso a conteudo protegido.
