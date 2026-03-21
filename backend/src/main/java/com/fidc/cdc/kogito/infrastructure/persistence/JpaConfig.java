package com.fidc.cdc.kogito.infrastructure.persistence;

import java.time.OffsetDateTime;
import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider", auditorAwareRef = "auditorAware")
public class JpaConfig {

    @Bean
    AuditorAware<String> auditorAware() {
        return () -> Optional.of("system");
    }

    @Bean
    DateTimeProvider auditingDateTimeProvider() {
        return () -> Optional.of(OffsetDateTime.now());
    }
}
