# UI Migration Contract

## Scope Contract

1. O criterio de inclusao no escopo e a existencia de equivalente oficial no catalogo
   `shadcn/ui`.
2. `button.tsx`, `input.tsx`, `dialog.tsx` e `table.tsx` entram no escopo de
   substituicao estrutural.
3. Componentes compostos que dependem dessas primitives devem ser adaptados para a
   nova base estrutural sem regressao funcional perceptivel.

## Behavioral Contract

4. Feedback textual de sucesso, erro, aviso e informacao deve permanecer visivel e
   semanticamente coerente.
5. Cores semanticas devem preservar o significado atual de sucesso, erro, aviso,
   informacao e neutralidade.
6. Componentes migrados devem manter foco visivel, navegacao por teclado e semantica
   acessivel minima.
7. Tema, menu do usuario, notificacoes e ajustes da conta nao podem perder clareza de
   comunicacao visual por causa da migracao.

## Validation Contract

8. A migracao deve registrar quais componentes foram substituidos e quais ficaram fora
   do escopo.
9. A validacao final deve incluir verificacao tecnica de observabilidade, auditoria e
   acessibilidade minima.
10. O frontend nao deve manter como base estrutural ativa componentes locais com
    equivalente oficial ja migrado.
11. A validacao tecnica deve evidenciar ao menos:
    - renderizacao bem-sucedida das primitives migradas;
    - preservacao de feedback de sucesso e erro nos fluxos priorizados;
    - preservacao de foco visivel e navegacao por teclado;
    - ausencia de regressao estrutural nos consumidores reais mapeados.
12. Os consumidores priorizados para evidencias tecnicas sao:
    - `theme-toggle`
    - `topbar-user-menu`
    - `account-settings-dialog`
    - `login-panel`
    - `cessao-list`
    - `cessao-detail`
    - `calculo-panel`
    - `contratos-panel`
    - `elegibilidade-panel`
    - `lastro-panel`
    - `registradora-panel`
13. A validacao tecnica registrada deve combinar:
    - testes de renderizacao e interacao das primitives migradas;
    - testes de consumidores priorizados;
    - verificacao documental de componentes migrados e componentes compostos fora do escopo estrutural.
