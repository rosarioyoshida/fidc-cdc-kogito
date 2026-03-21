package com.fidc.cdc.kogito.api.error;

public class ApiProblemException extends RuntimeException {

    private final String problemKey;

    public ApiProblemException(String problemKey, String message) {
        super(message);
        this.problemKey = problemKey;
    }

    public String getProblemKey() {
        return problemKey;
    }
}
