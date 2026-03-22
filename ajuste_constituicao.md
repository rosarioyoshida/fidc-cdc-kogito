  ## Ajuste recomendado na Constituição

  ### 1) No Princípio VI (após o trecho que cita shadcn/ui)

  Substitua o trecho atual:

  > “O projeto MUST usar shadcn/ui como base tecnica e estrutural de componentes, nunca como fonte de verdade visual. A fonte normativa para
  > decisoes visuais e comportamentais MUST ser o Design System da Atlassian, incluindo tokens, fundamentos, tipografia, espacamento, contraste,
  > acessibilidade, foco visivel, estados interativos, autenticidade da marca e documentacao utilizavel por design e desenvolvimento.”

  Por:

  O projeto MUST usar o shadcn/ui como base técnica e estrutural de componentes. Sempre que houver componente de UI utilitário de superfície
  (botão, input, dialog, tabela, dropdown, modal, card, badge, menu, etc.), os implementadores devem priorizar componentes disponíveis em shadcn/
  ui antes de qualquer implementação própria.

  A fonte de verdade visual e comportamental MUST ser o Design System da Atlassian, com valores, tokens, estados, acessibilidade e documentação
  comportamental centralizados. O código de UI implementado localmente MUST ser derivado de shadcn/ui quando tecnicamente possível, e só pode
  divergir por configuração, composição ou composição adicional de estilo alinhada aos tokens.

  A criação de componente novo fora do acervo padrão shadcn/ui (ou fora da família de componentes já instalada no repositório) só é permitida com
  justificativa forte e rastreável, explicitando: (a) ausência funcional no shadcn/ui para o caso concreto, (b) por que composição/extensão não
  resolve, (c) impacto de manutenção e cobertura de estados, (d) owner e prazo de revisão do componente.

  Sempre que uma nova funcionalidade de UI for necessária, o time MUST primeiro avaliar:

  1. Existe componente shadcn/ui equivalente com composição adequada;
  2. Existe componente local equivalente já reutilizável;
  3. Existe variação controlada via props/variants sem novo componente.

  ## 2) Nova seção em “Requisitos e Restrições de Engenharia”

  Adicionar esta subseção:

  ### Governança de componentes de UI (Reuso, controle de duplicação e exceções)

  O repositório e o projeto MUST adotar política de reuso estrito de componentes para reduzir dívida de UX e código.

  Todo novo componente de UI só pode ser criado após:

  - Revisão de catálogo existente em src/components/ui para reutilização.
  - Verificação de composição possível a partir de componentes shadcn/ui existentes.
  - Validação prévia de tokenização com Design System antes de criar estilos novos.

  São critérios de aceitação para criação de um novo componente não existente no repertório shadcn/ui/local:

  - Exceção formal com justificativa escrita no PR, contendo:
      - problema funcional não coberto por composição;
      - análise de 2 alternativas rejeitadas (composição/extensão e composição condicional via props);
      - impacto em acessibilidade e estados (hover/focus/active/disabled/loading/selected);
      - mapeamento de tokens usados (cores, tipografia, raio, espaço, sombra, foco);
      - owner técnico, suíte de testes e documentação de uso.
  - Aprovação obrigatória de arquitetura/frontend no gate de revisão.

  Medidas práticas de controle de criação desgovernada:

  - Cada PR com novo componente deve incluir seção de “Racional de novo componente” no template de revisão.
  - Manter catálogo único de componentes em src/components/ui e revisar duplicações por semântica visual/funcional antes do merge.
  - Proibir “variações locais” que só mudam classes sem introduzir nova semântica de comportamento.
  - Exigir testes de acessibilidade mínima para componentes interativos novos (foco visível, navegação por teclado, labels/roles, estado disabled/
    loading quando aplicável).
  - Registrar no changelog técnico quando houver exceção aprovada e revisar trimestralmente a necessidade de refatorar duplicações em um único
    componente compartilhado.