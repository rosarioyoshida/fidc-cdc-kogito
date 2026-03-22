package com.fidc.cdc.kogito.api.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateOwnPasswordRequest(
        @NotBlank(message = "A senha atual e obrigatoria.")
        String senhaAtual,
        @NotBlank(message = "A nova senha e obrigatoria.")
        @Size(min = 8, message = "A nova senha deve ter ao menos 8 caracteres.")
        String novaSenha
) {
}
