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
import jakarta.persistence.UniqueConstraint;
import java.time.OffsetDateTime;

@Entity
@Table(
        name = "termo_aceite",
        uniqueConstraints = @UniqueConstraint(name = "uq_termo_cessao_versao", columnNames = {
                "cessao_id",
                "versao"
        })
)
public class TermoAceite extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cessao_id", nullable = false)
    private Cessao cessao;

    @Column(nullable = false)
    private Integer versao;

    @Column(name = "emitido_em", nullable = false)
    private OffsetDateTime emitidoEm;

    @Column(name = "referencia_documento", nullable = false, length = 240)
    private String referenciaDocumento;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_documento", nullable = false, length = 20)
    private StatusDocumentoTermoAceite statusDocumento;

    public Cessao getCessao() {
        return cessao;
    }

    public void setCessao(Cessao cessao) {
        this.cessao = cessao;
    }

    public Integer getVersao() {
        return versao;
    }

    public void setVersao(Integer versao) {
        this.versao = versao;
    }

    public OffsetDateTime getEmitidoEm() {
        return emitidoEm;
    }

    public void setEmitidoEm(OffsetDateTime emitidoEm) {
        this.emitidoEm = emitidoEm;
    }

    public String getReferenciaDocumento() {
        return referenciaDocumento;
    }

    public void setReferenciaDocumento(String referenciaDocumento) {
        this.referenciaDocumento = referenciaDocumento;
    }

    public StatusDocumentoTermoAceite getStatusDocumento() {
        return statusDocumento;
    }

    public void setStatusDocumento(StatusDocumentoTermoAceite statusDocumento) {
        this.statusDocumento = statusDocumento;
    }
}
