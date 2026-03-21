package com.fidc.cdc.kogito.api.error;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Map;
import org.slf4j.MDC;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProblemDetailsHandler {

    private final ProblemTypeRegistry problemTypeRegistry;

    public ProblemDetailsHandler(ProblemTypeRegistry problemTypeRegistry) {
        this.problemTypeRegistry = problemTypeRegistry;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        var detail = createDetail(
                problemTypeRegistry.get("validation-error"),
                "Existem campos invalidos na requisicao.",
                request.getRequestURI()
        );
        detail.setProperty("errors", exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> Map.of("field", error.getField(), "message", error.getDefaultMessage()))
                .toList());
        return detail;
    }

    @ExceptionHandler(BindException.class)
    ProblemDetail handleBindException(BindException exception, HttpServletRequest request) {
        var detail = createDetail(
                problemTypeRegistry.get("validation-error"),
                "Parametros invalidos.",
                request.getRequestURI()
        );
        detail.setProperty("errors", exception.getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .toList());
        return detail;
    }

    @ExceptionHandler(ApiProblemException.class)
    ProblemDetail handleProblem(ApiProblemException exception, HttpServletRequest request) {
        return createDetail(
                problemTypeRegistry.get(exception.getProblemKey()),
                exception.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(Exception.class)
    ProblemDetail handleUnexpected(Exception exception, HttpServletRequest request) {
        var detail = createDetail(
                problemTypeRegistry.get("internal-error"),
                "Ocorreu uma falha inesperada durante o processamento da requisicao.",
                request.getRequestURI()
        );
        detail.setProperty("exception", exception.getClass().getSimpleName());
        return detail;
    }

    private ProblemDetail createDetail(
            ProblemTypeDefinition definition,
            String message,
            String instance
    ) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(definition.status(), message);
        detail.setType(URI.create(definition.type()));
        detail.setTitle(definition.title());
        detail.setInstance(URI.create(instance));
        detail.setProperty("timestamp", OffsetDateTime.now());
        detail.setProperty("correlationId", MDC.get("correlationId"));
        return detail;
    }
}
