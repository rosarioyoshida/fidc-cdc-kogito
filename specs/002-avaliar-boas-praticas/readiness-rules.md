# Readiness Rules

## Classificacao final

- `Apto`: nao ha achados altos ou criticos em aberto.
- `Apto com ressalvas`: ha achados altos controlados por plano de adequacao explicito,
  com criterio de aceite e priorizacao definidos.
- `Inapto`: existe pelo menos um achado critico que bloqueia evolucao segura.

## Regras complementares

- achados `Nao verificavel` precisam declarar a limitacao de evidencia;
- achados altos sem plano explicito impedem `Apto com ressalvas`;
- contradicoes entre artefatos de operacao, seguranca ou setup devem ser tratadas ao
  menos como severidade `Alta` ate alinhamento formal.
