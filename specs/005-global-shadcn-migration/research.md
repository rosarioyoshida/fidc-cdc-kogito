# Research: Migracao Global de Componentes Equivalentes para shadcn/ui

## Decision 1: Usar o catalogo oficial do shadcn/ui como criterio normativo de equivalencia

- **Decision**: Todo componente local em `frontend/src/components/ui` com equivalente
  oficial publicado em `https://ui.shadcn.com/docs/components` entra no escopo da
  migracao.
- **Rationale**: O criterio fica objetivo, verificavel e alinhado a constituicao.
- **Alternatives considered**:
  - Manter avaliacao subjetiva por semelhanca funcional: rejeitado por gerar excecao
    interpretativa.
  - Restringir a migracao apenas a um fluxo: rejeitado por nao atender o escopo
    definido pelo usuario.

## Decision 2: Substituir as primitives no proprio catalogo do projeto

- **Decision**: `button.tsx`, `input.tsx`, `dialog.tsx` e `table.tsx` serao
  substituidos em `frontend/src/components/ui` por implementacoes aderentes ao
  `shadcn/ui`, mantendo o ponto de importacao usado pelo projeto.
- **Rationale**: Reduz impacto de import paths e concentra a migracao estrutural em
  arquivos ja consumidos pelo frontend.
- **Alternatives considered**:
  - Criar um segundo catalogo paralelo: rejeitado por aumentar complexidade e periodo
    de convivencia entre duas bases.
  - Reaproveitar implementacoes locais com pequenos ajustes: rejeitado por conflito
    com o criterio constitucional e com a diretriz do usuario.

## Decision 3: Preservar comunicacao visual e cores semanticas como contrato de migracao

- **Decision**: Variantes, estados e tokens dos componentes migrados devem preservar
  feedback de sucesso, erro, aviso, informacao e neutralidade, alem de foco visivel e
  hierarquia de informacao nos fluxos existentes.
- **Rationale**: A migracao e aceitavel apenas se mantiver a comunicacao com o
  usuario e a semantica visual do produto.
- **Alternatives considered**:
  - Aceitar alteracao visual incidental ampla: rejeitado por risco de regressao de UX.
  - Preservar apenas markup/estrutura: rejeitado por ser insuficiente para o contrato
    visual e comportamental.

## Decision 4: Validar observabilidade e auditoria tecnicamente

- **Decision**: Observabilidade e auditoria nao serao tratadas apenas como
  documentacao; a feature exigira verificacao tecnica dos sinais definidos para os
  fluxos afetados.
- **Rationale**: A diretriz do usuario exige validacao tecnica, nao apenas registro
  narrativo.
- **Alternatives considered**:
  - Registrar somente em `research.md`: rejeitado por ser insuficiente.

## Security and Audit Notes

- A migracao nao deve expor credenciais, informacoes de conta ou dados sensiveis em
  mensagens, labels, logs de frontend ou estados de erro.
- O registro tecnico da migracao deve identificar claramente quais componentes foram
  substituidos e quais ficaram fora do escopo por ausencia de equivalente oficial.
- Fluxos com feedback critico devem continuar detectaveis em validacao tecnica, com
  sinais suficientes para diferenciar sucesso, falha e regressao visual relevante.

## Security Validation Notes

- componentes migrados nao devem expor credenciais, dados de conta ou valores
  sensiveis em labels, mensagens de erro, estados intermediarios ou logs de frontend;
- a migracao nao altera autorizacao, mas deve preservar corretamente a separacao entre
  fluxos autenticados e nao autenticados;
- qualquer regressao de semantica visual em mensagens de erro de autenticacao ou conta
  deve ser tratada como falha de migracao.
