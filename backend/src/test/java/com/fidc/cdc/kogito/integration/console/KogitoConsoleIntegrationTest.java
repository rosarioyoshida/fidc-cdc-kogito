package com.fidc.cdc.kogito.integration.console;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fidc.cdc.kogito.api.cessao.CessaoRequest;
import com.fidc.cdc.kogito.support.ApiContractTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class KogitoConsoleIntegrationTest extends ApiContractTestBase {

    @Test
    void shouldExposeTaskAndManagementConsoleContext() throws Exception {
        mockMvc.perform(post("/api/v1/cessoes")
                        .with(httpBasic("operador", "operador123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CessaoRequest("BK-CONSOLE-001", "CED-01", "CESS-01"))))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/cessoes/BK-CONSOLE-001/etapas/IMPORTAR_CARTEIRA/acoes/avancar")
                        .with(httpBasic("operador", "operador123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"responsavelId":"operador","justificativa":"avanco operacional"}
                                """))
                .andExpect(status().isAccepted());

        mockMvc.perform(get("/api/v1/cessoes/BK-CONSOLE-001/permissoes")
                        .with(httpBasic("operador", "operador123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.actorId").value("operador"))
                .andExpect(jsonPath("$.taskContext.humanTaskPending").value(true))
                .andExpect(jsonPath("$.taskContext.currentStage").value("VALIDAR_CEDENTE"))
                .andExpect(jsonPath("$.taskContext.candidateGroups", hasItem("OPERADOR")))
                .andExpect(jsonPath("$.taskContext.taskConsoleUrl").value("http://localhost:8280"))
                .andExpect(jsonPath("$.managementContext.processStatus").value("EM_ANDAMENTO"))
                .andExpect(jsonPath("$.managementContext.currentStage").value("VALIDAR_CEDENTE"))
                .andExpect(jsonPath("$.managementContext.availableAdminActions", hasItem("INSPECT_HUMAN_TASK")))
                .andExpect(jsonPath("$.managementContext.managementConsoleUrl").value("http://localhost:8380"))
                .andExpect(jsonPath("$.managementContext.dataIndexUrl").value("http://localhost:8180/graphql"))
                .andExpect(jsonPath("$.taskContext.workflowInstanceId", not(startsWith("workflow-"))))
                .andExpect(jsonPath("$.managementContext.workflowInstanceId", not(startsWith("workflow-"))));
    }

    @Test
    void shouldReturnPermissionContextForCompletedCessao() throws Exception {
        mockMvc.perform(post("/api/v1/cessoes")
                        .with(httpBasic("operador", "operador123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CessaoRequest("BK-CONSOLE-002", "CED-01", "CESS-01"))))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/cessoes/BK-CONSOLE-002/etapas/IMPORTAR_CARTEIRA/acoes/avancar")
                        .with(httpBasic("operador", "operador123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"responsavelId":"operador","justificativa":"avanco operacional"}
                                """))
                .andExpect(status().isAccepted());

        mockMvc.perform(post("/api/v1/cessoes/BK-CONSOLE-002/etapas/VALIDAR_CEDENTE/acoes/avancar")
                        .with(httpBasic("operador", "operador123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"responsavelId":"operador","justificativa":"cedente validado"}
                                """))
                .andExpect(status().isAccepted());

        String processInstanceId = objectMapper.readTree(
                        mockMvc.perform(post("/api/v1/cessoes/BK-CONSOLE-002/etapas/ANALISAR_ELEGIBILIDADE/acoes/avancar")
                                        .with(httpBasic("analista", "analista123"))
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content("""
                                                {"responsavelId":"analista","justificativa":"elegibilidade aprovada"}
                                                """))
                                .andExpect(status().isAccepted())
                                .andReturn()
                                .getResponse()
                                .getContentAsString()
                )
                .path("workflowInstanceId")
                .asText();

        mockMvc.perform(post("/api/v1/process/jobs/callbacks/test-console-002")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"businessKey":"BK-CONSOLE-002","processInstanceId":"%s"}
                                """.formatted(processInstanceId)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.resumed").value(true));

        mockMvc.perform(post("/api/v1/cessoes/BK-CONSOLE-002/etapas/COLETAR_TERMO_ACEITE/acoes/avancar")
                        .with(httpBasic("analista", "analista123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"responsavelId":"analista","justificativa":"termo coletado"}
                                """))
                .andExpect(status().isAccepted());

        mockMvc.perform(post("/api/v1/cessoes/BK-CONSOLE-002/analise/lastros")
                        .with(httpBasic("analista", "analista123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"contratoId":null,"parcelaId":null,"tipoDocumento":"NF-E","origem":"cedente"}
                                """))
                .andExpect(status().isAccepted());

        mockMvc.perform(post("/api/v1/cessoes/BK-CONSOLE-002/etapas/VALIDAR_LASTROS/acoes/avancar")
                        .with(httpBasic("analista", "analista123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"responsavelId":"analista","justificativa":"lastros validados"}
                                """))
                .andExpect(status().isAccepted());

        mockMvc.perform(post("/api/v1/cessoes/BK-CONSOLE-002/etapas/AUTORIZAR_PAGAMENTO/acoes/avancar")
                        .with(httpBasic("aprovador", "aprovador123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"responsavelId":"aprovador","justificativa":"pagamento autorizado"}
                                """))
                .andExpect(status().isAccepted());

        mockMvc.perform(get("/api/v1/cessoes/BK-CONSOLE-002/permissoes")
                        .with(httpBasic("operador", "operador123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskContext.humanTaskPending").value(false))
                .andExpect(jsonPath("$.taskContext.currentStage").value("ENCERRAR_CESSAO"))
                .andExpect(jsonPath("$.managementContext.processStatus").value("CONCLUIDA"))
                .andExpect(jsonPath("$.managementContext.currentStage").value("ENCERRAR_CESSAO"));
    }
}
