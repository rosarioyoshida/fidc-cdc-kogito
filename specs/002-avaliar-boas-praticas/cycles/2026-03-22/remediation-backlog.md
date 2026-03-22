# Remediation Backlog - 2026-03-22

| ID | Prioridade | Acao | Criterio de aceite | Dependencias |
|----|------------|------|--------------------|--------------|
| RB-001 | Imediata | Configurar lint do frontend como comando nao interativo e repetivel | `npm run lint` executa sem prompt e retorna status consistente | Consulta previa apenas se exigir nova dependencia |
| RB-002 | Imediata | Definir baseline unica para autenticacao local e ecossistema Kogito | Backend, quickstart e compose refletem o mesmo modelo de autenticacao | Decisao tecnica de seguranca |
| RB-003 | Curta | Alinhar o nome do agrupador entre compose e documentacao operacional | Compose e quickstart usam o mesmo nome | Nenhuma |
| RB-004 | Curta | Limpar placeholders remanescentes de `AGENTS.md` | Arquivo passa a refletir apenas contexto real do projeto | Nenhuma |
| RB-005 | Media | Tornar explicita a condicao de Docker/Testcontainers para regressao backend | Guia operacional e estrategia de teste descrevem execucao com e sem Docker | Nenhuma |
