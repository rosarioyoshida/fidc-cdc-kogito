package com.fidc.cdc.kogito.security;

import org.springframework.http.HttpMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
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
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        return new InMemoryUserDetailsManager(
                User.withUsername("operador")
                        .password(passwordEncoder.encode("operador123"))
                        .roles("OPERADOR")
                        .build(),
                User.withUsername("analista")
                        .password(passwordEncoder.encode("analista123"))
                        .roles("ANALISTA")
                        .build(),
                User.withUsername("aprovador")
                        .password(passwordEncoder.encode("aprovador123"))
                        .roles("APROVADOR")
                        .build(),
                User.withUsername("auditor")
                        .password(passwordEncoder.encode("auditor123"))
                        .roles("AUDITOR")
                        .build(),
                User.withUsername("integracao")
                        .password(passwordEncoder.encode("integracao123"))
                        .roles("INTEGRACAO")
                        .build()
        );
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
