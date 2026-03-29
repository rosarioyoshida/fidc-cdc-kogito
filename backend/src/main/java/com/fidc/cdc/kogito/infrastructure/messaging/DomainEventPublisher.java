package com.fidc.cdc.kogito.infrastructure.messaging;

import com.fidc.cdc.kogito.application.process.KogitoProcessSnapshot;
import com.fidc.cdc.kogito.application.process.KogitoTaskSnapshot;
import com.fidc.cdc.kogito.application.process.KogitoWorkflowRuntimeService;
import com.fidc.cdc.kogito.domain.cessao.Cessao;
import com.fidc.cdc.kogito.domain.cessao.EtapaCessaoNome;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import org.kie.kogito.event.process.ProcessDefinitionDataEvent;
import org.kie.kogito.event.process.ProcessDefinitionEventBody;
import org.kie.kogito.event.process.ProcessInstanceNodeDataEvent;
import org.kie.kogito.event.process.ProcessInstanceNodeEventBody;
import org.kie.kogito.event.process.ProcessInstanceStateDataEvent;
import org.kie.kogito.event.process.ProcessInstanceStateEventBody;
import org.kie.kogito.event.process.ProcessInstanceVariableDataEvent;
import org.kie.kogito.event.process.ProcessInstanceVariableEventBody;
import org.kie.kogito.event.usertask.UserTaskInstanceAssignmentDataEvent;
import org.kie.kogito.event.usertask.UserTaskInstanceAssignmentEventBody;
import org.kie.kogito.event.usertask.UserTaskInstanceStateDataEvent;
import org.kie.kogito.event.usertask.UserTaskInstanceStateEventBody;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Publica eventos relacionados a domain event.
 *
 * <p>Este tipo pertence a camada de adaptadores tecnicos e integracoes de infraestrutura. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
@Component
public class DomainEventPublisher {
    private static final Set<String> KOGITO_ADDONS = Set.of(
            "persistence",
            "monitoring",
            "jobs-service",
            "task-console",
            "management-console",
            "process-management",
            "process-svg"
    );

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaTopicsProperties topicsProperties;
    private final String runtimeInternalServiceUrl;
    private final AtomicBoolean processDefinitionPublished = new AtomicBoolean();

    public DomainEventPublisher(
            KafkaTemplate<String, Object> kafkaTemplate,
            KafkaTopicsProperties topicsProperties,
            @Value("${fidc.runtime.internal-service-url}") String runtimeInternalServiceUrl
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicsProperties = topicsProperties;
        this.runtimeInternalServiceUrl = trimTrailingSlash(runtimeInternalServiceUrl);
    }

    public void publishProcessEvent(String businessKey, String eventType, Map<String, ?> attributes) {
        kafkaTemplate.send(
                topicsProperties.getProcess(),
                businessKey,
                new ProcessEventPayload("process", businessKey, eventType, OffsetDateTime.now(), attributes)
        );
    }

    public void publishTaskEvent(String businessKey, String eventType, Map<String, ?> attributes) {
        kafkaTemplate.send(
                topicsProperties.getTasks(),
                businessKey,
                new ProcessEventPayload("task", businessKey, eventType, OffsetDateTime.now(), attributes)
        );
    }

    public void publishAuditEvent(String businessKey, String eventType, Map<String, ?> attributes) {
        kafkaTemplate.send(
                topicsProperties.getAudit(),
                businessKey,
                new ProcessEventPayload("audit", businessKey, eventType, OffsetDateTime.now(), attributes)
        );
    }

    @EventListener(ApplicationReadyEvent.class)
    public void publishKogitoProcessDefinitionOnStartup() {
        publishKogitoProcessDefinitionIfNecessary();
    }

