package com.fidc.cdc.kogito.integration.security;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fidc.cdc.kogito.support.ApiContractTestBase;
import org.junit.jupiter.api.Test;

class CurrentUserIntegrationTest extends ApiContractTestBase {

    @Test
    void shouldReturnAuthenticatedUserSnapshot() throws Exception {
        mockMvc.perform(get("/api/v1/usuarios/me")
                        .with(httpBasic("operador", "operador123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("operador"))
                .andExpect(jsonPath("$.nomeExibicao").value("Operador Padrao"))
                .andExpect(jsonPath("$.email").value("operador@fidc.local"))
                .andExpect(jsonPath("$.perfilAtivo").value("OPERADOR"));
    }
}
