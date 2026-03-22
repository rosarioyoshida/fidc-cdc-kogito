package com.fidc.cdc.kogito.api.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateOwnEmailRequest(
        @NotBlank(message = "O email e obrigatorio.")
        @Email(message = "Informe um email valido.")
        String email
) {
}
