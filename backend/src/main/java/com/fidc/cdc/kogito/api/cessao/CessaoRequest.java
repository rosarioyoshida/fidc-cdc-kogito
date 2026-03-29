package com.fidc.cdc.kogito.api.cessao;

import jakarta.validation.constraints.NotBlank;

/**
 * Representa a solicitacao de cessao.
 *
 * <p>Este tipo pertence a camada de superficie HTTP e contratos de transporte. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
public record CessaoRequest(
        @NotBlank(message = "businessKey e obrigatorio")
        String businessKey,
        @NotBlank(message = "cedenteId e obrigatorio")
        String cedenteId,
        @NotBlank(message = "cessionariaId e obrigatorio")
        String cessionariaId
) {
}
