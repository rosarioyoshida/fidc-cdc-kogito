package com.fidc.cdc.kogito.application.cessao;

import com.fidc.cdc.kogito.api.cessao.CessaoRequest;
import com.fidc.cdc.kogito.api.error.BusinessConflictException;
import com.fidc.cdc.kogito.application.process.KogitoProcessSnapshot;
import com.fidc.cdc.kogito.application.process.KogitoWorkflowRuntimeService;
import com.fidc.cdc.kogito.domain.cessao.Cessao;
import com.fidc.cdc.kogito.domain.cessao.CessaoRepository;
import com.fidc.cdc.kogito.domain.cessao.CessaoStatus;
import com.fidc.cdc.kogito.domain.cessao.EtapaCessao;
import com.fidc.cdc.kogito.domain.cessao.EtapaCessaoNome;
import com.fidc.cdc.kogito.domain.cessao.EtapaCessaoStatus;
import java.time.OffsetDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CessaoRegistrationService {

    private final CessaoRepository cessaoRepository;
    private final KogitoWorkflowRuntimeService workflowRuntimeService;

    public CessaoRegistrationService(
            CessaoRepository cessaoRepository,
            KogitoWorkflowRuntimeService workflowRuntimeService
    ) {
        this.cessaoRepository = cessaoRepository;
        this.workflowRuntimeService = workflowRuntimeService;
    }

    @Transactional
    public Cessao register(CessaoRequest request) {
        if (cessaoRepository.existsByBusinessKey(request.businessKey())) {
            throw new BusinessConflictException("Ja existe uma cessao para o businessKey informado.");
        }

        Cessao cessao = new Cessao();
        cessao.setBusinessKey(request.businessKey());
        cessao.setCedenteId(request.cedenteId());
        cessao.setCessionariaId(request.cessionariaId());
        cessao.setDataImportacao(OffsetDateTime.now());
        cessao.setStatus(CessaoStatus.EM_ANDAMENTO);

        KogitoProcessSnapshot snapshot = workflowRuntimeService.startProcess(request);
        cessao.setWorkflowInstanceId(snapshot.processInstanceId());

        int ordem = 1;
        for (EtapaCessaoNome nome : EtapaCessaoNome.orderedValues()) {
            EtapaCessao etapa = new EtapaCessao();
            etapa.setNomeEtapa(nome);
            etapa.setOrdem(ordem++);
            etapa.setStatusEtapa(snapshot.activeTask() != null && snapshot.activeTask().etapaNome() == nome
                    ? EtapaCessaoStatus.EM_EXECUCAO
                    : EtapaCessaoStatus.PENDENTE);
            if (snapshot.activeTask() != null && snapshot.activeTask().etapaNome() == nome) {
                etapa.setInicioEm(OffsetDateTime.now());
            }
            cessao.addEtapa(etapa);
        }

        return cessaoRepository.save(cessao);
    }
}
