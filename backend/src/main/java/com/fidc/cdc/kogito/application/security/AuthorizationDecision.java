package com.fidc.cdc.kogito.application.security;

public record AuthorizationDecision(
        String actorId,
        String perfil
) {
}
