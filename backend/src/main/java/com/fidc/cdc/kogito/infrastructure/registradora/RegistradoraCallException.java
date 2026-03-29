package com.fidc.cdc.kogito.infrastructure.registradora;

import com.fidc.cdc.kogito.api.error.ApiProblemException;

/**
 * Sinaliza falhas relacionadas a registradora call.
 *
 * <p>Este tipo pertence a camada de adaptadores tecnicos e integracoes de infraestrutura. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
public class RegistradoraCallException extends ApiProblemException {

    private final boolean retryable;

    public RegistradoraCallException(String message, boolean retryable, Throwable cause) {
        super("external-service-error", message);
        this.retryable = retryable;
        initCause(cause);
    }

    public RegistradoraCallException(String message, boolean retryable) {
        super("external-service-error", message);
        this.retryable = retryable;
    }

    public boolean isRetryable() {
        return retryable;
    }
}
