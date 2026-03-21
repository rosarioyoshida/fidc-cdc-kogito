package com.fidc.cdc.kogito.domain.analise;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository extends JpaRepository<Pagamento, UUID> {

    List<Pagamento> findByCessaoBusinessKeyOrderByCreatedAtAsc(String businessKey);

    Optional<Pagamento> findFirstByCessaoBusinessKeyOrderByCreatedAtDesc(String businessKey);
}
