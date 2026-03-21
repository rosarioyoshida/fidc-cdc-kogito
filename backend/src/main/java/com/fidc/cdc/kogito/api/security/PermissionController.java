package com.fidc.cdc.kogito.api.security;

import com.fidc.cdc.kogito.application.security.PermissionSnapshot;
import com.fidc.cdc.kogito.application.security.StageAuthorizationService;
import com.fidc.cdc.kogito.domain.cessao.Cessao;
import com.fidc.cdc.kogito.domain.cessao.CessaoRepository;
import com.fidc.cdc.kogito.domain.cessao.EtapaCessaoStatus;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cessoes/{businessKey}/permissoes")
public class PermissionController {

    private final StageAuthorizationService stageAuthorizationService;
    private final CessaoRepository cessaoRepository;

    public PermissionController(
            StageAuthorizationService stageAuthorizationService,
            CessaoRepository cessaoRepository
    ) {
        this.stageAuthorizationService = stageAuthorizationService;
        this.cessaoRepository = cessaoRepository;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> listar(@PathVariable String businessKey) {
        PermissionSnapshot snapshot = stageAuthorizationService.describePermissions(null);
        Cessao cessao = cessaoRepository.findByBusinessKey(businessKey)
                .orElseThrow(() -> new com.fidc.cdc.kogito.api.error.ResourceNotFoundException(
                        "Cessao nao encontrada para consulta de permissoes."
                ));
        String etapaAtual = cessao.getEtapas().stream()
                .filter(etapa -> etapa.getStatusEtapa() == EtapaCessaoStatus.EM_EXECUCAO)
                .map(etapa -> etapa.getNomeEtapa().name())
                .findFirst()
                .orElse("SEM_ETAPA_ATIVA");

        return ResponseEntity.ok(Map.of(
                "actorId", snapshot.actorId(),
                "perfis", snapshot.perfis(),
                "etapasPermitidas", snapshot.etapasPermitidas(),
                "etapaAtual", etapaAtual,
                "podeExecutarEtapaAtual", snapshot.etapasPermitidas().contains(etapaAtual)
        ));
    }
}
