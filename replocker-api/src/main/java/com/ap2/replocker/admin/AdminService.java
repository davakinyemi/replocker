package com.ap2.replocker.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AdminService {
    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;

    public AdminResponse syncWithKeycloak(Jwt token) {
        Admin admin = this.adminRepository.findByKeycloakUserId(UUID.fromString(token.getSubject()))
                .orElseGet(() -> this.createAdminFromToken(token));

        this.updateAdminFromToken(admin, token);
        return this.adminMapper.toAdminResponse(
                this.adminRepository.save(admin)
        );
    }

    private Admin createAdminFromToken(Jwt token) {
        Admin newAdmin = this.adminMapper.fromKeycloakToken(token);
        log.info("Creating new admin: {}", newAdmin.getUsername());
        return this.adminRepository.save(newAdmin);
    }

    private void updateAdminFromToken(Admin admin, Jwt token) {
        if (!admin.getUsername().equals(token.getClaimAsString("preferred_username"))) {
            admin.setUsername(token.getClaimAsString("preferred_username"));
        }
    }
}
