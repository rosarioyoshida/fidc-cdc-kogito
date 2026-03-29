package com.fidc.cdc.kogito.api.error;

import org.springframework.http.HttpStatus;

/**
 * Representa os dados de problem type definition.
 *
 * <p>Este tipo pertence a camada de superficie HTTP e contratos de transporte. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
public record ProblemTypeDefinition(String type, String title, HttpStatus status) {
}
