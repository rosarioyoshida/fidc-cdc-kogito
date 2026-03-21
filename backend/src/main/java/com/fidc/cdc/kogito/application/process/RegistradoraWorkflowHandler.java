package com.fidc.cdc.kogito.application.process;

import com.fidc.cdc.kogito.api.error.ResourceNotFoundException;
import com.fidc.cdc.kogito.application.analise.ElegibilidadeService;
import com.fidc.cdc.kogito.application.analise.ResultadoAvaliacaoElegibilidade;
import com.fidc.cdc.kogito.application.documental.LastroDraft;
import com.fidc.cdc.kogito.application.documental.LastroValidationService;
import com.fidc.cdc.kogito.application.documental.ResultadoValidacaoLastro;
import com.fidc.cdc.kogito.application.financeiro.CalculoValorPagarService;
import com.fidc.cdc.kogito.application.financeiro.ResultadoCalculoValorPagar;
import com.fidc.cdc.kogito.application.readmodel.CessaoReadModelProjector;
import com.fidc.cdc.kogito.domain.analise.Contrato;
import com.fidc.cdc.kogito.domain.analise.ContratoRepository;
import com.fidc.cdc.kogito.domain.analise.Lastro;
import com.fidc.cdc.kogito.domain.analise.LastroRepository;
import com.fidc.cdc.kogito.domain.analise.Pagamento;
import com.fidc.cdc.kogito.domain.analise.PagamentoRepository;
import com.fidc.cdc.kogito.domain.analise.StatusDocumentoTermoAceite;
import com.fidc.cdc.kogito.domain.analise.StatusPagamento;
import com.fidc.cdc.kogito.domain.analise.TermoAceite;
import com.fidc.cdc.kogito.domain.analise.TermoAceiteRepository;
import com.fidc.cdc.kogito.domain.analise.TipoOperacaoRegistradora;
import com.fidc.cdc.kogito.domain.cessao.Cessao;
import com.fidc.cdc.kogito.domain.cessao.CessaoRepository;
import com.fidc.cdc.kogito.infrastructure.registradora.RegistradoraClient;
import com.fidc.cdc.kogito.infrastructure.registradora.RegistradoraResult;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistradoraWorkflowHandler {

    private final CessaoRepository cessaoRepository;
    private final ContratoRepository contratoRepository;
    private final PagamentoRepository pagamentoRepository;
    private final TermoAceiteRepository termoAceiteRepository;
    private final LastroRepository lastroRepository;
    private final ElegibilidadeService elegibilidadeService;
    private final CalculoValorPagarService calculoValorPagarService;
    private final LastroValidationService lastroValidationService;
    private final RegistradoraClient registradoraClient;
    private final CessaoReadModelProjector readModelProjector;

    public RegistradoraWorkflowHandler(
            CessaoRepository cessaoRepository,
            ContratoRepository contratoRepository,
            PagamentoRepository pagamentoRepository,
            TermoAceiteRepository termoAceiteRepository,
            LastroRepository lastroRepository,
            ElegibilidadeService elegibilidadeService,
            CalculoValorPagarService calculoValorPagarService,
            LastroValidationService lastroValidationService,
            RegistradoraClient registradoraClient,
            CessaoReadModelProjector readModelProjector
    ) {
        this.cessaoRepository = cessaoRepository;
        this.contratoRepository = contratoRepository;
        this.pagamentoRepository = pagamentoRepository;
        this.termoAceiteRepository = termoAceiteRepository;
        this.lastroRepository = lastroRepository;
        this.elegibilidadeService = elegibilidadeService;
        this.calculoValorPagarService = calculoValorPagarService;
        this.lastroValidationService = lastroValidationService;
        this.registradoraClient = registradoraClient;
        this.readModelProjector = readModelProjector;
    }

    @Transactional
    public ResultadoAvaliacaoElegibilidade avaliarElegibilidade(String businessKey) {
        ResultadoAvaliacaoElegibilidade resultado = elegibilidadeService.avaliar(businessKey);
        refreshReadModel(businessKey, "ELEGIBILIDADE_AVALIADA");
        return resultado;
    }

    @Transactional
    public ResultadoCalculoValorPagar apurarValorPagar(String businessKey) {
        ResultadoCalculoValorPagar resultado = calculoValorPagarService.calcular(businessKey);
        refreshReadModel(businessKey, "VALOR_APURADO");
        return resultado;
    }

    @Transactional
    public RegistradoraResult executarOperacaoRegistradora(String businessKey, TipoOperacaoRegistradora tipoOperacao) {
        RegistradoraResult resultado = registradoraClient.executar(businessKey, tipoOperacao, buildPayload(businessKey, tipoOperacao));
        refreshReadModel(businessKey, "REGISTRADORA_" + tipoOperacao.name());
        return resultado;
    }

    @Transactional
    public TermoAceite emitirTermoAceite(String businessKey) {
        Cessao cessao = loadCessao(businessKey);
        int novaVersao = termoAceiteRepository.findByCessaoBusinessKeyOrderByVersaoDesc(businessKey)
                .stream()
                .map(TermoAceite::getVersao)
                .findFirst()
                .orElse(0) + 1;

        TermoAceite termoAceite = new TermoAceite();
        termoAceite.setCessao(cessao);
        termoAceite.setVersao(novaVersao);
        termoAceite.setEmitidoEm(OffsetDateTime.now());
        termoAceite.setReferenciaDocumento("termo-" + businessKey + "-v" + novaVersao + ".pdf");
        termoAceite.setStatusDocumento(StatusDocumentoTermoAceite.GERADO);
        TermoAceite salvo = termoAceiteRepository.save(termoAceite);
        refreshReadModel(businessKey, "TERMO_ACEITE_EMITIDO");
        return salvo;
    }

    @Transactional
    public Pagamento liberarPagamento(String businessKey, String autorizadoPor) {
        Cessao cessao = loadCessao(businessKey);
        Pagamento pagamento = pagamentoRepository.findFirstByCessaoBusinessKeyOrderByCreatedAtDesc(businessKey)
                .orElseGet(Pagamento::new);
        pagamento.setCessao(cessao);
        pagamento.setValor(Optional.ofNullable(cessao.getValorAprovado()).orElse(cessao.getValorCalculado()));
        pagamento.setStatusPagamento(StatusPagamento.LIBERADO);
        pagamento.setAutorizadoPor(autorizadoPor);
        pagamento.setAutorizadoEm(OffsetDateTime.now());
        Pagamento salvo = pagamentoRepository.save(pagamento);
        refreshReadModel(businessKey, "PAGAMENTO_LIBERADO");
        return salvo;
    }

    @Transactional
    public Lastro receberLastro(String businessKey, LastroDraft draft) {
        Lastro lastro = lastroValidationService.registrar(businessKey, draft);
        refreshReadModel(businessKey, "LASTRO_RECEBIDO");
        return lastro;
    }

    @Transactional
    public ResultadoValidacaoLastro validarLastros(String businessKey) {
        ResultadoValidacaoLastro resultado = lastroValidationService.validar(businessKey);
        refreshReadModel(businessKey, "LASTRO_VALIDADO");
        return resultado;
    }

    private Map<String, ?> buildPayload(String businessKey, TipoOperacaoRegistradora tipoOperacao) {
        Cessao cessao = loadCessao(businessKey);
        List<Contrato> contratos = contratoRepository.findByCessaoBusinessKeyOrderByIdentificadorExternoAsc(businessKey);
        return switch (tipoOperacao) {
            case CARTEIRA -> Map.of(
                    "businessKey", businessKey,
                    "cedenteId", cessao.getCedenteId(),
                    "cessionariaId", cessao.getCessionariaId()
            );
            case CONTRATO -> Map.of(
                    "businessKey", businessKey,
                    "contratos", contratos.stream().map(contrato -> Map.of(
                            "identificadorExterno", contrato.getIdentificadorExterno(),
                            "sacadoId", contrato.getSacadoId(),
                            "valorNominal", contrato.getValorNominal()
                    )).toList()
            );
            case PARCELA -> Map.of(
                    "businessKey", businessKey,
                    "parcelas", contratos.stream()
                            .flatMap(contrato -> contrato.getParcelas().stream())
                            .map(parcela -> Map.of(
                                    "identificadorExterno", parcela.getIdentificadorExterno(),
                                    "numeroParcela", parcela.getNumeroParcela(),
                                    "valor", parcela.getValor(),
                                    "vencimento", parcela.getVencimento()
                            ))
                            .toList()
            );
            case OFERTA -> Map.of(
                    "businessKey", businessKey,
                    "valorAprovado", cessao.getValorAprovado(),
                    "valorCalculado", cessao.getValorCalculado()
            );
            case ACEITE -> Map.of(
                    "businessKey", businessKey,
                    "termos", termoAceiteRepository.findByCessaoBusinessKeyOrderByVersaoDesc(businessKey)
                            .stream()
                            .map(termo -> Map.of(
                                    "versao", termo.getVersao(),
                                    "referenciaDocumento", termo.getReferenciaDocumento(),
                                    "statusDocumento", termo.getStatusDocumento().name()
                            ))
                            .toList()
            );
            case PAGAMENTO -> Map.of(
                    "businessKey", businessKey,
                    "pagamentos", pagamentoRepository.findByCessaoBusinessKeyOrderByCreatedAtAsc(businessKey)
                            .stream()
                            .map(pagamento -> Map.of(
                                    "valor", pagamento.getValor(),
                                    "statusPagamento", pagamento.getStatusPagamento().name(),
                                    "autorizadoEm", pagamento.getAutorizadoEm()
                            ))
                            .toList()
            );
            case LASTRO -> Map.of(
                    "businessKey", businessKey,
                    "lastros", lastroRepository.findByCessaoBusinessKeyOrderByRecebidoEmAsc(businessKey)
                            .stream()
                            .map(lastro -> Map.of(
                                    "tipoDocumento", lastro.getTipoDocumento(),
                                    "origem", lastro.getOrigem(),
                                    "statusValidacao", lastro.getStatusValidacao().name()
                            ))
                            .toList()
            );
        };
    }

    private Cessao loadCessao(String businessKey) {
        return cessaoRepository.findByBusinessKey(businessKey)
                .orElseThrow(() -> new ResourceNotFoundException("Cessao nao encontrada para o workflow da registradora."));
    }

    private void refreshReadModel(String businessKey, String eventName) {
        readModelProjector.projectCurrentState(businessKey, eventName);
    }
}
