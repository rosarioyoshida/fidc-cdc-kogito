package com.fidc.cdc.kogito.application.process;

import com.fidc.cdc.kogito.api.cessao.CessaoRequest;
import com.fidc.cdc.kogito.api.error.BusinessConflictException;
import com.fidc.cdc.kogito.api.error.ResourceNotFoundException;
import com.fidc.cdc.kogito.domain.cessao.EtapaCessaoNome;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.kie.kogito.process.bpmn2.BpmnProcess;
import org.kie.kogito.process.bpmn2.BpmnVariables;
import org.kie.kogito.process.ProcessInstance;
import org.kie.kogito.process.Signal;
import org.kie.kogito.process.WorkItem;
import org.kie.kogito.usertask.UserTask;
import org.springframework.stereotype.Service;

@Service
public class KogitoWorkflowRuntimeService {

    public static final String PROCESS_ID = "controle-cessao";
    public static final String PROCESS_NAME = "Controle de Cessao";
    public static final String PROCESS_VERSION = "1.0";
    public static final String PROCESS_TYPE = "BPMN";
    public static final String REGISTRADORA_CONFIRMADA_SIGNAL = "registradora-confirmada";
    private static final Map<EtapaCessaoNome, String> NODE_IDS = buildNodeIds();
    private static final Map<EtapaCessaoNome, String> NODE_NAMES = buildNodeNames();
    private static final Map<EtapaCessaoNome, String> POTENTIAL_GROUPS = buildPotentialGroups();
    private static final Map<EtapaCessaoNome, String> ADMIN_GROUPS = buildAdminGroups();
    private static final Set<String> CONSOLE_ADMIN_USERS = Set.of("kogito-admin");

    private final BpmnProcess process;

    public KogitoWorkflowRuntimeService(BpmnProcess process) {
        this.process = process;
    }

    public KogitoProcessSnapshot startProcess(CessaoRequest request) {
        BpmnVariables variables = BpmnVariables.create(Map.of(
                "businessKey", request.businessKey(),
                "cedenteId", request.cedenteId(),
                "cessionariaId", request.cessionariaId()
        ));
        ProcessInstance<BpmnVariables> instance = process.createInstance(request.businessKey(), variables);
        instance.start();
        return toSnapshot(instance);
    }

    public KogitoProcessSnapshot getProcess(String processInstanceId) {
        return process.instances()
                .findById(processInstanceId)
                .map(this::toSnapshot)
                .orElseThrow(() -> new ResourceNotFoundException("Instancia do processo Kogito nao encontrada."));
    }

    public KogitoProcessSnapshot getProcessByBusinessKey(String businessKey) {
        return process.instances()
                .findByBusinessKey(businessKey)
                .map(this::toSnapshot)
                .orElseThrow(() -> new ResourceNotFoundException("Instancia do processo Kogito nao encontrada."));
    }

    public KogitoProcessSnapshot completeActiveHumanTask(
            String businessKey,
            EtapaCessaoNome etapaEsperada,
            String actorId,
            String justificativa
    ) {
        ProcessInstance<BpmnVariables> instance = process.instances()
                .findByBusinessKey(businessKey)
                .orElseThrow(() -> new ResourceNotFoundException("Instancia do processo Kogito nao encontrada."));

        WorkItem workItem = instance.workItems()
                .stream()
                .findFirst()
                .orElseThrow(() -> new BusinessConflictException("Nao existe tarefa humana ativa para a cessao informada."));

        String definitionId = resolveTaskDefinitionId(workItem);
        if (!etapaEsperada.name().equals(definitionId)) {
            throw new BusinessConflictException("A etapa ativa no runtime nao corresponde a etapa solicitada.");
        }

        instance.completeWorkItem(workItem.getId(), Map.of(
                "resultado", "SUCESSO",
                "justificativa", justificativa == null ? "" : justificativa,
                "responsavelId", actorId
        ));

        return toSnapshot(instance);
    }

