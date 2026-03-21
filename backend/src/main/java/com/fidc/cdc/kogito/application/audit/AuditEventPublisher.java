package com.fidc.cdc.kogito.application.audit;

import java.time.OffsetDateTime;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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
