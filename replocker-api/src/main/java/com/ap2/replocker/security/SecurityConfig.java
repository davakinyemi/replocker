package com.ap2.replocker.security;

import com.ap2.replocker.interceptor.AdminSynchronizerFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

    private final AdminSynchronizerFilter adminSyncFilter;
    private final CustomBearerTokenAuthenticationEntryPoint customEntryPoint;

    @Value("${keycloak.replocker.role-name}")
    private String requiredRole;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .addFilterAfter(adminSyncFilter, BearerTokenAuthenticationFilter.class)
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests ->
                        requests
                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/report-collections/public/request-access/**"
                                ).permitAll()
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/report-collections/public/**"
                                ).permitAll()
                                .requestMatchers(
                                        "/admins/**",
                                        "/report-collections/my/**"
                                ).hasAnyRole(this.requiredRole)
                                .requestMatchers(
                                        "/v2/api-docs",
                                        "/v3/api-docs",
                                        "/v3/api-docs/**",
                                        "/swagger-resources",
                                        "/swagger-resources/**",
                                        "/configuration/ui",
                                        "/configuration/security",
                                        "/swagger-ui/**",
                                        "/webjars/**",
                                        "/swagger-ui.html",
                                        "/ws/**"
                                ).permitAll().anyRequest().authenticated()
                ).oauth2ResourceServer(auth ->
                        auth.jwt(token -> token.jwtAuthenticationConverter(this.keycloakJwtConverter()))
                                .authenticationEntryPoint(this.customEntryPoint)
                ).csrf(csrf -> csrf.ignoringRequestMatchers("/ws/**", "/report-collections/public/**"));
        return http.build();
    }

    private Converter<Jwt, AbstractAuthenticationToken> keycloakJwtConverter() {
        return new KeycloakJwtAuthenticationConverter();
    }
}
