package com.fidc.cdc.kogito.integration.cessao;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fidc.cdc.kogito.api.cessao.CessaoRequest;
import com.fidc.cdc.kogito.support.ApiContractTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class EtapaDependenciaIntegrationTest extends ApiContractTestBase {

    @Test
    void shouldBlockAdvanceWhenPreviousStepIsIncomplete() throws Exception {
        CessaoRequest request = new CessaoRequest("BK-002", "CED-01", "CESS-01");

        mockMvc.perform(post("/api/v1/cessoes")
                        .with(httpBasic("operador", "operador123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/cessoes/BK-002/etapas/VALIDAR_CEDENTE/acoes/avancar")
                        .with(httpBasic("operador", "operador123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"responsavelId\":\"operador\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("Conflito de negocio"));
    }
}
