# Contrato de Integracao com Registradora

## Objetivo

Definir o contrato conceitual de integracao REST sincrona com a registradora para as
etapas do fluxo que dependem de criacao de carteira, registro de contratos e parcelas,
oferta e confirmacao de aceite.

## Caracteristicas obrigatorias

- integracao sincrona via API REST
- retries automaticos com limite definido
- rastreamento de request e response
- timeout e tratamento de erro com RFC 9457 quando aplicavel
- observabilidade por correlation id

## Operacoes de negocio esperadas

- criar carteira
- registrar contratos
- registrar parcelas
- realizar oferta
- confirmar aceite

## Regras de contrato

- requests e responses devem ser persistidos ou referenciados para auditoria
- falhas de integracao devem gerar retries controlados antes de escalonamento
- erros HTTP devem ser mapeados para estados do processo e eventos de auditoria
- naming, versionamento, HATEOAS e padrao de erro devem seguir a constituicao do
  projeto
