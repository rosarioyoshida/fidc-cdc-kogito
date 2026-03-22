package com.fidc.cdc.kogito.integration.api;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fidc.cdc.kogito.support.ApiContractTestBase;
import org.junit.jupiter.api.Test;

class CurrentUserContractIntegrationTest extends ApiContractTestBase {

    @Test
    void shouldExposeCurrentUserResourceContract() throws Exception {
        mockMvc.perform(get("/api/v1/usuarios/me")
                        .with(httpBasic("analista", "analista123")))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.username").value("analista"))
                .andExpect(jsonPath("$.nomeExibicao").value("Analista Padrao"))
                .andExpect(jsonPath("$.perfilAtivo").value("ANALISTA"));
    }

    @Test
    void shouldReturnProblemDetailsWhenCredentialsAreInvalid() throws Exception {
        mockMvc.perform(get("/api/v1/usuarios/me")
                        .with(httpBasic("analista", "senha-invalida")))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.type").value("https://fidc-cdc-kogito.local/problems/authentication-failed"))
                .andExpect(jsonPath("$.detail", containsString("Credenciais invalidas")));
    }
}