    public void publishKogitoProcessDefinitionIfNecessary() {
        if (!processDefinitionPublished.compareAndSet(false, true)) {
            return;
        }
        ProcessDefinitionEventBody body = ProcessDefinitionEventBody.builder()
                .setId(KogitoWorkflowRuntimeService.PROCESS_ID)
                .setName(KogitoWorkflowRuntimeService.PROCESS_NAME)
                .setVersion(KogitoWorkflowRuntimeService.PROCESS_VERSION)
                .setType(KogitoWorkflowRuntimeService.PROCESS_TYPE)
                .setRoles(Set.of("OPERADOR", "ANALISTA", "APROVADOR", "AUDITOR", "INTEGRACAO"))
                .setAddons(KOGITO_ADDONS)
                .setEndpoint(runtimeServiceBase("/" + KogitoWorkflowRuntimeService.PROCESS_ID))
                .setSource("fidc-cdc-kogito")
                .setDescription("Fluxo operacional de cessao de FIDC.")
                .setAnnotations(Set.of())
                .setMetadata(Map.of())
                .setNodes(List.of())
                .build();
        try {
            kafkaTemplate.send(
                    topicsProperties.getKogitoProcessDefinitions(),
                    KogitoWorkflowRuntimeService.PROCESS_ID,
                    new ProcessDefinitionDataEvent(body)
            ).get();
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Falha ao publicar o ProcessDefinitionEvent do Kogito.", interruptedException);
        } catch (ExecutionException executionException) {
            throw new IllegalStateException("Falha ao publicar o ProcessDefinitionEvent do Kogito.", executionException);
        }
    }

    public void publishKogitoProcessState(Cessao cessao, KogitoProcessSnapshot snapshot, String actorId, Integer eventType) {
        ProcessInstanceStateEventBody body = ProcessInstanceStateEventBody.create()
                .eventDate(new Date())
                .eventUser(actorId == null || actorId.isBlank() ? "system" : actorId)
                .eventType(eventType)
                .processId(snapshot.processId())
                .processVersion(snapshot.processVersion())
                .processType(snapshot.processType())
                .processInstanceId(snapshot.processInstanceId())
                .businessKey(snapshot.businessKey())
                .processName(snapshot.processName())
                .parentInstanceId(null)
                .rootProcessId(snapshot.processId())
                .rootProcessInstanceId(snapshot.processInstanceId())
                .state(snapshot.status())
                .roles("OPERADOR", "ANALISTA", "APROVADOR", "AUDITOR", "INTEGRACAO")
                .slaDueDate(null)
                .build();
        ProcessInstanceStateDataEvent event = new ProcessInstanceStateDataEvent(
                processRuntimeEndpoint(cessao),
                "",
                actorId == null || actorId.isBlank() ? "system" : actorId,
                processMetadata(snapshot),
                body
        );
        event.setKogitoAddons(String.join(",", KOGITO_ADDONS));
        event.setSource(URI.create(processRuntimeEndpoint(cessao)));
        event.setKogitoBusinessKey(snapshot.businessKey());
        kafkaTemplate.send(topicsProperties.getKogitoProcessInstances(), snapshot.processInstanceId(), event);
    }

    public void publishKogitoProcessVariables(Cessao cessao, KogitoProcessSnapshot snapshot) {
        publishProcessVariable(cessao, snapshot, "businessKey", cessao.getBusinessKey());
        publishProcessVariable(cessao, snapshot, "cedenteId", cessao.getCedenteId());
        publishProcessVariable(cessao, snapshot, "cessionariaId", cessao.getCessionariaId());
        publishProcessVariable(cessao, snapshot, "status", cessao.getStatus().name());
    }

    public void publishKogitoProcessNodeEntered(Cessao cessao, KogitoProcessSnapshot snapshot, KogitoTaskSnapshot task, String actorId) {
        if (task == null) {
            return;
        }
        EtapaCessaoNome etapa = task.etapaNome();
        ProcessInstanceNodeEventBody body = ProcessInstanceNodeEventBody.create()
                .eventDate(new Date())
                .eventUser(defaultActor(actorId))
                .eventType(ProcessInstanceNodeEventBody.EVENT_TYPE_ENTER)
                .processId(snapshot.processId())
                .processVersion(snapshot.processVersion())
                .processInstanceId(snapshot.processInstanceId())
                .nodeDefinitionId(KogitoWorkflowRuntimeService.nodeIdFor(etapa))
                .nodeName(KogitoWorkflowRuntimeService.nodeNameFor(etapa))
                .nodeType("HumanTaskNode")
                .nodeInstanceId(task.id())
                .workItemId(task.id())
                .data("referenceName", task.definitionId())
                .build();
        ProcessInstanceNodeDataEvent event = new ProcessInstanceNodeDataEvent(
                processRuntimeEndpoint(cessao),
                "",
                defaultActor(actorId),
                processMetadata(snapshot),
                body
        );
        event.setKogitoAddons(String.join(",", KOGITO_ADDONS));
        event.setSource(URI.create(processRuntimeEndpoint(cessao)));
        event.setKogitoBusinessKey(snapshot.businessKey());
        kafkaTemplate.send(topicsProperties.getKogitoProcessInstances(), snapshot.processInstanceId(), event);
    }

