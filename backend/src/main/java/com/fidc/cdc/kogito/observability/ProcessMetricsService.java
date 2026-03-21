package com.fidc.cdc.kogito.observability;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class ProcessMetricsService {

    private final MeterRegistry meterRegistry;

    public ProcessMetricsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void incrementProcessEvent(String stage, String outcome) {
        Counter.builder("fidc.process.events")
                .tag("stage", stage)
                .tag("outcome", outcome)
                .register(meterRegistry)
                .increment();
    }
}
