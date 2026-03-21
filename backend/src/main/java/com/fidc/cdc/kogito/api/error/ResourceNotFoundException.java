package com.fidc.cdc.kogito.api.error;

public class ResourceNotFoundException extends ApiProblemException {

    public ResourceNotFoundException(String message) {
        super("resource-not-found", message);
    }
}
