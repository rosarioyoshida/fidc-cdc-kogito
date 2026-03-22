package com.fidc.cdc.kogito.application.process;

import org.kie.kogito.process.ProcessInstance;

public record KogitoProcessSnapshot(
        String processId,
        String processName,
        String processVersion,
        String processType,
        String processInstanceId,
        String businessKey,
        int status,
        KogitoTaskSnapshot activeTask
) {

    public boolean isCompleted() {
        return status == ProcessInstance.STATE_COMPLETED;
    }
}
