package com.fidc.cdc.kogito.application.readmodel;

import com.fidc.cdc.kogito.domain.audit.EventoAuditoria;
import com.fidc.cdc.kogito.domain.audit.EventoAuditoriaRepository;
import com.fidc.cdc.kogito.domain.analise.ContratoRepository;
import com.fidc.cdc.kogito.domain.analise.LastroRepository;
import com.fidc.cdc.kogito.domain.analise.PagamentoRepository;
import com.fidc.cdc.kogito.domain.analise.RegraElegibilidade;
import com.fidc.cdc.kogito.domain.analise.RegraElegibilidadeRepository;
import com.fidc.cdc.kogito.domain.analise.StatusValidacaoLastro;
import com.fidc.cdc.kogito.domain.cessao.Cessao;
import com.fidc.cdc.kogito.domain.cessao.CessaoRepository;
import com.fidc.cdc.kogito.domain.cessao.EtapaCessao;
import com.fidc.cdc.kogito.domain.cessao.EtapaCessaoNome;
import com.fidc.cdc.kogito.domain.cessao.EtapaCessaoStatus;
import com.fidc.cdc.kogito.infrastructure.messaging.ProcessEventPayload;
import com.fidc.cdc.kogito.observability.ProcessMetricsService;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Representa cessao read model projector no backend de cessao.
 *
 * <p>Este tipo pertence a camada de orquestracao de casos de uso e servicos de aplicacao. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
@Service
public class CessaoReadModelProjector {

    private static final EnumSet<EtapaCessaoNome> HUMAN_TASK_STAGES = EnumSet.of(
            EtapaCessaoNome.IMPORTAR_CARTEIRA,
            EtapaCessaoNome.VALIDAR_CEDENTE,
            EtapaCessaoNome.ANALISAR_ELEGIBILIDADE,
            EtapaCessaoNome.COLETAR_TERMO_ACEITE,
            EtapaCessaoNome.VALIDAR_LASTROS,
            EtapaCessaoNome.AUTORIZAR_PAGAMENTO
    );

    private final CessaoRepository cessaoRepository;
    private final CessaoReadModelRepository readModelRepository;
    private final ContratoRepository contratoRepository;
    private final PagamentoRepository pagamentoRepository;
    private final LastroRepository lastroRepository;
    private final RegraElegibilidadeRepository regraElegibilidadeRepository;
    private final EventoAuditoriaRepository eventoAuditoriaRepository;
    private final ProcessMetricsService processMetricsService;

    public CessaoReadModelProjector(
            CessaoRepository cessaoRepository,
            CessaoReadModelRepository readModelRepository,
            ContratoRepository contratoRepository,
            PagamentoRepository pagamentoRepository,
            LastroRepository lastroRepository,
            RegraElegibilidadeRepository regraElegibilidadeRepository,
            EventoAuditoriaRepository eventoAuditoriaRepository,
            ProcessMetricsService processMetricsService
    ) {
        this.cessaoRepository = cessaoRepository;
        this.readModelRepository = readModelRepository;
        this.contratoRepository = contratoRepository;
        this.pagamentoRepository = pagamentoRepository;
        this.lastroRepository = lastroRepository;
        this.regraElegibilidadeRepository = regraElegibilidadeRepository;
        this.eventoAuditoriaRepository = eventoAuditoriaRepository;
        this.processMetricsService = processMetricsService;
    }

    @Transactional(readOnly = true)
    public void projectCurrentState(String businessKey, String lastEvent) {
        OffsetDateTime startedAt = OffsetDateTime.now();
        cessaoRepository.findByBusinessKey(businessKey)
                .ifPresent(cessao -> {
                    readModelRepository.save(toDocument(cessao, lastEvent));
                    processMetricsService.recordProjectionLag(
                            "cessao-read-model",
                            Math.max(0, Duration.between(startedAt, OffsetDateTime.now()).toMillis())
                    );
                });
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
        List<RegraElegibilidade> regras = regraElegibilidadeRepository.findByCessaoBusinessKeyOrderByAvaliadaEmAsc(cessao.getBusinessKey());
        List<com.fidc.cdc.kogito.domain.analise.Contrato> contratos =
                contratoRepository.findByCessaoBusinessKeyOrderByIdentificadorExternoAsc(cessao.getBusinessKey());
        List<com.fidc.cdc.kogito.domain.analise.Pagamento> pagamentos =
                pagamentoRepository.findByCessaoBusinessKeyOrderByCreatedAtAsc(cessao.getBusinessKey());
        List<com.fidc.cdc.kogito.domain.analise.Lastro> lastros =
                lastroRepository.findByCessaoBusinessKeyOrderByRecebidoEmAsc(cessao.getBusinessKey());
        List<EventoAuditoria> eventosAuditoria =
                eventoAuditoriaRepository.findByCessaoBusinessKeyOrderByOcorridoEmAsc(cessao.getBusinessKey());

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
        document.setUltimaAtualizacao(Instant.now());
        document.setResumoFinanceiro(buildResumoFinanceiro(cessao, contratos, pagamentos));
        document.setResumoDocumental(buildResumoDocumental(cessao, regras, lastros));
        document.setResumoAuditoria(buildResumoAuditoria(eventosAuditoria));
        document.setIndicadoresTarefasHumanas(buildIndicadoresTarefasHumanas(cessao.getEtapas()));
        return document;
    }

