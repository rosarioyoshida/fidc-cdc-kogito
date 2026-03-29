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
import java.time.OffsetDateTime;

/**
 * Representa oferta registradora no backend de cessao.
 *
 * <p>Este tipo pertence a camada de modelo de dominio e contratos de persistencia. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
@Entity
@Table(name = "oferta_registradora")
public class OfertaRegistradora extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cessao_id", nullable = false)
    private Cessao cessao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_operacao", nullable = false, length = 20)
    private TipoOperacaoRegistradora tipoOperacao;

    @Column(name = "request_id", nullable = false, length = 120)
    private String requestId;

    @Column(name = "http_status")
    private Integer httpStatus;

    @Column(name = "status_negocio", length = 80)
    private String statusNegocio;

    @Column(nullable = false)
    private Integer tentativa;

    @Column(name = "request_payload_ref", length = 240)
    private String requestPayloadRef;

    @Column(name = "response_payload_ref", length = 240)
    private String responsePayloadRef;

    @Column(name = "executada_em", nullable = false)
    private OffsetDateTime executadaEm;

    public Cessao getCessao() {
        return cessao;
    }

    public void setCessao(Cessao cessao) {
        this.cessao = cessao;
    }

    public TipoOperacaoRegistradora getTipoOperacao() {
        return tipoOperacao;
    }

    public void setTipoOperacao(TipoOperacaoRegistradora tipoOperacao) {
        this.tipoOperacao = tipoOperacao;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(Integer httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getStatusNegocio() {
        return statusNegocio;
    }

    public void setStatusNegocio(String statusNegocio) {
        this.statusNegocio = statusNegocio;
    }

    public Integer getTentativa() {
        return tentativa;
    }

    public void setTentativa(Integer tentativa) {
        this.tentativa = tentativa;
    }

    public String getRequestPayloadRef() {
        return requestPayloadRef;
    }

    public void setRequestPayloadRef(String requestPayloadRef) {
        this.requestPayloadRef = requestPayloadRef;
    }

    public String getResponsePayloadRef() {
        return responsePayloadRef;
    }

    public void setResponsePayloadRef(String responsePayloadRef) {
        this.responsePayloadRef = responsePayloadRef;
    }

    public OffsetDateTime getExecutadaEm() {
        return executadaEm;
    }

    public void setExecutadaEm(OffsetDateTime executadaEm) {
        this.executadaEm = executadaEm;
    }
}
