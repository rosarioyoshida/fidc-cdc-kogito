package com.fidc.cdc.kogito.application.documental;

import com.fidc.cdc.kogito.api.error.BusinessConflictException;
import com.fidc.cdc.kogito.api.error.ResourceNotFoundException;
import com.fidc.cdc.kogito.domain.analise.Contrato;
import com.fidc.cdc.kogito.domain.analise.ContratoRepository;
import com.fidc.cdc.kogito.domain.analise.Lastro;
import com.fidc.cdc.kogito.domain.analise.LastroRepository;
import com.fidc.cdc.kogito.domain.analise.Parcela;
import com.fidc.cdc.kogito.domain.analise.ParcelaRepository;
import com.fidc.cdc.kogito.domain.analise.StatusValidacaoLastro;
import com.fidc.cdc.kogito.domain.cessao.Cessao;
import com.fidc.cdc.kogito.domain.cessao.CessaoRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LastroValidationService {

    private final CessaoRepository cessaoRepository;
    private final ContratoRepository contratoRepository;
    private final ParcelaRepository parcelaRepository;
    private final LastroRepository lastroRepository;

    public LastroValidationService(
            CessaoRepository cessaoRepository,
            ContratoRepository contratoRepository,
            ParcelaRepository parcelaRepository,
            LastroRepository lastroRepository
    ) {
        this.cessaoRepository = cessaoRepository;
        this.contratoRepository = contratoRepository;
        this.parcelaRepository = parcelaRepository;
        this.lastroRepository = lastroRepository;
    }

    @Transactional
    public Lastro registrar(String businessKey, LastroDraft draft) {
        Cessao cessao = cessaoRepository.findByBusinessKey(businessKey)
                .orElseThrow(() -> new ResourceNotFoundException("Cessao nao encontrada para registrar lastro."));

        Lastro lastro = new Lastro();
        lastro.setCessao(cessao);
        lastro.setContrato(loadContrato(draft.contratoId()));
        lastro.setParcela(loadParcela(draft.parcelaId()));
        lastro.setTipoDocumento(draft.tipoDocumento());
        lastro.setOrigem(draft.origem());
        lastro.setStatusValidacao(StatusValidacaoLastro.PENDENTE);
        lastro.setRecebidoEm(OffsetDateTime.now());

        validateOwnership(businessKey, lastro);
        return lastroRepository.save(lastro);
    }

    @Transactional
    public ResultadoValidacaoLastro validar(String businessKey) {
        List<Lastro> lastros = lastroRepository.findByCessaoBusinessKeyOrderByRecebidoEmAsc(businessKey);
        if (lastros.isEmpty()) {
            throw new BusinessConflictException("Nao existem lastros recebidos para validar nesta cessao.");
        }

        OffsetDateTime validadoEm = OffsetDateTime.now();
        for (Lastro lastro : lastros) {
            boolean valido = isValid(lastro);
            lastro.setStatusValidacao(valido ? StatusValidacaoLastro.VALIDADO : StatusValidacaoLastro.REJEITADO);
            lastro.setValidadoEm(validadoEm);
        }
        List<Lastro> persistidos = lastroRepository.saveAll(lastros);
        long rejeitados = persistidos.stream()
                .filter(item -> item.getStatusValidacao() == StatusValidacaoLastro.REJEITADO)
                .count();
        long validados = persistidos.size() - rejeitados;
        return new ResultadoValidacaoLastro(persistidos, rejeitados > 0, validados, rejeitados);
    }

    private Contrato loadContrato(UUID contratoId) {
        if (contratoId == null) {
            return null;
        }
        return contratoRepository.findById(contratoId)
                .orElseThrow(() -> new ResourceNotFoundException("Contrato informado para o lastro nao foi encontrado."));
    }

    private Parcela loadParcela(UUID parcelaId) {
        if (parcelaId == null) {
            return null;
        }
        return parcelaRepository.findById(parcelaId)
                .orElseThrow(() -> new ResourceNotFoundException("Parcela informada para o lastro nao foi encontrada."));
    }

    private void validateOwnership(String businessKey, Lastro lastro) {
        if (lastro.getContrato() != null
                && !businessKey.equals(lastro.getContrato().getCessao().getBusinessKey())) {
            throw new BusinessConflictException("O contrato informado nao pertence a cessao selecionada.");
        }
        if (lastro.getParcela() != null
                && !businessKey.equals(lastro.getParcela().getContrato().getCessao().getBusinessKey())) {
            throw new BusinessConflictException("A parcela informada nao pertence a cessao selecionada.");
        }
    }

    private boolean isValid(Lastro lastro) {
        return lastro.getTipoDocumento() != null
                && !lastro.getTipoDocumento().isBlank()
                && lastro.getOrigem() != null
                && !lastro.getOrigem().isBlank();
    }
}
