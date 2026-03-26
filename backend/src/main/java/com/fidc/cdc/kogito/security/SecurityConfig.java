package com.fidc.cdc.kogito.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fidc.cdc.kogito.api.error.ProblemTypeDefinition;
import com.fidc.cdc.kogito.api.error.ProblemTypeRegistry;
import com.fidc.cdc.kogito.domain.security.Usuario;
import com.fidc.cdc.kogito.domain.security.UsuarioRepository;
import com.fidc.cdc.kogito.infrastructure.audit.AuthAuditService;
import com.fidc.cdc.kogito.infrastructure.logging.CorrelationLoggingFilter;
import com.fidc.cdc.kogito.infrastructure.observability.AuthMetricsService;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationEntryPoint authenticationEntryPoint,
            AccessDeniedHandler accessDeniedHandler,
            JwtAuthenticationConverter jwtAuthenticationConverter
    ) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(
                                "/actuator/health",
                                "/actuator/info",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        )
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/svg/processes/**", "/management/processes/**").permitAll()
                        .requestMatchers(this::isInternalProcessManagementMutation).permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/process/jobs/callbacks/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/cessoes/*/*/*/*/schema").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/cessoes/*/*/*/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/controle-cessao/*/*/*/*/schema").permitAll()
                        .requestMatchers(HttpMethod.POST, "/controle-cessao/*/*/*/*").permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .httpBasic(Customizer.withDefaults())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter))
                        .authenticationEntryPoint(authenticationEntryPoint)
                );
        return http.build();
    }

    @Bean
    UserDetailsService userDetailsService(UsuarioRepository usuarioRepository) {
        return username -> usuarioRepository.findByUsernameAndAtivoTrue(username)
                .map(this::toUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario nao encontrado."));
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    AuthenticationEntryPoint authenticationEntryPoint(
            ProblemTypeRegistry problemTypeRegistry,
            ObjectMapper objectMapper,
            AuthAuditService authAuditService,
            AuthMetricsService authMetricsService
    ) {
        return (request, response, authException) -> {
            ProblemTypeDefinition definition = problemTypeRegistry.get("authentication-failed");
            String failureReason = resolveFailureReason(request);
            authAuditService.logLoginFailure(resolveUsername(request), failureReason);
            authMetricsService.recordAuthenticationFailure(failureReason);

            ProblemDetail detail = ProblemDetail.forStatusAndDetail(
                    definition.status(),
                    "Credenciais invalidas ou ausentes para acesso a area protegida."
            );
            detail.setType(URI.create(definition.type()));
            detail.setTitle(definition.title());
            detail.setInstance(URI.create(request.getRequestURI()));
            detail.setProperty("timestamp", OffsetDateTime.now());
            detail.setProperty("correlationId", MDC.get(CorrelationLoggingFilter.CORRELATION_ID_KEY));

            response.setStatus(definition.status().value());
            response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
            objectMapper.writeValue(response.getOutputStream(), Map.of(
                    "type", detail.getType(),
                    "title", detail.getTitle(),
                    "status", detail.getStatus(),
                    "detail", detail.getDetail(),
                    "instance", detail.getInstance(),
                    "timestamp", detail.getProperties().get("timestamp"),
                    "correlationId", detail.getProperties().get("correlationId")
            ));
        };
    }

    @Bean
    JwtDecoder jwtDecoder(
            @Value("${fidc.security.keycloak.jwk-set-uri}") String jwkSetUri,
            @Value("${fidc.security.keycloak.issuer-uri}") String issuerUri
    ) {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
        OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(
                JwtValidators.createDefaultWithIssuer(issuerUri)
        );
        jwtDecoder.setJwtValidator(validator);
        return jwtDecoder;
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setPrincipalClaimName("preferred_username");
        return converter;
    }

    @Bean
    AccessDeniedHandler accessDeniedHandler(
            ProblemTypeRegistry problemTypeRegistry,
            ObjectMapper objectMapper
    ) {
        return (request, response, accessDeniedException) -> {
            ProblemTypeDefinition definition = problemTypeRegistry.get("forbidden-operation");
            ProblemDetail detail = ProblemDetail.forStatusAndDetail(
                    definition.status(),
                    "O acesso ao recurso solicitado nao foi autorizado para este contexto."
            );
            detail.setType(URI.create(definition.type()));
            detail.setTitle(definition.title());
            detail.setInstance(URI.create(request.getRequestURI()));
            detail.setProperty("timestamp", OffsetDateTime.now());
            detail.setProperty("correlationId", MDC.get(CorrelationLoggingFilter.CORRELATION_ID_KEY));
            detail.setProperty("exception", AccessDeniedException.class.getSimpleName());

            response.setStatus(definition.status().value());
            response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
            objectMapper.writeValue(response.getOutputStream(), Map.of(
                    "type", detail.getType(),
                    "title", detail.getTitle(),
                    "status", detail.getStatus(),
                    "detail", detail.getDetail(),
                    "instance", detail.getInstance(),
                    "timestamp", detail.getProperties().get("timestamp"),
                    "correlationId", detail.getProperties().get("correlationId"),
                    "exception", detail.getProperties().get("exception")
            ));
        };
    }

    private User toUserDetails(Usuario usuario) {
        List<String> roles = usuario.getPerfis().stream()
                .map(perfil -> perfil.getNome())
                .distinct()
                .sorted()
                .toList();
        return (User) User.withUsername(usuario.getUsername())
                .password(usuario.getPasswordHash())
                .roles(roles.toArray(String[]::new))
                .build();
    }

    private String resolveUsername(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Basic ")) {
            return "desconhecido";
        }

        try {
            String decoded = new String(
                    Base64.getDecoder().decode(authorization.substring(6)),
                    StandardCharsets.UTF_8
            );
            int separator = decoded.indexOf(':');
            return separator >= 0 ? decoded.substring(0, separator) : decoded;
        } catch (IllegalArgumentException ex) {
            return "desconhecido";
        }
    }

    private String resolveFailureReason(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || authorization.isBlank()) {
            return "missing-authentication";
        }
        if (authorization.startsWith("Basic ")) {
            return "basic-auth-invalid";
        }
        if (authorization.startsWith("Bearer ")) {
            return "bearer-auth-invalid";
        }
        return "unsupported-auth-scheme";
    }

    private boolean isInternalProcessManagementMutation(HttpServletRequest request) {
        if (!request.getRequestURI().startsWith("/management/processes/")) {
            return false;
        }

        String method = request.getMethod();
        if (!(HttpMethod.POST.matches(method)
                || HttpMethod.PUT.matches(method)
                || HttpMethod.PATCH.matches(method)
                || HttpMethod.DELETE.matches(method))) {
            return false;
        }

        String serverName = request.getServerName();
        if ("backend".equalsIgnoreCase(serverName)) {
            return true;
        }

        String host = request.getHeader("Host");
        return host != null && host.toLowerCase(Locale.ROOT).startsWith("backend:");
    }
}
