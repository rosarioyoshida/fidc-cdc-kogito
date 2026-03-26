package com.fidc.cdc.kogito.application.process;

import com.fidc.cdc.kogito.api.error.BusinessConflictException;
import com.fidc.cdc.kogito.api.error.ResourceNotFoundException;
import com.fidc.cdc.kogito.application.cessao.CessaoProcessService;
import com.fidc.cdc.kogito.domain.cessao.EtapaCessaoNome;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

@Service
public class ConsoleRuntimeEndpointService {

    private final KogitoWorkflowRuntimeService workflowRuntimeService;
    private final CessaoProcessService cessaoProcessService;
    private final ManagementConsoleSupport managementConsoleSupport;

    public ConsoleRuntimeEndpointService(
            KogitoWorkflowRuntimeService workflowRuntimeService,
            CessaoProcessService cessaoProcessService,
            ManagementConsoleSupport managementConsoleSupport
    ) {
        this.workflowRuntimeService = workflowRuntimeService;
        this.cessaoProcessService = cessaoProcessService;
        this.managementConsoleSupport = managementConsoleSupport;
    }

    public String processDiagram(String processId) {
        return loadProcessSvg(processId);
    }

    public String processInstanceDiagram(String processId, String processInstanceId) {
        if (!KogitoWorkflowRuntimeService.PROCESS_ID.equals(processId)) {
            throw new ResourceNotFoundException("Processo Kogito nao encontrado.");
        }

        KogitoProcessSnapshot snapshot = workflowRuntimeService.getProcess(processInstanceId);
        ManagementConsoleContext context = managementConsoleSupport.describeProcessContext(snapshot.businessKey());
        return highlightStage(loadProcessSvg(processId), resolveHighlightedElementId(context.currentStage()));
    }

    public String processSource(String processId) {
        if (!KogitoWorkflowRuntimeService.PROCESS_ID.equals(processId)) {
            throw new ResourceNotFoundException("Processo Kogito nao encontrado.");
        }
        try {
            return StreamUtils.copyToString(
                    new ClassPathResource("processes/controle-cessao.bpmn").getInputStream(),
                    StandardCharsets.UTF_8
            );
        } catch (IOException ioException) {
            throw new IllegalStateException("Nao foi possivel carregar o BPMN da cessao.", ioException);
        }
    }

    public Map<String, Object> taskSchema(
            String businessKey,
            String processInstanceId,
            String taskName,
            String externalReferenceId,
            String user,
            List<String> groups
    ) {
        KogitoTaskSnapshot task = resolveTask(processInstanceId, businessKey, taskName, externalReferenceId);
        String stageGroup = task.groupId() == null || task.groupId().isBlank() ? "OPERADOR" : task.groupId();
        String actor = resolveActor(user, groups, stageGroup);
        return Map.of(
                "$schema", "http://json-schema.org/draft-07/schema#",
                "title", task.name(),
                "description", task.description() == null ? "Execucao operacional da tarefa." : task.description(),
                "type", "object",
                "properties", Map.of(
                        "responsavelId", Map.of(
                                "type", "string",
                                "title", "Usuario executor",
                                "default", actor,
                                "readOnly", true
                        ),
                        "justificativa", Map.of(
                                "type", "string",
                                "title", "Justificativa",
                                "default", "Execucao da tarefa via Task Console."
                        )
                ),
                "phases", List.of("complete")
        );
    }

    public Map<String, Object> submitTask(
            String businessKey,
            String processInstanceId,
            String taskName,
            String externalReferenceId,
            String phase,
            String user,
            List<String> groups,
            Map<String, Object> payload
    ) {
        KogitoTaskSnapshot task = resolveTask(processInstanceId, businessKey, taskName, externalReferenceId);
        String effectivePhase = phase == null || phase.isBlank() ? "complete" : phase;
        if (!"complete".equalsIgnoreCase(effectivePhase)) {
            throw new BusinessConflictException("Apenas a fase complete esta suportada para as tasks da cessao.");
        }

        String actor = resolveActor(user, groups, task.groupId());
        String justification = payload == null ? null : stringValue(payload.get("justificativa"));
        var cessao = cessaoProcessService.advanceStage(
                businessKey,
                EtapaCessaoNome.valueOf(taskName),
                actor,
                justification
        );
        return Map.of(
                "businessKey", cessao.getBusinessKey(),
                "workflowInstanceId", cessao.getWorkflowInstanceId(),
                "status", cessao.getStatus().name(),
                "submittedPhase", effectivePhase
        );
    }

