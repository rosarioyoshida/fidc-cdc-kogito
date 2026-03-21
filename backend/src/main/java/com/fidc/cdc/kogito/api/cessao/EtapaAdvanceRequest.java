package com.fidc.cdc.kogito.api.cessao;

public record EtapaAdvanceRequest(
        String responsavelId,
        String justificativa
) {
}
