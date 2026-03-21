package com.fidc.cdc.kogito.api.cessao;

import com.fidc.cdc.kogito.api.hateoas.CessaoLinkAssembler;
import com.fidc.cdc.kogito.application.cessao.CessaoProcessService;
import com.fidc.cdc.kogito.domain.cessao.Cessao;
import com.fidc.cdc.kogito.domain.cessao.CessaoStatus;
import com.fidc.cdc.kogito.domain.cessao.EtapaCessao;
import com.fidc.cdc.kogito.domain.cessao.EtapaCessaoNome;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cessoes")
public class CessaoController {

    private final CessaoProcessService cessaoProcessService;
    private final CessaoLinkAssembler linkAssembler;

    public CessaoController(CessaoProcessService cessaoProcessService, CessaoLinkAssembler linkAssembler) {
        this.cessaoProcessService = cessaoProcessService;
        this.linkAssembler = linkAssembler;
    }

    @PostMapping
    public ResponseEntity<CessaoResponse> create(@Valid @RequestBody CessaoRequest request) {
        Cessao cessao = cessaoProcessService.createAndStart(request);
        CessaoResponse response = toResponse(cessao, true);
        return ResponseEntity.created(URI.create(linkAssembler.self(cessao.getBusinessKey()).getHref()))
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<CessaoResponse>> list(
            @RequestParam(name = "status", required = false) CessaoStatus status,
            @RequestParam(name = "business-key", required = false) String businessKey
    ) {
        return ResponseEntity.ok(cessaoProcessService.list(status, businessKey)
                .stream()
                .map(cessao -> toResponse(cessao, false))
                .toList());
    }

    @GetMapping("/{businessKey}")
    public ResponseEntity<CessaoResponse> get(@PathVariable String businessKey) {
        return ResponseEntity.ok(toResponse(cessaoProcessService.getByBusinessKey(businessKey), true));
    }

    @GetMapping("/{businessKey}/etapas")
    public ResponseEntity<List<EtapaResponse>> listEtapas(@PathVariable String businessKey) {
        return ResponseEntity.ok(cessaoProcessService.getEtapas(businessKey)
                .stream()
                .map(this::toEtapaResponse)
                .toList());
    }

    @GetMapping("/{businessKey}/historico")
    public ResponseEntity<List<EtapaResponse>> historico(@PathVariable String businessKey) {
        return ResponseEntity.ok(cessaoProcessService.getEtapas(businessKey)
                .stream()
                .map(this::toEtapaResponse)
                .toList());
    }

    @PostMapping("/{businessKey}/etapas/{etapaNome}/acoes/avancar")
    public ResponseEntity<CessaoResponse> advance(
            @PathVariable String businessKey,
            @PathVariable EtapaCessaoNome etapaNome,
            @RequestBody(required = false) EtapaAdvanceRequest request
    ) {
        Cessao cessao = cessaoProcessService.advanceStage(
                businessKey,
                etapaNome,
                request != null ? request.responsavelId() : null,
                request != null ? request.justificativa() : null
        );
        return ResponseEntity.accepted().body(toResponse(cessao, true));
    }

    private CessaoResponse toResponse(Cessao cessao, boolean includeEtapas) {
        CessaoResponse response = new CessaoResponse();
        response.setBusinessKey(cessao.getBusinessKey());
        response.setStatus(cessao.getStatus());
        response.setWorkflowInstanceId(cessao.getWorkflowInstanceId());
        response.setCedenteId(cessao.getCedenteId());
        response.setCessionariaId(cessao.getCessionariaId());
        response.setDataImportacao(cessao.getDataImportacao());
        response.setDataEncerramento(cessao.getDataEncerramento());
        response.add(linkAssembler.self(cessao.getBusinessKey()));
        response.add(linkAssembler.etapas(cessao.getBusinessKey()));
        response.add(linkAssembler.historico(cessao.getBusinessKey()));
        if (includeEtapas) {
            response.setEtapas(cessao.getEtapas().stream().map(this::toEtapaResponse).toList());
        }
        return response;
    }

    private EtapaResponse toEtapaResponse(EtapaCessao etapa) {
        EtapaResponse response = new EtapaResponse();
        response.setNomeEtapa(etapa.getNomeEtapa());
        response.setStatusEtapa(etapa.getStatusEtapa());
        response.setOrdem(etapa.getOrdem());
        response.setResponsavelId(etapa.getResponsavelId());
        response.setInicioEm(etapa.getInicioEm());
        response.setConcluidaEm(etapa.getConcluidaEm());
        response.setResultado(etapa.getResultado());
        response.setJustificativa(etapa.getJustificativa());
        return response;
    }
}
