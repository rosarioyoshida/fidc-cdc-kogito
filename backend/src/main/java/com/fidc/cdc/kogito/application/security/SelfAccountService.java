package com.fidc.cdc.kogito.application.security;

import com.fidc.cdc.kogito.api.error.BusinessConflictException;
import com.fidc.cdc.kogito.domain.security.Usuario;
import com.fidc.cdc.kogito.domain.security.UsuarioRepository;
import com.fidc.cdc.kogito.infrastructure.audit.AccountAuditService;
import com.fidc.cdc.kogito.infrastructure.audit.AuthAuditService;
import com.fidc.cdc.kogito.infrastructure.observability.AuthMetricsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SelfAccountService {

    private final CurrentUserQueryService currentUserQueryService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountAuditService accountAuditService;
    private final AuthAuditService authAuditService;
    private final AuthMetricsService authMetricsService;

    public SelfAccountService(
            CurrentUserQueryService currentUserQueryService,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            AccountAuditService accountAuditService,
            AuthAuditService authAuditService,
            AuthMetricsService authMetricsService
    ) {
        this.currentUserQueryService = currentUserQueryService;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountAuditService = accountAuditService;
        this.authAuditService = authAuditService;
        this.authMetricsService = authMetricsService;
    }

    @Transactional
    public void updateOwnEmail(String email) {
        Usuario usuario = currentUserQueryService.loadAuthenticatedUser();
        usuario.setEmail(email.strip());
        usuarioRepository.save(usuario);
        accountAuditService.logAccountEmailUpdate(usuario.getUsername(), resolvePerfil(usuario), usuario.getEmail());
        authMetricsService.recordAccountUpdate("email", "success");
    }

    @Transactional
    public void changeOwnPassword(String currentPassword, String newPassword) {
        Usuario usuario = currentUserQueryService.loadAuthenticatedUser();
        if (!passwordEncoder.matches(currentPassword, usuario.getPasswordHash())) {
            authMetricsService.recordAuthenticationFailure("password-change");
            throw new BusinessConflictException("A senha atual informada nao confere.");
        }

        usuario.setPasswordHash(passwordEncoder.encode(newPassword));
        usuarioRepository.save(usuario);
        accountAuditService.logPasswordChange(usuario.getUsername(), resolvePerfil(usuario));
        authMetricsService.recordAccountUpdate("password", "success");
    }

    @Transactional(readOnly = true)
    public void registerLogout() {
        Usuario usuario = currentUserQueryService.loadAuthenticatedUser();
        String perfil = resolvePerfil(usuario);
        authAuditService.logLogout(usuario.getUsername(), perfil);
        authMetricsService.recordLogout(perfil);
    }

    private String resolvePerfil(Usuario usuario) {
        return usuario.getPerfis().stream()
                .map(perfil -> perfil.getNome())
                .sorted()
                .findFirst()
                .orElse("SEM_PERFIL");
    }
}
