package com.fidc.cdc.kogito.infrastructure.registradora;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fidc.cdc.kogito.domain.analise.OfertaRegistradora;
import com.fidc.cdc.kogito.domain.analise.OfertaRegistradoraRepository;
import com.fidc.cdc.kogito.domain.analise.TipoOperacaoRegistradora;
import com.fidc.cdc.kogito.domain.cessao.Cessao;
import com.fidc.cdc.kogito.domain.cessao.CessaoRepository;
import com.fidc.cdc.kogito.observability.RegistradoraMetricsService;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

@Component
public class RegistradoraClient {

    private final RestClient restClient;
    private final CessaoRepository cessaoRepository;
    private final OfertaRegistradoraRepository ofertaRegistradoraRepository;
    private final RegistradoraRetryPolicy retryPolicy;
    private final RegistradoraMetricsService metricsService;
    private final ObjectMapper objectMapper;

    public RegistradoraClient(
            RestClient.Builder restClientBuilder,
            RegistradoraProperties properties,
            CessaoRepository cessaoRepository,
            OfertaRegistradoraRepository ofertaRegistradoraRepository,
            RegistradoraRetryPolicy retryPolicy,
            RegistradoraMetricsService metricsService,
            ObjectMapper objectMapper
    ) {
        RestClient.Builder builder = restClientBuilder.baseUrl(properties.getBaseUrl());
        if (StringUtils.hasText(properties.getUsername())) {
            builder = builder.requestInterceptor((request, body, execution) -> {
                request.getHeaders().setBasicAuth(properties.getUsername(), properties.getPassword());
                return execution.execute(request, body);
            });
        }
        this.restClient = builder.build();
        this.cessaoRepository = cessaoRepository;
        this.ofertaRegistradoraRepository = ofertaRegistradoraRepository;
        this.retryPolicy = retryPolicy;
        this.metricsService = metricsService;
        this.objectMapper = objectMapper;
    }

    public RegistradoraResult executar(
            String businessKey,
            TipoOperacaoRegistradora tipoOperacao,
            Map<String, ?> payload
    ) {
        Cessao cessao = cessaoRepository.findByBusinessKey(businessKey)
                .orElseThrow(() -> new RegistradoraCallException("Cessao nao encontrada para chamada da registradora.", false));
        String requestId = UUID.randomUUID().toString();

        return retryPolicy.execute(tipoOperacao.name(), attempt -> invoke(cessao, requestId, tipoOperacao, payload, attempt));
    }

    private RegistradoraResult invoke(
            Cessao cessao,
            String requestId,
            TipoOperacaoRegistradora tipoOperacao,
            Map<String, ?> payload,
            int attempt
    ) {
        try {
            ResponseEntity<Map<String, Object>> response = restClient.post()
                    .uri("/api/v1/operacoes/{operacao}", tipoOperacao.name().toLowerCase())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(payload)
                    .retrieve()
                    .toEntity(new ParameterizedTypeReference<>() {
                    });

            Map<String, Object> body = response.getBody() == null ? Map.of() : response.getBody();
            String statusNegocio = body.getOrDefault("status", "PROCESSADO").toString();
            persistOffer(cessao, tipoOperacao, requestId, attempt, response.getStatusCode().value(), statusNegocio, payload, body);
            metricsService.registerCall(tipoOperacao.name(), "success");
            return new RegistradoraResult(requestId, response.getStatusCode().value(), statusNegocio, body, attempt);
        } catch (RestClientResponseException ex) {
            Map<String, Object> body = Map.of(
                    "error", ex.getStatusText(),
                    "detail", ex.getResponseBodyAsString()
            );
            persistOffer(cessao, tipoOperacao, requestId, attempt, ex.getStatusCode().value(), "ERRO_HTTP", payload, body);
            metricsService.registerCall(tipoOperacao.name(), "http_" + ex.getStatusCode().value());
            throw new RegistradoraCallException(
                    "Falha HTTP na registradora durante a operacao " + tipoOperacao.name() + ".",
                    ex.getStatusCode().is5xxServerError() || ex.getStatusCode().value() == 429,
                    ex
            );
        } catch (RestClientException ex) {
            persistOffer(cessao, tipoOperacao, requestId, attempt, 0, "ERRO_COMUNICACAO", payload, Map.of("error", ex.getMessage()));
            metricsService.registerCall(tipoOperacao.name(), "client_error");
            throw new RegistradoraCallException(
                    "Falha de comunicacao com a registradora durante a operacao " + tipoOperacao.name() + ".",
                    true,
                    ex
            );
        }
    }

    private void persistOffer(
            Cessao cessao,
            TipoOperacaoRegistradora tipoOperacao,
            String requestId,
            int attempt,
            int httpStatus,
            String statusNegocio,
            Map<String, ?> requestPayload,
            Map<String, ?> responsePayload
    ) {
        OfertaRegistradora oferta = new OfertaRegistradora();
        oferta.setCessao(cessao);
        oferta.setTipoOperacao(tipoOperacao);
        oferta.setRequestId(requestId);
        oferta.setHttpStatus(httpStatus);
        oferta.setStatusNegocio(statusNegocio);
        oferta.setTentativa(attempt);
        oferta.setRequestPayloadRef(serialize(requestPayload));
        oferta.setResponsePayloadRef(serialize(responsePayload));
        oferta.setExecutadaEm(OffsetDateTime.now());
        ofertaRegistradoraRepository.save(oferta);
    }

    private String serialize(Map<String, ?> payload) {
        try {
            return objectMapper.writeValueAsString(new LinkedHashMap<>(payload));
        } catch (JsonProcessingException ex) {
            return String.valueOf(payload);
        }
    }
}
