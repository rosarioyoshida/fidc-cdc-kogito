package com.fidc.cdc.kogito.infrastructure.messaging;

import java.time.OffsetDateTime;
import java.util.Map;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class DomainEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaTopicsProperties topicsProperties;

    public DomainEventPublisher(
            KafkaTemplate<String, Object> kafkaTemplate,
            KafkaTopicsProperties topicsProperties
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicsProperties = topicsProperties;
    }

    public void publishProcessEvent(String businessKey, String eventType, Map<String, ?> attributes) {
        kafkaTemplate.send(
                topicsProperties.getProcess(),
                businessKey,
                new ProcessEventPayload("process", businessKey, eventType, OffsetDateTime.now(), attributes)
        );
    }

    public void publishTaskEvent(String businessKey, String eventType, Map<String, ?> attributes) {
        kafkaTemplate.send(
                topicsProperties.getTasks(),
                businessKey,
                new ProcessEventPayload("task", businessKey, eventType, OffsetDateTime.now(), attributes)
        );
    }

    public void publishAuditEvent(String businessKey, String eventType, Map<String, ?> attributes) {
        kafkaTemplate.send(
                topicsProperties.getAudit(),
                businessKey,
                new ProcessEventPayload("audit", businessKey, eventType, OffsetDateTime.now(), attributes)
        );
    }
}