    public void publishKogitoProcessNodeExited(Cessao cessao, KogitoProcessSnapshot snapshot, KogitoTaskSnapshot task, String actorId) {
        if (task == null) {
            return;
        }
        EtapaCessaoNome etapa = task.etapaNome();
        ProcessInstanceNodeEventBody body = ProcessInstanceNodeEventBody.create()
                .eventDate(new Date())
                .eventUser(defaultActor(actorId))
                .eventType(ProcessInstanceNodeEventBody.EVENT_TYPE_EXIT)
                .processId(snapshot.processId())
                .processVersion(snapshot.processVersion())
                .processInstanceId(snapshot.processInstanceId())
                .nodeDefinitionId(KogitoWorkflowRuntimeService.nodeIdFor(etapa))
                .nodeName(KogitoWorkflowRuntimeService.nodeNameFor(etapa))
                .nodeType("HumanTaskNode")
                .nodeInstanceId(task.id())
                .workItemId(task.id())
                .data("referenceName", task.definitionId())
                .build();
        ProcessInstanceNodeDataEvent event = new ProcessInstanceNodeDataEvent(
                processRuntimeEndpoint(cessao),
                "",
                defaultActor(actorId),
                processMetadata(snapshot),
                body
        );
        event.setKogitoAddons(String.join(",", KOGITO_ADDONS));
        event.setSource(URI.create(processRuntimeEndpoint(cessao)));
        event.setKogitoBusinessKey(snapshot.businessKey());
        kafkaTemplate.send(topicsProperties.getKogitoProcessInstances(), snapshot.processInstanceId(), event);
    }

    public void publishKogitoUserTaskState(
            Cessao cessao,
            KogitoProcessSnapshot snapshot,
            KogitoTaskSnapshot task,
            String actorId,
            String state,
            String eventType
    ) {
        if (task == null) {
            return;
        }
        UserTaskInstanceStateEventBody body = UserTaskInstanceStateEventBody.create()
                .eventDate(new Date())
                .eventUser(actorId == null || actorId.isBlank() ? "system" : actorId)
                .userTaskDefinitionId(task.definitionId())
                .userTaskInstanceId(task.id())
                .userTaskName(task.name())
                .userTaskDescription(task.description())
                .userTaskPriority("Medium")
                .userTaskReferenceName(task.definitionId())
                .state(state)
                .actualOwner(task.actualOwner())
                .externalReferenceId(task.externalReferenceId())
                .eventType(eventType)
                .processInstanceId(snapshot.processInstanceId())
                .slaDueDate(null)
                .build();
        UserTaskInstanceStateDataEvent event = new UserTaskInstanceStateDataEvent(
                taskRuntimeEndpoint(cessao),
                "",
                actorId == null || actorId.isBlank() ? "system" : actorId,
                userTaskMetadata(snapshot, task, state),
                body
        );
        event.setKogitoAddons(String.join(",", KOGITO_ADDONS));
        event.setSource(URI.create(taskRuntimeEndpoint(cessao)));
        event.setKogitoBusinessKey(snapshot.businessKey());
        kafkaTemplate.send(topicsProperties.getKogitoUserTaskInstances(), task.id(), event);
    }

    public void publishKogitoUserTaskAssignments(Cessao cessao, KogitoProcessSnapshot snapshot, KogitoTaskSnapshot task, String actorId) {
        if (task == null) {
            return;
        }
        EtapaCessaoNome etapa = task.etapaNome();
        publishTaskAssignment(
                cessao,
                snapshot,
                task,
                actorId,
                "USER_GROUPS",
                KogitoWorkflowRuntimeService.potentialGroupFor(etapa)
        );
        publishTaskAssignment(
                cessao,
                snapshot,
                task,
                actorId,
                "USER_OWNERS",
                KogitoWorkflowRuntimeService.consoleAdminUsers().toArray(String[]::new)
        );
        publishTaskAssignment(
                cessao,
                snapshot,
                task,
                actorId,
                "ADMIN_GROUPS",
                KogitoWorkflowRuntimeService.adminGroupFor(etapa)
        );
    }

    private Map<String, Object> processMetadata(KogitoProcessSnapshot snapshot) {
        return Map.of(
                "kogito.processinstance.id", snapshot.processInstanceId(),
                "kogito.process.version", snapshot.processVersion(),
                "kogito.processinstance.parentInstanceId", "",
                "kogito.processinstance.rootInstanceId", snapshot.processInstanceId(),
                "kogito.process.id", snapshot.processId(),
                "kogito.processinstance.rootProcessId", snapshot.processId(),
                "kogito.processinstance.state", String.valueOf(snapshot.status()),
                "kogito.process.type", snapshot.processType()
        );
    }

