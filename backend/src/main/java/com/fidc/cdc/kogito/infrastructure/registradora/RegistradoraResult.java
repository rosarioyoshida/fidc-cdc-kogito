package com.fidc.cdc.kogito.infrastructure.registradora;

import java.util.Map;

public record RegistradoraResult(
        String requestId,
        int httpStatus,
        String statusNegocio,
        Map<String, Object> body,
        int tentativa
) {
}
