package com.fidc.cdc.kogito.api.security;

import com.fidc.cdc.kogito.application.security.SelfAccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Expoe endpoints HTTP para self account.
 *
 * <p>Este tipo pertence a camada de superficie HTTP e contratos de transporte. O contrato deve ser interpretado a partir da assinatura exposta, das anotacoes declarativas e das colaboracoes visiveis no codigo, sem assumir detalhes internos de framework, persistencia ou integracao que nao alterem o uso observavel da API.
 */
@RestController
@RequestMapping("/api/v1/usuarios/me")
public class SelfAccountController {

    private final SelfAccountService selfAccountService;

    public SelfAccountController(SelfAccountService selfAccountService) {
        this.selfAccountService = selfAccountService;
    }

    @PatchMapping
    public ResponseEntity<Void> updateEmail(@Valid @RequestBody UpdateOwnEmailRequest request) {
        selfAccountService.updateOwnEmail(request.email());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/alteracao-senha")
    public ResponseEntity<Void> updatePassword(@Valid @RequestBody UpdateOwnPasswordRequest request) {
        selfAccountService.changeOwnPassword(request.senhaAtual(), request.novaSenha());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/sessao")
    public ResponseEntity<Void> logout() {
        selfAccountService.registerLogout();
        return ResponseEntity.noContent().build();
    }
}
