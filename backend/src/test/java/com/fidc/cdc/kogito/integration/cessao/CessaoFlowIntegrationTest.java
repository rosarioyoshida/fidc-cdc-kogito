package com.fidc.cdc.kogito.integration.cessao;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
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
                .andExpect(jsonPath("$.workflowInstanceId", not(startsWith("workflow-"))))
                .andExpect(jsonPath("$.etapas", hasSize(15)));

        mockMvc.perform(get("/api/v1/cessoes/BK-001/historico")
                        .with(httpBasic("operador", "operador123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(15)))
                .andExpect(jsonPath("$[0].nomeEtapa").value("IMPORTAR_CARTEIRA"))
                .andExpect(jsonPath("$[0].statusEtapa").value("EM_EXECUCAO"));
    }

    @Test
    void shouldAdvanceThroughAutomaticStagesWithoutPuttingProcessInError() throws Exception {
        CessaoRequest request = new CessaoRequest("BK-002", "CED-01", "CESS-01");

        mockMvc.perform(post("/api/v1/cessoes")
                        .with(httpBasic("operador", "operador123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/cessoes/BK-002/etapas/IMPORTAR_CARTEIRA/acoes/avancar")
                        .with(httpBasic("operador", "operador123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"responsavelId":"operador","justificativa":"importacao concluida"}
                                """))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.status").value("EM_ANDAMENTO"));

        mockMvc.perform(post("/api/v1/cessoes/BK-002/etapas/VALIDAR_CEDENTE/acoes/avancar")
                        .with(httpBasic("operador", "operador123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"responsavelId":"operador","justificativa":"cedente validado"}
                                """))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.status").value("EM_ANDAMENTO"));

        mockMvc.perform(post("/api/v1/cessoes/BK-002/etapas/ANALISAR_ELEGIBILIDADE/acoes/avancar")
                        .with(httpBasic("analista", "analista123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"responsavelId":"analista","justificativa":"elegibilidade aprovada"}
                                """))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.status").value("EM_ANDAMENTO"))
                .andExpect(jsonPath("$.etapas[3].nomeEtapa").value("CONSOLIDAR_CONTRATOS"))
                .andExpect(jsonPath("$.etapas[3].statusEtapa").value("CONCLUIDA"))
                .andExpect(jsonPath("$.etapas[4].nomeEtapa").value("CONSOLIDAR_PARCELAS"))
                .andExpect(jsonPath("$.etapas[4].statusEtapa").value("CONCLUIDA"))
                .andExpect(jsonPath("$.etapas[5].nomeEtapa").value("CALCULAR_VALOR"))
                .andExpect(jsonPath("$.etapas[5].statusEtapa").value("CONCLUIDA"))
                .andExpect(jsonPath("$.etapas[6].nomeEtapa").value("PREPARAR_OFERTA_REGISTRADORA"))
                .andExpect(jsonPath("$.etapas[6].statusEtapa").value("CONCLUIDA"))
                .andExpect(jsonPath("$.etapas[7].nomeEtapa").value("ENVIAR_OFERTA_REGISTRADORA"))
                .andExpect(jsonPath("$.etapas[7].statusEtapa").value("CONCLUIDA"))
                .andExpect(jsonPath("$.etapas[8].nomeEtapa").value("AGUARDAR_CONFIRMACAO_REGISTRADORA"))
                .andExpect(jsonPath("$.etapas[8].statusEtapa").value("EM_EXECUCAO"));
    }
}
