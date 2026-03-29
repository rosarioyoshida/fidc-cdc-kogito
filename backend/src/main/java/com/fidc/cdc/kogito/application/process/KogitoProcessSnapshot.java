package com.fidc.cdc.kogito.application.process;

import org.kie.kogito.process.ProcessInstance;

/**
 * Representa o retrato observavel de kogito process.
 *
 * <p>Este tipo pertence a camada de orquestracao de casos de uso e servicos de aplicacao. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
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
