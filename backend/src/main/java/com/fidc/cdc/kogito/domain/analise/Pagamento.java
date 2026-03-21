package com.fidc.cdc.kogito.domain.analise;

import com.fidc.cdc.kogito.domain.cessao.Cessao;
import com.fidc.cdc.kogito.infrastructure.persistence.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "pagamento")
public class Pagamento extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cessao_id", nullable = false)
    private Cessao cessao;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_pagamento", nullable = false, length = 20)
    private StatusPagamento statusPagamento;

    @Column(name = "autorizado_por", length = 120)
    private String autorizadoPor;

    @Column(name = "autorizado_em")
    private OffsetDateTime autorizadoEm;

    @Column(name = "confirmado_em")
    private OffsetDateTime confirmadoEm;

    @Column(name = "comprovante_referencia", length = 240)
    private String comprovanteReferencia;

    public Cessao getCessao() {
        return cessao;
    }

    public void setCessao(Cessao cessao) {
        this.cessao = cessao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public StatusPagamento getStatusPagamento() {
        return statusPagamento;
    }

    public void setStatusPagamento(StatusPagamento statusPagamento) {
        this.statusPagamento = statusPagamento;
    }

    public String getAutorizadoPor() {
        return autorizadoPor;
    }

    public void setAutorizadoPor(String autorizadoPor) {
        this.autorizadoPor = autorizadoPor;
    }

    public OffsetDateTime getAutorizadoEm() {
        return autorizadoEm;
    }

    public void setAutorizadoEm(OffsetDateTime autorizadoEm) {
        this.autorizadoEm = autorizadoEm;
    }

    public OffsetDateTime getConfirmadoEm() {
        return confirmadoEm;
    }

    public void setConfirmadoEm(OffsetDateTime confirmadoEm) {
        this.confirmadoEm = confirmadoEm;
    }

    public String getComprovanteReferencia() {
        return comprovanteReferencia;
    }

    public void setComprovanteReferencia(String comprovanteReferencia) {
        this.comprovanteReferencia = comprovanteReferencia;
    }
}
