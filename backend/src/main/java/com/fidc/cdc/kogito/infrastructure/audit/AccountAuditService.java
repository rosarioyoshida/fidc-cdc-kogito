package com.fidc.cdc.kogito.infrastructure.audit;

import com.fidc.cdc.kogito.infrastructure.logging.CorrelationLoggingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

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
