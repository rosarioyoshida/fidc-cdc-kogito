package com.fidc.cdc.kogito.domain.security;

import com.fidc.cdc.kogito.infrastructure.persistence.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "usuario")
public class Usuario extends BaseEntity {

    @Column(nullable = false, unique = true, length = 120)
    private String username;

    @Column(name = "nome_exibicao", nullable = false, length = 160)
    private String nomeExibicao;

    @Column(nullable = false)
    private boolean ativo;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "usuario_perfil_acesso",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "perfil_acesso_id")
    )
    private Set<PerfilAcesso> perfis = new LinkedHashSet<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNomeExibicao() {
        return nomeExibicao;
    }

    public void setNomeExibicao(String nomeExibicao) {
        this.nomeExibicao = nomeExibicao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Set<PerfilAcesso> getPerfis() {
        return perfis;
    }

    public void setPerfis(Set<PerfilAcesso> perfis) {
        this.perfis = perfis;
    }
}
