package com.fidc.cdc.kogito.integration.analise;

import static org.assertj.core.api.Assertions.assertThat;

import com.fidc.cdc.kogito.api.cessao.CessaoRequest;
import com.fidc.cdc.kogito.application.analise.ElegibilidadeService;
import com.fidc.cdc.kogito.application.analise.ResultadoAvaliacaoElegibilidade;
import com.fidc.cdc.kogito.application.cessao.CessaoProcessService;
import com.fidc.cdc.kogito.domain.analise.Contrato;
import com.fidc.cdc.kogito.domain.analise.ContratoRepository;
import com.fidc.cdc.kogito.domain.analise.StatusRegistroAnalise;
import com.fidc.cdc.kogito.support.IntegrationTestBase;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ElegibilidadeIntegrationTest extends IntegrationTestBase {

    @Autowired
    private CessaoProcessService cessaoProcessService;

    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private ElegibilidadeService elegibilidadeService;

    @Test
    void shouldBlockWhenEligibilityFindsImpedingRule() {
        var cessao = cessaoProcessService.createAndStart(new CessaoRequest("BK-US2-001", "CED-01", "CESS-01"));

        Contrato contrato = new Contrato();
        contrato.setCessao(cessao);
        contrato.setIdentificadorExterno("CTR-001");
        contrato.setSacadoId("SAC-001");
        contrato.setValorNominal(new BigDecimal("-10.00"));
        contrato.setDataOrigem(LocalDate.now());
        contrato.setStatusRegistro(StatusRegistroAnalise.PENDENTE);
        contratoRepository.save(contrato);

        ResultadoAvaliacaoElegibilidade resultado = elegibilidadeService.avaliar("BK-US2-001");

        assertThat(resultado.possuiBloqueios()).isTrue();
        assertThat(resultado.regras())
                .anySatisfy(regra -> {
                    assertThat(regra.getCodigoRegra()).isEqualTo("VALOR_TOTAL_POSITIVO");
                    assertThat(regra.getResultado().name()).isEqualTo("REPROVADA");
                });
    }
}
