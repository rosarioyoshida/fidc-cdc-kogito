package com.fidc.cdc.kogito.domain.security;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    @EntityGraph(attributePaths = {"perfis", "perfis.permissoes"})
    Optional<Usuario> findByUsernameAndAtivoTrue(String username);
}
