package com.fidc.cdc.kogito.domain.security;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerfilAcessoRepository extends JpaRepository<PerfilAcesso, UUID> {

    Optional<PerfilAcesso> findByNome(String nome);
}
