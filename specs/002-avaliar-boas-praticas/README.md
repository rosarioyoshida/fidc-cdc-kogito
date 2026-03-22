# Avaliacao de Boas Praticas do Projeto

Este diretorio concentra os artefatos executaveis do ciclo de avaliacao da feature
`002-avaliar-boas-praticas`.

## Estrutura

- `plan.md`: plano tecnico da feature
- `spec.md`: especificacao funcional e de qualidade
- `tasks.md`: backlog executavel por fases
- `criteria-matrix.md`: criterios normativos da avaliacao
- `severity-model.md`: regra de classificacao de status e severidade
- `evidence-sources.md`: inventario de fontes de evidencia com caminhos absolutos
- `readiness-rules.md`: gatilhos formais para `Apto`, `Apto com ressalvas` e `Inapto`
- `governance-constraints.md`: restricoes de stack e aprovacao de dependencias
- `prioritization-rules.md`: modelo de priorizacao de adequacoes
- `recurring-cycle-playbook.md`: roteiro para reexecutar o ciclo em novas fases
- `contracts/assessment-report.md`: contrato minimo do relatorio
- `templates/`: modelos reutilizaveis para novos ciclos
- `cycles/2026-03-22/`: evidencias e parecer do ciclo inicial

## Como usar

1. Revise `criteria-matrix.md`, `severity-model.md` e `readiness-rules.md`.
2. Colete evidencias usando `evidence-sources.md` e os templates em `templates/`.
3. Preencha os arquivos do ciclo corrente em `cycles/<data>/`.
4. Emita o parecer final conforme `contracts/assessment-report.md`.
5. Compare com o ciclo anterior usando `templates/cycle-comparison-template.md`.

## Restricoes

- manter a stack atual do projeto
- nao adicionar dependencias sem consulta previa
- usar o repositorio como fonte primaria de evidencia sempre que possivel
