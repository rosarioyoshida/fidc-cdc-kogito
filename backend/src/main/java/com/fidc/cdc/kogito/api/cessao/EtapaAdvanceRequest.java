package com.fidc.cdc.kogito.api.cessao;

/**
 * Representa a solicitacao de etapa advance.
 *
 * <p>Este tipo pertence a camada de superficie HTTP e contratos de transporte. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
public record EtapaAdvanceRequest(
        String responsavelId,
        String justificativa
) {
}
