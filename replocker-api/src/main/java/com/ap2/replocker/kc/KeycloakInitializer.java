package com.ap2.replocker.kc;

import com.ap2.replocker.admin.Admin;
import com.ap2.replocker.admin.AdminRepository;
import com.ap2.replocker.exception.custom.KeycloakException;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author Dave AKN
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KeycloakInitializer {
    private final Keycloak keycloak;
    private final AdminRepository adminRepository;

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

    @Value("${keycloak.replocker.email}")
    private String replockerAdminUserName;

    @Value("${keycloak.replocker.email}")
    private String replockerAdminUserEmail;

    @Value("${keycloak.replocker.password}")
    private String replockerAdminUserPassword;

    @EventListener(ApplicationReadyEvent.class)
    public void initRealmAndAdminUser() {
        try {
            this.initRealm();
            this.initSingletonAdminUser();
        } catch (RuntimeException e) {
            throw new KeycloakException("Keycloak initialization failed: " + e.getMessage(), e);
        }
    }

    private void initRealm() {
        if (!this.realmExists()) {
            this.createRealmWithDefaults();
            log.info("Created new Keycloak realm: {}", this.replockerRealmName);
        }
    }

    private void initSingletonAdminUser() {
        UserRepresentation adminUser = this.keycloak.realms().realm(this.replockerRealmName).users()
                .search(this.replockerAdminUserName, true).stream().findFirst().orElse(null);

        if (adminUser == null) {
            this.createInitialAdminUser();
            this.syncAdminUserToDatabase();
        }
    }

    private boolean realmExists() {
        return this.keycloak.realms().findAll().stream().anyMatch(
                r -> r.getRealm().equals(this.replockerRealmName)
        );
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
        smtpConfig.put("starttls", "true");
        smtpConfig.put("ssl", "false");
        smtpConfig.put("auth", "true");
        smtpConfig.put("user", this.smtpUsername);
        smtpConfig.put("password", this.smtpPassword);
        smtpConfig.put("from", this.smtpUsername);
        smtpConfig.put("fromDisplayName", "noreply@replocker.de");
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
        RolesRepresentation roles = new RolesRepresentation();

        RoleRepresentation replockerAdminRole = new RoleRepresentation();
        replockerAdminRole.setName(replockerAdminRoleName);
        replockerAdminRole.setDescription("RepLocker Admin Role");
        replockerAdminRole.setComposite(false);

        List<RoleRepresentation> replockerRealmRoles = new ArrayList<>();
        replockerRealmRoles.add(replockerAdminRole);
        roles.setRealm(replockerRealmRoles);

        realm.setRoles(roles);
        /* realm.setRoles(new RolesRepresentation());
        realm.getRoles().getRealm().add(new RoleRepresentation(this.replockerAdminRoleName, "RepLocker Admin role", false)); */
    }

    private void createInitialAdminUser() {
        UsersResource usersResource = this.keycloak.realm(this.replockerRealmName).users();

        if (usersResource.search(this.replockerAdminUserName).isEmpty()) {
            UserRepresentation adminUser = new UserRepresentation();
            adminUser.setUsername(this.replockerAdminUserName);
            adminUser.setEmail(this.replockerAdminUserEmail);
            adminUser.setEnabled(true);
            adminUser.setEmailVerified(true);

            Response createResponse = usersResource.create(adminUser);

            if (createResponse.getStatus() == 201) {
                String adminUserId = CreatedResponseUtil.getCreatedId(createResponse);

                CredentialRepresentation credential = new CredentialRepresentation();
                credential.setType(CredentialRepresentation.PASSWORD);
                credential.setValue(this.replockerAdminUserPassword);
                credential.setTemporary(false);
                usersResource.get(adminUserId).resetPassword(credential);

                UserResource adminUserResource = usersResource.get(adminUserId);
                UserRepresentation userRepresentation = adminUserResource.toRepresentation();
                userRepresentation.setRequiredActions(List.of(
                        "UPDATE_PASSWORD", // Password change
                        "VERIFY_EMAIL" // Email verification
                        // "CONFIGURE_TOTP", // 2FA setup
                        // "UPDATE_PROFILE" // Profile update
                ));
                adminUserResource.update(userRepresentation);
                this.assignAdminRole(adminUserId);
            } else {
                String error = createResponse.readEntity(String.class);
                throw new KeycloakException("Failed to create admin user: " + error);
            }
        }
    }

    private void assignAdminRole(String adminUserId) {
        RealmResource realm = this.keycloak.realm(this.replockerRealmName);
        RoleRepresentation adminRole = realm.roles().get(this.replockerAdminRoleName).toRepresentation();

        realm.users().get(adminUserId).roles().realmLevel().add(Collections.singletonList(adminRole));
    }

    private void syncAdminUserToDatabase() {
        UserRepresentation adminUser = this.keycloak.realms().realm(this.replockerRealmName).users()
                .search(this.replockerAdminUserName, true).getFirst();

        Admin admin = Admin.builder()
                .username(adminUser.getUsername())
                .email(adminUser.getEmail())
                .keycloakUserId(UUID.fromString(adminUser.getId()))
                .build();

        this.adminRepository.save(admin);
    }
}
