package com.fidc.cdc.kogito.application.readmodel;

import com.fidc.cdc.kogito.domain.cessao.Cessao;
import com.fidc.cdc.kogito.domain.cessao.CessaoRepository;
import com.fidc.cdc.kogito.domain.cessao.EtapaCessao;
import com.fidc.cdc.kogito.domain.cessao.EtapaCessaoStatus;
import com.fidc.cdc.kogito.infrastructure.messaging.ProcessEventPayload;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CessaoReadModelProjector {

    private final CessaoRepository cessaoRepository;
    private final CessaoReadModelRepository readModelRepository;

    public CessaoReadModelProjector(
            CessaoRepository cessaoRepository,
            CessaoReadModelRepository readModelRepository
    ) {
        this.cessaoRepository = cessaoRepository;
        this.readModelRepository = readModelRepository;
    }

    @Transactional(readOnly = true)
    public void projectCurrentState(String businessKey, String lastEvent) {
        cessaoRepository.findByBusinessKey(businessKey)
                .ifPresent(cessao -> readModelRepository.save(toDocument(cessao, lastEvent)));
    }

    @KafkaListener(
            topics = "#{@kafkaTopicsProperties.process}",
            groupId = "fidc-read-model",
            autoStartup = "${fidc.read-model.kafka-listener-enabled:false}"
    )
    public void consumeProcessEvent(ProcessEventPayload payload) {
        projectCurrentState(payload.businessKey(), payload.eventType());
    }

    private CessaoReadModelDocument toDocument(Cessao cessao, String lastEvent) {
        CessaoReadModelDocument document = new CessaoReadModelDocument();
        document.setCessaoBusinessKey(cessao.getBusinessKey());
        document.setStatusAtual(cessao.getStatus().name());
        document.setEtapaAtual(identifyCurrentStage(cessao.getEtapas()));
        document.setPendencias(cessao.getEtapas()
                .stream()
                .filter(etapa -> etapa.getStatusEtapa() != EtapaCessaoStatus.CONCLUIDA)
                .map(etapa -> etapa.getNomeEtapa().name())
                .toList());
        document.setUltimoEvento(lastEvent);
        document.setUltimaAtualizacao(OffsetDateTime.now());
        document.setResumoFinanceiro(Map.of(
                "valorCalculado", cessao.getValorCalculado(),
                "valorAprovado", cessao.getValorAprovado()
        ));
        document.setResumoDocumental(Map.of(
                "totalEtapas", cessao.getEtapas().size(),
                "etapasConcluidas", cessao.getEtapas().stream()
                        .filter(etapa -> etapa.getStatusEtapa() == EtapaCessaoStatus.CONCLUIDA)
                        .count()
        ));
        return document;
    }

    private String identifyCurrentStage(List<EtapaCessao> etapas) {
        return etapas.stream()
                .filter(etapa -> etapa.getStatusEtapa() == EtapaCessaoStatus.EM_EXECUCAO)
                .map(etapa -> etapa.getNomeEtapa().name())
                .findFirst()
                .orElse("SEM_ETAPA_ATIVA");
    }
}
