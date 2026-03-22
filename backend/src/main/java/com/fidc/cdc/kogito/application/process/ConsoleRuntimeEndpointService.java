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

    private static final List<String> DEFAULT_ALLOWED_ORIGINS = List.of(
            "http://localhost:3000",
            "http://localhost:8280",
            "http://localhost:8380"
    );

    private final KogitoWorkflowRuntimeService workflowRuntimeService;
    private final CessaoProcessService cessaoProcessService;

    public ConsoleRuntimeEndpointService(
            KogitoWorkflowRuntimeService workflowRuntimeService,
            CessaoProcessService cessaoProcessService
    ) {
        this.workflowRuntimeService = workflowRuntimeService;
        this.cessaoProcessService = cessaoProcessService;
    }

    public String processDiagram(String processId, String processInstanceId) {
        KogitoProcessSnapshot snapshot = resolveProcess(processId, processInstanceId);
        String activeStage = snapshot.activeTask() == null
                ? "PROCESSO CONCLUIDO"
                : KogitoWorkflowRuntimeService.nodeNameFor(snapshot.activeTask().etapaNome()).toUpperCase();
        String status = snapshot.isCompleted() ? "CONCLUIDA" : "EM ANDAMENTO";
        return """
                <svg xmlns="http://www.w3.org/2000/svg" width="1200" height="260" viewBox="0 0 1200 260" role="img" aria-labelledby="title desc">
                  <title id="title">Processo %s</title>
                  <desc id="desc">Instancia %s da cessao %s em %s</desc>
                  <rect x="0" y="0" width="1200" height="260" fill="#f8fafc"/>
                  <rect x="40" y="48" width="260" height="128" rx="20" fill="#dbeafe" stroke="#2563eb" stroke-width="3"/>
                  <rect x="360" y="48" width="360" height="128" rx="20" fill="#ecfccb" stroke="#65a30d" stroke-width="3"/>
                  <rect x="780" y="48" width="380" height="128" rx="20" fill="#fef3c7" stroke="#d97706" stroke-width="3"/>
                  <line x1="300" y1="112" x2="360" y2="112" stroke="#64748b" stroke-width="6"/>
                  <line x1="720" y1="112" x2="780" y2="112" stroke="#64748b" stroke-width="6"/>
                  <text x="70" y="88" font-size="20" font-family="Arial, sans-serif" fill="#1e3a8a">Processo</text>
                  <text x="70" y="118" font-size="28" font-family="Arial, sans-serif" fill="#0f172a">%s</text>
                  <text x="70" y="150" font-size="16" font-family="Arial, sans-serif" fill="#334155">Instancia %s</text>
                  <text x="390" y="88" font-size="20" font-family="Arial, sans-serif" fill="#3f6212">Business Key</text>
                  <text x="390" y="118" font-size="28" font-family="Arial, sans-serif" fill="#1f2937">%s</text>
                  <text x="390" y="150" font-size="16" font-family="Arial, sans-serif" fill="#334155">Status %s</text>
                  <text x="810" y="88" font-size="20" font-family="Arial, sans-serif" fill="#9a3412">Etapa ativa</text>
                  <text x="810" y="118" font-size="26" font-family="Arial, sans-serif" fill="#1f2937">%s</text>
                  <text x="810" y="150" font-size="16" font-family="Arial, sans-serif" fill="#334155">Runtime Kogito materializado para o console</text>
                </svg>
                """.formatted(
                xml(snapshot.processName()),
                xml(snapshot.processInstanceId()),
                xml(snapshot.businessKey()),
                xml(status),
                xml(snapshot.processName()),
                xml(snapshot.processInstanceId()),
                xml(snapshot.businessKey()),
                xml(status),
                xml(activeStage)
        );
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

    public List<String> allowedOrigins() {
        return DEFAULT_ALLOWED_ORIGINS;
    }

    private KogitoProcessSnapshot resolveProcess(String processId, String processInstanceId) {
        if (!KogitoWorkflowRuntimeService.PROCESS_ID.equals(processId)) {
            throw new ResourceNotFoundException("Processo Kogito nao encontrado.");
        }
        return workflowRuntimeService.getProcess(processInstanceId);
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

    private String xml(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}
