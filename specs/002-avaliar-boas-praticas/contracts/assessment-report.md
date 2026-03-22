# Contract: Assessment Report

## Purpose

Definir a estrutura minima obrigatoria do relatorio de avaliacao de boas praticas para
consumo por lideranca tecnica, gestao e equipe executora.

## Required Sections

1. **Contexto do ciclo**
   - identificador da fase avaliada
   - data da avaliacao
   - escopo e limitacoes

2. **Referencias adotadas**
   - diretrizes internas utilizadas
   - constituicao aplicavel
   - boas praticas complementares usadas apenas para lacunas

3. **Cobertura por area**
   - implementacao
   - configuracao
   - seguranca
   - observabilidade
   - testes
   - documentacao operacional
   - UX/acessibilidade quando aplicavel

4. **Achados**
   - identificador do achado
   - criterio relacionado
   - evidencias associadas
   - status: conforme, parcialmente conforme, nao conforme ou nao verificavel
   - severidade: baixa, media, alta ou critica
   - impacto
   - recomendacao
   - indicacao se bloqueia a proxima fase

5. **Parecer de prontidao**
   - `Apto`: sem achados criticos ou altos
   - `Apto com ressalvas`: com achados altos controlados por plano explicito
   - `Inapto`: com qualquer bloqueio critico

6. **Plano de adequacao**
   - prioridade
   - acao recomendada
   - criterio de aceite
   - dependencias conhecidas

## Validation Rules

- Todo achado alto ou critico deve conter evidencia e recomendacao.
- Todo parecer deve listar explicitamente os bloqueios ou ressalvas que o sustentam.
- O relatorio deve permitir rastrear cada recomendacao ate o criterio e a evidencia.
- O contrato nao pressupoe nova API, banco ou dependencia.
