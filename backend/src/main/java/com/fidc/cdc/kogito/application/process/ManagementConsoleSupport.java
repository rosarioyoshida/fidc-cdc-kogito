package com.fidc.cdc.kogito.application.process;

import com.fidc.cdc.kogito.api.error.ResourceNotFoundException;
import com.fidc.cdc.kogito.application.readmodel.CessaoReadModelDocument;
import com.fidc.cdc.kogito.application.readmodel.CessaoReadModelRepository;
import com.fidc.cdc.kogito.domain.cessao.Cessao;
import com.fidc.cdc.kogito.domain.cessao.CessaoRepository;
import com.fidc.cdc.kogito.domain.cessao.EtapaCessao;
import com.fidc.cdc.kogito.domain.cessao.EtapaCessaoNome;
import com.fidc.cdc.kogito.domain.cessao.EtapaCessaoStatus;
import com.fidc.cdc.kogito.observability.ProcessMetricsService;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.core.io.ClassPathResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ManagementConsoleSupport {

    private final CessaoRepository cessaoRepository;
    private final CessaoReadModelRepository cessaoReadModelRepository;
    private final TaskAssignmentService taskAssignmentService;
    private final KogitoWorkflowRuntimeService workflowRuntimeService;
    private final ProcessMetricsService processMetricsService;
    private final String managementConsoleUrl;
    private final String dataIndexUrl;
    private final String jobsServiceUrl;

    public ManagementConsoleSupport(
            CessaoRepository cessaoRepository,
            CessaoReadModelRepository cessaoReadModelRepository,
            TaskAssignmentService taskAssignmentService,
            KogitoWorkflowRuntimeService workflowRuntimeService,
            ProcessMetricsService processMetricsService,
            @Value("${fidc.consoles.management-console-url}") String managementConsoleUrl,
            @Value("${fidc.data-index.url}") String dataIndexUrl,
            @Value("${fidc.jobs.public-service-url}") String jobsServiceUrl
    ) {
        this.cessaoRepository = cessaoRepository;
        this.cessaoReadModelRepository = cessaoReadModelRepository;
        this.taskAssignmentService = taskAssignmentService;
        this.workflowRuntimeService = workflowRuntimeService;
        this.processMetricsService = processMetricsService;
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
        StageDescriptor currentStageDescriptor = resolveStageDescriptor(cessao);
        String currentStage = currentStageDescriptor.stageName();
        Optional<CessaoReadModelDocument> readModel = cessaoReadModelRepository.findById(businessKey);
        boolean processSvgAvailable = processSvgAvailable(workflowRuntimeService.PROCESS_ID);
        boolean processSvgAuthorized = true;
        String processSvgAvailabilityReason = processSvgAvailabilityReason(processSvgAvailable);

        boolean humanTaskPending = currentStageDescriptor.activeStage().isPresent()
                && taskAssignmentService.isHumanTaskStage(currentStageDescriptor.activeStage().get().getNomeEtapa());
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

        processMetricsService.registerConsoleAccess(
                "management-console",
                processSvgAvailable ? "process-svg-ready" : "process-svg-missing"
        );
        if ("UNRESOLVED".equals(currentStageDescriptor.source())) {
            processMetricsService.registerConsoleAccess("management-console", "process-svg-no-current-stage");
        }
        if (currentStageDescriptor.processEndedWithoutActiveStage()) {
            processMetricsService.registerConsoleAccess("management-console", "process-svg-last-completed-stage");
        }

        return new ManagementConsoleContext(
                businessKey,
                cessao.getWorkflowInstanceId(),
                cessao.getStatus().name(),
                currentStage,
                currentStageDescriptor.stageLabel(),
                currentStageDescriptor.source(),
                currentStageDescriptor.processEndedWithoutActiveStage(),
                humanTaskPending,
                waitingForTimerJob,
                processSvgAvailable,
                processSvgAuthorized,
                processSvgAvailabilityReason,
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

    private StageDescriptor resolveStageDescriptor(Cessao cessao) {
        Optional<EtapaCessao> currentStageEntity = cessao.getEtapas().stream()
                .filter(etapa -> etapa.getStatusEtapa() == EtapaCessaoStatus.EM_EXECUCAO)
                .findFirst();
        if (currentStageEntity.isPresent()) {
            EtapaCessao etapa = currentStageEntity.get();
            return new StageDescriptor(
                    etapa.getNomeEtapa().name(),
                    stageLabelFor(etapa.getNomeEtapa()),
                    "ACTIVE_STAGE",
                    false,
                    Optional.of(etapa)
            );
        }

        Optional<EtapaCessao> lastCompletedStage = findLastCompletedStage(cessao);
        if (lastCompletedStage.isPresent()) {
            EtapaCessao etapa = lastCompletedStage.get();
            return new StageDescriptor(
                    etapa.getNomeEtapa().name(),
                    stageLabelFor(etapa.getNomeEtapa()),
                    "LAST_COMPLETED_STAGE",
                    true,
                    Optional.empty()
            );
        }

        return new StageDescriptor(
                "SEM_ETAPA_ATIVA",
                "Etapa atual nao determinada",
                "UNRESOLVED",
                false,
                Optional.empty()
        );
    }

    private Optional<EtapaCessao> findLastCompletedStage(Cessao cessao) {
        return cessao.getEtapas().stream()
                .filter(etapa -> etapa.getStatusEtapa() == EtapaCessaoStatus.CONCLUIDA)
                .max(Comparator.comparingInt(EtapaCessao::getOrdem));
    }

    private boolean processSvgAvailable(String processId) {
        return new ClassPathResource("META-INF/processSVG/" + processId + ".svg").exists();
    }

    private String processSvgAvailabilityReason(boolean processSvgAvailable) {
        return processSvgAvailable ? "available" : "svg-missing";
    }

    private String stageLabelFor(EtapaCessaoNome etapa) {
        String nodeName = KogitoWorkflowRuntimeService.nodeNameFor(etapa);
        return nodeName != null ? nodeName : etapa.getDisplayName();
    }

    private record StageDescriptor(
            String stageName,
            String stageLabel,
            String source,
            boolean processEndedWithoutActiveStage,
            Optional<EtapaCessao> activeStage
    ) {
    }
}
