package com.fidc.cdc.kogito.api.analise;

import com.fidc.cdc.kogito.application.analise.ResultadoAvaliacaoElegibilidade;
import com.fidc.cdc.kogito.application.documental.LastroDraft;
import com.fidc.cdc.kogito.application.documental.ResultadoValidacaoLastro;
import com.fidc.cdc.kogito.application.financeiro.ResultadoCalculoValorPagar;
import com.fidc.cdc.kogito.application.process.RegistradoraWorkflowHandler;
import com.fidc.cdc.kogito.domain.analise.Contrato;
import com.fidc.cdc.kogito.domain.analise.ContratoRepository;
import com.fidc.cdc.kogito.domain.analise.Lastro;
import com.fidc.cdc.kogito.domain.analise.LastroRepository;
import com.fidc.cdc.kogito.domain.analise.Pagamento;
import com.fidc.cdc.kogito.domain.analise.PagamentoRepository;
import com.fidc.cdc.kogito.domain.analise.Parcela;
import com.fidc.cdc.kogito.domain.analise.ParcelaRepository;
import com.fidc.cdc.kogito.domain.analise.RegraElegibilidade;
import com.fidc.cdc.kogito.domain.analise.RegraElegibilidadeRepository;
import com.fidc.cdc.kogito.domain.analise.TipoOperacaoRegistradora;
import com.fidc.cdc.kogito.infrastructure.registradora.RegistradoraResult;
import jakarta.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cessoes/{businessKey}/analise")
public class AnaliseController {

    private final RegraElegibilidadeRepository regraElegibilidadeRepository;
    private final ContratoRepository contratoRepository;
    private final ParcelaRepository parcelaRepository;
    private final PagamentoRepository pagamentoRepository;
    private final LastroRepository lastroRepository;
    private final RegistradoraWorkflowHandler workflowHandler;

    public AnaliseController(
            RegraElegibilidadeRepository regraElegibilidadeRepository,
            ContratoRepository contratoRepository,
            ParcelaRepository parcelaRepository,
            PagamentoRepository pagamentoRepository,
            LastroRepository lastroRepository,
            RegistradoraWorkflowHandler workflowHandler
    ) {
        this.regraElegibilidadeRepository = regraElegibilidadeRepository;
        this.contratoRepository = contratoRepository;
        this.parcelaRepository = parcelaRepository;
        this.pagamentoRepository = pagamentoRepository;
        this.lastroRepository = lastroRepository;
        this.workflowHandler = workflowHandler;
    }

    @GetMapping("/regras")
    public ResponseEntity<List<Map<String, Object>>> listRegras(@PathVariable String businessKey) {
        return ResponseEntity.ok(regraElegibilidadeRepository.findByCessaoBusinessKeyOrderByAvaliadaEmAsc(businessKey)
                .stream()
                .map(this::toRegra)
                .toList());
    }

    @GetMapping("/contratos")
    public ResponseEntity<List<Map<String, Object>>> listContratos(@PathVariable String businessKey) {
        return ResponseEntity.ok(contratoRepository.findByCessaoBusinessKeyOrderByIdentificadorExternoAsc(businessKey)
                .stream()
                .map(this::toContrato)
                .toList());
    }

    @GetMapping("/parcelas")
    public ResponseEntity<List<Map<String, Object>>> listParcelas(@PathVariable String businessKey) {
        return ResponseEntity.ok(parcelaRepository.findByContratoCessaoBusinessKeyOrderByNumeroParcelaAsc(businessKey)
                .stream()
                .map(this::toParcela)
                .toList());
    }

    @GetMapping("/pagamentos")
    public ResponseEntity<List<Map<String, Object>>> listPagamentos(@PathVariable String businessKey) {
        return ResponseEntity.ok(pagamentoRepository.findByCessaoBusinessKeyOrderByCreatedAtAsc(businessKey)
                .stream()
                .map(this::toPagamento)
                .toList());
    }

    @GetMapping("/lastros")
    public ResponseEntity<List<Map<String, Object>>> listLastros(@PathVariable String businessKey) {
        return ResponseEntity.ok(lastroRepository.findByCessaoBusinessKeyOrderByRecebidoEmAsc(businessKey)
                .stream()
                .map(this::toLastro)
                .toList());
    }

    @GetMapping("/historico-documental")
    public ResponseEntity<List<Map<String, Object>>> historicoDocumental(@PathVariable String businessKey) {
        return listLastros(businessKey);
    }

    @PostMapping("/elegibilidade/avaliar")
    public ResponseEntity<Map<String, Object>> avaliarElegibilidade(@PathVariable String businessKey) {
        ResultadoAvaliacaoElegibilidade resultado = workflowHandler.avaliarElegibilidade(businessKey);
        return ResponseEntity.accepted().body(Map.of(
                "possuiBloqueios", resultado.possuiBloqueios(),
                "regras", resultado.regras().stream().map(this::toRegra).toList()
        ));
    }

