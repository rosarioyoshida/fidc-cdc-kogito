package com.fidc.cdc.kogito.api.cessao;

import com.fidc.cdc.kogito.domain.cessao.CessaoStatus;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.hateoas.RepresentationModel;

/**
 * Representa a resposta de cessao.
 *
 * <p>Este tipo pertence a camada de superficie HTTP e contratos de transporte. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
public class CessaoResponse extends RepresentationModel<CessaoResponse> {

    private String businessKey;
    private CessaoStatus status;
    private String workflowInstanceId;
    private String cedenteId;
    private String cessionariaId;
    private OffsetDateTime dataImportacao;
    private OffsetDateTime dataEncerramento;
    private List<EtapaResponse> etapas = new ArrayList<>();

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

    public List<EtapaResponse> getEtapas() {
        return etapas;
    }

    public void setEtapas(List<EtapaResponse> etapas) {
        this.etapas = etapas;
    }
}
