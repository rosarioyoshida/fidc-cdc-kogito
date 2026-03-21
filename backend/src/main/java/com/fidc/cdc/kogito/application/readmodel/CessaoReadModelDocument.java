package com.fidc.cdc.kogito.application.readmodel;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "cessao_read_model")
public class CessaoReadModelDocument {

    @Id
    private String cessaoBusinessKey;

    private String statusAtual;
    private String etapaAtual;
    private List<String> pendencias;
    private String ultimoEvento;
    private Instant ultimaAtualizacao;
    private Map<String, Object> resumoFinanceiro;
    private Map<String, Object> resumoDocumental;
    private Map<String, Object> resumoAuditoria;
    private Map<String, Object> indicadoresTarefasHumanas;

    public String getCessaoBusinessKey() {
        return cessaoBusinessKey;
    }

    public void setCessaoBusinessKey(String cessaoBusinessKey) {
        this.cessaoBusinessKey = cessaoBusinessKey;
    }

    public String getStatusAtual() {
        return statusAtual;
    }

    public void setStatusAtual(String statusAtual) {
        this.statusAtual = statusAtual;
    }

    public String getEtapaAtual() {
        return etapaAtual;
    }

    public void setEtapaAtual(String etapaAtual) {
        this.etapaAtual = etapaAtual;
    }

    public List<String> getPendencias() {
        return pendencias;
    }

    public void setPendencias(List<String> pendencias) {
        this.pendencias = pendencias;
    }

    public String getUltimoEvento() {
        return ultimoEvento;
    }

    public void setUltimoEvento(String ultimoEvento) {
        this.ultimoEvento = ultimoEvento;
    }

    public Instant getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }

    public void setUltimaAtualizacao(Instant ultimaAtualizacao) {
        this.ultimaAtualizacao = ultimaAtualizacao;
    }

    public Map<String, Object> getResumoFinanceiro() {
        return resumoFinanceiro;
    }

    public void setResumoFinanceiro(Map<String, Object> resumoFinanceiro) {
        this.resumoFinanceiro = resumoFinanceiro;
    }

    public Map<String, Object> getResumoDocumental() {
        return resumoDocumental;
    }

    public void setResumoDocumental(Map<String, Object> resumoDocumental) {
        this.resumoDocumental = resumoDocumental;
    }

    public Map<String, Object> getResumoAuditoria() {
        return resumoAuditoria;
    }

    public void setResumoAuditoria(Map<String, Object> resumoAuditoria) {
        this.resumoAuditoria = resumoAuditoria;
    }

    public Map<String, Object> getIndicadoresTarefasHumanas() {
        return indicadoresTarefasHumanas;
    }

    public void setIndicadoresTarefasHumanas(Map<String, Object> indicadoresTarefasHumanas) {
        this.indicadoresTarefasHumanas = indicadoresTarefasHumanas;
    }
}