    @PostMapping("/calculo/apurar")
    public ResponseEntity<Map<String, Object>> apurarValor(@PathVariable String businessKey) {
        ResultadoCalculoValorPagar resultado = workflowHandler.apurarValorPagar(businessKey);
        return ResponseEntity.accepted().body(Map.of(
                "valorCalculado", resultado.valorCalculado(),
                "valorAprovado", resultado.valorAprovado(),
                "baseCalculo", resultado.baseCalculo()
        ));
    }

    @PostMapping("/lastros")
    public ResponseEntity<Map<String, Object>> receberLastro(
            @PathVariable String businessKey,
            @Valid @RequestBody LastroRequest request
    ) {
        Lastro lastro = workflowHandler.receberLastro(
                businessKey,
                new LastroDraft(request.contratoId(), request.parcelaId(), request.tipoDocumento(), request.origem())
        );
        return ResponseEntity.accepted().body(toLastro(lastro));
    }

    @PostMapping("/lastros/validar")
    public ResponseEntity<Map<String, Object>> validarLastros(@PathVariable String businessKey) {
        ResultadoValidacaoLastro resultado = workflowHandler.validarLastros(businessKey);
        return ResponseEntity.accepted().body(Map.of(
                "bloqueiaAceiteFinal", resultado.bloqueiaAceiteFinal(),
                "lastrosValidados", resultado.lastrosValidados(),
                "lastrosRejeitados", resultado.lastrosRejeitados(),
                "lastros", resultado.lastros().stream().map(this::toLastro).toList()
        ));
    }

    @PostMapping("/registradora/{tipoOperacao}")
    public ResponseEntity<Map<String, Object>> executarOperacaoRegistradora(
            @PathVariable String businessKey,
            @PathVariable TipoOperacaoRegistradora tipoOperacao
    ) {
        RegistradoraResult resultado = workflowHandler.executarOperacaoRegistradora(businessKey, tipoOperacao);
        return ResponseEntity.accepted().body(Map.of(
                "requestId", resultado.requestId(),
                "httpStatus", resultado.httpStatus(),
                "statusNegocio", resultado.statusNegocio(),
                "tentativa", resultado.tentativa(),
                "body", resultado.body()
        ));
    }

    private Map<String, Object> toRegra(RegraElegibilidade regra) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("id", regra.getId());
        response.put("codigoRegra", regra.getCodigoRegra());
        response.put("descricao", regra.getDescricao());
        response.put("resultado", regra.getResultado().name());
        response.put("severidade", regra.getSeveridade().name());
        response.put("mensagem", regra.getMensagem());
        response.put("avaliadaEm", regra.getAvaliadaEm());
        return response;
    }

    private Map<String, Object> toContrato(Contrato contrato) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("id", contrato.getId());
        response.put("identificadorExterno", contrato.getIdentificadorExterno());
        response.put("sacadoId", contrato.getSacadoId());
        response.put("valorNominal", contrato.getValorNominal());
        response.put("dataOrigem", contrato.getDataOrigem());
        response.put("statusRegistro", contrato.getStatusRegistro().name());
        response.put("parcelas", contrato.getParcelas().stream().map(this::toParcela).toList());
        return response;
    }

    private Map<String, Object> toParcela(Parcela parcela) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("id", parcela.getId());
        response.put("identificadorExterno", parcela.getIdentificadorExterno());
        response.put("numeroParcela", parcela.getNumeroParcela());
        response.put("vencimento", parcela.getVencimento());
        response.put("valor", parcela.getValor());
        response.put("statusRegistro", parcela.getStatusRegistro().name());
        return response;
    }

    private Map<String, Object> toPagamento(Pagamento pagamento) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("id", pagamento.getId());
        response.put("valor", pagamento.getValor());
        response.put("statusPagamento", pagamento.getStatusPagamento().name());
        response.put("autorizadoPor", pagamento.getAutorizadoPor());
        response.put("autorizadoEm", pagamento.getAutorizadoEm());
        response.put("confirmadoEm", pagamento.getConfirmadoEm());
        response.put("comprovanteReferencia", pagamento.getComprovanteReferencia());
        return response;
    }

    private Map<String, Object> toLastro(Lastro lastro) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("id", lastro.getId());
        response.put("tipoDocumento", lastro.getTipoDocumento());
        response.put("origem", lastro.getOrigem());
        response.put("statusValidacao", lastro.getStatusValidacao().name());
        response.put("recebidoEm", lastro.getRecebidoEm());
        response.put("validadoEm", lastro.getValidadoEm());
        response.put("contratoId", lastro.getContrato() != null ? lastro.getContrato().getId() : null);
        response.put("parcelaId", lastro.getParcela() != null ? lastro.getParcela().getId() : null);
        return response;
    }
}
