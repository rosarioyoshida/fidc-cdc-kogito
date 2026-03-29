package com.fidc.cdc.kogito.application.security;

import com.fidc.cdc.kogito.api.security.CurrentUserResponse;
import com.fidc.cdc.kogito.domain.security.Usuario;
import com.fidc.cdc.kogito.infrastructure.audit.AuthAuditService;
import com.fidc.cdc.kogito.infrastructure.observability.AuthMetricsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Coordena current user na camada de aplicacao.
 *
 * <p>Este tipo pertence a camada de orquestracao de casos de uso e servicos de aplicacao. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
@Service
public class CurrentUserService {

    private final CurrentUserQueryService currentUserQueryService;
    private final AuthAuditService authAuditService;
    private final AuthMetricsService authMetricsService;

    public CurrentUserService(
            CurrentUserQueryService currentUserQueryService,
            AuthAuditService authAuditService,
            AuthMetricsService authMetricsService
    ) {
        this.currentUserQueryService = currentUserQueryService;
        this.authAuditService = authAuditService;
        this.authMetricsService = authMetricsService;
    }

    @Transactional(readOnly = true)
    public CurrentUserResponse getCurrentUser(boolean bootstrapLogin) {
        Usuario usuario = currentUserQueryService.loadAuthenticatedUser();
        String perfilAtivo = usuario.getPerfis().stream()
                .map(perfil -> perfil.getNome())
                .sorted()
                .findFirst()
                .orElse("SEM_PERFIL");

        if (bootstrapLogin) {
            authAuditService.logLoginSuccess(usuario.getUsername(), perfilAtivo);
            authMetricsService.recordAuthenticationSuccess(perfilAtivo);
        }

        return new CurrentUserResponse(
                usuario.getUsername(),
                usuario.getNomeExibicao(),
                usuario.getEmail(),
                perfilAtivo
        );
    }
}
