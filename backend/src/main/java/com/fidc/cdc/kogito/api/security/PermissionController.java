package com.fidc.cdc.kogito.api.security;

import com.fidc.cdc.kogito.application.process.ManagementConsoleContext;
import com.fidc.cdc.kogito.application.process.ManagementConsoleSupport;
import com.fidc.cdc.kogito.application.process.TaskAssignmentContext;
import com.fidc.cdc.kogito.application.process.TaskAssignmentService;
import com.fidc.cdc.kogito.application.security.PermissionSnapshot;
import com.fidc.cdc.kogito.application.security.StageAuthorizationService;
import com.fidc.cdc.kogito.observability.ProcessMetricsService;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Expoe endpoints HTTP para permission.
 *
 * <p>Este tipo pertence a camada de superficie HTTP e contratos de transporte. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
@RestController
@RequestMapping("/api/v1/cessoes/{businessKey}/permissoes")
public class PermissionController {

    private final StageAuthorizationService stageAuthorizationService;
    private final TaskAssignmentService taskAssignmentService;
    private final ManagementConsoleSupport managementConsoleSupport;
    private final ProcessMetricsService processMetricsService;

    public PermissionController(
            StageAuthorizationService stageAuthorizationService,
            TaskAssignmentService taskAssignmentService,
            ManagementConsoleSupport managementConsoleSupport,
            ProcessMetricsService processMetricsService
    ) {
        this.stageAuthorizationService = stageAuthorizationService;
        this.taskAssignmentService = taskAssignmentService;
        this.managementConsoleSupport = managementConsoleSupport;
        this.processMetricsService = processMetricsService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> listar(@PathVariable String businessKey) {
        PermissionSnapshot snapshot = stageAuthorizationService.describePermissions(null);
        TaskAssignmentContext taskContext = taskAssignmentService.describeTaskContext(businessKey, snapshot.actorId());
        ManagementConsoleContext managementContext = managementConsoleSupport.describeProcessContext(businessKey);
        processMetricsService.registerConsoleAccess("task-console", "context-served");
        processMetricsService.registerConsoleAccess("management-console", "context-served");
        processMetricsService.registerConsoleAccess(
                "management-console",
                managementContext.processSvgAvailable() ? "process-svg-eligible" : managementContext.processSvgAvailabilityReason()
        );

        return ResponseEntity.ok(Map.of(
                "actorId", snapshot.actorId(),
                "perfis", snapshot.perfis(),
                "etapasPermitidas", snapshot.etapasPermitidas(),
                "etapaAtual", taskContext.currentStage(),
                "podeExecutarEtapaAtual", snapshot.etapasPermitidas().contains(taskContext.currentStage()),
                "taskContext", taskContext,
                "managementContext", managementContext
        ));
    }
}
