package com.fidc.cdc.kogito.domain.analise;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermoAceiteRepository extends JpaRepository<TermoAceite, UUID> {

    List<TermoAceite> findByCessaoBusinessKeyOrderByVersaoDesc(String businessKey);
}
