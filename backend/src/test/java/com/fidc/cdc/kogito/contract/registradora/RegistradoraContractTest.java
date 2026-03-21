package com.fidc.cdc.kogito.contract.registradora;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fidc.cdc.kogito.api.cessao.CessaoRequest;
import com.fidc.cdc.kogito.application.cessao.CessaoProcessService;
import com.fidc.cdc.kogito.domain.analise.OfertaRegistradoraRepository;
import com.fidc.cdc.kogito.domain.analise.TipoOperacaoRegistradora;
import com.fidc.cdc.kogito.domain.cessao.CessaoRepository;
import com.fidc.cdc.kogito.infrastructure.registradora.RegistradoraCallException;
import com.fidc.cdc.kogito.infrastructure.registradora.RegistradoraClient;
import com.fidc.cdc.kogito.infrastructure.registradora.RegistradoraProperties;
import com.fidc.cdc.kogito.infrastructure.registradora.RegistradoraRetryPolicy;
import com.fidc.cdc.kogito.observability.RegistradoraMetricsService;
import com.fidc.cdc.kogito.support.IntegrationTestBase;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

class RegistradoraContractTest extends IntegrationTestBase {

    @Autowired
    private CessaoProcessService cessaoProcessService;

    @Autowired
    private OfertaRegistradoraRepository ofertaRegistradoraRepository;

    @Autowired
    private CessaoRepository cessaoRepository;

    @Autowired
    private RegistradoraProperties registradoraProperties;

    @Autowired
    private RegistradoraRetryPolicy registradoraRetryPolicy;

    @Autowired
    private RegistradoraMetricsService registradoraMetricsService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockRestServiceServer server;
    private RegistradoraClient registradoraClient;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder();
        server = MockRestServiceServer.bindTo(builder).build();
        registradoraClient = new RegistradoraClient(
                builder,
                registradoraProperties,
                cessaoRepository,
                ofertaRegistradoraRepository,
                registradoraRetryPolicy,
                registradoraMetricsService,
                objectMapper
        );
    }

    @Test
    void shouldRetryRegistradoraCallsAndPersistProblemEvidence() {
        cessaoProcessService.createAndStart(new CessaoRequest("BK-US2-004", "CED-01", "CESS-01"));

        server.expect(ExpectedCount.times(3), requestTo("http://localhost:8090/api/v1/operacoes/oferta"))
                .andExpect(method(POST))
                .andRespond(withStatus(HttpStatus.BAD_GATEWAY)
                        .contentType(MediaType.parseMediaType("application/problem+json"))
                        .body("""
                                {"type":"https://registradora.local/problems/gateway","title":"Gateway","status":502,"detail":"Falha temporaria"}
                                """));

        assertThatThrownBy(() -> registradoraClient.executar(
                        "BK-US2-004",
                        TipoOperacaoRegistradora.OFERTA,
                        Map.of("businessKey", "BK-US2-004")
                ))
                .isInstanceOf(RegistradoraCallException.class)
                .hasMessageContaining("Falha HTTP na registradora");

        assertThat(ofertaRegistradoraRepository.findByCessaoBusinessKeyOrderByExecutadaEmAsc("BK-US2-004"))
                .hasSize(3);
    }
}
