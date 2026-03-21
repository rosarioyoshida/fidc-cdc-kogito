package com.fidc.cdc.kogito.domain.cessao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EtapaCessaoRepository extends JpaRepository<EtapaCessao, UUID> {

    List<EtapaCessao> findByCessaoBusinessKeyOrderByOrdemAsc(String businessKey);

    Optional<EtapaCessao> findByCessaoBusinessKeyAndNomeEtapa(String businessKey, EtapaCessaoNome nomeEtapa);
}
