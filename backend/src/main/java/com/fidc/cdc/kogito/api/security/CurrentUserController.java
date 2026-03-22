package com.fidc.cdc.kogito.api.security;

import com.fidc.cdc.kogito.application.security.CurrentUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
