package com.fidc.cdc.kogito.application.security;

import java.util.List;

public record PermissionSnapshot(
        String actorId,
        List<String> perfis,
        List<String> etapasPermitidas
) {
}
