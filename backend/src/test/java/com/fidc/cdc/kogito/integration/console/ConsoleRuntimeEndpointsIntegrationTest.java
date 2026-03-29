package com.fidc.cdc.kogito.integration.console;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

@EnabledIfSystemProperty(named = "fidc.external-stack", matches = "true")
class ConsoleRuntimeEndpointsIntegrationTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private static final String BACKEND_URL = System.getProperty("fidc.external.backend-url", "http://localhost:8080");
    private static final String DATA_INDEX_URL = System.getProperty("fidc.external.data-index-url", "http://localhost:8180/graphql");
    private static final String MANAGEMENT_CONSOLE_URL = System.getProperty(
            "fidc.external.management-console-url",
            "http://localhost:8380"
    );
    private static final String TASK_CONSOLE_URL = System.getProperty("fidc.external.task-console-url", "http://localhost:8280");
    private static final String KEYCLOAK_URL = System.getProperty("fidc.external.keycloak-url", "http://localhost:8480/auth");

    @Test
    void shouldResolveProcessDiagramAndTaskSchemaThroughConsoleRuntimeEndpoints() throws Exception {
        String businessKey = "BK-CONSOLE-E2E-" + System.currentTimeMillis();

        createCessao(businessKey);
        JsonNode process = awaitProcessInstance(businessKey);
        String processInstanceId = process.path("id").asText();
        JsonNode tasks = awaitUserTaskInstances(processInstanceId, 1);
        JsonNode activeTask = tasks.get(0);
        String taskId = activeTask.path("id").asText();
        String taskEndpoint = normalizeRuntimeUrl(activeTask.path("endpoint").asText());
        String processId = process.path("processId").asText();
        JsonNode processDefinition = fetchProcessDefinition(processId);
        String processSvg = fetch(BACKEND_URL + "/svg/processes/" + processId).body();
        String instanceSvg = fetch(BACKEND_URL + "/svg/processes/" + processId + "/instances/" + processInstanceId).body();

        assertThat(processInstanceId).isNotBlank();
        assertThat(processId).isEqualTo("controle-cessao");
        assertThat(processDefinition.path("addons")).anySatisfy(node -> assertThat(node.asText()).isEqualTo("process-svg"));
        assertThat(fetch(BACKEND_URL + "/management/processes/controle-cessao/source").statusCode()).isEqualTo(200);
        assertThat(processSvg).contains("<svg").contains("task-01").contains("gateway-lastros");
        assertThat(instanceSvg).contains("<svg");
        assertThat(fetch(MANAGEMENT_CONSOLE_URL + "/Process/" + processInstanceId).statusCode()).isEqualTo(200);
        assertThat(fetch(TASK_CONSOLE_URL + "/TaskDetails/" + taskId).statusCode()).isEqualTo(200);

        JsonNode taskSchema = fetchJson(taskEndpoint + "/schema?user=kogito-admin&group=OPERADOR");
        assertThat(taskSchema.path("phases")).anySatisfy(node -> assertThat(node.asText()).isEqualToIgnoringCase("complete"));
        assertThat(taskSchema.path("properties").path("justificativa").path("type").asText()).isEqualTo("string");

        HttpResponse<String> submitResponse = postJson(
                taskEndpoint + "?phase=complete&user=kogito-admin&group=OPERADOR",
                """
                        {"justificativa":"execucao e2e via task console"}
                        """
        );
        assertThat(submitResponse.statusCode()).isEqualTo(200);

        JsonNode tasksAfterSubmit = awaitUserTaskInstances(processInstanceId, 2);
        assertThat(findTaskByReferenceName(tasksAfterSubmit, "VALIDAR_CEDENTE")).isNotNull();
    }

    @Test
    void shouldAcceptKeycloakBearerTokenOnProtectedManagementMutation() throws Exception {
        String token = fetchAccessToken();
        String businessKey = "BK-CONSOLE-ABORT-" + System.currentTimeMillis();

        createCessao(businessKey);
        JsonNode process = awaitProcessInstance(businessKey);
        String processInstanceId = process.path("id").asText();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BACKEND_URL + "/management/processes/controle-cessao/instances/" + processInstanceId))
                .header("Authorization", "Bearer " + token)
                .DELETE()
                .build();

        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    void shouldPropagateKeycloakBearerTokenThroughDataIndexAbortMutation() throws Exception {
        String token = fetchAccessToken();
        String businessKey = "BK-DATA-INDEX-ABORT-" + System.currentTimeMillis();

        createCessao(businessKey);
        JsonNode process = awaitProcessInstance(businessKey);
        String processInstanceId = process.path("id").asText();

        JsonNode mutationResponse = executeGraphql(
                """
                        mutation($processId: String) {
                          ProcessInstanceAbort(id: $processId)
                        }
                        """,
                Map.of("processId", processInstanceId),
                token
        );

        assertThat(mutationResponse.path("data").path("ProcessInstanceAbort").asText()).isNotBlank();

        JsonNode abortedProcess = awaitProcessState(processInstanceId, "ABORTED");
        assertThat(abortedProcess.path("state").asText()).isEqualTo("ABORTED");
    }

    private String normalizeRuntimeUrl(String url) {
        if (url == null || url.isBlank()) {
            return url;
        }
        return url.replace("http://backend:8080", BACKEND_URL);
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

    private JsonNode awaitProcessInstance(String businessKey) throws Exception {
        Instant deadline = Instant.now().plusSeconds(45);
        JsonNode lastResponse = null;
        while (Instant.now().isBefore(deadline)) {
            lastResponse = executeGraphql("""
                    query($businessKey: String!) {
                      ProcessInstances(where: {businessKey: {equal: $businessKey}}) {
                        id
                        businessKey
                        processId
                      }
                    }
                    """, Map.of("businessKey", businessKey));
            JsonNode instances = lastResponse.path("data").path("ProcessInstances");
            if (instances.isArray() && instances.size() >= 1) {
                return instances.get(0);
            }
            Thread.sleep(Duration.ofSeconds(2));
        }
        throw new AssertionError("GraphQL nao retornou a instancia de processo para a cessao: " + lastResponse);
    }

    private JsonNode awaitUserTaskInstances(String processInstanceId, int minimumSize) throws Exception {
        Instant deadline = Instant.now().plusSeconds(45);
        JsonNode lastResponse = null;
        while (Instant.now().isBefore(deadline)) {
            lastResponse = executeGraphql("""
                    query($processInstanceId: String!) {
                      UserTaskInstances(where: {processInstanceId: {equal: $processInstanceId}}) {
                        id
                        endpoint
                        referenceName
                        state
                      }
                    }
                    """, Map.of("processInstanceId", processInstanceId));
            JsonNode tasks = lastResponse.path("data").path("UserTaskInstances");
            if (tasks.isArray() && tasks.size() >= minimumSize) {
                return tasks;
            }
            Thread.sleep(Duration.ofSeconds(2));
        }
        throw new AssertionError("GraphQL nao retornou tasks para a instancia: " + lastResponse);
    }

    private JsonNode fetchProcessDefinition(String processId) throws Exception {
        JsonNode payload = executeGraphql("""
                query($processId: String!) {
                  ProcessDefinitions(where: {id: {equal: $processId}}) {
                    id
                    addons
                    serviceUrl
                  }
                }
                """, Map.of("processId", processId));
        JsonNode definitions = payload.path("data").path("ProcessDefinitions");
        assertThat(definitions.isArray()).isTrue();
        assertThat(definitions).hasSize(1);
        return definitions.get(0);
    }

    private JsonNode executeGraphql(String query, Map<String, Object> variables) throws Exception {
        return executeGraphql(query, variables, null);
    }

    private JsonNode executeGraphql(String query, Map<String, Object> variables, String bearerToken) throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(DATA_INDEX_URL))
                .header("Content-Type", "application/json");
        if (bearerToken != null && !bearerToken.isBlank()) {
            requestBuilder.header("Authorization", "Bearer " + bearerToken);
        }

        HttpResponse<String> response = HTTP_CLIENT.send(
                requestBuilder.POST(HttpRequest.BodyPublishers.ofString(
                        OBJECT_MAPPER.writeValueAsString(Map.of("query", query, "variables", variables))
                )).build(),
                HttpResponse.BodyHandlers.ofString()
        );
        assertThat(response.statusCode()).isEqualTo(200);
        JsonNode payload = OBJECT_MAPPER.readTree(response.body());
        assertThat(payload.path("errors").isMissingNode() || payload.path("errors").isEmpty()).isTrue();
        return payload;
    }

    private JsonNode awaitProcessState(String processInstanceId, String expectedState) throws Exception {
        Instant deadline = Instant.now().plusSeconds(45);
        JsonNode lastResponse = null;
        while (Instant.now().isBefore(deadline)) {
            lastResponse = executeGraphql("""
                    query($processInstanceId: String!) {
                      ProcessInstances(where: {id: {equal: $processInstanceId}}) {
                        id
                        state
                      }
                    }
                    """, Map.of("processInstanceId", processInstanceId));
            JsonNode instances = lastResponse.path("data").path("ProcessInstances");
            if (instances.isArray() && instances.size() == 1) {
                JsonNode process = instances.get(0);
                if (expectedState.equals(process.path("state").asText())) {
                    return process;
                }
            }
            Thread.sleep(Duration.ofSeconds(2));
        }
        throw new AssertionError("GraphQL nao retornou o estado esperado para a instancia: " + lastResponse);
    }

    private JsonNode fetchJson(String url) throws Exception {
        HttpResponse<String> response = fetch(url);
        assertThat(response.statusCode()).isEqualTo(200);
        return OBJECT_MAPPER.readTree(response.body());
    }

    private HttpResponse<String> fetch(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        return HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> postJson(String url, String body) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        return HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private String fetchAccessToken() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(KEYCLOAK_URL + "/realms/kogito/protocol/openid-connect/token"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "client_id=kogito-console-quarkus&username=kogito-admin&password=kogito123&grant_type=password"
                ))
                .build();

        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        assertThat(response.statusCode()).isEqualTo(200);
        return OBJECT_MAPPER.readTree(response.body()).path("access_token").asText();
    }

    private JsonNode findTaskByReferenceName(JsonNode tasks, String referenceName) {
        for (JsonNode task : tasks) {
            if (referenceName.equals(task.path("referenceName").asText())) {
                return task;
            }
        }
        return null;
    }

    private String basicAuth(String username, String password) {
        return "Basic " + Base64.getEncoder()
                .encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
    }
}
