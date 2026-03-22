# Severity and Status Rubric

## Status de avaliacao

| Status | Definicao | Condicao minima |
|--------|-----------|-----------------|
| Conforme | O criterio esta atendido com evidencia suficiente | Evidencia objetiva e sem contradicoes relevantes |
| Parcialmente conforme | O criterio esta parcialmente atendido ou depende de formalizacao adicional | Evidencia parcial, lacuna controlavel |
| Nao conforme | O criterio nao esta atendido ou existe desvio objetivo | Evidencia mostra falha, incoerencia ou ausencia relevante |
| Nao verificavel | O repositorio nao oferece evidencia suficiente para concluir | Limitacao explicitada no parecer |

## Severidade dos achados

| Severidade | Quando usar | Efeito na prontidao |
|------------|-------------|---------------------|
| Baixa | Melhoria desejavel sem risco imediato de entrega | Nao bloqueia |
| Media | Lacuna relevante, mas controlavel sem impacto imediato de liberacao | Nao bloqueia por si so |
| Alta | Risco importante para qualidade, operacao ou governanca | Permite apenas `Apto com ressalvas` se houver plano explicito |
| Critica | Bloqueio direto para evolucao segura ou aderente | Obriga classificacao `Inapto` |

## Regra de consistencia

- status e severidade devem ser coerentes entre si;
- achado `Conforme` nao recebe severidade;
- achado `Nao verificavel` pode receber severidade apenas quando a ausencia de
  evidencia em si representar risco relevante.
