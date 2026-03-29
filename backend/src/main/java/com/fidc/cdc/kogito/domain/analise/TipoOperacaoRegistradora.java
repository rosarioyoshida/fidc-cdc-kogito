package com.fidc.cdc.kogito.domain.analise;

/**
 * Enumera os estados ou valores de tipo operacao registradora.
 *
 * <p>Este tipo pertence a camada de modelo de dominio e contratos de persistencia. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
public enum TipoOperacaoRegistradora {
    CARTEIRA,
    CONTRATO,
    PARCELA,
    OFERTA,
    ACEITE,
    PAGAMENTO,
    LASTRO
}
