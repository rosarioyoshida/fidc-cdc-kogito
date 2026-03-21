package com.fidc.cdc.kogito.integration.audit;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fidc.cdc.kogito.api.cessao.CessaoRequest;
import com.fidc.cdc.kogito.support.ApiContractTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class AuditTrailIntegrationTest extends ApiContractTestBase {

    @Test
    void shouldPersistAndExposeAuditTrailEntries() throws Exception {
        mockMvc.perform(post("/api/v1/cessoes")
                        .with(httpBasic("operador", "operador123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CessaoRequest("BK-AUD-001", "CED-01", "CESS-01"))))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/cessoes/BK-AUD-001/etapas/IMPORTAR_CARTEIRA/acoes/avancar")
                        .with(httpBasic("operador", "operador123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"responsavelId":"operador","justificativa":"carteira importada"}
                                """))
                .andExpect(status().isAccepted());

        mockMvc.perform(get("/api/v1/cessoes/BK-AUD-001/auditoria")
                        .with(httpBasic("auditor", "auditor123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].tipoEvento").value("CESSAO_CRIADA"))
                .andExpect(jsonPath("$[0].atorId").value("system"))
                .andExpect(jsonPath("$[1].tipoEvento").value("ETAPA_AVANCADA"))
                .andExpect(jsonPath("$[1].atorId").value("operador"))
                .andExpect(jsonPath("$[1].correlationId").isNotEmpty())
                .andExpect(jsonPath("$[1].detalheRef").value(org.hamcrest.Matchers.containsString("IMPORTAR_CARTEIRA")));
    }
}
