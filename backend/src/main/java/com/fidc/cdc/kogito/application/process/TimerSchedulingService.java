package com.fidc.cdc.kogito.application.process;

import com.fidc.cdc.kogito.domain.cessao.Cessao;
import com.fidc.cdc.kogito.domain.cessao.EtapaCessao;
import com.fidc.cdc.kogito.observability.ProcessMetricsService;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
public class TimerSchedulingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimerSchedulingService.class);

    private final RestClient restClient;
    private final String callbackBaseUrl;
    private final ProcessMetricsService processMetricsService;

    public TimerSchedulingService(
            RestClient.Builder restClientBuilder,
            @Value("${fidc.jobs.service-url}") String jobsServiceUrl,
            @Value("${fidc.jobs.callback-base-url}") String callbackBaseUrl,
            ProcessMetricsService processMetricsService
    ) {
        this.restClient = restClientBuilder.baseUrl(jobsServiceUrl).build();
        this.callbackBaseUrl = callbackBaseUrl;
        this.processMetricsService = processMetricsService;
    }

    public String scheduleAwaitingConfirmationTimer(Cessao cessao, EtapaCessao etapa, Duration delay) {
        String externalJobId = UUID.randomUUID().toString();
        Map<String, Object> payload = Map.of(
                "id", externalJobId,
                "processId", "controle-cessao",
                "processInstanceId", cessao.getWorkflowInstanceId(),
                "businessKey", cessao.getBusinessKey(),
                "triggerType", "timer",
                "callbackEndpoint", callbackBaseUrl + "/" + externalJobId,
                "scheduledAt", OffsetDateTime.now().plus(delay).toString(),
                "repeatLimit", 0
        );

        try {
            restClient.post()
                    .uri("/jobs")
                    .body(payload)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (request, response) -> {
                        throw new RestClientException("Jobs Service returned status " + response.getStatusCode());
                    })
                    .toBodilessEntity();
            processMetricsService.incrementProcessEvent(etapa.getNomeEtapa().name(), "timer-scheduled");
        } catch (RestClientException exception) {
            LOGGER.warn(
                    "Falha ao agendar timer no Jobs Service para businessKey={} etapa={}: {}",
                    cessao.getBusinessKey(),
                    etapa.getNomeEtapa(),
                    exception.getMessage()
            );
            processMetricsService.incrementProcessEvent(etapa.getNomeEtapa().name(), "timer-schedule-failed");
        }

        return externalJobId;
    }
}
