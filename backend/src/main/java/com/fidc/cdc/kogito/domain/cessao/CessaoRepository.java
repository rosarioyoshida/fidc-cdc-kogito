package com.fidc.cdc.kogito.domain.cessao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Define operacoes de persistencia para cessao.
 *
 * <p>Este tipo pertence a camada de modelo de dominio e contratos de persistencia. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
public interface CessaoRepository extends JpaRepository<Cessao, UUID> {

    boolean existsByBusinessKey(String businessKey);

    @EntityGraph(attributePaths = "etapas")
    Optional<Cessao> findByBusinessKey(String businessKey);

    @EntityGraph(attributePaths = "etapas")
    Optional<Cessao> findByWorkflowInstanceId(String workflowInstanceId);

    List<Cessao> findByStatus(CessaoStatus status);
}
