package com.fidc.cdc.kogito.api.audit;

import com.fidc.cdc.kogito.application.audit.AuditTrailService;
import com.fidc.cdc.kogito.domain.audit.EventoAuditoria;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cessoes/{businessKey}/auditoria")
public class AuditController {

    private final AuditTrailService auditTrailService;

    public AuditController(AuditTrailService auditTrailService) {
        this.auditTrailService = auditTrailService;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listar(@PathVariable String businessKey) {
        return ResponseEntity.ok(auditTrailService.listarPorBusinessKey(businessKey)
                .stream()
                .map(this::toResponse)
                .toList());
    }

    private Map<String, Object> toResponse(EventoAuditoria evento) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("id", evento.getId());
        response.put("atorId", evento.getAtorId());
        response.put("perfil", evento.getPerfil());
        response.put("tipoEvento", evento.getTipoEvento());
        response.put("resultado", evento.getResultado());
        response.put("correlationId", evento.getCorrelationId());
        response.put("ocorridoEm", evento.getOcorridoEm());
        response.put("detalheRef", evento.getDetalheRef());
        response.put("etapa", evento.getEtapa() != null ? evento.getEtapa().getNomeEtapa().name() : null);
        return response;
    }
}
