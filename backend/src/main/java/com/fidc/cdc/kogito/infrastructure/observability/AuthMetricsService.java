package com.fidc.cdc.kogito.infrastructure.observability;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class AuthMetricsService {

    private final MeterRegistry meterRegistry;

    public AuthMetricsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void recordAuthenticationSuccess(String profile) {
        Counter.builder("fidc.auth.events")
                .tag("event", "login-success")
                .tag("profile", profile)
                .register(meterRegistry)
                .increment();
    }

    public void recordAuthenticationFailure(String reason) {
        Counter.builder("fidc.auth.events")
                .tag("event", "login-failure")
                .tag("reason", reason)
                .register(meterRegistry)
                .increment();
    }

    public void recordLogout(String profile) {
        Counter.builder("fidc.auth.events")
                .tag("event", "logout")
                .tag("profile", profile)
                .register(meterRegistry)
                .increment();
    }

    public void recordAccountUpdate(String operation, String outcome) {
        Counter.builder("fidc.account.events")
                .tag("operation", operation)
                .tag("outcome", outcome)
                .register(meterRegistry)
                .increment();
    }
}
