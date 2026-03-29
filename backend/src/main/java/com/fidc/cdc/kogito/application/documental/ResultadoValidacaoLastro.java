package com.fidc.cdc.kogito.application.documental;

import com.fidc.cdc.kogito.domain.analise.Lastro;
import java.util.List;

/**
 * Representa os dados de resultado validacao lastro.
 *
 * <p>Este tipo pertence a camada de orquestracao de casos de uso e servicos de aplicacao. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
public record ResultadoValidacaoLastro(
        List<Lastro> lastros,
        boolean bloqueiaAceiteFinal,
        long lastrosValidados,
        long lastrosRejeitados
) {
}
