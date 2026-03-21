package com.fidc.cdc.kogito.domain.audit;

import com.fidc.cdc.kogito.domain.cessao.Cessao;
import com.fidc.cdc.kogito.domain.cessao.EtapaCessao;
import com.fidc.cdc.kogito.infrastructure.persistence.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "evento_auditoria")
public class EventoAuditoria extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cessao_id", nullable = false)
    private Cessao cessao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etapa_id")
    private EtapaCessao etapa;

    @Column(name = "ator_id", nullable = false, length = 120)
    private String atorId;

    @Column(nullable = false, length = 80)
    private String perfil;

    @Column(name = "tipo_evento", nullable = false, length = 120)
    private String tipoEvento;

    @Column(nullable = false, length = 80)
    private String resultado;

    @Column(name = "correlation_id", nullable = false, length = 120)
    private String correlationId;

    @Column(name = "ocorrido_em", nullable = false)
    private OffsetDateTime ocorridoEm;

    @Column(name = "detalhe_ref", length = 4000)
    private String detalheRef;

    public Cessao getCessao() {
        return cessao;
    }

    public void setCessao(Cessao cessao) {
        this.cessao = cessao;
    }

    public EtapaCessao getEtapa() {
        return etapa;
    }

    public void setEtapa(EtapaCessao etapa) {
        this.etapa = etapa;
    }

    public String getAtorId() {
        return atorId;
    }

    public void setAtorId(String atorId) {
        this.atorId = atorId;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public OffsetDateTime getOcorridoEm() {
        return ocorridoEm;
    }

    public void setOcorridoEm(OffsetDateTime ocorridoEm) {
        this.ocorridoEm = ocorridoEm;
    }

    public String getDetalheRef() {
        return detalheRef;
    }

    public void setDetalheRef(String detalheRef) {
        this.detalheRef = detalheRef;
    }
}
