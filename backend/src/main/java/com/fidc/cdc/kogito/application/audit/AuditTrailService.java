package com.fidc.cdc.kogito.application.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fidc.cdc.kogito.domain.audit.EventoAuditoria;
import com.fidc.cdc.kogito.domain.audit.EventoAuditoriaRepository;
import com.fidc.cdc.kogito.domain.cessao.Cessao;
import com.fidc.cdc.kogito.domain.cessao.CessaoRepository;
import com.fidc.cdc.kogito.domain.cessao.EtapaCessao;
import com.fidc.cdc.kogito.infrastructure.logging.CorrelationLoggingFilter;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Coordena audit trail na camada de aplicacao.
 *
 * <p>Este tipo pertence a camada de orquestracao de casos de uso e servicos de aplicacao. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
@Service
public class AuditTrailService {

    private final EventoAuditoriaRepository eventoAuditoriaRepository;
    private final CessaoRepository cessaoRepository;
    private final ObjectMapper objectMapper;

    public AuditTrailService(
            EventoAuditoriaRepository eventoAuditoriaRepository,
            CessaoRepository cessaoRepository,
            ObjectMapper objectMapper
    ) {
        this.eventoAuditoriaRepository = eventoAuditoriaRepository;
        this.cessaoRepository = cessaoRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public EventoAuditoria registrarEvento(
            Cessao cessao,
            EtapaCessao etapa,
            String atorId,
            String perfil,
            String tipoEvento,
            String resultado,
            Map<String, ?> detalhe
    ) {
        EventoAuditoria evento = new EventoAuditoria();
        evento.setCessao(cessao);
        evento.setEtapa(etapa);
        evento.setAtorId(atorId);
        evento.setPerfil(perfil);
        evento.setTipoEvento(tipoEvento);
        evento.setResultado(resultado);
        evento.setCorrelationId(resolveCorrelationId());
        evento.setOcorridoEm(OffsetDateTime.now());
        evento.setDetalheRef(serialize(detalhe));
        return eventoAuditoriaRepository.save(evento);
    }

    @Transactional(readOnly = true)
    public List<EventoAuditoria> listarPorBusinessKey(String businessKey) {
        cessaoRepository.findByBusinessKey(businessKey)
                .orElseThrow(() -> new com.fidc.cdc.kogito.api.error.ResourceNotFoundException(
                        "Cessao nao encontrada para consulta de auditoria."
                ));
        return eventoAuditoriaRepository.findByCessaoBusinessKeyOrderByOcorridoEmAsc(businessKey);
    }

    private String resolveCorrelationId() {
        String correlationId = MDC.get(CorrelationLoggingFilter.CORRELATION_ID_KEY);
        return correlationId == null || correlationId.isBlank() ? "sem-correlation-id" : correlationId;
    }

    private String serialize(Map<String, ?> detalhe) {
        try {
            return objectMapper.writeValueAsString(detalhe);
        } catch (JsonProcessingException ex) {
            return String.valueOf(detalhe);
        }
    }
}
