package com.fidc.cdc.kogito.application.documental;

import java.util.UUID;

public record LastroDraft(
        UUID contratoId,
        UUID parcelaId,
        String tipoDocumento,
        String origem
) {
}
