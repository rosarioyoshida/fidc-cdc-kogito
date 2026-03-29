package com.fidc.cdc.kogito.domain.cessao;

/**
 * Enumera os estados ou valores de etapa cessao status.
 *
 * <p>Este tipo pertence a camada de modelo de dominio e contratos de persistencia. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
public enum EtapaCessaoStatus {
    PENDENTE,
    EM_EXECUCAO,
    CONCLUIDA,
    FALHA,
    CANCELADA
}
