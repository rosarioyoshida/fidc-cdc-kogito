package com.fidc.cdc.kogito.application.readmodel;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Define operacoes de persistencia para cessao read model.
 *
 * <p>Este tipo pertence a camada de orquestracao de casos de uso e servicos de aplicacao. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
public interface CessaoReadModelRepository extends JpaRepository<CessaoReadModelDocument, String> {

    List<CessaoReadModelDocument> findByStatusAtual(String statusAtual);
}
