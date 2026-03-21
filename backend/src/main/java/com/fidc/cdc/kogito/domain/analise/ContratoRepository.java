package com.fidc.cdc.kogito.domain.analise;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContratoRepository extends JpaRepository<Contrato, UUID> {

    @EntityGraph(attributePaths = "parcelas")
    List<Contrato> findByCessaoBusinessKeyOrderByIdentificadorExternoAsc(String businessKey);

    Optional<Contrato> findByCessaoBusinessKeyAndIdentificadorExterno(String businessKey, String identificadorExterno);
}
