package com.ap2.replocker.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public void synchronizeWithIdp(Jwt token) {
        log.info("Synchronizing admin with idp");
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
        admin.setUsername(token.getClaimAsString("preferred_username"));
        this.adminRepository.save(admin);
    }
}
