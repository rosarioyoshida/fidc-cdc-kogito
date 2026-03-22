package com.fidc.cdc.kogito.application.process;

import com.fidc.cdc.kogito.domain.cessao.EtapaCessaoNome;

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
