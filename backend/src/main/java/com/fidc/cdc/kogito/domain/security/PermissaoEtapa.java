package com.fidc.cdc.kogito.domain.security;

import com.fidc.cdc.kogito.domain.cessao.EtapaCessaoNome;
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

@Entity
@Table(
        name = "permissao_etapa",
        uniqueConstraints = @UniqueConstraint(name = "uq_permissao_etapa", columnNames = {
                "perfil_acesso_id",
                "nome_etapa"
        })
)
public class PermissaoEtapa extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "perfil_acesso_id", nullable = false)
    private PerfilAcesso perfilAcesso;

    @Enumerated(EnumType.STRING)
    @Column(name = "nome_etapa", nullable = false, length = 80)
    private EtapaCessaoNome nomeEtapa;

    public PerfilAcesso getPerfilAcesso() {
        return perfilAcesso;
    }

    public void setPerfilAcesso(PerfilAcesso perfilAcesso) {
        this.perfilAcesso = perfilAcesso;
    }

    public EtapaCessaoNome getNomeEtapa() {
        return nomeEtapa;
    }

    public void setNomeEtapa(EtapaCessaoNome nomeEtapa) {
        this.nomeEtapa = nomeEtapa;
    }
}
