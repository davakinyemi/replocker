package com.ap2.replocker.admin;

import com.ap2.replocker.exception.custom.AdminNotFoundException;
import com.ap2.replocker.exception.custom.OperationNotPermittedException;
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

    public AdminResponse getAdminProfile(Jwt jwt) {
        Admin admin = this.adminRepository.findByKeycloakUserId(UUID.fromString(jwt.getSubject()))
                .orElseThrow(() -> new AdminNotFoundException("Admin not found", UUID.fromString(jwt.getSubject())));

        if (!admin.getEmail().equals(jwt.getClaimAsString("email")) ||
                !admin.getUsername().equals(jwt.getClaimAsString("preferred_username"))
        ) {
            return this.syncWithKeycloak(admin, jwt);
        }
        return this.adminMapper.toAdminResponse(admin);
    }

    public void verifyAdminOwnership(UUID adminId, UUID resourceOwnerId) {
        if (!adminId.equals(resourceOwnerId)) {
            throw new OperationNotPermittedException("Admin ownership mismatch");
        }
    }

    private AdminResponse syncWithKeycloak(Admin admin, Jwt token) {
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
        if (!admin.getEmail().equals(token.getClaimAsString("email"))) {
            admin.setEmail(token.getClaimAsString("email"));
        }
    }
}
