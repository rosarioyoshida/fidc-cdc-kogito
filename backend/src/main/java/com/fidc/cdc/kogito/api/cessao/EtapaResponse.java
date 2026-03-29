package com.fidc.cdc.kogito.api.cessao;

import com.fidc.cdc.kogito.domain.cessao.EtapaCessaoNome;
import com.fidc.cdc.kogito.domain.cessao.EtapaCessaoStatus;
import java.time.OffsetDateTime;
import org.springframework.hateoas.RepresentationModel;

/**
 * Representa a resposta de etapa.
 *
 * <p>Este tipo pertence a camada de superficie HTTP e contratos de transporte. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
public class EtapaResponse extends RepresentationModel<EtapaResponse> {

    private EtapaCessaoNome nomeEtapa;
    private EtapaCessaoStatus statusEtapa;
    private int ordem;
    private String responsavelId;
    private OffsetDateTime inicioEm;
    private OffsetDateTime concluidaEm;
    private String resultado;
    private String justificativa;

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
