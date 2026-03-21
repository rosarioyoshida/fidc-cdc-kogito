package com.fidc.cdc.kogito.domain.analise;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LastroRepository extends JpaRepository<Lastro, UUID> {

    List<Lastro> findByCessaoBusinessKeyOrderByRecebidoEmAsc(String businessKey);
}
