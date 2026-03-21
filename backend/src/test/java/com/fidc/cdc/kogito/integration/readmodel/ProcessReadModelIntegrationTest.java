package com.fidc.cdc.kogito.integration.readmodel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fidc.cdc.kogito.api.cessao.CessaoRequest;
import com.fidc.cdc.kogito.application.readmodel.CessaoReadModelRepository;
import com.fidc.cdc.kogito.support.ApiContractTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

class ProcessReadModelIntegrationTest extends ApiContractTestBase {

    @Autowired
    private CessaoReadModelRepository readModelRepository;

    @Test
    void shouldProjectCurrentStateIntoMongoReadModel() throws Exception {
        mockMvc.perform(post("/api/v1/cessoes")
                        .with(httpBasic("operador", "operador123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CessaoRequest("BK-003", "CED-01", "CESS-01"))))
                .andExpect(status().isCreated());

        var document = readModelRepository.findById("BK-003");
        assertThat(document).isPresent();
        assertThat(document.get().getEtapaAtual()).isEqualTo("IMPORTAR_CARTEIRA");
        assertThat(document.get().getUltimoEvento()).isEqualTo("CESSAO_CRIADA");
    }
}
