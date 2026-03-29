package com.fidc.cdc.kogito.domain.analise;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Define operacoes de persistencia para termo aceite.
 *
 * <p>Este tipo pertence a camada de modelo de dominio e contratos de persistencia. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
public interface TermoAceiteRepository extends JpaRepository<TermoAceite, UUID> {

    List<TermoAceite> findByCessaoBusinessKeyOrderByVersaoDesc(String businessKey);
}
