package com.fidc.cdc.kogito.domain.cessao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CessaoRepository extends JpaRepository<Cessao, UUID> {

    boolean existsByBusinessKey(String businessKey);

    @EntityGraph(attributePaths = "etapas")
    Optional<Cessao> findByBusinessKey(String businessKey);

    @EntityGraph(attributePaths = "etapas")
    Optional<Cessao> findByWorkflowInstanceId(String workflowInstanceId);

    List<Cessao> findByStatus(CessaoStatus status);
}
