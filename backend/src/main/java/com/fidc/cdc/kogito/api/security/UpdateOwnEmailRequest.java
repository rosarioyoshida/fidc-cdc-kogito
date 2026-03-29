package com.fidc.cdc.kogito.api.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Representa a solicitacao de update own email.
 *
 * <p>Este tipo pertence a camada de superficie HTTP e contratos de transporte. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
public record UpdateOwnEmailRequest(
        @NotBlank(message = "O email e obrigatorio.")
        @Email(message = "Informe um email valido.")
        String email
) {
}
