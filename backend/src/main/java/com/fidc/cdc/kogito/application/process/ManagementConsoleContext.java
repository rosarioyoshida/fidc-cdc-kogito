package com.fidc.cdc.kogito.application.process;

import java.time.OffsetDateTime;
import java.util.List;

public record ManagementConsoleContext(
        String businessKey,
        String workflowInstanceId,
        String processStatus,
        String currentStage,
        String currentStageLabel,
        String currentStageSource,
        boolean processEndedWithoutActiveStage,
        boolean humanTaskPending,
        boolean waitingForTimerJob,
        boolean processSvgAvailable,
        boolean processSvgAuthorized,
        String processSvgAvailabilityReason,
        boolean readModelAvailable,
        String lastProjectionEvent,
        OffsetDateTime lastProjectionUpdate,
        List<String> availableAdminActions,
        String managementConsoleUrl,
        String dataIndexUrl,
        String jobsServiceUrl
) {
}
