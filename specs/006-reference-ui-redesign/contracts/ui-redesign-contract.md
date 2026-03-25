# UI Redesign Contract

## Scope Contract

1. A feature cobre apenas migracao visual das telas e componentes existentes do
   frontend.
2. Nenhum modulo, dado, filtro, acao ou caminho de navegacao novo pode ser criado por
   causa da imagem de referencia.
3. As telas prioritarias sao login, topbar autenticada, listagem de cessoes, detalhe
   da cessao, analise e auditoria.

## Visual Contract

4. A linguagem visual pode mudar hierarquia, agrupamento, espacamento e acabamento das
   superficies, mas deve preservar o significado funcional de cada tela.
5. As cores atuais definidas no design system local devem permanecer como fonte de
   verdade; a imagem nao autoriza nova paleta.
6. A tipografia atual do produto deve permanecer; a migracao nao pode introduzir nova
   familia tipografica.
7. Tema claro/escuro, contraste, foco visivel e semantica cromatica de sucesso, erro,
   aviso e informacao devem continuar coerentes.

## Component Governance Contract

8. Toda necessidade de UI nova deve verificar primeiro o catalogo local em
   `frontend/src/components/ui`.
9. Se nao houver cobertura local suficiente, a equipe deve verificar o componente
   equivalente na base `shadcn/ui` e adiciona-lo ao catalogo local quando aplicavel.
10. Variacoes por props, variants e composicao devem ser preferidas a criar componente
    novo.
11. E proibido criar componente customizado novo sem justificativa forte e rastreavel
    contendo: ausencia funcional no `shadcn/ui`, motivo de composicao/extensao nao
    resolver, impacto de manutencao e estados, owner tecnico e prazo de revisao.

## Behavioral Contract

12. O usuario deve continuar identificando rapidamente onde esta, qual e o conteudo
    principal e qual acao e primaria em cada tela priorizada.
13. Fluxos ja existentes devem continuar executaveis sem novos passos obrigatorios.
14. Estados `default`, `hover`, `focus-visible`, `disabled`, `selected` e `loading`
    devem permanecer claros quando aplicaveis.
15. A migracao deve continuar funcional nas larguras de tela ja suportadas pelo
    produto.

## Validation Contract

16. A aprovacao da feature exige evidencia de preservacao dos tokens atuais de cor,
    radius, sombra e tipografia.
17. A aprovacao da feature exige evidencia de governanca de componentes para cada nova
    superficie adicionada ou alterada.
18. A validacao final deve combinar testes existentes/ajustados, revisao tecnica e
    validacao manual guiada dos fluxos priorizados.
19. A validacao final deve confirmar que sinais criticos de feedback e erro permanecem
    detectaveis nas telas prioritarias, tanto na execucao manual quanto nas suites de
    teste atualizadas.

## Implemented Component Inventory

- `card`, `badge`, `alert`, `separator` e `dropdown-menu` foram adicionados ao catalogo
  local em `frontend/src/components/ui/`.
- `button`, `input`, `dialog` e `table` permaneceram como primitives reutilizadas.
- Nenhuma tela da primeira onda passou a depender de componente fora de
  `frontend/src/components/ui/`.
- Nenhuma excecao formal de componente customizado foi aprovada nesta feature.

## Evidence Summary

- Cobertura automatizada atualizada para topbar, login, cessoes, detalhe, analise e
  auditoria.
- Regressao de escopo reforcada nas suites de integracao para garantir ausencia de
  novas acoes e caminhos indevidos.
- Evidencia manual de 5 avaliadores permanece dependente de execucao presencial no
  fluxo de aceite.
