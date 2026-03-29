package com.fidc.cdc.kogito.application.process;

import java.util.List;

/**
 * Representa o contexto operacional de task assignment.
 *
 * <p>Este tipo pertence a camada de orquestracao de casos de uso e servicos de aplicacao. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
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
