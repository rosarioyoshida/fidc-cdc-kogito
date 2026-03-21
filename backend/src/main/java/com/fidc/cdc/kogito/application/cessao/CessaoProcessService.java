package com.fidc.cdc.kogito.application.cessao;

import com.fidc.cdc.kogito.api.cessao.CessaoRequest;
import com.fidc.cdc.kogito.api.error.BusinessConflictException;
import com.fidc.cdc.kogito.api.error.ResourceNotFoundException;
import com.fidc.cdc.kogito.application.audit.AuditEventPublisher;
import com.fidc.cdc.kogito.application.process.TimerSchedulingService;
import com.fidc.cdc.kogito.domain.cessao.Cessao;
import com.fidc.cdc.kogito.domain.cessao.CessaoRepository;
import com.fidc.cdc.kogito.domain.cessao.CessaoStatus;
import com.fidc.cdc.kogito.domain.cessao.EtapaCessao;
import com.fidc.cdc.kogito.domain.cessao.EtapaCessaoNome;
import com.fidc.cdc.kogito.domain.cessao.EtapaCessaoRepository;
import com.fidc.cdc.kogito.domain.cessao.EtapaCessaoStatus;
import com.fidc.cdc.kogito.observability.ProcessMetricsService;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CessaoProcessService {

    private final CessaoRepository cessaoRepository;
    private final EtapaCessaoRepository etapaCessaoRepository;
    private final CessaoRegistrationService cessaoRegistrationService;
    private final CessaoEventPublisher cessaoEventPublisher;
    private final AuditEventPublisher auditEventPublisher;
    private final ProcessMetricsService processMetricsService;
    private final TimerSchedulingService timerSchedulingService;

    public CessaoProcessService(
            CessaoRepository cessaoRepository,
            EtapaCessaoRepository etapaCessaoRepository,
            CessaoRegistrationService cessaoRegistrationService,
            CessaoEventPublisher cessaoEventPublisher,
            AuditEventPublisher auditEventPublisher,
            ProcessMetricsService processMetricsService,
            TimerSchedulingService timerSchedulingService
    ) {
        this.cessaoRepository = cessaoRepository;
        this.etapaCessaoRepository = etapaCessaoRepository;
        this.cessaoRegistrationService = cessaoRegistrationService;
        this.cessaoEventPublisher = cessaoEventPublisher;
        this.auditEventPublisher = auditEventPublisher;
        this.processMetricsService = processMetricsService;
        this.timerSchedulingService = timerSchedulingService;
    }

    @Transactional
    public Cessao createAndStart(CessaoRequest request) {
        Cessao cessao = cessaoRegistrationService.register(request);
        cessaoEventPublisher.publishCessaoCreated(cessao);
        processMetricsService.incrementProcessEvent("CESSAO_CRIADA", "success");
        return cessao;
    }

    @Transactional(readOnly = true)
    public List<Cessao> list(CessaoStatus status, String businessKey) {
        if (businessKey != null && !businessKey.isBlank()) {
            return cessaoRepository.findByBusinessKey(businessKey)
                    .map(List::of)
                    .orElse(List.of());
        }
        if (status != null) {
            return cessaoRepository.findByStatus(status);
        }
        return cessaoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Cessao getByBusinessKey(String businessKey) {
        return cessaoRepository.findByBusinessKey(businessKey)
                .orElseThrow(() -> new ResourceNotFoundException("Cessao nao encontrada para o businessKey informado."));
    }

    @Transactional(readOnly = true)
    public List<EtapaCessao> getEtapas(String businessKey) {
        ensureExists(businessKey);
        return etapaCessaoRepository.findByCessaoBusinessKeyOrderByOrdemAsc(businessKey);
    }

    @Transactional
    public Cessao advanceStage(
            String businessKey,
            EtapaCessaoNome etapaNome,
            String responsavelId,
            String justificativa
    ) {
        Cessao cessao = getByBusinessKey(businessKey);
        EtapaCessao etapa = etapaCessaoRepository.findByCessaoBusinessKeyAndNomeEtapa(businessKey, etapaNome)
                .orElseThrow(() -> new ResourceNotFoundException("Etapa nao encontrada para a cessao informada."));

        validateAdvancePreconditions(cessao, etapa);

        etapa.setStatusEtapa(EtapaCessaoStatus.CONCLUIDA);
        etapa.setResultado("SUCESSO");
        etapa.setResponsavelId(responsavelId);
        etapa.setJustificativa(justificativa);
        etapa.setConcluidaEm(OffsetDateTime.now());

        EtapaCessao next = cessao.getEtapas()
                .stream()
                .sorted(Comparator.comparingInt(EtapaCessao::getOrdem))
                .filter(candidate -> candidate.getOrdem() == etapa.getOrdem() + 1)
                .findFirst()
                .orElse(null);

        if (next != null) {
            next.setStatusEtapa(EtapaCessaoStatus.EM_EXECUCAO);
            next.setInicioEm(OffsetDateTime.now());
            cessao.setStatus(CessaoStatus.EM_ANDAMENTO);
            if (next.getNomeEtapa() == EtapaCessaoNome.AGUARDAR_CONFIRMACAO_REGISTRADORA) {
                String jobId = timerSchedulingService.scheduleAwaitingConfirmationTimer(
                        cessao,
                        next,
                        Duration.ofMinutes(30)
                );
                cessaoEventPublisher.publishTimerScheduled(cessao, next, jobId);
            }
        } else {
            cessao.setStatus(CessaoStatus.CONCLUIDA);
            cessao.setDataEncerramento(OffsetDateTime.now());
        }

        cessaoEventPublisher.publishStageAdvanced(cessao, etapa, next);
        processMetricsService.incrementProcessEvent("ETAPA_AVANCADA", "success");
        auditEventPublisher.publish(
                "ETAPA_AVANCADA",
                responsavelId == null || responsavelId.isBlank() ? "sistema" : responsavelId,
                Map.of("businessKey", businessKey, "etapa", etapaNome.name())
        );
        return cessao;
    }

    private void validateAdvancePreconditions(Cessao cessao, EtapaCessao etapa) {
        if (etapa.getStatusEtapa() != EtapaCessaoStatus.EM_EXECUCAO) {
            throw new BusinessConflictException("A etapa informada nao esta pronta para avancar.");
        }

        boolean blockedByDependency = cessao.getEtapas()
                .stream()
                .anyMatch(candidate -> candidate.getOrdem() < etapa.getOrdem()
                        && candidate.getStatusEtapa() != EtapaCessaoStatus.CONCLUIDA);

        if (blockedByDependency) {
            throw new BusinessConflictException("Existem etapas anteriores ainda nao concluidas.");
        }
    }

    private void ensureExists(String businessKey) {
        if (!cessaoRepository.existsByBusinessKey(businessKey)) {
            throw new ResourceNotFoundException("Cessao nao encontrada para o businessKey informado.");
        }
    }
}
