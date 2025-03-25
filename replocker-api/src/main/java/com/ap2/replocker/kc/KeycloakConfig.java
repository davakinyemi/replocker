package com.ap2.replocker.kc;

import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Dave AKN
 * @version 1.0
 */
@Configuration
@RequiredArgsConstructor
public class KeycloakConfig {
    @Value("${keycloak.server-url}")
    private String serverUrl;

    @Value("${keycloak.admin.realm}")
    private String realm;

    @Value("${keycloak.admin.client-id}")
    private String adminClientId;

    @Value("${keycloak.admin.username}")
    private String adminUsername;

    @Value("${keycloak.admin.password}")
    private String adminPassword;

    @Bean
    public Keycloak keycloakAdmin() {
        return KeycloakBuilder.builder()
                .serverUrl(this.serverUrl)
                .realm(this.realm)
                .clientId(this.adminClientId)
                .username(this.adminUsername)
                .password(this.adminPassword)
                .grantType(OAuth2Constants.PASSWORD)
                .build();
    }
}
