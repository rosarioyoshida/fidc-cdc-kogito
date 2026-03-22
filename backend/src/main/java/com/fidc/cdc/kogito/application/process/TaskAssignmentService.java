package com.fidc.cdc.kogito.application.process;

import com.fidc.cdc.kogito.api.error.ResourceNotFoundException;
import com.fidc.cdc.kogito.application.security.PermissionSnapshot;
import com.fidc.cdc.kogito.application.security.StageAuthorizationService;
import com.fidc.cdc.kogito.domain.cessao.Cessao;
import com.fidc.cdc.kogito.domain.cessao.CessaoRepository;
import com.fidc.cdc.kogito.domain.cessao.EtapaCessao;
import com.fidc.cdc.kogito.domain.cessao.EtapaCessaoNome;
import com.fidc.cdc.kogito.domain.cessao.EtapaCessaoStatus;
import com.fidc.cdc.kogito.domain.security.PermissaoEtapaRepository;
import com.fidc.cdc.kogito.domain.security.UsuarioRepository;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskAssignmentService {

    private static final Set<EtapaCessaoNome> HUMAN_TASK_STAGES = EnumSet.of(
            EtapaCessaoNome.IMPORTAR_CARTEIRA,
            EtapaCessaoNome.VALIDAR_CEDENTE,
            EtapaCessaoNome.ANALISAR_ELEGIBILIDADE,
            EtapaCessaoNome.COLETAR_TERMO_ACEITE,
            EtapaCessaoNome.VALIDAR_LASTROS,
            EtapaCessaoNome.AUTORIZAR_PAGAMENTO
    );

    private final CessaoRepository cessaoRepository;
    private final PermissaoEtapaRepository permissaoEtapaRepository;
    private final UsuarioRepository usuarioRepository;
    private final StageAuthorizationService stageAuthorizationService;
    private final KogitoWorkflowRuntimeService workflowRuntimeService;
    private final String taskConsoleUrl;

    public TaskAssignmentService(
            CessaoRepository cessaoRepository,
            PermissaoEtapaRepository permissaoEtapaRepository,
            UsuarioRepository usuarioRepository,
            StageAuthorizationService stageAuthorizationService,
            KogitoWorkflowRuntimeService workflowRuntimeService,
            @Value("${fidc.consoles.task-console-url}") String taskConsoleUrl
    ) {
        this.cessaoRepository = cessaoRepository;
        this.permissaoEtapaRepository = permissaoEtapaRepository;
        this.usuarioRepository = usuarioRepository;
        this.stageAuthorizationService = stageAuthorizationService;
        this.workflowRuntimeService = workflowRuntimeService;
        this.taskConsoleUrl = taskConsoleUrl;
    }

    @Transactional(readOnly = true)
    public TaskAssignmentContext describeTaskContext(String businessKey, String actorHint) {
        Cessao cessao = cessaoRepository.findByBusinessKey(businessKey)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cessao nao encontrada para consulta de task context."
                ));
        PermissionSnapshot snapshot = stageAuthorizationService.describePermissions(actorHint);
        KogitoProcessSnapshot runtimeSnapshot = workflowRuntimeService.getProcessByBusinessKey(businessKey);
        KogitoTaskSnapshot currentTask = runtimeSnapshot.activeTask();
        Optional<EtapaCessao> currentStage = findCurrentStage(cessao);
        String currentStageName = currentTask != null
                ? currentTask.etapaNome().name()
                : currentStage.map(etapa -> etapa.getNomeEtapa().name()).orElse("SEM_ETAPA_ATIVA");

        if (currentTask == null || !isHumanTaskStage(currentTask.etapaNome())) {
            return new TaskAssignmentContext(
                    businessKey,
                    cessao.getWorkflowInstanceId(),
                    snapshot.actorId(),
                    currentStageName,
                    false,
                    false,
                    null,
                    null,
                    null,
                    List.of(),
                    List.of(),
                    List.of("AUDITOR"),
                    taskConsoleUrl
            );
        }

        EtapaCessaoNome etapaNome = currentTask.etapaNome();
        List<String> candidateGroups = permissaoEtapaRepository.findByNomeEtapa(etapaNome)
                .stream()
                .map(permissao -> permissao.getPerfilAcesso().getNome())
                .distinct()
                .sorted()
                .toList();
        if (currentTask.groupId() != null && !currentTask.groupId().isBlank() && !candidateGroups.contains(currentTask.groupId())) {
            candidateGroups = java.util.stream.Stream.concat(candidateGroups.stream(), java.util.stream.Stream.of(currentTask.groupId()))
                    .distinct()
                    .sorted(Comparator.naturalOrder())
                    .toList();
        }
        List<String> candidateUsers = usuarioRepository.findDistinctByPerfisPermissoesNomeEtapaAndAtivoTrue(
                        etapaNome
                )
                .stream()
                .map(usuario -> usuario.getUsername())
                .sorted(Comparator.naturalOrder())
                .toList();
        boolean actorAuthorized = snapshot.etapasPermitidas().contains(etapaNome.name());

        return new TaskAssignmentContext(
                businessKey,
                cessao.getWorkflowInstanceId(),
                snapshot.actorId(),
                etapaNome.name(),
                true,
                actorAuthorized,
                etapaNome.name(),
                currentTask.description(),
                currentTask.actualOwner(),
                candidateGroups,
                candidateUsers,
                List.of("AUDITOR"),
                taskConsoleUrl
        );
    }

    public boolean isHumanTaskStage(EtapaCessaoNome etapa) {
        return HUMAN_TASK_STAGES.contains(etapa);
    }

    private Optional<EtapaCessao> findCurrentStage(Cessao cessao) {
        return cessao.getEtapas().stream()
                .filter(etapa -> etapa.getStatusEtapa() == EtapaCessaoStatus.EM_EXECUCAO)
                .min(Comparator.comparingInt(EtapaCessao::getOrdem));
    }
}
