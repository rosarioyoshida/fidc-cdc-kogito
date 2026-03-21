package com.fidc.cdc.kogito.api.hateoas;

import static com.fidc.cdc.kogito.api.config.ApiVersionConfig.API_BASE_PATH;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

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
}
