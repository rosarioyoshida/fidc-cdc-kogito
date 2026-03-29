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

/**
 * Coordena task assignment na camada de aplicacao.
 *
 * <p>Este tipo pertence a camada de orquestracao de casos de uso e servicos de aplicacao. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
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
        Optional<EtapaCessao> currentStage = findCurrentStage(cessao);
        String currentStageName = currentStage.map(etapa -> etapa.getNomeEtapa().name())
                .or(() -> findLastCompletedStage(cessao).map(etapa -> etapa.getNomeEtapa().name()))
                .orElse("SEM_ETAPA_ATIVA");

        if (currentStage.isEmpty() || !isHumanTaskStage(currentStage.get().getNomeEtapa())) {
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

        EtapaCessaoNome etapaNome = currentStage.get().getNomeEtapa();
        List<String> candidateGroups = permissaoEtapaRepository.findByNomeEtapa(etapaNome)
                .stream()
                .map(permissao -> permissao.getPerfilAcesso().getNome())
                .distinct()
                .sorted()
                .toList();
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
                null,
                currentStage.get().getResponsavelId(),
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

    private Optional<EtapaCessao> findLastCompletedStage(Cessao cessao) {
        return cessao.getEtapas().stream()
                .filter(etapa -> etapa.getStatusEtapa() == EtapaCessaoStatus.CONCLUIDA)
                .max(Comparator.comparingInt(EtapaCessao::getOrdem));
    }
}
