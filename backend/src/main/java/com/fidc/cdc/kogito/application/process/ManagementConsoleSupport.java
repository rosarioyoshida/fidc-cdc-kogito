package com.fidc.cdc.kogito.application.process;

import com.fidc.cdc.kogito.api.error.ResourceNotFoundException;
import com.fidc.cdc.kogito.application.readmodel.CessaoReadModelDocument;
import com.fidc.cdc.kogito.application.readmodel.CessaoReadModelRepository;
import com.fidc.cdc.kogito.domain.cessao.Cessao;
import com.fidc.cdc.kogito.domain.cessao.CessaoRepository;
import com.fidc.cdc.kogito.domain.cessao.EtapaCessaoNome;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ManagementConsoleSupport {

    private final CessaoRepository cessaoRepository;
    private final CessaoReadModelRepository cessaoReadModelRepository;
    private final TaskAssignmentService taskAssignmentService;
    private final KogitoWorkflowRuntimeService workflowRuntimeService;
    private final String managementConsoleUrl;
    private final String dataIndexUrl;
    private final String jobsServiceUrl;

    public ManagementConsoleSupport(
            CessaoRepository cessaoRepository,
            CessaoReadModelRepository cessaoReadModelRepository,
            TaskAssignmentService taskAssignmentService,
            KogitoWorkflowRuntimeService workflowRuntimeService,
            @Value("${fidc.consoles.management-console-url}") String managementConsoleUrl,
            @Value("${fidc.data-index.url}") String dataIndexUrl,
            @Value("${fidc.jobs.public-service-url}") String jobsServiceUrl
    ) {
        this.cessaoRepository = cessaoRepository;
        this.cessaoReadModelRepository = cessaoReadModelRepository;
        this.taskAssignmentService = taskAssignmentService;
        this.workflowRuntimeService = workflowRuntimeService;
        this.managementConsoleUrl = managementConsoleUrl;
        this.dataIndexUrl = dataIndexUrl;
        this.jobsServiceUrl = jobsServiceUrl;
    }

    @Transactional(readOnly = true)
    public ManagementConsoleContext describeProcessContext(String businessKey) {
        Cessao cessao = cessaoRepository.findByBusinessKey(businessKey)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cessao nao encontrada para consulta de management context."
                ));
        KogitoProcessSnapshot runtimeSnapshot = workflowRuntimeService.getProcessByBusinessKey(businessKey);
        String currentStage = runtimeSnapshot.activeTask() != null
                ? runtimeSnapshot.activeTask().etapaNome().name()
                : cessao.getEtapas().stream()
                        .filter(etapa -> etapa.getStatusEtapa().name().equals("EM_EXECUCAO"))
                        .map(etapa -> etapa.getNomeEtapa().name())
                        .findFirst()
                        .orElse("SEM_ETAPA_ATIVA");
        Optional<CessaoReadModelDocument> readModel = cessaoReadModelRepository.findById(businessKey);

        boolean humanTaskPending = runtimeSnapshot.activeTask() != null
                && taskAssignmentService.isHumanTaskStage(runtimeSnapshot.activeTask().etapaNome());
        boolean waitingForTimerJob = EtapaCessaoNome.AGUARDAR_CONFIRMACAO_REGISTRADORA.name().equals(currentStage);

        List<String> availableAdminActions = new ArrayList<>();
        availableAdminActions.add("VIEW_PROCESS_INSTANCE");
        availableAdminActions.add("VIEW_DATA_INDEX_STATE");
        if (humanTaskPending) {
            availableAdminActions.add("INSPECT_HUMAN_TASK");
            availableAdminActions.add("REASSIGN_HUMAN_TASK");
        }
        if (waitingForTimerJob) {
            availableAdminActions.add("INSPECT_TIMER_JOB");
            availableAdminActions.add("RETRY_TIMER_JOB");
        }
        if (!"CONCLUIDA".equals(cessao.getStatus().name())) {
            availableAdminActions.add("CANCEL_PROCESS_INSTANCE");
        }

        return new ManagementConsoleContext(
                businessKey,
                cessao.getWorkflowInstanceId(),
                runtimeSnapshot.isCompleted() ? "CONCLUIDA" : cessao.getStatus().name(),
                currentStage,
                humanTaskPending,
                waitingForTimerJob,
                readModel.isPresent(),
                readModel.map(CessaoReadModelDocument::getUltimoEvento).orElse(null),
                readModel.map(CessaoReadModelDocument::getUltimaAtualizacao)
                        .map(instant -> OffsetDateTime.ofInstant(instant, ZoneOffset.UTC))
                        .orElse(null),
                List.copyOf(availableAdminActions),
                managementConsoleUrl,
                dataIndexUrl,
                jobsServiceUrl
        );
    }
}
