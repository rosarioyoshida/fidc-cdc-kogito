package com.fidc.cdc.kogito.application.security;

/**
 * Representa a decisao de authorization.
 *
 * <p>Este tipo pertence a camada de orquestracao de casos de uso e servicos de aplicacao. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
public record AuthorizationDecision(
        String actorId,
        String perfil
) {
}
