package com.fidc.cdc.kogito.api.hateoas;

import static com.fidc.cdc.kogito.api.config.ApiVersionConfig.API_BASE_PATH;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

/**
 * Monta representacoes de API para cessao link.
 *
 * <p>Este tipo pertence a camada de superficie HTTP e contratos de transporte. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
@Component
public class CessaoLinkAssembler {

    public Link self(String businessKey) {
        return Link.of(API_BASE_PATH + "/cessoes/" + businessKey).withSelfRel();
    }

    public Link etapas(String businessKey) {
        return Link.of(API_BASE_PATH + "/cessoes/" + businessKey + "/etapas").withRel("etapas");
    }

    public Link historico(String businessKey) {
        return Link.of(API_BASE_PATH + "/cessoes/" + businessKey + "/historico").withRel("historico");
    }

    public Link analise(String businessKey) {
        return Link.of(API_BASE_PATH + "/cessoes/" + businessKey + "/analise").withRel("analise");
    }

    public Link auditoria(String businessKey) {
        return Link.of(API_BASE_PATH + "/cessoes/" + businessKey + "/auditoria").withRel("auditoria");
    }

    public Link permissoes(String businessKey) {
        return Link.of(API_BASE_PATH + "/cessoes/" + businessKey + "/permissoes").withRel("permissoes");
    }
}
