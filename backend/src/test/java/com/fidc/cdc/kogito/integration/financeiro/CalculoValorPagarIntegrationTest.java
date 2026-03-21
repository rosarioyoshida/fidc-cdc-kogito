package com.fidc.cdc.kogito.integration.financeiro;

import static org.assertj.core.api.Assertions.assertThat;

import com.fidc.cdc.kogito.api.cessao.CessaoRequest;
import com.fidc.cdc.kogito.application.cessao.CessaoProcessService;
import com.fidc.cdc.kogito.application.financeiro.CalculoValorPagarService;
import com.fidc.cdc.kogito.application.financeiro.ResultadoCalculoValorPagar;
import com.fidc.cdc.kogito.domain.analise.Contrato;
import com.fidc.cdc.kogito.domain.analise.ContratoRepository;
import com.fidc.cdc.kogito.domain.analise.Parcela;
import com.fidc.cdc.kogito.domain.analise.PagamentoRepository;
import com.fidc.cdc.kogito.domain.analise.StatusPagamento;
import com.fidc.cdc.kogito.domain.analise.StatusRegistroAnalise;
import com.fidc.cdc.kogito.support.IntegrationTestBase;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CalculoValorPagarIntegrationTest extends IntegrationTestBase {

    @Autowired
    private CessaoProcessService cessaoProcessService;

    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private CalculoValorPagarService calculoValorPagarService;

    @Test
    void shouldCalculatePaymentBasedOnContractParcels() {
        var cessao = cessaoProcessService.createAndStart(new CessaoRequest("BK-US2-002", "CED-01", "CESS-01"));

        Contrato contrato = new Contrato();
        contrato.setCessao(cessao);
        contrato.setIdentificadorExterno("CTR-002");
        contrato.setSacadoId("SAC-002");
        contrato.setValorNominal(new BigDecimal("150.00"));
        contrato.setDataOrigem(LocalDate.now());
        contrato.setStatusRegistro(StatusRegistroAnalise.PENDENTE);

        Parcela parcelaUm = new Parcela();
        parcelaUm.setIdentificadorExterno("PAR-002-1");
        parcelaUm.setNumeroParcela(1);
        parcelaUm.setVencimento(LocalDate.now().plusDays(30));
        parcelaUm.setValor(new BigDecimal("100.00"));
        parcelaUm.setStatusRegistro(StatusRegistroAnalise.PENDENTE);
        contrato.addParcela(parcelaUm);

        Parcela parcelaDois = new Parcela();
        parcelaDois.setIdentificadorExterno("PAR-002-2");
        parcelaDois.setNumeroParcela(2);
        parcelaDois.setVencimento(LocalDate.now().plusDays(60));
        parcelaDois.setValor(new BigDecimal("50.00"));
        parcelaDois.setStatusRegistro(StatusRegistroAnalise.PENDENTE);
        contrato.addParcela(parcelaDois);

        contratoRepository.save(contrato);

        ResultadoCalculoValorPagar resultado = calculoValorPagarService.calcular("BK-US2-002");

        assertThat(resultado.valorCalculado()).isEqualByComparingTo("150.00");
        assertThat(resultado.baseCalculo()).isEqualTo("SOMA_PARCELAS_VALIDAS");
        assertThat(pagamentoRepository.findByCessaoBusinessKeyOrderByCreatedAtAsc("BK-US2-002"))
                .singleElement()
                .satisfies(pagamento -> {
                    assertThat(pagamento.getValor()).isEqualByComparingTo("150.00");
                    assertThat(pagamento.getStatusPagamento()).isEqualTo(StatusPagamento.PENDENTE);
                });
    }
}
