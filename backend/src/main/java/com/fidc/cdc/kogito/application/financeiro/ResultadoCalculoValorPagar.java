package com.fidc.cdc.kogito.application.financeiro;

import java.math.BigDecimal;

public record ResultadoCalculoValorPagar(
        BigDecimal valorCalculado,
        BigDecimal valorAprovado,
        String baseCalculo
) {
}
