package com.fidc.cdc.kogito.domain.analise;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegraElegibilidadeRepository extends JpaRepository<RegraElegibilidade, UUID> {

    void deleteByCessaoBusinessKey(String businessKey);

    List<RegraElegibilidade> findByCessaoBusinessKeyOrderByAvaliadaEmAsc(String businessKey);
}
