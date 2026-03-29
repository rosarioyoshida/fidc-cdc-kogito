package com.fidc.cdc.kogito.observability;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

/**
 * Coordena process metrics na camada de aplicacao.
 *
 * <p>Este tipo pertence a camada de instrumentacao e observabilidade operacional. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
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
