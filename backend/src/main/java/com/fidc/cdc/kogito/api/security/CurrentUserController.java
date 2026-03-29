package com.fidc.cdc.kogito.api.security;

import com.fidc.cdc.kogito.application.security.CurrentUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Expoe endpoints HTTP para current user.
 *
 * <p>Este tipo pertence a camada de superficie HTTP e contratos de transporte. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
@RestController
@RequestMapping("/api/v1/usuarios/me")
public class CurrentUserController {

    private final CurrentUserService currentUserService;

    public CurrentUserController(CurrentUserService currentUserService) {
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public ResponseEntity<CurrentUserResponse> getCurrentUser(
            @RequestHeader(name = "X-Auth-Bootstrap", defaultValue = "false") boolean bootstrapLogin
    ) {
        return ResponseEntity.ok(currentUserService.getCurrentUser(bootstrapLogin));
    }
}
