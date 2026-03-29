package com.fidc.cdc.kogito.infrastructure.audit;

import com.fidc.cdc.kogito.infrastructure.logging.CorrelationLoggingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

/**
 * Coordena account audit na camada de aplicacao.
 *
 * <p>Este tipo pertence a camada de adaptadores tecnicos e integracoes de infraestrutura. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
@Service
public class AccountAuditService {

    private static final Logger log = LoggerFactory.getLogger(AccountAuditService.class);

    public void logAccountEmailUpdate(String username, String perfil, String email) {
        log.info(
                "account_audit event=EMAIL_UPDATED actor={} profile={} email={} correlationId={}",
                username,
                perfil,
                email,
                resolveCorrelationId()
        );
    }

    public void logPasswordChange(String username, String perfil) {
        log.info(
                "account_audit event=PASSWORD_CHANGED actor={} profile={} correlationId={}",
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
