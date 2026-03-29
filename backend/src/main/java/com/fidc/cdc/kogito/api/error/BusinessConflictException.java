package com.fidc.cdc.kogito.api.error;

/**
 * Sinaliza falhas relacionadas a business conflict.
 *
 * <p>Este tipo pertence a camada de superficie HTTP e contratos de transporte. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
public class BusinessConflictException extends ApiProblemException {

    public BusinessConflictException(String message) {
        super("business-conflict", message);
    }
}
