# Governance Constraints

## Restricoes obrigatorias

- manter a stack atual do projeto:
  - backend em Java 21 com Spring Boot/Kogito
  - frontend em TypeScript, React e Next.js
- nao adicionar dependencias de backend, frontend ou infraestrutura sem consulta
  previa
- priorizar documentacao, configuracao e reorganizacao antes de propor novos servicos
  ou frameworks
- tratar o repositorio como fonte primaria de evidencia

## Implicacoes praticas

- ajustes de qualidade podem introduzir arquivos Markdown, templates e guias sem
  alterar o runtime do produto;
- qualquer proposta de automacao adicional deve justificar valor incremental e custo
  operacional;
- comandos interativos que inviabilizam gates automatizados devem ser tratados como
  lacuna de governanca ou de qualidade de entrega.
