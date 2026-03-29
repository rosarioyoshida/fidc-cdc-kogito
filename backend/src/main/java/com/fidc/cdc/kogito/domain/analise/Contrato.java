package com.fidc.cdc.kogito.domain.analise;

import com.fidc.cdc.kogito.domain.cessao.Cessao;
import com.fidc.cdc.kogito.infrastructure.persistence.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa contrato no backend de cessao.
 *
 * <p>Este tipo pertence a camada de modelo de dominio e contratos de persistencia. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
@Entity
@Table(
        name = "contrato",
        uniqueConstraints = @UniqueConstraint(name = "uq_contrato_cessao_identificador", columnNames = {
                "cessao_id",
                "identificador_externo"
        })
)
public class Contrato extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cessao_id", nullable = false)
    private Cessao cessao;

    @Column(name = "identificador_externo", nullable = false, length = 120)
    private String identificadorExterno;

    @Column(name = "sacado_id", nullable = false, length = 120)
    private String sacadoId;

    @Column(name = "valor_nominal", nullable = false, precision = 19, scale = 2)
    private BigDecimal valorNominal;

    @Column(name = "data_origem", nullable = false)
    private LocalDate dataOrigem;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_registro", nullable = false, length = 20)
    private StatusRegistroAnalise statusRegistro;

    @OneToMany(mappedBy = "contrato", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("numeroParcela ASC")
    private List<Parcela> parcelas = new ArrayList<>();

    public void addParcela(Parcela parcela) {
        parcela.setContrato(this);
        this.parcelas.add(parcela);
    }

    public Cessao getCessao() {
        return cessao;
    }

    public void setCessao(Cessao cessao) {
        this.cessao = cessao;
    }

    public String getIdentificadorExterno() {
        return identificadorExterno;
    }

    public void setIdentificadorExterno(String identificadorExterno) {
        this.identificadorExterno = identificadorExterno;
    }

    public String getSacadoId() {
        return sacadoId;
    }

    public void setSacadoId(String sacadoId) {
        this.sacadoId = sacadoId;
    }

    public BigDecimal getValorNominal() {
        return valorNominal;
    }

    public void setValorNominal(BigDecimal valorNominal) {
        this.valorNominal = valorNominal;
    }

    public LocalDate getDataOrigem() {
        return dataOrigem;
    }

    public void setDataOrigem(LocalDate dataOrigem) {
        this.dataOrigem = dataOrigem;
    }

    public StatusRegistroAnalise getStatusRegistro() {
        return statusRegistro;
    }

    public void setStatusRegistro(StatusRegistroAnalise statusRegistro) {
        this.statusRegistro = statusRegistro;
    }

    public List<Parcela> getParcelas() {
        return parcelas;
    }

    public void setParcelas(List<Parcela> parcelas) {
        this.parcelas = parcelas;
    }
}
