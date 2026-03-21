package com.fidc.cdc.kogito.domain.security;

import com.fidc.cdc.kogito.domain.cessao.EtapaCessaoNome;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissaoEtapaRepository extends JpaRepository<PermissaoEtapa, UUID> {

    List<PermissaoEtapa> findByPerfilAcessoNomeAndNomeEtapa(String perfil, EtapaCessaoNome nomeEtapa);
}
