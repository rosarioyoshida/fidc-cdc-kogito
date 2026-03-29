package com.fidc.cdc.kogito.infrastructure.registradora;

import com.fidc.cdc.kogito.observability.RegistradoraMetricsService;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;

/**
 * Define a politica de registradora retry.
 *
 * <p>Este tipo pertence a camada de adaptadores tecnicos e integracoes de infraestrutura. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
@Component
public class RegistradoraRetryPolicy {

    private final RegistradoraProperties properties;
    private final RegistradoraMetricsService metricsService;

    public RegistradoraRetryPolicy(
            RegistradoraProperties properties,
            RegistradoraMetricsService metricsService
    ) {
        this.properties = properties;
        this.metricsService = metricsService;
    }

    public <T> T execute(String operation, RetryableOperation<T> operationSupplier) {
        RegistradoraCallException lastFailure = null;
        for (int attempt = 1; attempt <= properties.getRetries().getMaxAttempts(); attempt++) {
            try {
                return operationSupplier.execute(attempt);
            } catch (RegistradoraCallException ex) {
                lastFailure = ex;
                if (!ex.isRetryable() || attempt >= properties.getRetries().getMaxAttempts()) {
                    throw ex;
                }
                metricsService.registerRetry(operation);
                sleep();
            }
        }
        throw lastFailure == null
                ? new RegistradoraCallException("Falha inesperada ao acionar a registradora.", false)
                : lastFailure;
    }

    private void sleep() {
        try {
            TimeUnit.MILLISECONDS.sleep(properties.getRetries().getDelay().toMillis());
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new RegistradoraCallException("Execucao interrompida durante retry da registradora.", false, ex);
        }
    }

    /**
     * Define o contrato de retryable operation.
     *
     * <p>Este tipo pertence a camada de adaptadores tecnicos e integracoes de infraestrutura. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
     */
    @FunctionalInterface
    public interface RetryableOperation<T> {
        T execute(int attempt);
    }
}
