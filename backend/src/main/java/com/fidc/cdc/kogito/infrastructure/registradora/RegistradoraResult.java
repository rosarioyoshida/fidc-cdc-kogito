package com.fidc.cdc.kogito.infrastructure.registradora;

import java.util.Map;

/**
 * Representa o resultado de registradora.
 *
 * <p>Este tipo pertence a camada de adaptadores tecnicos e integracoes de infraestrutura. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
public record RegistradoraResult(
        String requestId,
        int httpStatus,
        String statusNegocio,
        Map<String, Object> body,
        int tentativa
) {
}
