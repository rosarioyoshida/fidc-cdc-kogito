package com.fidc.cdc.kogito.application.cessao;

import com.fidc.cdc.kogito.api.cessao.CessaoRequest;
import com.fidc.cdc.kogito.api.error.BusinessConflictException;
import com.fidc.cdc.kogito.api.error.ResourceNotFoundException;
import com.fidc.cdc.kogito.application.audit.AuditEventPublisher;
import com.fidc.cdc.kogito.application.audit.AuditTrailService;
import com.fidc.cdc.kogito.application.process.KogitoProcessSnapshot;
import com.fidc.cdc.kogito.application.process.KogitoTaskSnapshot;
import com.fidc.cdc.kogito.application.process.KogitoWorkflowRuntimeService;
import com.fidc.cdc.kogito.application.process.RegistradoraWorkflowHandler;
import com.fidc.cdc.kogito.application.process.TimerSchedulingService;
import com.fidc.cdc.kogito.application.security.AuthorizationDecision;
import com.fidc.cdc.kogito.application.security.StageAuthorizationService;
import com.fidc.cdc.kogito.application.documental.ResultadoValidacaoLastro;
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
    private final AuditTrailService auditTrailService;
    private final ProcessMetricsService processMetricsService;
    private final TimerSchedulingService timerSchedulingService;
    private final StageAuthorizationService stageAuthorizationService;
    private final KogitoWorkflowRuntimeService workflowRuntimeService;
    private final RegistradoraWorkflowHandler registradoraWorkflowHandler;

    public CessaoProcessService(
            CessaoRepository cessaoRepository,
            EtapaCessaoRepository etapaCessaoRepository,
            CessaoRegistrationService cessaoRegistrationService,
            CessaoEventPublisher cessaoEventPublisher,
            AuditEventPublisher auditEventPublisher,
            AuditTrailService auditTrailService,
            ProcessMetricsService processMetricsService,
            TimerSchedulingService timerSchedulingService,
            StageAuthorizationService stageAuthorizationService,
            KogitoWorkflowRuntimeService workflowRuntimeService,
            RegistradoraWorkflowHandler registradoraWorkflowHandler
    ) {
        this.cessaoRepository = cessaoRepository;
        this.etapaCessaoRepository = etapaCessaoRepository;
        this.cessaoRegistrationService = cessaoRegistrationService;
        this.cessaoEventPublisher = cessaoEventPublisher;
        this.auditEventPublisher = auditEventPublisher;
        this.auditTrailService = auditTrailService;
        this.processMetricsService = processMetricsService;
        this.timerSchedulingService = timerSchedulingService;
        this.stageAuthorizationService = stageAuthorizationService;
        this.workflowRuntimeService = workflowRuntimeService;
        this.registradoraWorkflowHandler = registradoraWorkflowHandler;
    }

    @Transactional
    public Cessao createAndStart(CessaoRequest request) {
        Cessao cessao = cessaoRegistrationService.register(request);
        KogitoProcessSnapshot snapshot = workflowRuntimeService.getProcess(cessao.getWorkflowInstanceId());
        cessaoEventPublisher.publishCessaoCreated(cessao, snapshot);
        processMetricsService.incrementProcessEvent("CESSAO_CRIADA", "success");
        auditTrailService.registrarEvento(
                cessao,
                cessao.getEtapas().stream().findFirst().orElse(null),
                "system",
                "INTEGRACAO",
                "CESSAO_CRIADA",
                "SUCESSO",
                Map.of("businessKey", cessao.getBusinessKey())
        );
        return cessao;
    }

    @Transactional(readOnly = true)
    public List<Cessao> list(CessaoStatus status, String businessKey) {
        if (businessKey != null && !businessKey.isBlank()) {
            return List.of(getByBusinessKey(businessKey));
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

        AuthorizationDecision authorization = stageAuthorizationService.authorizeStageAction(responsavelId, etapaNome);
        validateAdvancePreconditions(cessao, etapa);
        KogitoProcessSnapshot beforeSnapshot = workflowRuntimeService.getProcess(cessao.getWorkflowInstanceId());
        KogitoTaskSnapshot completedTask = beforeSnapshot.activeTask();
        if (completedTask == null || completedTask.etapaNome() != etapaNome) {
            throw new BusinessConflictException("A etapa ativa no runtime nao corresponde a etapa solicitada.");
        }

        validateStageBusinessRules(businessKey, etapaNome);

        KogitoProcessSnapshot afterSnapshot = workflowRuntimeService.completeActiveHumanTask(
                businessKey,
                etapaNome,
                authorization.actorId(),
                justificativa
        );

        etapa.setStatusEtapa(EtapaCessaoStatus.CONCLUIDA);
        etapa.setResultado("SUCESSO");
        etapa.setResponsavelId(authorization.actorId());
        etapa.setJustificativa(justificativa);
        etapa.setConcluidaEm(OffsetDateTime.now());

        EtapaCessao next = synchronizeFollowingStages(cessao, etapa, afterSnapshot, authorization.actorId());

        if (next != null) {
            cessao.setStatus(CessaoStatus.EM_ANDAMENTO);
            if (next.getNomeEtapa() == EtapaCessaoNome.AGUARDAR_CONFIRMACAO_REGISTRADORA) {
                String jobId = timerSchedulingService.scheduleAwaitingConfirmationTimer(
                        cessao,
                        next,
                        Duration.ofMinutes(30)
                );
                cessaoEventPublisher.publishTimerScheduled(cessao, next, jobId);
            }
        } else if (afterSnapshot.isCompleted()) {
            cessao.setStatus(CessaoStatus.CONCLUIDA);
            cessao.setDataEncerramento(OffsetDateTime.now());
        }

        cessaoEventPublisher.publishStageAdvanced(cessao, etapa, completedTask, next, afterSnapshot, authorization.actorId());
        processMetricsService.incrementProcessEvent("ETAPA_AVANCADA", "success");
        auditEventPublisher.publish(
                "ETAPA_AVANCADA",
                authorization.actorId(),
                Map.of("businessKey", businessKey, "etapa", etapaNome.name())
        );
        auditTrailService.registrarEvento(
                cessao,
                etapa,
                authorization.actorId(),
                authorization.perfil(),
                "ETAPA_AVANCADA",
                "SUCESSO",
                Map.of(
                        "businessKey", businessKey,
                        "etapa", etapaNome.name(),
                        "justificativa", justificativa == null ? "" : justificativa
                )
        );
        return cessao;
    }

    @Transactional
    public Cessao resumeAwaitingConfirmation(String businessKey, String processInstanceId, String actorId) {
        Cessao cessao = getByBusinessKey(businessKey);
        EtapaCessao etapa = etapaCessaoRepository.findByCessaoBusinessKeyAndNomeEtapa(
                        businessKey,
                        EtapaCessaoNome.AGUARDAR_CONFIRMACAO_REGISTRADORA
                )
                .orElseThrow(() -> new ResourceNotFoundException("Etapa de aguardando confirmacao nao encontrada."));

        if (etapa.getStatusEtapa() != EtapaCessaoStatus.EM_EXECUCAO) {
            throw new BusinessConflictException("A etapa de aguardando confirmacao nao esta ativa para retomada.");
        }

        KogitoProcessSnapshot afterSnapshot = workflowRuntimeService.confirmAwaitingRegistradora(
                processInstanceId,
                businessKey
        );

        OffsetDateTime now = OffsetDateTime.now();
        etapa.setStatusEtapa(EtapaCessaoStatus.CONCLUIDA);
        etapa.setResultado("SUCESSO");
        etapa.setResponsavelId(actorId == null || actorId.isBlank() ? "system" : actorId);
        etapa.setJustificativa("Confirmacao recebida via callback do Jobs Service.");
        etapa.setConcluidaEm(now);

        EtapaCessao next = synchronizeFollowingStages(cessao, etapa, afterSnapshot, actorId);
        if (next != null) {
            cessao.setStatus(CessaoStatus.EM_ANDAMENTO);
        } else if (afterSnapshot.isCompleted()) {
            cessao.setStatus(CessaoStatus.CONCLUIDA);
            cessao.setDataEncerramento(now);
        }

        cessaoEventPublisher.publishStageAdvanced(
                cessao,
                etapa,
                null,
                next,
                afterSnapshot,
                actorId == null || actorId.isBlank() ? "system" : actorId
        );
        processMetricsService.incrementProcessEvent("TIMER_CALLBACK_PROCESSADO", "success");
        auditEventPublisher.publish(
                "TIMER_CALLBACK_PROCESSADO",
                actorId == null || actorId.isBlank() ? "system" : actorId,
                Map.of("businessKey", businessKey, "etapa", etapa.getNomeEtapa().name())
        );
        auditTrailService.registrarEvento(
                cessao,
                etapa,
                actorId == null || actorId.isBlank() ? "system" : actorId,
                "INTEGRACAO",
                "TIMER_CALLBACK_PROCESSADO",
                "SUCESSO",
                Map.of("businessKey", businessKey, "processInstanceId", processInstanceId)
        );
        return cessao;
    }

    private EtapaCessao synchronizeFollowingStages(
            Cessao cessao,
            EtapaCessao etapaConcluida,
            KogitoProcessSnapshot afterSnapshot,
            String actorId
    ) {
        List<EtapaCessao> orderedStages = cessao.getEtapas()
                .stream()
                .sorted(Comparator.comparingInt(EtapaCessao::getOrdem))
                .toList();

        EtapaCessaoNome currentRuntimeStage = resolveCurrentRuntimeStage(afterSnapshot, etapaConcluida);
        OffsetDateTime now = OffsetDateTime.now();
        EtapaCessao next = null;

        for (EtapaCessao candidate : orderedStages) {
            if (candidate.getOrdem() <= etapaConcluida.getOrdem()) {
                continue;
            }

            if (afterSnapshot.isCompleted()) {
                markAutomaticStageAsCompleted(candidate, now, actorId);
                continue;
            }

            if (candidate.getNomeEtapa() == currentRuntimeStage) {
                candidate.setStatusEtapa(EtapaCessaoStatus.EM_EXECUCAO);
                if (candidate.getInicioEm() == null) {
                    candidate.setInicioEm(now);
                }
                next = candidate;
                break;
            }

            markAutomaticStageAsCompleted(candidate, now, actorId);
        }

        return next;
    }

    private EtapaCessaoNome resolveCurrentRuntimeStage(KogitoProcessSnapshot afterSnapshot, EtapaCessao etapaConcluida) {
        if (afterSnapshot.activeTask() != null) {
            return afterSnapshot.activeTask().etapaNome();
        }
        if (afterSnapshot.isCompleted()) {
            return null;
        }
        if (etapaConcluida.getOrdem() < EtapaCessaoNome.AGUARDAR_CONFIRMACAO_REGISTRADORA.ordinal() + 1) {
            return EtapaCessaoNome.AGUARDAR_CONFIRMACAO_REGISTRADORA;
        }
        throw new BusinessConflictException("O runtime avancou para um estado sem etapa humana ativa que nao esta mapeado.");
    }

    private void markAutomaticStageAsCompleted(EtapaCessao etapa, OffsetDateTime timestamp, String actorId) {
        etapa.setStatusEtapa(EtapaCessaoStatus.CONCLUIDA);
        if (etapa.getInicioEm() == null) {
            etapa.setInicioEm(timestamp);
        }
        etapa.setConcluidaEm(timestamp);
        etapa.setResultado("SUCESSO");
        etapa.setResponsavelId(actorId == null || actorId.isBlank() ? "system" : actorId);
        if (etapa.getJustificativa() == null || etapa.getJustificativa().isBlank()) {
            etapa.setJustificativa("Etapa automatica concluida pelo runtime.");
        }
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

    @Transactional(readOnly = true)
    public void assertExists(String businessKey) {
        ensureExists(businessKey);
    }

    private void validateStageBusinessRules(String businessKey, EtapaCessaoNome etapaNome) {
        if (etapaNome != EtapaCessaoNome.VALIDAR_LASTROS) {
            return;
        }
        ResultadoValidacaoLastro resultado = registradoraWorkflowHandler.validarLastros(businessKey);
        if (resultado.bloqueiaAceiteFinal()) {
            throw new BusinessConflictException(
                    "Existem lastros rejeitados. Corrija os documentos pendentes antes de concluir VALIDAR_LASTROS."
            );
        }
    }
}
