package com.fidc.cdc.kogito.application.cessao;

import com.fidc.cdc.kogito.application.process.KogitoProcessSnapshot;
import com.fidc.cdc.kogito.application.process.KogitoTaskSnapshot;
import com.fidc.cdc.kogito.application.readmodel.CessaoReadModelProjector;
import com.fidc.cdc.kogito.domain.cessao.Cessao;
import com.fidc.cdc.kogito.domain.cessao.EtapaCessao;
import com.fidc.cdc.kogito.infrastructure.messaging.DomainEventPublisher;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class CessaoEventPublisher {

    private final DomainEventPublisher domainEventPublisher;
    private final CessaoReadModelProjector readModelProjector;

    public CessaoEventPublisher(
            DomainEventPublisher domainEventPublisher,
            CessaoReadModelProjector readModelProjector
    ) {
        this.domainEventPublisher = domainEventPublisher;
        this.readModelProjector = readModelProjector;
    }

    public void publishCessaoCreated(Cessao cessao, KogitoProcessSnapshot snapshot) {
        domainEventPublisher.publishKogitoProcessDefinitionIfNecessary();
        domainEventPublisher.publishKogitoProcessVariables(cessao, snapshot);
        domainEventPublisher.publishKogitoProcessState(
                cessao,
                snapshot,
                "system",
                org.kie.kogito.event.process.ProcessInstanceStateEventBody.EVENT_TYPE_STARTED
        );
        domainEventPublisher.publishProcessEvent(
                cessao.getBusinessKey(),
                "CESSAO_CRIADA",
                Map.of(
                        "businessKey", cessao.getBusinessKey(),
                        "status", cessao.getStatus().name(),
                        "workflowInstanceId", cessao.getWorkflowInstanceId()
                )
        );
        publishCurrentTask(cessao, snapshot, "ETAPA_ATIVA", "system");
        readModelProjector.projectCurrentState(cessao.getBusinessKey(), "CESSAO_CRIADA");
    }

    public void publishStageAdvanced(
            Cessao cessao,
            EtapaCessao etapaConcluida,
            KogitoTaskSnapshot completedTask,
            EtapaCessao proximaEtapa,
            KogitoProcessSnapshot snapshot,
            String actorId
    ) {
        domainEventPublisher.publishKogitoProcessVariables(cessao, snapshot);
        domainEventPublisher.publishProcessEvent(
                cessao.getBusinessKey(),
                "ETAPA_AVANCADA",
                Map.of(
                        "businessKey", cessao.getBusinessKey(),
                        "etapaConcluida", etapaConcluida.getNomeEtapa().name(),
                        "status", cessao.getStatus().name()
                )
        );
        domainEventPublisher.publishKogitoProcessState(
                cessao,
                snapshot,
                actorId,
                snapshot.isCompleted()
                        ? org.kie.kogito.event.process.ProcessInstanceStateEventBody.EVENT_TYPE_ENDED
                        : org.kie.kogito.event.process.ProcessInstanceStateEventBody.EVENT_TYPE_STARTED
        );
        domainEventPublisher.publishKogitoProcessNodeExited(cessao, snapshot, completedTask, actorId);
        domainEventPublisher.publishKogitoUserTaskState(
                cessao,
                snapshot,
                completedTask,
                actorId,
                "Completed",
                "COMPLETED"
        );
        if (proximaEtapa != null) {
            publishCurrentTask(cessao, snapshot, "ETAPA_ATIVA", actorId);
        }
        readModelProjector.projectCurrentState(cessao.getBusinessKey(), "ETAPA_AVANCADA");
    }

    public void publishTimerScheduled(Cessao cessao, EtapaCessao etapa, String externalJobId) {
        domainEventPublisher.publishTaskEvent(
                cessao.getBusinessKey(),
                "TIMER_AGENDADO",
                Map.of(
                        "businessKey", cessao.getBusinessKey(),
                        "etapa", etapa.getNomeEtapa().name(),
                        "jobId", externalJobId
                )
        );
        readModelProjector.projectCurrentState(cessao.getBusinessKey(), "TIMER_AGENDADO");
    }

    private void publishCurrentTask(Cessao cessao, KogitoProcessSnapshot snapshot, String eventType, String actorId) {
        if (snapshot.activeTask() != null) {
            domainEventPublisher.publishKogitoProcessNodeEntered(cessao, snapshot, snapshot.activeTask(), actorId);
            domainEventPublisher.publishKogitoUserTaskState(
                    cessao,
                    snapshot,
                    snapshot.activeTask(),
                    actorId,
                    "Ready",
                    "STARTED"
            );
            domainEventPublisher.publishKogitoUserTaskAssignments(cessao, snapshot, snapshot.activeTask(), actorId);
        }
        cessao.getEtapas()
                .stream()
                .filter(etapa -> etapa.getStatusEtapa().name().equals("EM_EXECUCAO"))
                .findFirst()
                .ifPresent(etapa -> domainEventPublisher.publishTaskEvent(
                        cessao.getBusinessKey(),
                        eventType,
                        Map.of(
                                "businessKey", cessao.getBusinessKey(),
                                "etapa", etapa.getNomeEtapa().name(),
                                "statusEtapa", etapa.getStatusEtapa().name()
                        )
                ));
    }
}
