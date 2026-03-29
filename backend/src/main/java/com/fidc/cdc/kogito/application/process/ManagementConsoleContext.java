package com.fidc.cdc.kogito.application.process;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Representa o contexto operacional de management console.
 *
 * <p>Este tipo pertence a camada de orquestracao de casos de uso e servicos de aplicacao. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
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
