<!--
Sync Impact Report
Version change: 1.8.0 -> 1.9.0
Modified principles:
- VI. Experiencia, Performance e Qualidade de Entrega
Added sections:
- Governança de componentes de UI (Reuso, controle de duplicação e exceções)
Removed sections:
- None
Templates requiring updates:
- ✅ updated: .specify/templates/plan-template.md
- ✅ updated: .specify/templates/spec-template.md
- ✅ updated: .specify/templates/tasks-template.md
Follow-up TODOs:
- Nenhum
-->
# FIDC CDC Kogito Constitution

## Core Principles

### I. Simplicidade e Evolucao Sustentavel
Toda solucao MUST comecar pelo desenho mais simples capaz de atender o requisito atual.
KISS e YAGNI sao obrigatorios: abstracoes, camadas, flags, frameworks adicionais e
generalizacoes futuras somente podem ser introduzidas quando houver necessidade
comprovada por requisito atual, duplicacao recorrente ou restricao operacional real.
Cada mudanca MUST reduzir ou preservar a complexidade acidental, com racional claro
quando ampliar o desenho for inevitavel.

Rationale: simplicidade aumenta previsibilidade, reduz custo de manutencao e acelera
entrega segura.

### II. Arquitetura e Codigo de Alta Coesao
O codigo MUST seguir SOLID quando aplicavel ao contexto e MUST priorizar alta coesao,
baixo acoplamento, contratos explicitos e responsabilidade unica por modulo. DRY e
obrigatorio para logica de negocio, validacoes, contratos e regras compartilhadas, mas
nao justifica abstracoes prematuras. APIs, servicos e componentes de UI MUST possuir
nomes claros, fronteiras estaveis, tratamento consistente de erro e cobertura adequada
para os cenarios criticos de negocio. Quando houver APIs REST, a nomeacao de resources
MUST seguir convencoes consistentes de resource naming: URIs MUST representar recursos
como substantivos, nao acoes; colecoes SHOULD usar nomes no plural; itens individuais
MUST ser enderecados como sub-recursos da colecao; hierarquias MUST usar `/`; URIs
MUST evitar trailing slash; segmentos MUST usar letras minusculas e hifens, nunca
underscores ou camelCase; extensoes de arquivo MUST NOT aparecer na URI; operacoes
CRUD MUST ser expressas por metodos HTTP, nao por verbos no path; filtros, ordenacao,
paginacao e refinamentos de colecao SHOULD usar query parameters, nao novos paths
verbais. Quando houver versionamento de REST API, a estrategia MUST ser explicita,
documentada e consistente por API. Versionamento MUST ocorrer para breaking changes,
especialmente quando houver remocao de partes da API, alteracao de tipos ou mudanca
incompativel no formato de request ou response. Breaking changes MUST implicar nova
major version. Mudancas nao quebradoras SHOULD evitar incremento de major version,
embora possam ser rastreadas internamente em minor version quando necessario. A API
MUST adotar um unico padrao de versionamento por superficie publica e mantelo de forma
uniforme; URI versioning, custom headers ou media type negotiation sao aceitaveis,
desde que a escolha seja documentada e aplicada consistentemente. APIs versionadas
MUST prever plano gradual de depreciacao e compatibilidade para consumidores ativos.
Quando o contexto de negocio exigir navegacao dinamica entre recursos ou descoberta de
acoes disponiveis, APIs REST SHOULD adotar HATEOAS como restricao arquitetural.
Respostas hipermidia MUST expor links ou controles relevantes para o proximo estado da
aplicacao, reduzindo acoplamento do cliente a URIs hardcoded. O formato de hipermidia
MUST ser escolhido de forma explicita e consistente por API, podendo usar links no
corpo, headers `Link`, RFC 5988, HAL ou convencao equivalente, desde que `href`,
`rel` e demais metadados de navegacao sejam semanticamente claros.
Para erros HTTP em APIs, o projeto MUST adotar RFC 9457 como formato padrao de problem
details sempre que a API nao estiver retornando uma representacao de dominio mais
apropriada. Respostas de erro MUST usar `application/problem+json` quando em JSON e
seguir a semantica dos membros `type`, `title`, `status`, `detail` e `instance`.
Problem types especificos MUST ser documentados com URI propria sempre que o status
code sozinho nao for suficiente. Extensoes de problem details MAY ser usadas para
detalhes adicionais de negocio, mas clientes MUST ignorar membros de extensao que nao
reconhecam. O campo `detail` MUST ajudar o cliente a corrigir o problema e MUST NOT
vazar detalhes internos de implementacao.

