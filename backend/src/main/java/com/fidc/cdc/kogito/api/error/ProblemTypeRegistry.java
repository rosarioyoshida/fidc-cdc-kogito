package com.fidc.cdc.kogito.api.error;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Representa problem type registry no backend de cessao.
 *
 * <p>Este tipo pertence a camada de superficie HTTP e contratos de transporte. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
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
            "authentication-failed", new ProblemTypeDefinition(
                    "https://fidc-cdc-kogito.local/problems/authentication-failed",
                    "Autenticacao obrigatoria",
                    HttpStatus.UNAUTHORIZED
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
