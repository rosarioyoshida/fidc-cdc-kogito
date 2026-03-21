package com.fidc.cdc.kogito.infrastructure.messaging;

import java.time.OffsetDateTime;
import java.util.Map;

public record ProcessEventPayload(
        String channel,
        String businessKey,
        String eventType,
        OffsetDateTime createdAt,
        Map<String, ?> attributes
) {
}
