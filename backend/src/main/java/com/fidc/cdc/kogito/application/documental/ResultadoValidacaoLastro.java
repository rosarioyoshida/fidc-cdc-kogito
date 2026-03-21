package com.fidc.cdc.kogito.application.documental;

import com.fidc.cdc.kogito.domain.analise.Lastro;
import java.util.List;

public record ResultadoValidacaoLastro(
        List<Lastro> lastros,
        boolean bloqueiaAceiteFinal,
        long lastrosValidados,
        long lastrosRejeitados
) {
}
