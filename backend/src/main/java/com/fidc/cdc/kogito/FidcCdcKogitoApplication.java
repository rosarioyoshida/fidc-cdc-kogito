package com.fidc.cdc.kogito;

import com.fidc.cdc.kogito.infrastructure.messaging.KafkaTopicsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = "com.fidc.cdc.kogito")
public class FidcCdcKogitoApplication {

    public static void main(String[] args) {
        SpringApplication.run(FidcCdcKogitoApplication.class, args);
    }
}
