# Prioritization Rules

## Modelo de priorizacao

| Prioridade | Criterio | Quando aplicar |
|------------|----------|----------------|
| Imediata | Corrigir antes de nova fase | Achado critico ou alto sem mitigacao aceitavel |
| Curta | Corrigir no proximo ciclo de trabalho | Achado alto com mitigacao parcial ou medio com risco acumulado |
| Media | Corrigir conforme capacidade planejada | Achado medio com impacto controlado |
| Longa | Melhoria recomendada | Achado baixo sem risco imediato |

## Fatores de ordenacao

1. severidade do achado
2. impacto em liberacao, seguranca ou operacao
3. custo de manter a falha aberta
4. dependencia de outras correcoes
5. repetibilidade do problema entre ciclos
