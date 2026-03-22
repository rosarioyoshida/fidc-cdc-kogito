# Prioritized Findings - 2026-03-22

## Prioridade imediata

1. **F-001 - Lint do frontend nao automatizado**
   - Impacto: enfraquece quality gate e CI local
   - Recomendacao: configurar lint nao interativo sem adicionar dependencia sem consulta
2. **F-002 - Baseline de autenticacao inconsistente**
   - Impacto: cria ambiguidade operacional e de seguranca
   - Recomendacao: alinhar backend, compose e quickstart para um modelo unico ou
     relacao explicitamente documentada

## Prioridade curta

3. **F-003 - Drift do nome do agrupador**
   - Impacto: confusao na operacao local e em guias de setup
4. **F-004 - Placeholders remanescentes em `AGENTS.md`**
   - Impacto: reduz confiabilidade das diretrizes do projeto

## Prioridade media

5. **F-005 - Evidencia limitada de integracao backend sem Docker**
   - Impacto: reduz profundidade do gate de regressao no host atual
