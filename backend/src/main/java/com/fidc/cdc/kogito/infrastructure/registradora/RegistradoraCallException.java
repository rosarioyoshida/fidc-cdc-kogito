package com.fidc.cdc.kogito.infrastructure.registradora;

import com.fidc.cdc.kogito.api.error.ApiProblemException;

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
