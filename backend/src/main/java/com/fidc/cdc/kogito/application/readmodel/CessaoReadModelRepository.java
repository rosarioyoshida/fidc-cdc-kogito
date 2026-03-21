package com.fidc.cdc.kogito.application.readmodel;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CessaoReadModelRepository extends MongoRepository<CessaoReadModelDocument, String> {

    List<CessaoReadModelDocument> findByStatusAtual(String statusAtual);
}