    private Map<String, Object> buildResumoFinanceiro(
            Cessao cessao,
            List<com.fidc.cdc.kogito.domain.analise.Contrato> contratos,
            List<com.fidc.cdc.kogito.domain.analise.Pagamento> pagamentos
    ) {
        Map<String, Object> resumo = new LinkedHashMap<>();
        resumo.put("valorCalculado", cessao.getValorCalculado());
        resumo.put("valorAprovado", cessao.getValorAprovado());
        resumo.put("totalContratos", contratos.size());
        resumo.put("totalParcelas", contratos.stream().mapToLong(contrato -> contrato.getParcelas().size()).sum());
        resumo.put("totalPagamentos", pagamentos.size());
        resumo.put("ultimoStatusPagamento", pagamentos.isEmpty()
                ? "SEM_PAGAMENTO"
                : pagamentos.get(pagamentos.size() - 1).getStatusPagamento().name());
        return resumo;
    }

    private Map<String, Object> buildResumoDocumental(
            Cessao cessao,
            List<RegraElegibilidade> regras,
            List<com.fidc.cdc.kogito.domain.analise.Lastro> lastros
    ) {
        Map<String, Object> resumo = new LinkedHashMap<>();
        resumo.put("totalEtapas", cessao.getEtapas().size());
        resumo.put("etapasConcluidas", cessao.getEtapas().stream()
                .filter(etapa -> etapa.getStatusEtapa() == EtapaCessaoStatus.CONCLUIDA)
                .count());
        resumo.put("totalRegrasElegibilidade", regras.size());
        resumo.put("regrasImpeditivas", regras.stream().filter(RegraElegibilidade::isFalhaImpeditiva).count());
        resumo.put("totalLastros", lastros.size());
        resumo.put("lastrosValidados", lastros.stream()
                .filter(lastro -> lastro.getStatusValidacao() == StatusValidacaoLastro.VALIDADO)
                .count());
        resumo.put("lastrosRejeitados", lastros.stream()
                .filter(lastro -> lastro.getStatusValidacao() == StatusValidacaoLastro.REJEITADO)
                .count());
        return resumo;
    }

    private Map<String, Object> buildResumoAuditoria(List<EventoAuditoria> eventosAuditoria) {
        Map<String, Object> resumo = new LinkedHashMap<>();
        resumo.put("totalEventos", eventosAuditoria.size());
        resumo.put("ultimoEventoAuditoria", eventosAuditoria.isEmpty()
                ? "SEM_EVENTO"
                : eventosAuditoria.get(eventosAuditoria.size() - 1).getTipoEvento());
        resumo.put("ultimoAtor", eventosAuditoria.isEmpty()
                ? "SEM_ATOR"
                : eventosAuditoria.get(eventosAuditoria.size() - 1).getAtorId());
        return resumo;
    }

    private Map<String, Object> buildIndicadoresTarefasHumanas(List<EtapaCessao> etapas) {
        String etapaAtiva = identifyCurrentStage(etapas);
        boolean humanTaskPending = etapas.stream()
                .filter(etapa -> etapa.getStatusEtapa() == EtapaCessaoStatus.EM_EXECUCAO)
                .map(EtapaCessao::getNomeEtapa)
                .anyMatch(HUMAN_TASK_STAGES::contains);
        Map<String, Object> resumo = new LinkedHashMap<>();
        resumo.put("tarefasHumanasPendentes", humanTaskPending ? 1 : 0);
        resumo.put("etapaHumanaAtual", humanTaskPending ? etapaAtiva : "SEM_ETAPA_HUMANA_ATIVA");
        resumo.put("haAcaoOperacionalPendente", humanTaskPending);
        return resumo;
    }

    private String identifyCurrentStage(List<EtapaCessao> etapas) {
        return etapas.stream()
                .filter(etapa -> etapa.getStatusEtapa() == EtapaCessaoStatus.EM_EXECUCAO)
                .map(etapa -> etapa.getNomeEtapa().name())
                .findFirst()
                .orElse("SEM_ETAPA_ATIVA");
    }
}
