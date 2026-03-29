package com.fidc.cdc.kogito.infrastructure.messaging;

import java.time.OffsetDateTime;
import java.util.Map;

/**
 * Representa os dados de process event payload.
 *
 * <p>Este tipo pertence a camada de adaptadores tecnicos e integracoes de infraestrutura. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
public record ProcessEventPayload(
        String channel,
        String businessKey,
        String eventType,
        OffsetDateTime createdAt,
        Map<String, ?> attributes
) {
}
