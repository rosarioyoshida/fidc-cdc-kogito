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

@Entity
@Table(name = "regra_elegibilidade")
public class RegraElegibilidade extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cessao_id", nullable = false)
    private Cessao cessao;

    @Column(name = "codigo_regra", nullable = false, length = 80)
    private String codigoRegra;

    @Column(nullable = false, length = 240)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ResultadoRegraElegibilidade resultado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SeveridadeRegraElegibilidade severidade;

    @Column(nullable = false, length = 500)
    private String mensagem;

    @Column(name = "avaliada_em", nullable = false)
    private OffsetDateTime avaliadaEm;

    public boolean isFalhaImpeditiva() {
        return resultado == ResultadoRegraElegibilidade.REPROVADA
                && severidade == SeveridadeRegraElegibilidade.IMPEDITIVA;
    }

    public Cessao getCessao() {
        return cessao;
    }

    public void setCessao(Cessao cessao) {
        this.cessao = cessao;
    }

    public String getCodigoRegra() {
        return codigoRegra;
    }

    public void setCodigoRegra(String codigoRegra) {
        this.codigoRegra = codigoRegra;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public ResultadoRegraElegibilidade getResultado() {
        return resultado;
    }

    public void setResultado(ResultadoRegraElegibilidade resultado) {
        this.resultado = resultado;
    }

    public SeveridadeRegraElegibilidade getSeveridade() {
        return severidade;
    }

    public void setSeveridade(SeveridadeRegraElegibilidade severidade) {
        this.severidade = severidade;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public OffsetDateTime getAvaliadaEm() {
        return avaliadaEm;
    }

    public void setAvaliadaEm(OffsetDateTime avaliadaEm) {
        this.avaliadaEm = avaliadaEm;
    }
}
