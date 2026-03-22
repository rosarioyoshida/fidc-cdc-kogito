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
import java.util.Map;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationEntryPoint authenticationEntryPoint
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
                                "/v3/api-docs/**",
                                "/svg/processes/**",
                                "/management/processes/**"
                        )
                        .permitAll()
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
                )
                .httpBasic(Customizer.withDefaults());
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
            authAuditService.logLoginFailure(resolveUsername(request), "basic-auth-invalid");
            authMetricsService.recordAuthenticationFailure("basic-auth-invalid");

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
}
