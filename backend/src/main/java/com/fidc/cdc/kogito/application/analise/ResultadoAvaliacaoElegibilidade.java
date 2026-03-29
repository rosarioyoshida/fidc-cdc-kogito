package com.fidc.cdc.kogito.application.analise;

import com.fidc.cdc.kogito.domain.analise.RegraElegibilidade;
import java.util.List;

/**
 * Representa os dados de resultado avaliacao elegibilidade.
 *
 * <p>Este tipo pertence a camada de orquestracao de casos de uso e servicos de aplicacao. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
public record ResultadoAvaliacaoElegibilidade(
        List<RegraElegibilidade> regras,
        boolean possuiBloqueios
) {
}
