package com.fidc.cdc.kogito.domain.audit;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoAuditoriaRepository extends JpaRepository<EventoAuditoria, UUID> {

    @EntityGraph(attributePaths = {"cessao", "etapa"})
    List<EventoAuditoria> findByCessaoBusinessKeyOrderByOcorridoEmAsc(String businessKey);
}
