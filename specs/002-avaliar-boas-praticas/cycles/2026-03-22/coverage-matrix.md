# Coverage Matrix - 2026-03-22

| Area | Criterios principais | Status | Evidencias-chave | Observacao |
|------|----------------------|--------|------------------|------------|
| Implementacao | CR-001, CR-002 | Parcialmente conforme | E-001, E-004 | Estrutura boa, mas governanca documental ainda com placeholders |
| Configuracao | CR-002 | Parcialmente conforme | E-001, E-008 | Configuracao externalizada, com drift de nome entre compose e quickstart |
| Seguranca | CR-003 | Parcialmente conforme | E-002, E-008 | Backend usa Basic Auth e compose expoe Keycloak, exigindo baseline mais coerente |
| Observabilidade | CR-004 | Conforme | E-003, E-009 | Logs e metricas minimas estao explicitados |
| Testes | CR-005 | Parcialmente conforme | E-005, E-006, E-007 | Testes repetiveis existem, mas lint nao e gate automatizado e integracoes dependem de Docker |
| Documentacao operacional | CR-007 | Parcialmente conforme | E-008, E-010 | Quickstart forte, mas ha inconsistencias de agrupador e AGENTS |
| UX/acessibilidade | CR-006 | Conforme | E-004, E-006 | Tokens e testes de interface mostram alinhamento funcional |
| Governanca de stack | CR-008 | Conforme | D:\desenv\fidc-cdc-kogito\specs\002-avaliar-boas-praticas\governance-constraints.md | Restricao formalizada e preservada no ciclo |
