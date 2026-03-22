# Recurring Cycle Playbook

## Objetivo

Reexecutar a avaliacao em cada fase relevante do projeto usando o mesmo modelo de
criterio, severidade e prontidao.

## Passos

1. atualizar a data do novo ciclo em `cycles/<data>/`
2. copiar `templates/assessment-report-template.md` e `templates/evidence-log-template.md`
3. revisar `criteria-matrix.md`, `severity-model.md` e `readiness-rules.md`
4. coletar evidencias do estado atual do repositorio e dos comandos repetiveis
5. consolidar achados, backlog de adequacao e parecer final
6. comparar com o ciclo anterior usando `templates/cycle-comparison-template.md`

## Regras

- manter a stack atual do projeto;
- consultar antes de qualquer dependencia nova;
- registrar limitacoes ambientais que reduzam a confiabilidade do ciclo;
- nao alterar a logica de `Apto`, `Apto com ressalvas` e `Inapto` sem nova clarificacao.
