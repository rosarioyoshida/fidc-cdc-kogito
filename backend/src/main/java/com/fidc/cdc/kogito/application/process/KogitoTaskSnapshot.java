package com.fidc.cdc.kogito.application.process;

import com.fidc.cdc.kogito.domain.cessao.EtapaCessaoNome;

/**
 * Representa o retrato observavel de kogito task.
 *
 * <p>Este tipo pertence a camada de orquestracao de casos de uso e servicos de aplicacao. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
public record KogitoTaskSnapshot(
        String id,
        String definitionId,
        String name,
        String description,
        String state,
        String actualOwner,
        String groupId,
        String businessAdministratorGroupId,
        String externalReferenceId
) {

    public EtapaCessaoNome etapaNome() {
        return EtapaCessaoNome.valueOf(definitionId);
    }
}
