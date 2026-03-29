package com.fidc.cdc.kogito.infrastructure.audit;

import com.fidc.cdc.kogito.infrastructure.logging.CorrelationLoggingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

/**
 * Coordena auth audit na camada de aplicacao.
 *
 * <p>Este tipo pertence a camada de adaptadores tecnicos e integracoes de infraestrutura. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
@Service
public class AuthAuditService {

    private static final Logger log = LoggerFactory.getLogger(AuthAuditService.class);

    public void logLoginSuccess(String username, String perfil) {
        log.info(
                "auth_audit event=LOGIN_SUCCESS actor={} profile={} correlationId={}",
                username,
                perfil,
                resolveCorrelationId()
        );
    }

    public void logLoginFailure(String username, String reason) {
        log.warn(
                "auth_audit event=LOGIN_FAILURE actor={} reason={} correlationId={}",
                username == null || username.isBlank() ? "desconhecido" : username,
                reason,
                resolveCorrelationId()
        );
    }

    public void logLogout(String username, String perfil) {
        log.info(
                "auth_audit event=LOGOUT actor={} profile={} correlationId={}",
                username,
                perfil,
                resolveCorrelationId()
        );
    }

    private String resolveCorrelationId() {
        String correlationId = MDC.get(CorrelationLoggingFilter.CORRELATION_ID_KEY);
        return correlationId == null || correlationId.isBlank() ? "sem-correlation-id" : correlationId;
    }
}
