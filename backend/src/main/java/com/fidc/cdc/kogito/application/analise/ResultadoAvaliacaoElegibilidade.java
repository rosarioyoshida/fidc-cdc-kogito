package com.fidc.cdc.kogito.application.analise;

import com.fidc.cdc.kogito.domain.analise.RegraElegibilidade;
import java.util.List;

public record ResultadoAvaliacaoElegibilidade(
        List<RegraElegibilidade> regras,
        boolean possuiBloqueios
) {
}
