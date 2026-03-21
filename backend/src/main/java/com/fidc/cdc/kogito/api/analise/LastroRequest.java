package com.fidc.cdc.kogito.api.analise;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record LastroRequest(
        UUID contratoId,
        UUID parcelaId,
        @NotBlank String tipoDocumento,
        @NotBlank String origem
) {
}
