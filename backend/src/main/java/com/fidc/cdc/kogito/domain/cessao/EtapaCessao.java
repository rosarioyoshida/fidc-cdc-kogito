package com.fidc.cdc.kogito.domain.cessao;

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

@Entity
@Table(name = "etapa_cessao")
public class EtapaCessao extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cessao_id", nullable = false)
    private Cessao cessao;

    @Enumerated(EnumType.STRING)
    @Column(name = "nome_etapa", nullable = false, length = 80)
    private EtapaCessaoNome nomeEtapa;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_etapa", nullable = false, length = 40)
    private EtapaCessaoStatus statusEtapa;

    @Column(nullable = false)
    private int ordem;

    @Column(name = "responsavel_id", length = 120)
    private String responsavelId;

    @Column(name = "inicio_em")
    private OffsetDateTime inicioEm;

    @Column(name = "concluida_em")
    private OffsetDateTime concluidaEm;

    @Column(length = 120)
    private String resultado;

    @Column(length = 500)
    private String justificativa;

    public Cessao getCessao() {
        return cessao;
    }

    public void setCessao(Cessao cessao) {
        this.cessao = cessao;
    }

    public EtapaCessaoNome getNomeEtapa() {
        return nomeEtapa;
    }

    public void setNomeEtapa(EtapaCessaoNome nomeEtapa) {
        this.nomeEtapa = nomeEtapa;
    }

    public EtapaCessaoStatus getStatusEtapa() {
        return statusEtapa;
    }

    public void setStatusEtapa(EtapaCessaoStatus statusEtapa) {
        this.statusEtapa = statusEtapa;
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }

    public String getResponsavelId() {
        return responsavelId;
    }

    public void setResponsavelId(String responsavelId) {
        this.responsavelId = responsavelId;
    }

    public OffsetDateTime getInicioEm() {
        return inicioEm;
    }

    public void setInicioEm(OffsetDateTime inicioEm) {
        this.inicioEm = inicioEm;
    }

    public OffsetDateTime getConcluidaEm() {
        return concluidaEm;
    }

    public void setConcluidaEm(OffsetDateTime concluidaEm) {
        this.concluidaEm = concluidaEm;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }
}
