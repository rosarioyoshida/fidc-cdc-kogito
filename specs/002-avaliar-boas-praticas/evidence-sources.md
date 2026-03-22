# Evidence Sources

## Fontes principais com caminhos absolutos

- D:\desenv\fidc-cdc-kogito\AGENTS.md
- D:\desenv\fidc-cdc-kogito\backend\pom.xml
- D:\desenv\fidc-cdc-kogito\backend\src\main\resources\application.yml
- D:\desenv\fidc-cdc-kogito\backend\src\main\resources\log4j2-spring.xml
- D:\desenv\fidc-cdc-kogito\backend\src\main\resources\db\migration\
- D:\desenv\fidc-cdc-kogito\backend\src\main\resources\processes\
- D:\desenv\fidc-cdc-kogito\backend\src\main\java\com\fidc\cdc\kogito\security\SecurityConfig.java
- D:\desenv\fidc-cdc-kogito\backend\src\main\java\com\fidc\cdc\kogito\api\error\ProblemDetailsHandler.java
- D:\desenv\fidc-cdc-kogito\frontend\package.json
- D:\desenv\fidc-cdc-kogito\frontend\src\design-system\theme.css
- D:\desenv\fidc-cdc-kogito\frontend\src\lib\problem-details.ts
- D:\desenv\fidc-cdc-kogito\frontend\tests\integration\
- D:\desenv\fidc-cdc-kogito\infra\compose\docker-compose.yml
- D:\desenv\fidc-cdc-kogito\infra\observability\README.md
- D:\desenv\fidc-cdc-kogito\specs\001-controle-cessao-fidc\quickstart.md
- D:\desenv\fidc-cdc-kogito\specs\001-controle-cessao-fidc\contracts\backend-openapi.yaml

## Comandos executados no ciclo inicial

- `git status --short`
- `mvn test` em `D:\desenv\fidc-cdc-kogito\backend`
- `npm test` em `D:\desenv\fidc-cdc-kogito\frontend`
- `npm run lint` em `D:\desenv\fidc-cdc-kogito\frontend`

## Observacoes

- `mvn test` passou, mas a maioria dos testes de integracao ficou `skipped` por falta
  de Docker valido no host.
- `npm run lint` nao executou como gate repetivel porque o projeto ainda solicita a
  inicializacao interativa do ESLint.
