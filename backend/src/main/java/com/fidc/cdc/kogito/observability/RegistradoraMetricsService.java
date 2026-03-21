package com.fidc.cdc.kogito.observability;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class RegistradoraMetricsService {

    private final MeterRegistry meterRegistry;

    public RegistradoraMetricsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void registerCall(String operation, String outcome) {
        Counter.builder("fidc.registradora.calls")
                .tag("operation", operation)
                .tag("outcome", outcome)
                .register(meterRegistry)
                .increment();
    }

    public void registerRetry(String operation) {
        Counter.builder("fidc.registradora.retries")
                .tag("operation", operation)
                .register(meterRegistry)
                .increment();
    }
}
