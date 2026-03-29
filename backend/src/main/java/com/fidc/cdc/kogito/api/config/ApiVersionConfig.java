package com.fidc.cdc.kogito.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configura api version no backend.
 *
 * <p>Este tipo pertence a camada de superficie HTTP e contratos de transporte. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
@Configuration
public class ApiVersionConfig {

    public static final String API_BASE_PATH = "/api/v1";

    @Bean
    OpenAPI fidcOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("FIDC CDC Kogito API")
                        .version("1.0.0")
                        .description("API operacional do fluxo de cessao de FIDC."))
                .servers(List.of(new Server().url(API_BASE_PATH)));
    }
}
