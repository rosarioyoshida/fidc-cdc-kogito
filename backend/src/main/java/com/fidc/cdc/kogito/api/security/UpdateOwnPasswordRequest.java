package com.fidc.cdc.kogito.api.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Representa a solicitacao de update own password.
 *
 * <p>Este tipo pertence a camada de superficie HTTP e contratos de transporte. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
public record UpdateOwnPasswordRequest(
        @NotBlank(message = "A senha atual e obrigatoria.")
        String senhaAtual,
        @NotBlank(message = "A nova senha e obrigatoria.")
        @Size(min = 8, message = "A nova senha deve ter ao menos 8 caracteres.")
        String novaSenha
) {
}
