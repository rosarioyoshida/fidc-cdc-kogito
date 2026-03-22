package com.fidc.cdc.kogito.application.security;

import com.fidc.cdc.kogito.api.error.ForbiddenOperationException;
import com.fidc.cdc.kogito.domain.security.Usuario;
import com.fidc.cdc.kogito.domain.security.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CurrentUserQueryService {

    private final UsuarioRepository usuarioRepository;

    public CurrentUserQueryService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public Usuario loadAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getName() == null) {
            throw new ForbiddenOperationException("Nao existe usuario autenticado na sessao atual.");
        }

        return usuarioRepository.findByUsernameAndAtivoTrue(authentication.getName())
                .orElseThrow(() -> new ForbiddenOperationException(
                        "Usuario autenticado sem cadastro operacional ativo."
                ));
    }
}
