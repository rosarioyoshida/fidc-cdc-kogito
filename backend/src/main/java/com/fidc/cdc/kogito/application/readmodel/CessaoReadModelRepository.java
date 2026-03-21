package com.fidc.cdc.kogito.application.readmodel;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CessaoReadModelRepository extends JpaRepository<CessaoReadModelDocument, String> {

    List<CessaoReadModelDocument> findByStatusAtual(String statusAtual);
}