Rationale: coesao e contratos claros tornam o sistema mais facil de entender, testar,
evoluir e auditar.

### III. Security, Compliance e Auditoria by Design
Seguranca, conformidade regulatoria e auditabilidade MUST ser tratadas desde a
especificacao. Toda feature MUST identificar dados sensiveis, requisitos regulatorios,
perfis de acesso, trilhas de auditoria, retencao de evidencias e controles de integridade.
Segredos MUST ficar fora do codigo-fonte; dados sensiveis MUST ser minimizados,
mascarados ou criptografados conforme criticidade; autorizacao MUST operar por menor
privilegio; eventos relevantes de negocio e seguranca MUST gerar trilha auditavel com
quem, o que, quando e contexto suficiente para investigacao.

Rationale: controles tardios geram retrabalho, risco regulatorio e baixa capacidade de
resposta a incidentes.

### IV. Observabilidade e Operacao by Design
Servicos, processos e fluxos criticos MUST nascer observaveis. Logs estruturados,
metricas, correlacao entre requisicoes, health checks e sinais de falha MUST ser
definidos para cada capacidade relevante. Alertas e dashboards SHOULD ser derivados
dos objetivos operacionais e riscos do fluxo. Erros MUST ser acionaveis, sem exposicao
indevida de segredo ou dado sensivel, e a telemetria MUST permitir diagnostico de
causa raiz sem depender de reproducao manual.

Rationale: sistemas sem observabilidade adequada tornam suporte, operacao e auditoria
reativos e caros.

### V. Manutenibilidade e Escalabilidade por Design
A arquitetura e a implementacao MUST favorecer manutencao segura e crescimento
previsivel. Modulos, servicos e componentes MUST ser projetados para facilitar
evolucao, troca controlada, isolamento de impacto e diagnostico. Dependencias,
contratos, ownership tecnico e pontos de extensao MUST ser claros. Escalabilidade
MUST ser tratada como requisito de arquitetura quando houver expectativa de aumento
de carga, volume de dados, usuarios, integracoes ou times atuando em paralelo; isso
inclui identificacao de gargalos, limites conhecidos, estrategias de particionamento,
concorrencia, cache, filas ou elasticidade quando aplicavel.

Rationale: sistemas faceis de alterar e capazes de crescer com previsibilidade
reduzem custo total de propriedade e evitam reescritas reativas.

### VI. Experiencia, Performance e Qualidade de Entrega
Toda interface MUST ser util, acessivel, consistente e orientada a tarefa do usuario.
Principios de UX MUST colocar o usuario no centro do fluxo e cobrir utilidade,
usabilidade e desejabilidade. A interface MUST adotar hierarquia de informacao clara,
respeitar o contexto operacional, preservar controle do usuario sobre navegacao e
desfazer quando aplicavel, limitar opcoes concorrentes para reduzir carga decisoria
conforme a Lei de Hick e tornar a boa experiencia "invisivel", isto e, o usuario MUST
focar no objetivo e nao em decifrar a interface. Principios de UI MUST garantir
contraste adequado, alinhamento, proximidade, repeticao consistente de padroes,
espacamento suficiente e hierarquia visual explicita. O sistema SHOULD aplicar a regra
60-30-10 para composicao cromatica: 60% base neutra ou primaria, 30% cor secundaria e
10% destaque intencional. A semantica de cores MUST ser consistente em todo o produto:
verde para sucesso ou confirmacao, vermelho para erro, perigo ou exclusao, amarelo
para aviso e azul para informacao, neutralidade ou confianca. Cores neutras MUST
suportar fundo, borda e texto com contraste e legibilidade adequados.
Quando houver interface, o projeto MUST operar com principios de Design System.
Consistencia e coesao visual e comportamental MUST ser mantidas em toda a plataforma.
Componentes reutilizaveis, design tokens e padroes compartilhados MUST ser priorizados
antes da criacao de variacoes locais. O Design System MUST considerar acessibilidade
conforme WCAG, clareza, hierarquia, funcionalidade, autenticidade da marca e
documentacao utilizavel por design e desenvolvimento. Novos componentes ou variacoes
MUST nascer documentados, com criterio de uso, estados, comportamento, restricoes,
tokens associados e guidance de acessibilidade.
Para frontends web, React + Next.js + TypeScript MUST ser a stack principal de
implementacao. O projeto MUST usar shadcn/ui como base tecnica e estrutural de
componentes de interface, nunca como fonte de verdade visual. O projeto MUST priorizar
componentes de superficie em shadcn/ui (ou sua familia instalada no repositorio) antes de criar
implementacao propria de componentes como button, input, dialog, tabela, dropdown, modal,
card, badge, menu e equivalentes.

