package com.fidc.cdc.kogito.api.cessao;

import jakarta.validation.constraints.NotBlank;

public record CessaoRequest(
        @NotBlank(message = "businessKey e obrigatorio")
        String businessKey,
        @NotBlank(message = "cedenteId e obrigatorio")
        String cedenteId,
        @NotBlank(message = "cessionariaId e obrigatorio")
        String cessionariaId
) {
}
