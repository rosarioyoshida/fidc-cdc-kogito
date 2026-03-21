package com.fidc.cdc.kogito.domain.analise;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfertaRegistradoraRepository extends JpaRepository<OfertaRegistradora, UUID> {

    List<OfertaRegistradora> findByCessaoBusinessKeyOrderByExecutadaEmAsc(String businessKey);
}
