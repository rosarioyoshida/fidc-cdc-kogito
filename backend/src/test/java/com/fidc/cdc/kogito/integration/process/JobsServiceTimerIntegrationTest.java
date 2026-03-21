package com.fidc.cdc.kogito.integration.process;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.fidc.cdc.kogito.api.cessao.CessaoRequest;
import com.fidc.cdc.kogito.application.cessao.CessaoProcessService;
import com.fidc.cdc.kogito.application.process.TimerSchedulingService;
import com.fidc.cdc.kogito.domain.cessao.EtapaCessaoNome;
import com.fidc.cdc.kogito.support.IntegrationTestBase;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class JobsServiceTimerIntegrationTest extends IntegrationTestBase {

    @Autowired
    private CessaoProcessService cessaoProcessService;

    @Autowired
    private TimerSchedulingService timerSchedulingService;

    @Test
    void shouldGenerateExternalJobIdForAwaitingConfirmationTimer() {
        var cessao = cessaoProcessService.createAndStart(new CessaoRequest("BK-004", "CED-01", "CESS-01"));
        var etapa = cessao.getEtapas()
                .stream()
                .filter(item -> item.getNomeEtapa() == EtapaCessaoNome.AGUARDAR_CONFIRMACAO_REGISTRADORA)
                .findFirst()
                .orElseThrow();

        String jobId = assertDoesNotThrow(
                () -> timerSchedulingService.scheduleAwaitingConfirmationTimer(cessao, etapa, Duration.ofMinutes(5))
        );

        assertThat(jobId).isNotBlank();
    }
}
