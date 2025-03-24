/* package com.ap2.replocker.kc;

import com.ap2.replocker.admin.AdminRepository;
import com.ap2.replocker.exception.custom.KeycloakException;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map; */

/**
 * @author Dave AKN
 * @version 1.0
 */
/* @Component
@RequiredArgsConstructor
@Slf4j
public class _KeycloakInitializer {
    private final Keycloak keycloak;

    @Value("${keycloak.replocker.realm}")
    private String replockerRealmName;

    @Value("${spring.mail.host}")
    private String smtpHost;

    @Value("${spring.mail.port}")
    private int smtpPort;

    @Value("${spring.mail.username}")
    private String smtpUsername;

    @Value("${spring.mail.password}")
    private String smtpPassword;

    @Value("${keycloak.replocker.client-id}")
    private String replockerKcClientId;

    @Value("${keycloak.replocker.client-secret}")
    private String replockerKcClientSecret;

    @Value("${keycloak.replocker.role-name}")
    private String replockerAdminRoleName;

    @Value("${keycloak.replocker.username}")
    private String replockerAdminUserName;

    @Value("${keycloak.replocker.password}")
    private String replockerAdminUserPassword;

    @EventListener(ApplicationReadyEvent.class)
    public void initRealm() {
        try {
            RealmRepresentation realm = this.keycloak.realms().realm(this.replockerRealmName).toRepresentation();
        } catch (NotFoundException e) {
            try {
                this.createRealmWithDefaults();
                log.info("Created new Keycloak realm: {}", this.replockerRealmName);
            } catch (Exception creationError) {
                log.error(
                        "Failed to create Keycloak realm - {}: {}",
                        this.replockerRealmName,
                        creationError.getMessage()
                );
                throw new RuntimeException("Keycloak realm creation failed", creationError);
            }
        } catch (Exception e) {
            log.error("Error checking Keycloak realm existence - {}: {}", this.replockerRealmName, e.getMessage());
            throw new RuntimeException("Keycloak realm initialization failed: " + this.replockerRealmName, e);
        }
    }

    private void createRealmWithDefaults() throws KeycloakException {
        try {
            RealmRepresentation realm = new RealmRepresentation();
            realm.setRealm(this.replockerRealmName);
            realm.setEnabled(true);
            realm.setRegistrationEmailAsUsername(true);
            realm.setRegistrationAllowed(false);
            realm.setResetPasswordAllowed(true);

            this.configureSmtp(realm);
            this.configureClients(realm);
            this.configureRoles(realm);

            this.keycloak.realms().create(realm);
            this.createInitialAdminUser();
        } catch (ClientErrorException e) {
            throw new KeycloakException("Client configuration error: " + e.getMessage(), e);
        } catch (ProcessingException e) {
            throw new KeycloakException("Network/IO error: " + e.getMessage(), e);
        }
    }

    private void configureSmtp(RealmRepresentation realm) {
        Map<String, String> smtpConfig = new HashMap<>();
        smtpConfig.put("host", smtpHost);
        smtpConfig.put("port", String.valueOf(smtpPort));
        smtpConfig.put("ssl", "false");
        smtpConfig.put("starttls", "true");
        smtpConfig.put("auth", "true");
        smtpConfig.put("user", this.smtpUsername);
        smtpConfig.put("password", this.smtpPassword);
        realm.setSmtpServer(smtpConfig);
    }

    private void configureClients(RealmRepresentation realm) {
        ClientRepresentation client = new ClientRepresentation();
        client.setClientId(this.replockerKcClientId);
        client.setPublicClient(false);
        client.setDirectAccessGrantsEnabled(true);
        client.setRedirectUris(List.of("*"));
        client.setWebOrigins(List.of("*"));
        client.setSecret(this.replockerKcClientSecret);
        realm.setClients(List.of(client));
    }

    private void configureRoles(RealmRepresentation realm) {
        realm.setRoles(new RolesRepresentation());
        realm.getRoles().getRealm().add(new RoleRepresentation(this.replockerAdminRoleName, "Admin role", false));
    }

    private void createInitialAdminUser() {
        UsersResource usersResource = this.keycloak.realm(this.replockerRealmName).users();

        if (usersResource.search(this.replockerAdminUserName).isEmpty()) {
            UserRepresentation adminUser = new UserRepresentation();
            adminUser.setUsername(this.replockerAdminUserName);
            adminUser.setEnabled(true);
            adminUser.setEmailVerified(true);

            try {
                Response createResponse = usersResource.create(adminUser);
                String adminUserId = CreatedResponseUtil.getCreatedId(createResponse);

                CredentialRepresentation credential = new CredentialRepresentation();
                credential.setType(CredentialRepresentation.PASSWORD);
                credential.setValue(this.replockerAdminUserPassword);
                credential.setTemporary(false);

                usersResource.get(adminUserId).resetPassword(credential);

                this.assignAdminRole(adminUserId);
            } catch (RuntimeException e) {
                throw new KeycloakException("Failed to create admin user: " + e.getMessage(), e);
            }
        } */
        /* UserRepresentation adminUser = new UserRepresentation();
        adminUser.setUsername(this.replockerAdminUserName);
        adminUser.setEnabled(true);
        adminUser.setEmailVerified(true);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(this.replockerAdminUserPassword);
        credential.setTemporary(false);

        UserResource user = this.keycloak.realms()
                .realm(this.replockerRealmName)
                .users()
                .create(adminUser); */
    /* }

    private void assignAdminRole(String adminUserId) {
        RealmResource realm = this.keycloak.realm(this.replockerRealmName);
        RoleRepresentation adminRole = realm.roles().get(this.replockerAdminRoleName).toRepresentation();

        realm.users().get(adminUserId).roles().realmLevel().add(Collections.singletonList(adminRole));
    }
} */