    private KogitoTaskSnapshot resolveTask(
            String processInstanceId,
            String businessKey,
            String taskName,
            String externalReferenceId
    ) {
        KogitoProcessSnapshot snapshot = workflowRuntimeService.getProcess(processInstanceId);
        if (!snapshot.businessKey().equals(businessKey)) {
            throw new BusinessConflictException("Business key divergente da instancia informada.");
        }
        KogitoTaskSnapshot task = snapshot.activeTask();
        if (task == null) {
            throw new ResourceNotFoundException("Nao existe task humana ativa para a instancia informada.");
        }
        if (!task.definitionId().equals(taskName)) {
            throw new BusinessConflictException("A task ativa nao corresponde a task solicitada.");
        }
        if (externalReferenceId != null
                && !externalReferenceId.isBlank()
                && !externalReferenceId.equals(task.externalReferenceId())) {
            throw new BusinessConflictException("A referencia externa da task nao corresponde ao runtime atual.");
        }
        return task;
    }

    private String resolveActor(String user, List<String> groups, String fallbackGroup) {
        if (user != null && !user.isBlank() && !"kogito-admin".equalsIgnoreCase(user)) {
            return user;
        }
        List<String> effectiveGroups = groups == null || groups.isEmpty()
                ? List.of(fallbackGroup == null || fallbackGroup.isBlank() ? "OPERADOR" : fallbackGroup)
                : groups;
        if (effectiveGroups.contains("APROVADOR")) {
            return "aprovador";
        }
        if (effectiveGroups.contains("ANALISTA")) {
            return "analista";
        }
        return "operador";
    }

    private String stringValue(Object value) {
        return value == null ? null : value.toString();
    }

    private String loadProcessSvg(String processId) {
        if (!KogitoWorkflowRuntimeService.PROCESS_ID.equals(processId)) {
            throw new ResourceNotFoundException("Processo Kogito nao encontrado.");
        }
        try {
            ClassPathResource resource = new ClassPathResource("META-INF/processSVG/" + processId + ".svg");
            if (!resource.exists()) {
                throw new ResourceNotFoundException("SVG do processo Kogito nao encontrado.");
            }
            return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException ioException) {
            throw new IllegalStateException("Nao foi possivel carregar o SVG da cessao.", ioException);
        }
    }

    private String resolveHighlightedElementId(String currentStage) {
        if (currentStage == null || currentStage.isBlank() || "SEM_ETAPA_ATIVA".equals(currentStage)) {
            return null;
        }
        if ("ENCERRAR_CESSAO".equals(currentStage)) {
            return "end-cessao";
        }
        try {
            return KogitoWorkflowRuntimeService.nodeIdFor(EtapaCessaoNome.valueOf(currentStage));
        } catch (IllegalArgumentException illegalArgumentException) {
            return null;
        }
    }

    private String highlightStage(String svg, String highlightedElementId) {
        if (highlightedElementId == null || highlightedElementId.isBlank()) {
            return svg;
        }
        String markerStyles = """
                <style>
                  .kogito-process-svg-active rect,
                  .kogito-process-svg-active path,
                  .kogito-process-svg-active circle {
                    stroke: #dc2626 !important;
                    stroke-width: 4 !important;
                  }
                  .kogito-process-svg-active rect,
                  .kogito-process-svg-active circle {
                    fill: #fee2e2 !important;
                  }
                </style>
                """;
        String withStyles = svg.contains("</defs>")
                ? svg.replace("</defs>", markerStyles + "</defs>")
                : svg.replaceFirst(">", ">" + markerStyles);
        return withStyles.replace(
                "id=\"" + highlightedElementId + "\"",
                "id=\"" + highlightedElementId + "\" class=\"kogito-process-svg-active\""
        );
    }
}
