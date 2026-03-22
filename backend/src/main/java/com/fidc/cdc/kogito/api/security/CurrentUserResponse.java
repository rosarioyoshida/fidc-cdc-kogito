package com.fidc.cdc.kogito.api.security;

public record CurrentUserResponse(
        String username,
        String nomeExibicao,
        String email,
        String perfilAtivo
) {
}
