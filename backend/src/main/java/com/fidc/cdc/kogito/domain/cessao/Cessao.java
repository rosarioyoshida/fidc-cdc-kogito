package com.fidc.cdc.kogito.domain.cessao;

import com.fidc.cdc.kogito.infrastructure.persistence.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cessao")
public class Cessao extends BaseEntity {

    @Column(name = "business_key", nullable = false, unique = true, length = 120)
    private String businessKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private CessaoStatus status;

    @Column(name = "workflow_instance_id", length = 120)
    private String workflowInstanceId;

    @Column(name = "cedente_id", nullable = false, length = 120)
    private String cedenteId;

    @Column(name = "cessionaria_id", nullable = false, length = 120)
    private String cessionariaId;

    @Column(name = "valor_calculado", precision = 19, scale = 2)
    private BigDecimal valorCalculado;

    @Column(name = "valor_aprovado", precision = 19, scale = 2)
    private BigDecimal valorAprovado;

    @Column(name = "data_importacao", nullable = false)
    private OffsetDateTime dataImportacao;

    @Column(name = "data_encerramento")
    private OffsetDateTime dataEncerramento;

    @OneToMany(mappedBy = "cessao", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("ordem ASC")
    private List<EtapaCessao> etapas = new ArrayList<>();

    public void addEtapa(EtapaCessao etapa) {
        etapa.setCessao(this);
        this.etapas.add(etapa);
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public CessaoStatus getStatus() {
        return status;
    }

    public void setStatus(CessaoStatus status) {
        this.status = status;
    }

    public String getWorkflowInstanceId() {
        return workflowInstanceId;
    }

    public void setWorkflowInstanceId(String workflowInstanceId) {
        this.workflowInstanceId = workflowInstanceId;
    }

    public String getCedenteId() {
        return cedenteId;
    }

    public void setCedenteId(String cedenteId) {
        this.cedenteId = cedenteId;
    }

    public String getCessionariaId() {
        return cessionariaId;
    }

    public void setCessionariaId(String cessionariaId) {
        this.cessionariaId = cessionariaId;
    }

    public BigDecimal getValorCalculado() {
        return valorCalculado;
    }

    public void setValorCalculado(BigDecimal valorCalculado) {
        this.valorCalculado = valorCalculado;
    }

    public BigDecimal getValorAprovado() {
        return valorAprovado;
    }

    public void setValorAprovado(BigDecimal valorAprovado) {
        this.valorAprovado = valorAprovado;
    }

    public OffsetDateTime getDataImportacao() {
        return dataImportacao;
    }

    public void setDataImportacao(OffsetDateTime dataImportacao) {
        this.dataImportacao = dataImportacao;
    }

    public OffsetDateTime getDataEncerramento() {
        return dataEncerramento;
    }

    public void setDataEncerramento(OffsetDateTime dataEncerramento) {
        this.dataEncerramento = dataEncerramento;
    }

    public List<EtapaCessao> getEtapas() {
        return etapas;
    }

    public void setEtapas(List<EtapaCessao> etapas) {
        this.etapas = etapas;
    }
}
