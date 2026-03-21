package com.fidc.cdc.kogito.api.error;

public class ForbiddenOperationException extends ApiProblemException {

    public ForbiddenOperationException(String message) {
        super("forbidden-operation", message);
    }
}