A fonte normativa para decisoes visuais e comportamentais MUST ser o Design System da
Atlassian, com valores, tokens, estados, acessibilidade, semantica e documentacao
centralizados. O codigo de UI implementado localmente MUST ser derivado de shadcn/ui quando
tecnicamente possivel, e so pode divergir por configuracao, composicao ou estilizacao
adicional alinhada aos tokens adotados.

Criacao de componente novo fora do acervo shadcn/ui padrao ou da familia ja instalada no
repositorio e permitida apenas com justificativa forte e rastreavel. Essa justificativa
MUST documentar: (a) ausencia funcional no shadcn/ui para o caso concreto, (b) por que
composicao/extensao nao resolve, (c) impacto de manutencao e cobertura de estados, (d) owner e
prazo de revisao do componente.

Antes de propor novo componente de UI, o time MUST validar:
1. Componente shadcn/ui equivalente e suficiente por composição.
2. Componente local reutilizável equivalente já existente.
3. Variação controlada via props/variants sem criação de novo componente.

Valores visuais como cores, bordas, raios, opacidade, sombras, elevacao, hover, pressed,
disabled, selected e foco MUST vir de uma camada central de tokens. Componentes derivados
de shadcn/ui MUST ser adaptados para obedecer a esse contrato. Botoes MUST
suportar ao menos variantes primary, secondary e subtle, com semantica coerente.
Todo componente interativo MUST contemplar estados default, hover, active ou pressed,
focus visible, disabled, loading quando aplicavel e selected quando aplicavel.
Componentes focaveis MUST ser navegaveis por teclado, semanticamente corretos e com
foco visivel consistente. Tipografia, espacamento e composicao MUST seguir escalas
centralizadas. Regra de negocio, dados e apresentacao SHOULD permanecer desacoplados
na camada de UI. Instrucoes para geracao assistida por IA MUST explicitar que
shadcn/ui e apenas a base estrutural e que tokens, comportamento e aparencia derivam
da Atlassian.
Performance MUST ser tratada como requisito funcional: tempos de resposta, consumo de
recursos e fluidez de interface precisam de metas explicitas e verificaveis. Mudancas
MUST incluir validacao proporcional ao risco, cobrindo comportamento funcional,
seguranca, observabilidade e desempenho.

Rationale: software correto, mas lento, confuso ou opaco, falha em entregar valor
real ao usuario e ao negocio.

## Requisitos e Restricoes de Engenharia

Especificacoes e planos MUST registrar explicitamente:

- impacto de seguranca, compliance, auditoria e observabilidade;
- requisitos de manutenibilidade, evolucao e ownership dos componentes criticos;
- metas e limites de escalabilidade, capacidade e crescimento esperado;
- metas de performance, capacidade e comportamento sob falha;
- requisitos de UX, acessibilidade, consistencia visual e semantica de cores quando
  houver interface;
- regras de Design System, incluindo componentes reutilizaveis, design tokens,
  documentacao e aderencia a WCAG quando houver interface;
- convencoes de API e nomeacao de resources quando houver interfaces REST, incluindo
  substantivos, pluralizacao consistente, paths hierarquicos, lowercase com hifens e
  ausencia de verbos CRUD em URIs;
- estrategia de versionamento para APIs REST quando houver interface publica, incluindo
  criterio de breaking change, major version, abordagem de versionamento adotada e
  politica de depreciacao;
- estrategia de hipermidia para APIs REST quando navegacao dinamica entre recursos for
  requisito, incluindo formato de links, relacoes, descoberta de acoes e consistencia
  de controles HATEOAS;
- padrao de erros HTTP para APIs REST, incluindo uso de RFC 9457, `application/problem+json`,
  problem types documentados, semantica dos campos e politica para extensoes;
- stack de interface, convencoes de implementacao e adaptacao de componentes quando
  houver frontend web, incluindo React, Next.js, TypeScript, shadcn/ui e aderencia
  normativa ao Design System da Atlassian;
- decisoes de arquitetura que introduzam complexidade, com justificativa objetiva;
- estrategia de testes proporcional ao risco, cobrindo fluxos criticos e regressao.

### Governança de componentes de UI (Reuso, controle de duplicação e exceções)

O repositorio e o projeto MUST adotar politica de reuso estrito de componentes para reduzir
divida de UX e codigo.

