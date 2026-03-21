package com.fidc.cdc.kogito.application.security;

import com.fidc.cdc.kogito.api.error.ForbiddenOperationException;
import com.fidc.cdc.kogito.domain.cessao.EtapaCessaoNome;
import com.fidc.cdc.kogito.domain.security.Usuario;
import com.fidc.cdc.kogito.domain.security.UsuarioRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StageAuthorizationService {

    private final UsuarioRepository usuarioRepository;

    public StageAuthorizationService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public AuthorizationDecision authorizeStageAction(String actorHint, EtapaCessaoNome etapa) {
        Usuario usuario = loadUsuario(actorHint);
        String actorId = usuario.getUsername();

        return usuario.getPerfis()
                .stream()
                .filter(perfil -> perfil.getPermissoes()
                        .stream()
                        .anyMatch(permissao -> permissao.getNomeEtapa() == etapa))
                .findFirst()
                .map(perfil -> new AuthorizationDecision(actorId, perfil.getNome()))
                .orElseThrow(() -> new ForbiddenOperationException(
                        "O usuario nao possui permissao para executar a etapa " + etapa.name() + "."
                ));
    }

    @Transactional(readOnly = true)
    public PermissionSnapshot describePermissions(String actorHint) {
        Usuario usuario = loadUsuario(actorHint);
        List<String> perfis = usuario.getPerfis().stream()
                .map(perfil -> perfil.getNome())
                .sorted()
                .toList();
        List<String> etapasPermitidas = usuario.getPerfis().stream()
                .flatMap(perfil -> perfil.getPermissoes().stream())
                .map(permissao -> permissao.getNomeEtapa().name())
                .distinct()
                .sorted(Comparator.naturalOrder())
                .toList();
        return new PermissionSnapshot(usuario.getUsername(), perfis, etapasPermitidas);
    }

    private Usuario loadUsuario(String actorHint) {
        String actorId = resolveActor(actorHint);
        return usuarioRepository.findByUsernameAndAtivoTrue(actorId)
                .orElseThrow(() -> new ForbiddenOperationException("Usuario sem cadastro operacional para executar a etapa."));
    }

    private String resolveActor(String actorHint) {
        if (actorHint != null && !actorHint.isBlank()) {
            return actorHint;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return Optional.ofNullable(actorHint).orElse("system");
    }
}
