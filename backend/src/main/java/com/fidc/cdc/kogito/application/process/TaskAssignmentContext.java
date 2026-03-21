package com.fidc.cdc.kogito.application.process;

import java.util.List;

public record TaskAssignmentContext(
        String businessKey,
        String workflowInstanceId,
        String actorId,
        String currentStage,
        boolean humanTaskPending,
        boolean actorAuthorizedForTask,
        String taskName,
        String taskDescription,
        String assignedActor,
        List<String> candidateGroups,
        List<String> candidateUsers,
        List<String> businessAdministratorGroups,
        String taskConsoleUrl
) {
}
