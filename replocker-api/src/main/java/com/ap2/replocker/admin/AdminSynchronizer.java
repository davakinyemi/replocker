package com.ap2.replocker.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Dave AKN
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AdminSynchronizer {
    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;
    private final Keycloak keycloak;

    @Value("${keycloak.replocker.realm}")
    private String replockerRealmName;

    public void synchronizeWithIdp(Jwt token) {
        log.info("Synchronizing admin user with idp");
        UUID keycloakUserId = UUID.fromString(token.getSubject());
        this.adminRepository.findByKeycloakUserId(keycloakUserId)
                .ifPresentOrElse(
                        admin -> this.updateAdmin(admin, token),
                        () -> this.createAdmin(token)
                );
    }

    private void createAdmin(Jwt token) {
        Admin admin = this.adminMapper.fromKeycloakToken(token);
        this.adminRepository.save(admin);
    }

    private void updateAdmin(Admin admin, Jwt token) {
        String newEmail = token.getClaimAsString("email");
        if (!admin.getEmail().equals(newEmail)) {
            this.updateAdminKeycloakEmail(admin.getKeycloakUserId().toString(), newEmail);
            admin.setEmail(newEmail);
        }

        admin.setUsername(token.getClaimAsString("preferred_username"));
        this.adminRepository.save(admin);
    }

    private void updateAdminKeycloakEmail(String adminUserId, String newEmail) {
        UserRepresentation adminUser = this.keycloak.realms().realm(this.replockerRealmName)
                .users().get(adminUserId).toRepresentation();
        adminUser.setEmail(newEmail);
        adminUser.setEmailVerified(true);
        this.keycloak.realms().realm(this.replockerRealmName).users().get(adminUserId).update(adminUser);
    }
}
