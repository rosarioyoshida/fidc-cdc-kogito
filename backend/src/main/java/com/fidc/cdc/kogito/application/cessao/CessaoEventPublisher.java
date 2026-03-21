package com.fidc.cdc.kogito.application.cessao;

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

    public void publishCessaoCreated(Cessao cessao) {
        domainEventPublisher.publishProcessEvent(
                cessao.getBusinessKey(),
                "CESSAO_CRIADA",
                Map.of(
                        "businessKey", cessao.getBusinessKey(),
                        "status", cessao.getStatus().name(),
                        "workflowInstanceId", cessao.getWorkflowInstanceId()
                )
        );
        publishCurrentTask(cessao, "ETAPA_ATIVA");
        readModelProjector.projectCurrentState(cessao.getBusinessKey(), "CESSAO_CRIADA");
    }

    public void publishStageAdvanced(Cessao cessao, EtapaCessao etapaConcluida, EtapaCessao proximaEtapa) {
        domainEventPublisher.publishProcessEvent(
                cessao.getBusinessKey(),
                "ETAPA_AVANCADA",
                Map.of(
                        "businessKey", cessao.getBusinessKey(),
                        "etapaConcluida", etapaConcluida.getNomeEtapa().name(),
                        "status", cessao.getStatus().name()
                )
        );
        if (proximaEtapa != null) {
            publishCurrentTask(cessao, "ETAPA_ATIVA");
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

    private void publishCurrentTask(Cessao cessao, String eventType) {
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
