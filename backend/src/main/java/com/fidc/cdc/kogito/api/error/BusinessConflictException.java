package com.fidc.cdc.kogito.api.error;

public class BusinessConflictException extends ApiProblemException {

    public BusinessConflictException(String message) {
        super("business-conflict", message);
    }
}
