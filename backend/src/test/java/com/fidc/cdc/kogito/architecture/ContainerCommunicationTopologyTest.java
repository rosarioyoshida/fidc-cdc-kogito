package com.fidc.cdc.kogito.architecture;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

class ContainerCommunicationTopologyTest {

    private static final Path COMPOSE_FILE = Path.of("..", "infra", "compose", "docker-compose.yml").normalize();

    @Test
    void shouldKeepComposeCommunicationAlignedWithExpectedContainerTopology() throws IOException {
        Map<String, Object> compose = readYaml(COMPOSE_FILE);
        Map<String, Object> services = map(compose.get("services"));

        assertThat(compose.get("name")).isEqualTo("fidc-cdc-kogito");
        assertThat(services.keySet()).contains(
                "backend",
                "frontend",
                "postgres",
                "kafka",
                "data-index",
                "jobs-service",
                "task-console",
                "management-console"
        );
        assertThat(services.keySet()).doesNotContain("mysql", "mongodb");

        assertThat(env(services, "frontend", "FIDC_API_INTERNAL_URL")).contains("http://backend:8080/api/v1");

        assertThat(env(services, "backend", "SPRING_DATASOURCE_URL")).isEqualTo("jdbc:postgresql://postgres:5432/fidc_cdc");
        assertThat(dependsOn(services, "backend")).containsKey("postgres");

        assertThat(env(services, "backend", "SPRING_KAFKA_BOOTSTRAP_SERVERS")).isEqualTo("kafka:9092");
        assertThat(env(services, "backend", "FIDC_TASK_CONSOLE_URL")).isEqualTo("http://localhost:8280");
        assertThat(env(services, "backend", "FIDC_MANAGEMENT_CONSOLE_URL")).isEqualTo("http://localhost:8380");

        assertThat(map(services.get("data-index")).get("image"))
                .isEqualTo("apache/incubator-kie-kogito-data-index-postgresql:10.1.0");
        assertThat(env(services, "data-index", "KAFKA_BOOTSTRAP_SERVERS")).isEqualTo("kafka:9092");
        assertThat(env(services, "data-index", "QUARKUS_DATASOURCE_JDBC_URL"))
                .isEqualTo("jdbc:postgresql://postgres:5432/kogito_data_index");
        assertThat(dependsOn(services, "data-index")).containsKey("postgres");

        assertThat(map(services.get("jobs-service")).get("image"))
                .isEqualTo("apache/incubator-kie-kogito-jobs-service-postgresql:10.1.0");
        assertThat(env(services, "backend", "FIDC_JOBS_SERVICE_URL")).isEqualTo("http://jobs-service:8080");
        assertThat(env(services, "backend", "FIDC_JOBS_PUBLIC_SERVICE_URL")).isEqualTo("http://localhost:8085");
        assertThat(env(services, "backend", "FIDC_JOBS_CALLBACK_BASE_URL"))
                .isEqualTo("http://backend:8080/api/v1/process/jobs/callbacks");
        assertThat(env(services, "jobs-service", "KAFKA_BOOTSTRAP_SERVERS")).isEqualTo("kafka:9092");
        assertThat(env(services, "jobs-service", "QUARKUS_DATASOURCE_JDBC_URL"))
                .isEqualTo("jdbc:postgresql://postgres:5432/kogito_jobs");
        assertThat(env(services, "jobs-service", "QUARKUS_DATASOURCE_REACTIVE_URL"))
                .isEqualTo("postgresql://postgres:5432/kogito_jobs");
        assertThat(dependsOn(services, "jobs-service")).containsKey("postgres");

        assertThat(env(services, "backend", "FIDC_DATA_INDEX_URL"))
                .isEqualTo("http://localhost:8180/graphql");
        assertThat(env(services, "management-console", "KOGITO_DATAINDEX_HTTP_URL"))
                .isEqualTo("http://localhost:8180/graphql");
        assertThat(env(services, "task-console", "KOGITO_DATAINDEX_HTTP_URL"))
                .isEqualTo("http://localhost:8180/graphql");
        assertThat(dependsOn(services, "management-console")).containsKey("backend");
        assertThat(dependsOn(services, "task-console")).containsKey("backend");
    }

    private static Map<String, Object> readYaml(Path path) throws IOException {
        try (var reader = Files.newBufferedReader(path)) {
            Object loaded = new Yaml().load(reader);
            return map(loaded);
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> map(Object value) {
        return (Map<String, Object>) value;
    }

    private static Map<String, Object> environment(Map<String, Object> services, String serviceName) {
        return map(map(services.get(serviceName)).get("environment"));
    }

    private static String env(Map<String, Object> services, String serviceName, String key) {
        return String.valueOf(environment(services, serviceName).get(key));
    }

    private static Map<String, Object> dependsOn(Map<String, Object> services, String serviceName) {
        return map(map(services.get(serviceName)).get("depends_on"));
    }

}