    private void publishProcessVariable(Cessao cessao, KogitoProcessSnapshot snapshot, String variableName, Object variableValue) {
        ProcessInstanceVariableEventBody body = ProcessInstanceVariableEventBody.create()
                .eventDate(new Date())
                .eventUser("system")
                .processId(snapshot.processId())
                .processVersion(snapshot.processVersion())
                .processInstanceId(snapshot.processInstanceId())
                .nodeContainerDefinitionId(snapshot.processId())
                .nodeContainerInstanceId(snapshot.processInstanceId())
                .variableId(variableName)
                .variableName(variableName)
                .variableValue(variableValue)
                .build();
        ProcessInstanceVariableDataEvent event = new ProcessInstanceVariableDataEvent(
                processRuntimeEndpoint(cessao),
                "",
                "system",
                processMetadata(snapshot),
                body
        );
        event.setKogitoAddons(String.join(",", KOGITO_ADDONS));
        event.setSource(URI.create(processRuntimeEndpoint(cessao)));
        event.setKogitoBusinessKey(snapshot.businessKey());
        event.setKogitoVariableName(variableName);
        kafkaTemplate.send(topicsProperties.getKogitoProcessInstances(), snapshot.processInstanceId(), event);
    }

    private void publishTaskAssignment(
            Cessao cessao,
            KogitoProcessSnapshot snapshot,
            KogitoTaskSnapshot task,
            String actorId,
            String assignmentType,
            String... users
    ) {
        if (users == null || users.length == 0) {
            return;
        }
        UserTaskInstanceAssignmentEventBody body = UserTaskInstanceAssignmentEventBody.create()
                .eventDate(new Date())
                .eventUser(defaultActor(actorId))
                .userTaskDefinitionId(task.definitionId())
                .userTaskInstanceId(task.id())
                .userTaskName(task.name())
                .assignmentType(assignmentType)
                .users(users)
                .eventType("ASSIGNED")
                .build();
        UserTaskInstanceAssignmentDataEvent event = new UserTaskInstanceAssignmentDataEvent(
                taskRuntimeEndpoint(cessao),
                "",
                defaultActor(actorId),
                userTaskMetadata(snapshot, task, task.state()),
                body
        );
        event.setKogitoAddons(String.join(",", KOGITO_ADDONS));
        event.setSource(URI.create(taskRuntimeEndpoint(cessao)));
        event.setKogitoBusinessKey(snapshot.businessKey());
        kafkaTemplate.send(topicsProperties.getKogitoUserTaskInstances(), task.id(), event);
    }

    private Map<String, Object> userTaskMetadata(KogitoProcessSnapshot snapshot, KogitoTaskSnapshot task, String state) {
        return Map.ofEntries(
                Map.entry("kogito.usertaskinstance.id", task.id()),
                Map.entry("kogito.usertaskinstance.state", state),
                Map.entry("kogito.processinstance.id", snapshot.processInstanceId()),
                Map.entry("kogito.process.version", snapshot.processVersion()),
                Map.entry("kogito.processinstance.parentInstanceId", ""),
                Map.entry("kogito.processinstance.rootInstanceId", snapshot.processInstanceId()),
                Map.entry("kogito.process.id", snapshot.processId()),
                Map.entry("kogito.processinstance.rootProcessId", snapshot.processId()),
                Map.entry("kogito.processinstance.state", String.valueOf(snapshot.status())),
                Map.entry("kogito.process.type", snapshot.processType()),
                Map.entry("kogito.usertaskinstance.referenceId", task.definitionId())
        );
    }

    private String defaultActor(String actorId) {
        return actorId == null || actorId.isBlank() ? "system" : actorId;
    }

    private String processRuntimeEndpoint(Cessao cessao) {
        return runtimeServiceBase("/" + KogitoWorkflowRuntimeService.PROCESS_ID + "/" + cessao.getBusinessKey());
    }

    private String taskRuntimeEndpoint(Cessao cessao) {
        return runtimeServiceBase("/" + KogitoWorkflowRuntimeService.PROCESS_ID + "/" + cessao.getBusinessKey());
    }

    private String runtimeServiceBase(String path) {
        return runtimeInternalServiceUrl + path;
    }

    private String trimTrailingSlash(String url) {
        if (url == null || url.isBlank()) {
            return "http://localhost:8080";
        }
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }
}
