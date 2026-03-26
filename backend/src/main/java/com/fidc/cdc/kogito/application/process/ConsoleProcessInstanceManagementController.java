package com.fidc.cdc.kogito.application.process;

import com.fidc.cdc.kogito.api.error.ResourceNotFoundException;
import com.fidc.cdc.kogito.application.cessao.CessaoEventPublisher;
import com.fidc.cdc.kogito.domain.cessao.Cessao;
import com.fidc.cdc.kogito.domain.cessao.CessaoRepository;
import com.fidc.cdc.kogito.domain.cessao.CessaoStatus;
import com.fidc.cdc.kogito.domain.cessao.EtapaCessaoStatus;
import java.time.OffsetDateTime;
import org.kie.kogito.Application;
import org.kie.kogito.Model;
import org.kie.kogito.process.Processes;
import org.kie.kogito.process.ProcessInstance;
import org.kie.kogito.process.management.ProcessInstanceManagementRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

public class ConsoleProcessInstanceManagementController extends ProcessInstanceManagementRestController {

    private final KogitoWorkflowRuntimeService workflowRuntimeService;
    private final CessaoRepository cessaoRepository;
    private final CessaoEventPublisher cessaoEventPublisher;

    public ConsoleProcessInstanceManagementController(
            Processes processes,
            Application application,
            KogitoWorkflowRuntimeService workflowRuntimeService,
            CessaoRepository cessaoRepository,
            CessaoEventPublisher cessaoEventPublisher
    ) {
        super(processes, application);
        this.workflowRuntimeService = workflowRuntimeService;
        this.cessaoRepository = cessaoRepository;
        this.cessaoEventPublisher = cessaoEventPublisher;
    }

    @Override
    public <R> ResponseEntity buildOkResponse(R body) {
        if (body == null || body instanceof Model) {
            return ResponseEntity.ok().build();
        }
        return super.buildOkResponse(body);
    }

    @Override
    @Transactional
    public ResponseEntity cancelProcessInstanceId(String processId, String processInstanceId) {
        KogitoProcessSnapshot beforeAbort = workflowRuntimeService.getProcess(processInstanceId);
        if (!beforeAbort.processId().equals(processId)) {
            return badRequestResponse("Process id nao corresponde a instancia informada.");
        }

        workflowRuntimeService.abortProcess(processInstanceId);

        Cessao cessao = cessaoRepository.findByWorkflowInstanceId(processInstanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Cessao nao encontrada para abortar o processo."));

        OffsetDateTime now = OffsetDateTime.now();
        cessao.setStatus(CessaoStatus.FALHA);
        cessao.setDataEncerramento(now);
        cessao.getEtapas().stream()
                .filter(etapa -> etapa.getStatusEtapa() == EtapaCessaoStatus.EM_EXECUCAO)
                .forEach(etapa -> {
                    etapa.setStatusEtapa(EtapaCessaoStatus.CANCELADA);
                    etapa.setConcluidaEm(now);
                    etapa.setResultado("ABORTED");
                });

        cessaoRepository.save(cessao);

        KogitoProcessSnapshot abortedSnapshot = new KogitoProcessSnapshot(
                beforeAbort.processId(),
                beforeAbort.processName(),
                beforeAbort.processVersion(),
                beforeAbort.processType(),
                beforeAbort.processInstanceId(),
                beforeAbort.businessKey(),
                ProcessInstance.STATE_ABORTED,
                null
        );
        cessaoEventPublisher.publishProcessAborted(
                cessao,
                abortedSnapshot,
                beforeAbort.activeTask(),
                resolveActor()
        );
        return ResponseEntity.ok().build();
    }

    private String resolveActor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getName() == null) {
            return "system";
        }
        return authentication.getName();
    }
}
