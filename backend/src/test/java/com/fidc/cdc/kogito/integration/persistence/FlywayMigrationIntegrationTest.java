package com.fidc.cdc.kogito.integration.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers(disabledWithoutDocker = true)
class FlywayMigrationIntegrationTest {

    @Container
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("fidc_cdc")
            .withUsername("fidc")
            .withPassword("fidc");

    @Test
    void shouldApplyAllVersionedMigrationsOnCleanPostgresDatabase() {
        Flyway flyway = Flyway.configure()
                .dataSource(POSTGRES.getJdbcUrl(), POSTGRES.getUsername(), POSTGRES.getPassword())
                .locations("classpath:db/migration")
                .load();

        var result = flyway.migrate();

        assertThat(result.success).isTrue();
        assertThat(result.migrationsExecuted).isEqualTo(6);
        assertThat(flyway.info().applied())
                .extracting(info -> info.getVersion() == null ? null : info.getVersion().getVersion())
                .containsExactly("1", "2", "3", "4", "5", "6");
    }
}
