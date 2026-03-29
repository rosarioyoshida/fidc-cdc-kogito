# Quickstart: Validar o Addon Process SVG no Management Console

## Pre-requisitos

- Java 21 e Maven disponiveis no ambiente
- Dependencias do backend resolvidas
- Stack externa do Kogito disponivel via `infra/compose/docker-compose.yml`
- SVG do processo exportado e disponivel como `META-INF/processSVG/controle-cessao.svg` no classpath final da aplicacao

## Preparacao

1. Suba a stack externa:

```powershell
cd D:\desenv\fidc-cdc-kogito\infra\compose
docker compose up -d
```

2. Inicie o backend:

```powershell
cd D:\desenv\fidc-cdc-kogito\backend
mvn spring-boot:run
```

3. Confirme que o SVG foi empacotado:

```powershell
Get-ChildItem D:\desenv\fidc-cdc-kogito\backend\target\classes\META-INF\processSVG
```

O resultado esperado inclui `controle-cessao.svg`.

## Roteiro de validacao

### 1. Elegibilidade do addon

1. Crie ou reutilize uma instancia de `controle-cessao`.
2. Abra a instancia correspondente no Management Console.
3. Confirme que o addon `Process SVG` aparece para a instancia elegivel.
4. Confirme que o addon nao aparece para um processo sem SVG disponivel.

### 2. Exibicao do diagrama

1. Abra o addon `Process SVG`.
2. Verifique que o fluxo aparece em SVG.
3. Confirme que a renderizacao inicial ocorre em ate 2 segundos em condicoes normais.

### 3. Etapa atual em andamento

1. Use uma instancia com etapa ativa.
2. Verifique que o diagrama destaca a etapa corrente.
3. Confirme que a indicacao textual da etapa coincide com o destaque visual.

### 4. Processo encerrado

1. Use uma instancia concluida.
2. Abra novamente o addon.
3. Confirme que a ultima etapa concluida fica destacada.
4. Confirme que a mensagem textual informa que nao ha etapa ativa.

### 5. Ausencia de SVG

1. Remova temporariamente o SVG do processo em ambiente de teste ou use um processo sem ativo SVG.
2. Abra a instancia correspondente no Management Console.
3. Confirme a mensagem clara de indisponibilidade do fluxo.

### 6. Etapa indeterminada ou divergente

1. Simule uma instancia sem etapa ativa determinavel ou com divergencia temporaria entre o estado e o SVG.
2. Confirme que a etapa textual mais recente da instancia prevalece.
3. Confirme que o estado gera diagnostico observavel.

### 7. Acesso e falhas

1. Tente acessar a visualizacao sem contexto autorizado, se o ambiente permitir.
2. Confirme que nao ha exposicao indevida do fluxo.
3. Force uma falha de carregamento do runtime ou do addon.
4. Confirme mensagem clara e evidencia tecnica de erro.

### 8. Compatibilidade de autenticacao do console

1. Confirme que o Management Console continua abrindo a instancia e o addon sem regressao apos o endurecimento de seguranca.
2. Verifique que os `GET` tecnicos de SVG e `management/processes` permanecem acessiveis para o ecossistema do console.
3. Verifique que os demais endpoints protegidos do backend continuam exigindo autenticacao normal.

## Verificacoes tecnicas

Execute a suite de testes relevante do backend:

```powershell
cd D:\desenv\fidc-cdc-kogito\backend
mvn test
```

Para validar a stack externa:

```powershell
cd D:\desenv\fidc-cdc-kogito\backend
mvn test -Dfidc.external-stack=true
```

## Evidencias esperadas

- `KogitoConsoleIntegrationTest` cobrindo contexto textual e fallback de etapa
- `ConsoleRuntimeEndpointsIntegrationTest` cobrindo integracao com Data Index / Management Console / Task Console
- Confirmacao de que o SVG do processo esta presente no classpath final
- Confirmacao de que o Management Console mostra o addon apenas para processos elegiveis
- Confirmacao de que a compatibilidade de autenticacao do console foi preservada para os endpoints tecnicos do addon

## Resultado da execucao atual

- Data: 2026-03-25
- Comando executado: `mvn test`
- Resultado: aprovado com `28` testes executados, `0` falhas e `27` cenarios pulados por dependerem de stack externa ou ambiente dedicado
- Comando executado: `docker compose up -d --build backend`
- Resultado: stack externa recomposta com backend atualizado
- Comando executado: `mvn test "-Dfidc.external-stack=true" "-Dtest=ConsoleRuntimeEndpointsIntegrationTest,DataIndexGraphqlIntegrationTest"`
- Resultado: aprovado com `2` testes executados, `0` falhas e validacao do contrato SVG oficial via `/svg/processes/{processId}` e `/svg/processes/{processId}/instances/{processInstanceId}`
