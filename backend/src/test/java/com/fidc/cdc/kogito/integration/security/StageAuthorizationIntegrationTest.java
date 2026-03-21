package com.fidc.cdc.kogito.integration.security;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fidc.cdc.kogito.api.cessao.CessaoRequest;
import com.fidc.cdc.kogito.support.ApiContractTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class StageAuthorizationIntegrationTest extends ApiContractTestBase {

    @Test
    void shouldRejectAdvanceWhenActorDoesNotMatchStagePermissions() throws Exception {
        mockMvc.perform(post("/api/v1/cessoes")
                        .with(httpBasic("operador", "operador123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CessaoRequest("BK-SEC-001", "CED-01", "CESS-01"))))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/cessoes/BK-SEC-001/etapas/IMPORTAR_CARTEIRA/acoes/avancar")
                        .with(httpBasic("operador", "operador123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"responsavelId":"operador","justificativa":"importacao concluida"}
                                """))
                .andExpect(status().isAccepted());

        mockMvc.perform(post("/api/v1/cessoes/BK-SEC-001/etapas/VALIDAR_CEDENTE/acoes/avancar")
                        .with(httpBasic("analista", "analista123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"responsavelId":"analista","justificativa":"tentativa indevida"}
                                """))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.type").value("https://fidc-cdc-kogito/problems/forbidden-operation"))
                .andExpect(jsonPath("$.detail", containsString("VALIDAR_CEDENTE")));
    }
}
