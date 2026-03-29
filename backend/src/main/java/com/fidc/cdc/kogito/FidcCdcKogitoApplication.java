package com.fidc.cdc.kogito;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Representa fidc cdc kogito application no backend de cessao.
 *
 * <p>Este tipo pertence a camada de coordenacao raiz da aplicacao Spring Boot. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = "com.fidc.cdc.kogito")
public class FidcCdcKogitoApplication {

    public static void main(String[] args) {
        SpringApplication.run(FidcCdcKogitoApplication.class, args);
    }
}
