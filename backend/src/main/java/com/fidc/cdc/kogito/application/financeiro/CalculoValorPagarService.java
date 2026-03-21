package com.fidc.cdc.kogito.application.financeiro;

import com.fidc.cdc.kogito.api.error.BusinessConflictException;
import com.fidc.cdc.kogito.api.error.ResourceNotFoundException;
import com.fidc.cdc.kogito.domain.analise.Contrato;
import com.fidc.cdc.kogito.domain.analise.ContratoRepository;
import com.fidc.cdc.kogito.domain.analise.Pagamento;
import com.fidc.cdc.kogito.domain.analise.PagamentoRepository;
import com.fidc.cdc.kogito.domain.analise.StatusPagamento;
import com.fidc.cdc.kogito.domain.cessao.Cessao;
import com.fidc.cdc.kogito.domain.cessao.CessaoRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CalculoValorPagarService {

    private final CessaoRepository cessaoRepository;
    private final ContratoRepository contratoRepository;
    private final PagamentoRepository pagamentoRepository;

    public CalculoValorPagarService(
            CessaoRepository cessaoRepository,
            ContratoRepository contratoRepository,
            PagamentoRepository pagamentoRepository
    ) {
        this.cessaoRepository = cessaoRepository;
        this.contratoRepository = contratoRepository;
        this.pagamentoRepository = pagamentoRepository;
    }

    @Transactional
    public ResultadoCalculoValorPagar calcular(String businessKey) {
        Cessao cessao = cessaoRepository.findByBusinessKey(businessKey)
                .orElseThrow(() -> new ResourceNotFoundException("Cessao nao encontrada para calculo financeiro."));
        List<Contrato> contratos = contratoRepository.findByCessaoBusinessKeyOrderByIdentificadorExternoAsc(businessKey);
        if (contratos.isEmpty()) {
            throw new BusinessConflictException("Nao e possivel calcular o valor da cessao sem contratos vinculados.");
        }

        boolean possuiParcelas = contratos.stream().anyMatch(contrato -> !contrato.getParcelas().isEmpty());
        BigDecimal valorCalculado = possuiParcelas
                ? contratos.stream()
                        .flatMap(contrato -> contrato.getParcelas().stream())
                        .map(parcela -> parcela.getValor() == null ? BigDecimal.ZERO : parcela.getValor())
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                : contratos.stream()
                        .map(contrato -> contrato.getValorNominal() == null ? BigDecimal.ZERO : contrato.getValorNominal())
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (valorCalculado.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessConflictException("O valor calculado da cessao deve ser positivo.");
        }

        BigDecimal valorAprovado = valorCalculado;
        cessao.setValorCalculado(valorCalculado);
        cessao.setValorAprovado(valorAprovado);

        Pagamento pagamento = pagamentoRepository.findFirstByCessaoBusinessKeyOrderByCreatedAtDesc(businessKey)
                .orElseGet(Pagamento::new);
        pagamento.setCessao(cessao);
        pagamento.setValor(valorAprovado);
        pagamento.setStatusPagamento(StatusPagamento.PENDENTE);
        pagamentoRepository.save(pagamento);

        String baseCalculo = possuiParcelas ? "SOMA_PARCELAS_VALIDAS" : "SOMA_CONTRATOS_IMPORTADOS";
        return new ResultadoCalculoValorPagar(valorCalculado, valorAprovado, baseCalculo);
    }
}
