package com.fidc.cdc.kogito.domain.cessao;

import java.util.List;

/**
 * Enumera os estados ou valores de etapa cessao nome.
 *
 * <p>Este tipo pertence a camada de modelo de dominio e contratos de persistencia. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
public enum EtapaCessaoNome {
    IMPORTAR_CARTEIRA("Importar carteira"),
    VALIDAR_CEDENTE("Validar cedente"),
    ANALISAR_ELEGIBILIDADE("Analisar elegibilidade"),
    CONSOLIDAR_CONTRATOS("Consolidar contratos"),
    CONSOLIDAR_PARCELAS("Consolidar parcelas"),
    CALCULAR_VALOR("Calcular valor"),
    PREPARAR_OFERTA_REGISTRADORA("Preparar oferta registradora"),
    ENVIAR_OFERTA_REGISTRADORA("Enviar oferta registradora"),
    AGUARDAR_CONFIRMACAO_REGISTRADORA("Aguardar confirmacao registradora"),
    COLETAR_TERMO_ACEITE("Coletar termo de aceite"),
    VALIDAR_LASTROS("Validar lastros"),
    AUTORIZAR_PAGAMENTO("Autorizar pagamento"),
    PROCESSAR_PAGAMENTO("Processar pagamento"),
    CONFIRMAR_PAGAMENTO("Confirmar pagamento"),
    ENCERRAR_CESSAO("Encerrar cessao");

    private final String displayName;

    EtapaCessaoNome(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static List<EtapaCessaoNome> orderedValues() {
        return List.of(values());
    }
}
