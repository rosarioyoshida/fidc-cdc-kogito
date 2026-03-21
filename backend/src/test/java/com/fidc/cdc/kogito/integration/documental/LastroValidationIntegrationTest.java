package com.fidc.cdc.kogito.integration.documental;

import static org.assertj.core.api.Assertions.assertThat;

import com.fidc.cdc.kogito.api.cessao.CessaoRequest;
import com.fidc.cdc.kogito.application.cessao.CessaoProcessService;
import com.fidc.cdc.kogito.application.documental.LastroDraft;
import com.fidc.cdc.kogito.application.documental.LastroValidationService;
import com.fidc.cdc.kogito.application.documental.ResultadoValidacaoLastro;
import com.fidc.cdc.kogito.support.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class LastroValidationIntegrationTest extends IntegrationTestBase {

    @Autowired
    private CessaoProcessService cessaoProcessService;

    @Autowired
    private LastroValidationService lastroValidationService;

    @Test
    void shouldBlockFinalAcceptanceWhenInvalidLastroExists() {
        cessaoProcessService.createAndStart(new CessaoRequest("BK-US2-003", "CED-01", "CESS-01"));

        lastroValidationService.registrar("BK-US2-003", new LastroDraft(null, null, "NF-E", ""));
        lastroValidationService.registrar("BK-US2-003", new LastroDraft(null, null, "BORDERO", "cedente"));

        ResultadoValidacaoLastro resultado = lastroValidationService.validar("BK-US2-003");

        assertThat(resultado.bloqueiaAceiteFinal()).isTrue();
        assertThat(resultado.lastrosRejeitados()).isEqualTo(1);
        assertThat(resultado.lastrosValidados()).isEqualTo(1);
    }
}
