package com.fidc.cdc.kogito.application.audit;

import java.time.OffsetDateTime;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Publica eventos relacionados a audit event.
 *
 * <p>Este tipo pertence a camada de orquestracao de casos de uso e servicos de aplicacao. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
@Component
public class AuditEventPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditEventPublisher.class);

    public void publish(String type, String actor, Map<String, ?> payload) {
        LOGGER.info(
                "audit_event type={} actor={} createdAt={} payload={}",
                type,
                actor,
                OffsetDateTime.now(),
                payload
        );
    }
}