Todo novo componente de UI só pode ser criado após:
- Revisão do catálogo em `frontend/src/components/ui` para reutilização.
- Verificação de composição possível a partir de componentes shadcn/ui existentes.
- Validação prévia de tokenização com o Design System antes de criar estilos novos.

São critérios de aceitação para criação de novo componente não existente no repertório:
- Exceção formal com justificativa escrita no PR, contendo:
  - problema funcional não coberto por composição;
  - análise de duas alternativas rejeitadas (composição/extensão e composição condicional via props);
  - impacto em acessibilidade e estados (hover/focus/active/disabled/loading/selected);
  - mapeamento de tokens usados (cores, tipografia, raio, espacamento, sombra, foco);
  - owner técnico, suíte de testes e documentação de uso.
- Aprovação obrigatória de arquitetura/frontend no gate de revisão.

Medidas práticas de controle de criação desgovernada:
- Cada PR com novo componente deve incluir secao de "Racional de novo componente" no template de revisao.
- Manter catalogo unico de componentes em `frontend/src/components/ui` e revisar duplicacoes por
  semantica visual/funcional antes do merge.
- Proibir variacoes locais que apenas trocam classes sem nova semantica de comportamento.
- Exigir validacao de acessibilidade minima para novos componentes interativos.
- Registrar excecoes aprovadas em changelog tecnico e revisar trimestralmente deduplicacao.

Solucoes que aumentem acoplamento, multipliquem pontos de configuracao ou criem
abstracoes sem caso concreto SHOULD ser rejeitadas em revisao. Reuso MUST priorizar
componentes e padroes existentes do repositorio antes de novas implementacoes.

## Fluxo de Entrega e Quality Gates

Toda entrega MUST passar por revisao de codigo com verificacao explicita desta
constituicao. O fluxo de desenvolvimento MUST validar:

- aderencia a KISS, DRY, YAGNI e principios SOLID aplicaveis;
- preservacao de manutenibilidade, isolamento de impacto e clareza de ownership;
- riscos de escalabilidade, gargalos previstos e evidencias de capacidade esperada;
- ameacas, controles e impacto regulatorio da mudanca;
- instrumentacao minima de logs, metricas e trilha de auditoria;
- experiencia do usuario, acessibilidade, hierarquia visual, semantica de cores e
  mensagens de erro;
- aderencia ao Design System, reutilizacao de componentes, design tokens e
  documentacao das variacoes visuais ou comportamentais;
- aderencia das APIs REST a resource naming consistente, sem verbos em URI e com uso
  correto de metodos HTTP, query parameters e hierarquia de recursos;
- aderencia das APIs REST a estrategia uniforme de versionamento, criterios de
  breaking change e documentacao de depreciacao e compatibilidade;
- aderencia das APIs REST a HATEOAS quando aplicavel, com links navegaveis,
  relacoes consistentes e ausencia de acoplamento desnecessario do cliente a paths;
- aderencia das APIs REST a RFC 9457 para respostas de erro, com `type`, `title`,
  `status`, `detail` e `instance` coerentes e sem exposicao indevida de internals;
- aderencia da proposta de novos componentes de UI ao fluxo de reuso: reutilizar shadcn/ui
  e componentes locais antes de criar novos componentes;
- aderencia da implementacao frontend a React, Next.js, TypeScript, shadcn/ui como
  base tecnica e Atlassian como contrato visual e comportamental;
- metas de performance e ausencia de degradacao critica;
- cobertura de testes e plano de rollback quando o risco operacional justificar.

Pull requests SHOULD descrever trade-offs, riscos residuais e evidencias de validacao.
Falhas contra principios MUST bloquear a entrega ate correcao ou aprovacao formal com
justificativa registrada.

## Governance

Esta constituicao prevalece sobre praticas locais informais. Toda alteracao MUST ser
proposta de forma explicita, revisada pelos responsaveis tecnicos do projeto e
acompanhada de avaliacao de impacto nos templates, processos e artefatos derivados.
Versionamento segue SemVer: MAJOR para remocao ou redefinicao incompativel de
principios, MINOR para novos principios ou expansoes normativas relevantes e PATCH
para clarificacoes sem impacto de governanca. Revisoes de conformidade com esta
constituicao MUST ocorrer em especificacoes, planos, tarefas e revisoes de codigo.
Excecoes MUST ser temporarias, documentadas e com prazo de remediacao definido.

**Version**: 1.9.0 | **Ratified**: 2026-03-20 | **Last Amended**: 2026-03-22
