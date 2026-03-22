# Backend Evidence

## Escopo

- configuracao de aplicacao
- seguranca do backend
- tratamento de erros
- testes do backend

## Evidencias

| ID | Origem | Resumo | Leitura |
|----|--------|--------|---------|
| BE-001 | D:\desenv\fidc-cdc-kogito\backend\src\main\resources\application.yml | Datasource, Flyway, Kafka, Actuator e parametros de registradora estao externalizados por variaveis de ambiente | Boa aderencia de configuracao |
| BE-002 | D:\desenv\fidc-cdc-kogito\backend\src\main\java\com\fidc\cdc\kogito\security\SecurityConfig.java | Backend usa `Basic Auth` com usuarios em memoria e regras explicitas de autorizacao por rota | Controle funcional, mas baseline simples |
| BE-003 | D:\desenv\fidc-cdc-kogito\backend\src\main\resources\log4j2-spring.xml | Logging inclui `correlationId` no pattern do console | Aderencia positiva de observabilidade |
| BE-004 | D:\desenv\fidc-cdc-kogito\backend\src\main\java\com\fidc\cdc\kogito\api\error\ProblemDetailsHandler.java | Existe camada de problem details no backend | Indica alinhamento com RFC 9457 |
| BE-005 | `mvn test` em D:\desenv\fidc-cdc-kogito\backend | Build sucesso; 22 testes executados, 21 skipped por indisponibilidade de Docker/Testcontainers | Cobertura repetivel parcial no host atual |

## Observacoes

- o backend apresenta boa externalizacao de configuracoes e base de observabilidade;
- a evidencia de integracao ponta a ponta fica limitada no host sem Docker valido;
- a estrategia de autenticacao do backend difere do ecossistema Keycloak presente no
  compose, exigindo alinhamento documental.
