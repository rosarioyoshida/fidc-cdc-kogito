package com.fidc.cdc.kogito.domain.security;

import com.fidc.cdc.kogito.infrastructure.persistence.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "perfil_acesso")
public class PerfilAcesso extends BaseEntity {

    @Column(nullable = false, unique = true, length = 80)
    private String nome;

    @Column(nullable = false, length = 240)
    private String descricao;

    @OneToMany(mappedBy = "perfilAcesso", fetch = FetchType.LAZY)
    private Set<PermissaoEtapa> permissoes = new LinkedHashSet<>();

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Set<PermissaoEtapa> getPermissoes() {
        return permissoes;
    }

    public void setPermissoes(Set<PermissaoEtapa> permissoes) {
        this.permissoes = permissoes;
    }
}
