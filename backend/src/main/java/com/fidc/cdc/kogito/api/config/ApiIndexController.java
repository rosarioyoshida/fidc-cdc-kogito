package com.fidc.cdc.kogito.api.config;

import java.util.Map;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiIndexController {

    @GetMapping(ApiVersionConfig.API_BASE_PATH)
    ResponseEntity<Map<String, Object>> index() {
        return ResponseEntity.ok(Map.of(
                "name", "fidc-cdc-kogito-api",
                "version", "v1",
                "links", Map.of(
                        "self", Link.of(ApiVersionConfig.API_BASE_PATH).getHref(),
                        "cessoes", Link.of(ApiVersionConfig.API_BASE_PATH + "/cessoes").getHref(),
                        "swagger", Link.of("/swagger-ui.html").getHref(),
                        "openapi", Link.of("/v3/api-docs").getHref(),
                        "health", Link.of("/actuator/health").getHref()
                )
        ));
    }
}
