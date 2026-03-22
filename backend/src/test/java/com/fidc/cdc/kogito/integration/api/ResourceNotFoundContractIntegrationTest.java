package com.fidc.cdc.kogito.integration.api;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fidc.cdc.kogito.support.ApiContractTestBase;
import org.junit.jupiter.api.Test;

class ResourceNotFoundContractIntegrationTest extends ApiContractTestBase {

    @Test
    void shouldReturn404WhenFilteringCessaoListByUnknownBusinessKey() throws Exception {
        mockMvc.perform(get("/api/v1/cessoes")
                        .with(httpBasic("operador", "operador123"))
                        .param("business-key", "BK-INEXISTENTE"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Cessao nao encontrada para o businessKey informado."));
    }

    @Test
    void shouldReturn404WhenRequestingAnalysisOfUnknownCessao() throws Exception {
        mockMvc.perform(get("/api/v1/cessoes/BK-INEXISTENTE/analise/regras")
                        .with(httpBasic("operador", "operador123")))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Cessao nao encontrada para o businessKey informado."));
    }
}
