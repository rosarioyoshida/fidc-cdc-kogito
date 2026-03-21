package com.fidc.cdc.kogito.integration.console;

import static org.hamcrest.Matchers.hasItem;
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

        mockMvc.perform(get("/api/v1/cessoes/BK-CONSOLE-001/permissoes")
                        .with(httpBasic("operador", "operador123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.actorId").value("operador"))
                .andExpect(jsonPath("$.taskContext.humanTaskPending").value(true))
                .andExpect(jsonPath("$.taskContext.currentStage").value("IMPORTAR_CARTEIRA"))
                .andExpect(jsonPath("$.taskContext.candidateGroups", hasItem("OPERADOR")))
                .andExpect(jsonPath("$.taskContext.taskConsoleUrl").value("http://localhost:8280"))
                .andExpect(jsonPath("$.managementContext.processStatus").value("EM_ANDAMENTO"))
                .andExpect(jsonPath("$.managementContext.currentStage").value("IMPORTAR_CARTEIRA"))
                .andExpect(jsonPath("$.managementContext.availableAdminActions", hasItem("INSPECT_HUMAN_TASK")))
                .andExpect(jsonPath("$.managementContext.managementConsoleUrl").value("http://localhost:8380"))
                .andExpect(jsonPath("$.managementContext.dataIndexUrl").value("http://localhost:8180/graphql"));
    }
}
