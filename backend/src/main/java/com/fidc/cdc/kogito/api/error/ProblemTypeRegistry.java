package com.fidc.cdc.kogito.api.error;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ProblemTypeRegistry {

    private final Map<String, ProblemTypeDefinition> registry = Map.of(
            "validation-error", new ProblemTypeDefinition(
                    "https://fidc-cdc-kogito.local/problems/validation-error",
                    "Dados invalidos",
                    HttpStatus.BAD_REQUEST
            ),
            "resource-not-found", new ProblemTypeDefinition(
                    "https://fidc-cdc-kogito.local/problems/resource-not-found",
                    "Recurso nao encontrado",
                    HttpStatus.NOT_FOUND
            ),
            "business-conflict", new ProblemTypeDefinition(
                    "https://fidc-cdc-kogito.local/problems/business-conflict",
                    "Conflito de negocio",
                    HttpStatus.CONFLICT
            ),
            "forbidden-operation", new ProblemTypeDefinition(
                    "https://fidc-cdc-kogito.local/problems/forbidden-operation",
                    "Operacao nao autorizada",
                    HttpStatus.FORBIDDEN
            ),
            "external-service-error", new ProblemTypeDefinition(
                    "https://fidc-cdc-kogito.local/problems/external-service-error",
                    "Falha de servico externo",
                    HttpStatus.BAD_GATEWAY
            ),
            "internal-error", new ProblemTypeDefinition(
                    "https://fidc-cdc-kogito.local/problems/internal-error",
                    "Falha interna",
                    HttpStatus.INTERNAL_SERVER_ERROR
            )
    );

    public ProblemTypeDefinition get(String key) {
        return registry.getOrDefault(key, registry.get("internal-error"));
    }
}
