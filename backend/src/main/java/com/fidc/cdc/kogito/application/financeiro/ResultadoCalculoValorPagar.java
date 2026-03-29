package com.fidc.cdc.kogito.application.financeiro;

import java.math.BigDecimal;

/**
 * Representa os dados de resultado calculo valor pagar.
 *
 * <p>Este tipo pertence a camada de orquestracao de casos de uso e servicos de aplicacao. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
public record ResultadoCalculoValorPagar(
        BigDecimal valorCalculado,
        BigDecimal valorAprovado,
        String baseCalculo
) {
}