    public KogitoProcessSnapshot confirmAwaitingRegistradora(String processInstanceId, String referenceId) {
        ProcessInstance<BpmnVariables> instance = process.instances()
                .findById(processInstanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Instancia do processo Kogito nao encontrada."));
        instance.send(new RuntimeSignal<>(REGISTRADORA_CONFIRMADA_SIGNAL, Map.of(), referenceId));
        return toSnapshot(instance);
    }

    public KogitoProcessSnapshot abortProcess(String processInstanceId) {
        ProcessInstance<BpmnVariables> instance = process.instances()
                .findById(processInstanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Instancia do processo Kogito nao encontrada."));
        instance.abort();
        return toSnapshot(instance);
    }

    private KogitoProcessSnapshot toSnapshot(ProcessInstance<BpmnVariables> instance) {
        KogitoTaskSnapshot activeTask = instance.workItems()
                .stream()
                .findFirst()
                .map(this::toTaskSnapshot)
                .orElse(null);

        return new KogitoProcessSnapshot(
                process.id(),
                process.name(),
                defaultProcessVersion(process.version()),
                process.type(),
                instance.id(),
                instance.businessKey(),
                instance.status(),
                activeTask
        );
    }

    private KogitoTaskSnapshot toTaskSnapshot(WorkItem workItem) {
        Map<String, Object> parameters = workItem.getParameters();
        return new KogitoTaskSnapshot(
                workItem.getId(),
                resolveTaskDefinitionId(workItem),
                workItem.getName(),
                stringValue(parameters.get("Description")),
                workItem.getPhaseStatus(),
                stringValue(parameters.get("ActorId")),
                stringValue(parameters.get("GroupId")),
                stringValue(parameters.get("BusinessAdministratorGroupId")),
                workItem.getExternalReferenceId()
        );
    }

    private String resolveTaskDefinitionId(WorkItem workItem) {
        Object value = workItem.getParameters().get("TaskName");
        if (value != null) {
            return value.toString();
        }
        throw new IllegalStateException("Tarefa humana sem TaskName configurado no BPMN.");
    }

    private String defaultProcessVersion(String version) {
        return version == null || version.isBlank() ? PROCESS_VERSION : version;
    }

    private String stringValue(Object value) {
        return value == null ? null : value.toString();
    }

    public static String nodeIdFor(EtapaCessaoNome etapa) {
        return NODE_IDS.get(etapa);
    }

    public static String nodeNameFor(EtapaCessaoNome etapa) {
        return NODE_NAMES.get(etapa);
    }

    public static String potentialGroupFor(EtapaCessaoNome etapa) {
        return POTENTIAL_GROUPS.get(etapa);
    }

    public static String adminGroupFor(EtapaCessaoNome etapa) {
        return ADMIN_GROUPS.get(etapa);
    }

    public static Set<String> consoleAdminUsers() {
        return CONSOLE_ADMIN_USERS;
    }

    private static Map<EtapaCessaoNome, String> buildNodeIds() {
        Map<EtapaCessaoNome, String> mapping = new EnumMap<>(EtapaCessaoNome.class);
        mapping.put(EtapaCessaoNome.IMPORTAR_CARTEIRA, "task-01");
        mapping.put(EtapaCessaoNome.VALIDAR_CEDENTE, "task-02");
        mapping.put(EtapaCessaoNome.ANALISAR_ELEGIBILIDADE, "task-03");
        mapping.put(EtapaCessaoNome.COLETAR_TERMO_ACEITE, "task-10");
        mapping.put(EtapaCessaoNome.VALIDAR_LASTROS, "task-11");
        mapping.put(EtapaCessaoNome.AUTORIZAR_PAGAMENTO, "task-12");
        return mapping;
    }

    private static Map<EtapaCessaoNome, String> buildNodeNames() {
        Map<EtapaCessaoNome, String> mapping = new EnumMap<>(EtapaCessaoNome.class);
        mapping.put(EtapaCessaoNome.IMPORTAR_CARTEIRA, "01 Importar carteira");
        mapping.put(EtapaCessaoNome.VALIDAR_CEDENTE, "02 Validar cedente");
        mapping.put(EtapaCessaoNome.ANALISAR_ELEGIBILIDADE, "03 Analisar elegibilidade");
        mapping.put(EtapaCessaoNome.COLETAR_TERMO_ACEITE, "10 Coletar termo de aceite");
        mapping.put(EtapaCessaoNome.VALIDAR_LASTROS, "11 Validar lastros");
        mapping.put(EtapaCessaoNome.AUTORIZAR_PAGAMENTO, "12 Autorizar pagamento");
        return mapping;
    }

    private static Map<EtapaCessaoNome, String> buildPotentialGroups() {
        Map<EtapaCessaoNome, String> mapping = new EnumMap<>(EtapaCessaoNome.class);
        mapping.put(EtapaCessaoNome.IMPORTAR_CARTEIRA, "OPERADOR");
        mapping.put(EtapaCessaoNome.VALIDAR_CEDENTE, "OPERADOR");
        mapping.put(EtapaCessaoNome.ANALISAR_ELEGIBILIDADE, "ANALISTA");
        mapping.put(EtapaCessaoNome.COLETAR_TERMO_ACEITE, "ANALISTA");
        mapping.put(EtapaCessaoNome.VALIDAR_LASTROS, "ANALISTA");
        mapping.put(EtapaCessaoNome.AUTORIZAR_PAGAMENTO, "APROVADOR");
        return mapping;
    }

    private static Map<EtapaCessaoNome, String> buildAdminGroups() {
        Map<EtapaCessaoNome, String> mapping = new EnumMap<>(EtapaCessaoNome.class);
        mapping.put(EtapaCessaoNome.IMPORTAR_CARTEIRA, "AUDITOR");
        mapping.put(EtapaCessaoNome.VALIDAR_CEDENTE, "AUDITOR");
        mapping.put(EtapaCessaoNome.ANALISAR_ELEGIBILIDADE, "AUDITOR");
        mapping.put(EtapaCessaoNome.COLETAR_TERMO_ACEITE, "AUDITOR");
        mapping.put(EtapaCessaoNome.VALIDAR_LASTROS, "AUDITOR");
        mapping.put(EtapaCessaoNome.AUTORIZAR_PAGAMENTO, "AUDITOR");
        return mapping;
    }

    private record RuntimeSignal<T>(String channel, T payload, String referenceId) implements Signal<T> {
    }
}
