package com.fidc.cdc.kogito.api.error;

import org.springframework.http.HttpStatus;

public record ProblemTypeDefinition(String type, String title, HttpStatus status) {
}
