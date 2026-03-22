package com.fidc.cdc.kogito.integration.console;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

@EnabledIfSystemProperty(named = "fidc.external-stack", matches = "true")
class DataIndexGraphqlIntegrationTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private static final String BACKEND_URL = System.getProperty("fidc.external.backend-url", "http://localhost:8080");
    private static final String DATA_INDEX_URL = System.getProperty("fidc.external.data-index-url", "http://localhost:8180/graphql");
    private static final String DATA_INDEX_JDBC_URL = System.getProperty(
            "fidc.external.data-index-jdbc-url",
            "jdbc:postgresql://localhost:5432/kogito_data_index"
    );
    private static final String DATA_INDEX_DB_USERNAME = System.getProperty("fidc.external.data-index-db-username", "fidc");
    private static final String DATA_INDEX_DB_PASSWORD = System.getProperty("fidc.external.data-index-db-password", "fidc");

    @Test
    void shouldExposeProcessAndUserTasksThroughDataIndexGraphqlAndPersistThemInPostgresql() throws Exception {
        String businessKey = "BK-GQL-" + System.currentTimeMillis();

        createCessao(businessKey);
        JsonNode processInstancesAfterCreate = awaitProcessInstances(businessKey, 1);
        String processInstanceId = processInstancesAfterCreate.get(0).path("id").asText();
        JsonNode createdProcess = processInstancesAfterCreate.get(0);
        JsonNode userTasksAfterCreate = awaitUserTaskInstances(processInstanceId, 1);
        JsonNode activeTaskAfterCreate = userTasksAfterCreate.get(0);

        assertThat(processInstanceId).isNotBlank();
        assertThat(createdProcess.path("nodes").isArray()).isTrue();
        assertThat(createdProcess.path("nodes")).hasSizeGreaterThanOrEqualTo(1);
        assertThat(createdProcess.path("variables").path("businessKey").asText()).isEqualTo(businessKey);
        assertThat(createdProcess.path("variables").path("status").asText()).isEqualTo("EM_ANDAMENTO");
        assertThat(createdProcess.path("endpoint").asText()).startsWith("http://localhost:8080/");
        assertThat(activeTaskAfterCreate.path("referenceName").asText()).isEqualTo("IMPORTAR_CARTEIRA");
        assertThat(activeTaskAfterCreate.path("potentialUsers")).anySatisfy(node -> assertThat(node.asText()).isEqualTo("kogito-admin"));
        assertThat(activeTaskAfterCreate.path("potentialGroups")).anySatisfy(node -> assertThat(node.asText()).isEqualTo("OPERADOR"));
        assertThat(countProcessesByBusinessKey(businessKey)).isEqualTo(1);
        assertThat(countTasksByProcessInstanceId(processInstanceId)).isGreaterThanOrEqualTo(1);
        assertThat(countTaskAssignments("tasks_potential_users", processInstanceId)).isGreaterThanOrEqualTo(1);
        assertThat(countTaskAssignments("tasks_potential_groups", processInstanceId)).isGreaterThanOrEqualTo(1);

        advanceImportarCarteira(businessKey);
        JsonNode processInstancesAfterAdvance = awaitProcessInstances(businessKey, 1);
        JsonNode userTasksAfterAdvance = awaitUserTaskInstances(processInstanceId, 2);
        JsonNode nextActiveTask = findTaskByReferenceName(userTasksAfterAdvance, "VALIDAR_CEDENTE");

        assertThat(userTasksAfterAdvance.isArray()).isTrue();
        assertThat(userTasksAfterAdvance).hasSizeGreaterThanOrEqualTo(2);
        assertThat(userTasksAfterAdvance.toString()).contains("VALIDAR_CEDENTE");
        assertThat(processInstancesAfterAdvance.get(0).path("nodes")).hasSizeGreaterThanOrEqualTo(2);
        assertThat(nextActiveTask).isNotNull();
        assertThat(nextActiveTask.path("state").asText()).isEqualTo("Ready");
        assertThat(nextActiveTask.path("potentialUsers")).anySatisfy(node -> assertThat(node.asText()).isEqualTo("kogito-admin"));
        assertThat(nextActiveTask.path("potentialGroups")).anySatisfy(node -> assertThat(node.asText()).isEqualTo("OPERADOR"));
        assertThat(countTasksByProcessInstanceId(processInstanceId)).isGreaterThanOrEqualTo(2);
    }

    private void createCessao(String businessKey) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BACKEND_URL + "/api/v1/cessoes"))
                .header("Authorization", basicAuth("operador", "operador123"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("""
                        {"businessKey":"%s","cedenteId":"CED-01","cessionariaId":"CESS-01"}
                        """.formatted(businessKey)))
                .build();

        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        assertThat(response.statusCode()).isEqualTo(201);
    }

    private void advanceImportarCarteira(String businessKey) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BACKEND_URL + "/api/v1/cessoes/" + businessKey + "/etapas/IMPORTAR_CARTEIRA/acoes/avancar"))
                .header("Authorization", basicAuth("operador", "operador123"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("""
                        {"responsavelId":"operador","justificativa":"validacao e2e"}
                        """))
                .build();

        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        assertThat(response.statusCode()).isEqualTo(202);
    }

    private JsonNode awaitProcessInstances(String businessKey, int minimumSize) throws Exception {
        return awaitGraphqlArray("""
                query($businessKey: String!) {
                  ProcessInstances(where: {businessKey: {equal: $businessKey}}) {
                    id
                    processId
                    processName
                    businessKey
                    state
                    endpoint
                    nodes {
                      id
                      name
                      type
                      enter
                      exit
                      definitionId
                    }
                    variables
                  }
                }
                """, Map.of("businessKey", businessKey), "ProcessInstances", minimumSize);
    }

    private JsonNode awaitUserTaskInstances(String processInstanceId, int minimumSize) throws Exception {
        return awaitGraphqlArray("""
                query($processInstanceId: String!) {
                  UserTaskInstances(where: {processInstanceId: {equal: $processInstanceId}}) {
                    id
                    name
                    state
                    processInstanceId
                    referenceName
                    actualOwner
                    potentialUsers
                    potentialGroups
                    adminUsers
                    adminGroups
                  }
                }
                """, Map.of("processInstanceId", processInstanceId), "UserTaskInstances", minimumSize);
    }

    private JsonNode awaitGraphqlArray(
            String query,
            Map<String, Object> variables,
            String field,
            int minimumSize
    ) throws Exception {
        Instant deadline = Instant.now().plusSeconds(45);
        JsonNode lastResponse = null;
        while (Instant.now().isBefore(deadline)) {
            lastResponse = executeGraphql(query, variables);
            JsonNode data = lastResponse.path("data").path(field);
            if (data.isArray() && data.size() >= minimumSize) {
                return data;
            }
            Thread.sleep(Duration.ofSeconds(2));
        }
        throw new AssertionError("GraphQL nao retornou " + minimumSize + " item(ns) para " + field + ": " + lastResponse);
    }

    private JsonNode executeGraphql(String query, Map<String, Object> variables) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(DATA_INDEX_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(OBJECT_MAPPER.writeValueAsString(Map.of(
                        "query", query,
                        "variables", variables
                ))))
                .build();

        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        assertThat(response.statusCode()).isEqualTo(200);
        JsonNode payload = OBJECT_MAPPER.readTree(response.body());
        assertThat(payload.path("errors").isMissingNode() || payload.path("errors").isEmpty()).isTrue();
        return payload;
    }

    private long countRows(String table) throws Exception {
        try (Connection connection = DriverManager.getConnection(
                DATA_INDEX_JDBC_URL,
                DATA_INDEX_DB_USERNAME,
                DATA_INDEX_DB_PASSWORD
        );
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("select count(*) from " + table)) {
            resultSet.next();
            return resultSet.getLong(1);
        }
    }

    private long countProcessesByBusinessKey(String businessKey) throws Exception {
        return countRows("processes", "business_key", businessKey);
    }

    private long countTasksByProcessInstanceId(String processInstanceId) throws Exception {
        return countRows("tasks", "process_instance_id", processInstanceId);
    }

    private long countTaskAssignments(String table, String processInstanceId) throws Exception {
        String taskIdColumn = switch (table) {
            case "tasks_potential_users", "tasks_potential_groups", "tasks_admin_groups", "tasks_admin_users" -> "task_id";
            default -> throw new IllegalArgumentException("Tabela de assignment nao suportada: " + table);
        };
        String sql = """
                select count(*)
                from %s assignment
                join tasks task on task.id = assignment.%s
                where task.process_instance_id = ?
                """.formatted(table, taskIdColumn);
        try (Connection connection = DriverManager.getConnection(
                DATA_INDEX_JDBC_URL,
                DATA_INDEX_DB_USERNAME,
                DATA_INDEX_DB_PASSWORD
        );
             var statement = connection.prepareStatement(sql)) {
            statement.setString(1, processInstanceId);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getLong(1);
            }
        }
    }

    private JsonNode findTaskByReferenceName(JsonNode tasks, String referenceName) {
        for (JsonNode task : tasks) {
            if (referenceName.equals(task.path("referenceName").asText())) {
                return task;
            }
        }
        return null;
    }

    private long countRows(String table, String column, String value) throws Exception {
        String sql = "select count(*) from %s where %s = ?".formatted(table, column);
        try (Connection connection = DriverManager.getConnection(
                DATA_INDEX_JDBC_URL,
                DATA_INDEX_DB_USERNAME,
                DATA_INDEX_DB_PASSWORD
        );
             var statement = connection.prepareStatement(sql)) {
            statement.setString(1, value);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getLong(1);
            }
        }
    }

    private String basicAuth(String username, String password) {
        return "Basic " + Base64.getEncoder()
                .encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
    }
}
