# UI Contract: Process SVG no Management Console

## 1. Objetivo

Definir o contrato de integracao entre o backend Kogito deste repositorio e o addon `Process SVG` exibido pelo Kogito Management Console, cobrindo elegibilidade, fonte dos dados, estados de exibicao e validacoes obrigatorias.

## 2. Elegibilidade do addon

- O addon `Process SVG` so pode aparecer para instancias cujo processo possua SVG disponivel no contrato oficial do Kogito.
- O criterio de disponibilidade primario e a resolucao de `META-INF/processSVG/{processId}.svg` pelo addon oficial `kie-addons-springboot-process-svg`.
- A integracao nao deve depender de um SVG desenhado manualmente em endpoint proprio como fonte principal de exibicao.

## 3. Fontes de dados

### 3.1 Diagrama visual

- O diagrama visual deve ser servido pelas operacoes REST oficiais expostas pelo addon `process-svg` do Kogito 10.1.
- A implementacao do repositorio deve evitar expandir ou perpetuar um caminho custom que replique a responsabilidade do addon oficial.
- Se existir compatibilidade temporaria com endpoint legado, esse endpoint deve funcionar apenas como transicao tecnica e nao como contrato final da feature.

### 3.2 Etapa textual atual

- A indicacao textual da etapa atual deve vir da logica de `ManagementConsoleSupport.describeProcessContext(...)`.
- A fonte textual deve seguir esta ordem:
  1. etapa em execucao;
  2. ultima etapa concluida, quando o processo estiver encerrado;
  3. estado indeterminado com mensagem explicita.

### 3.3 Fonte de verdade em divergencias

- Quando houver divergencia temporaria entre o SVG destacado e o estado atual da instancia, a etapa mais recente calculada pelo estado da instancia prevalece.
- O addon deve manter coerencia entre indicacao textual e destaque visual final apresentado ao usuario.

## 4. Estados obrigatorios de exibicao

### 4.1 `diagram-ready`

- O SVG do processo aparece no addon.
- A etapa atual ou a ultima etapa concluida fica destacada.
- O nome textual da etapa fica visivel junto ao diagrama.

### 4.2 `no-svg`

- O addon informa que o fluxo nao pode ser exibido naquele momento.
- O estado deve permitir diagnostico tecnico de `svg-missing` sem bloquear a tela principal do console.

### 4.3 `no-current-stage`

- O SVG pode continuar visivel se existir.
- A mensagem textual deve informar que a etapa atual nao foi determinada.

### 4.4 `unauthorized`

- A integracao nao pode expor dados da instancia para usuario sem autorizacao.
- Falhas de acesso devem permanecer auditaveis.

### 4.5 `error`

- Falhas de consulta, carregamento ou reconciliacao visual devem gerar mensagem compreensivel e sinal observavel para diagnostico.

## 5. Regras de processo encerrado

- Quando nao houver etapa ativa e o processo estiver encerrado, o destaque deve recair sobre a ultima etapa concluida conhecida.
- A mensagem textual deve informar explicitamente que nao ha etapa ativa.
- A interface nao deve simular uma etapa em andamento quando o status for finalizado.

## 6. Seguranca e auditoria

- O acesso ao addon e ao contexto textual deve respeitar os mesmos controles de visualizacao do Management Console e do backend.
- A estrategia adotada nesta entrega preserva acesso `GET` ao contrato oficial de SVG e de `management/processes` apenas para compatibilidade tecnica com o ecossistema do console no ambiente atual, mantendo os demais endpoints sob autenticacao normal do backend.
- A implementacao deve revisar a exposicao atual de caminhos anonimos relacionados a SVG e `management/processes`.
- Devem existir evidencias para:
  - carregamento bem-sucedido do diagrama;
  - diagrama ausente;
  - etapa textual ausente;
  - divergencia entre etapa textual e destaque visual;
  - falha de consulta ou acesso negado.

## 7. Evidencias minimas de validacao

- Teste de integracao backend confirmando elegibilidade do addon para processo com SVG.
- Teste ou validacao tecnica confirmando fallback para ultima etapa concluida em processo encerrado.
- Validacao tecnica da stack externa confirmando disponibilidade do fluxo no Management Console.
- Validacao de ausencia de SVG com mensagem clara.
- Validacao de acesso nao autorizado ou nao elegivel sem exposicao indevida.
