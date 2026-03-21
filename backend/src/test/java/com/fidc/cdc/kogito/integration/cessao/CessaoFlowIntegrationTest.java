package com.fidc.cdc.kogito.integration.cessao;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fidc.cdc.kogito.api.cessao.CessaoRequest;
import com.fidc.cdc.kogito.support.ApiContractTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class CessaoFlowIntegrationTest extends ApiContractTestBase {

    @Test
    void shouldCreateCessaoAndReturnHistorico() throws Exception {
        CessaoRequest request = new CessaoRequest("BK-001", "CED-01", "CESS-01");

        mockMvc.perform(post("/api/v1/cessoes")
                        .with(httpBasic("operador", "operador123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/cessoes/BK-001"))
                .andExpect(jsonPath("$.businessKey").value("BK-001"))
                .andExpect(jsonPath("$.status").value("EM_ANDAMENTO"))
                .andExpect(jsonPath("$.etapas", hasSize(15)));

        mockMvc.perform(get("/api/v1/cessoes/BK-001/historico")
                        .with(httpBasic("operador", "operador123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(15)))
                .andExpect(jsonPath("$[0].nomeEtapa").value("IMPORTAR_CARTEIRA"))
                .andExpect(jsonPath("$[0].statusEtapa").value("EM_EXECUCAO"));
    }
}
