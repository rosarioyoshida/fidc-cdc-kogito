package com.fidc.cdc.kogito.infrastructure.registradora;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Agrupa propriedades de configuracao para registradora.
 *
 * <p>Este tipo pertence a camada de adaptadores tecnicos e integracoes de infraestrutura. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
@ConfigurationProperties(prefix = "fidc.registradora")
public class RegistradoraProperties {

    private String baseUrl;
    private String username;
    private String password;
    private final Retries retries = new Retries();

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Retries getRetries() {
        return retries;
    }

    /**
     * Representa retries no backend de cessao.
     *
     * <p>Este tipo pertence a camada de adaptadores tecnicos e integracoes de infraestrutura. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
     */
    public static class Retries {

        private int maxAttempts = 3;
        private Duration delay = Duration.ofSeconds(1);

        public int getMaxAttempts() {
            return maxAttempts;
        }

        public void setMaxAttempts(int maxAttempts) {
            this.maxAttempts = maxAttempts;
        }

        public Duration getDelay() {
            return delay;
        }

        public void setDelay(Duration delay) {
            this.delay = delay;
        }
    }
}
