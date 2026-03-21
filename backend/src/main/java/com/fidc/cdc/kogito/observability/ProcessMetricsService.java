package com.fidc.cdc.kogito.observability;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
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

    public void registerConsoleAccess(String console, String outcome) {
        Counter.builder("fidc.console.access")
                .tag("console", console)
                .tag("outcome", outcome)
                .register(meterRegistry)
                .increment();
    }

    public void recordProjectionLag(String projection, long millis) {
        DistributionSummary.builder("fidc.readmodel.projection.lag.ms")
                .baseUnit("milliseconds")
                .tag("projection", projection)
                .register(meterRegistry)
                .record(millis);
    }
}
