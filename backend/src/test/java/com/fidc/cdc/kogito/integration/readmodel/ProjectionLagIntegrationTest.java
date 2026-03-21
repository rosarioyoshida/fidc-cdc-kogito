package com.fidc.cdc.kogito.integration.readmodel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fidc.cdc.kogito.api.cessao.CessaoRequest;
import com.fidc.cdc.kogito.application.readmodel.CessaoReadModelRepository;
import com.fidc.cdc.kogito.support.ApiContractTestBase;
import java.time.Duration;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

class ProjectionLagIntegrationTest extends ApiContractTestBase {

    @Autowired
    private CessaoReadModelRepository readModelRepository;

    @Test
    void shouldKeepProjectedStateWithinOperationalLagTarget() throws Exception {
        mockMvc.perform(post("/api/v1/cessoes")
                        .with(httpBasic("operador", "operador123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CessaoRequest("BK-LAG-001", "CED-01", "CESS-01"))))
                .andExpect(status().isCreated());

        var document = readModelRepository.findById("BK-LAG-001");
        assertThat(document).isPresent();
        assertThat(document.get().getUltimaAtualizacao()).isNotNull();
        assertThat(Math.abs(Duration.between(document.get().getUltimaAtualizacao(), OffsetDateTime.now()).toSeconds()))
                .isLessThanOrEqualTo(10);
    }
}
