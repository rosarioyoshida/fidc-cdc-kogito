package com.fidc.cdc.kogito.application.cessao;

import com.fidc.cdc.kogito.api.cessao.CessaoRequest;
import com.fidc.cdc.kogito.api.error.BusinessConflictException;
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

    public CessaoRegistrationService(CessaoRepository cessaoRepository) {
        this.cessaoRepository = cessaoRepository;
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
        cessao.setWorkflowInstanceId("workflow-" + request.businessKey());

        int ordem = 1;
        for (EtapaCessaoNome nome : EtapaCessaoNome.orderedValues()) {
            EtapaCessao etapa = new EtapaCessao();
            etapa.setNomeEtapa(nome);
            etapa.setOrdem(ordem++);
            etapa.setStatusEtapa(nome == EtapaCessaoNome.IMPORTAR_CARTEIRA
                    ? EtapaCessaoStatus.EM_EXECUCAO
                    : EtapaCessaoStatus.PENDENTE);
            if (nome == EtapaCessaoNome.IMPORTAR_CARTEIRA) {
                etapa.setInicioEm(OffsetDateTime.now());
            }
            cessao.addEtapa(etapa);
        }

        return cessaoRepository.save(cessao);
    }
}
