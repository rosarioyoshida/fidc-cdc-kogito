package com.fidc.cdc.kogito.domain.analise;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParcelaRepository extends JpaRepository<Parcela, UUID> {

    List<Parcela> findByContratoCessaoBusinessKeyOrderByNumeroParcelaAsc(String businessKey);
}
