package com.fidc.cdc.kogito.observability;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

/**
 * Coordena registradora metrics na camada de aplicacao.
 *
 * <p>Este tipo pertence a camada de instrumentacao e observabilidade operacional. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
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
