package com.fidc.cdc.kogito.api.security;

/**
 * Representa a resposta de current user.
 *
 * <p>Este tipo pertence a camada de superficie HTTP e contratos de transporte. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
public record CurrentUserResponse(
        String username,
        String nomeExibicao,
        String email,
        String perfilAtivo
) {
}
