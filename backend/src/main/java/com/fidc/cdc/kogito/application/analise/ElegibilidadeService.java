package com.fidc.cdc.kogito.application.analise;

import com.fidc.cdc.kogito.api.error.ResourceNotFoundException;
import com.fidc.cdc.kogito.domain.analise.Contrato;
import com.fidc.cdc.kogito.domain.analise.ContratoRepository;
import com.fidc.cdc.kogito.domain.analise.Lastro;
import com.fidc.cdc.kogito.domain.analise.LastroRepository;
import com.fidc.cdc.kogito.domain.analise.RegraElegibilidade;
import com.fidc.cdc.kogito.domain.analise.RegraElegibilidadeRepository;
import com.fidc.cdc.kogito.domain.analise.ResultadoRegraElegibilidade;
import com.fidc.cdc.kogito.domain.analise.SeveridadeRegraElegibilidade;
import com.fidc.cdc.kogito.domain.cessao.Cessao;
import com.fidc.cdc.kogito.domain.cessao.CessaoRepository;
import com.fidc.cdc.kogito.domain.cessao.CessaoStatus;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ElegibilidadeService {

    private final CessaoRepository cessaoRepository;
    private final ContratoRepository contratoRepository;
    private final LastroRepository lastroRepository;
    private final RegraElegibilidadeRepository regraElegibilidadeRepository;

    public ElegibilidadeService(
            CessaoRepository cessaoRepository,
            ContratoRepository contratoRepository,
            LastroRepository lastroRepository,
            RegraElegibilidadeRepository regraElegibilidadeRepository
    ) {
        this.cessaoRepository = cessaoRepository;
        this.contratoRepository = contratoRepository;
        this.lastroRepository = lastroRepository;
        this.regraElegibilidadeRepository = regraElegibilidadeRepository;
    }

    @Transactional
    public ResultadoAvaliacaoElegibilidade avaliar(String businessKey) {
        Cessao cessao = cessaoRepository.findByBusinessKey(businessKey)
                .orElseThrow(() -> new ResourceNotFoundException("Cessao nao encontrada para avaliacao de elegibilidade."));
        List<Contrato> contratos = contratoRepository.findByCessaoBusinessKeyOrderByIdentificadorExternoAsc(businessKey);
        List<Lastro> lastros = lastroRepository.findByCessaoBusinessKeyOrderByRecebidoEmAsc(businessKey);

        regraElegibilidadeRepository.deleteByCessaoBusinessKey(businessKey);

        List<RegraElegibilidade> regras = new ArrayList<>();
        regras.add(buildRule(
                cessao,
                "CONTRATOS_PRESENTES",
                "Cessao possui contratos importados",
                !contratos.isEmpty(),
                SeveridadeRegraElegibilidade.IMPEDITIVA,
                contratos.isEmpty()
                        ? "A cessao nao possui contratos vinculados para analise."
                        : "Foram encontrados contratos vinculados para analise."
        ));
        regras.add(buildRule(
                cessao,
                "VALOR_TOTAL_POSITIVO",
                "Somatorio financeiro da cessao e positivo",
                contratos.stream().allMatch(contrato -> contrato.getValorNominal() != null
                        && contrato.getValorNominal().compareTo(BigDecimal.ZERO) > 0),
                SeveridadeRegraElegibilidade.IMPEDITIVA,
                contratos.stream().anyMatch(contrato -> contrato.getValorNominal() == null
                        || contrato.getValorNominal().compareTo(BigDecimal.ZERO) <= 0)
                        ? "Existe contrato com valor nominal ausente ou nao positivo."
                        : "Todos os contratos possuem valor nominal valido."
        ));
        regras.add(buildRule(
                cessao,
                "LASTROS_RECEBIDOS",
                "Cessao possui lastros recebidos para validacao posterior",
                !lastros.isEmpty(),
                SeveridadeRegraElegibilidade.ATENCAO,
                lastros.isEmpty()
                        ? "Nao ha lastros recebidos ate o momento para a cessao."
                        : "Foram recebidos lastros para processamento documental."
        ));

        List<RegraElegibilidade> salvas = regraElegibilidadeRepository.saveAll(regras);
        boolean possuiBloqueios = salvas.stream().anyMatch(RegraElegibilidade::isFalhaImpeditiva);
        if (possuiBloqueios) {
            cessao.setStatus(CessaoStatus.FALHA);
        }
        return new ResultadoAvaliacaoElegibilidade(salvas, possuiBloqueios);
    }

    private RegraElegibilidade buildRule(
            Cessao cessao,
            String codigoRegra,
            String descricao,
            boolean aprovada,
            SeveridadeRegraElegibilidade severidadeFalha,
            String mensagem
    ) {
        RegraElegibilidade regra = new RegraElegibilidade();
        regra.setCessao(cessao);
        regra.setCodigoRegra(codigoRegra);
        regra.setDescricao(descricao);
        regra.setResultado(aprovada ? ResultadoRegraElegibilidade.APROVADA : ResultadoRegraElegibilidade.REPROVADA);
        regra.setSeveridade(aprovada ? SeveridadeRegraElegibilidade.INFORMATIVA : severidadeFalha);
        regra.setMensagem(mensagem);
        regra.setAvaliadaEm(OffsetDateTime.now());
        return regra;
    }
}
