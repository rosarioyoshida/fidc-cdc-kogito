package com.fidc.cdc.kogito.api.error;

/**
 * Sinaliza falhas relacionadas a api problem.
 *
 * <p>Este tipo pertence a camada de superficie HTTP e contratos de transporte. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
public class ApiProblemException extends RuntimeException {

    private final String problemKey;

    public ApiProblemException(String problemKey, String message) {
        super(message);
        this.problemKey = problemKey;
    }

    public String getProblemKey() {
        return problemKey;
    }
}
