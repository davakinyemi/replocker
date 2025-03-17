/* package com.ap2.replocker.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class _AdminSynchronizer {

    private final AdminRepository adminRepository;
    private final _AdminMapper adminMapper;
    public void synchronizeWithIdp(Jwt token) { // IDP = Identity Provider (Keycloak)
        log.info("Synchronizing admin with idp");
        this.getAdminEmail(token).ifPresent(adminEmail -> {
            log.info("Synchronizing admin having email {}", adminEmail);
            Optional<Admin> optionalAdmin = this.adminRepository.findByEmail(adminEmail);
            Admin admin = this.adminMapper.fromTokenAttributes(token.getClaims());
            optionalAdmin.ifPresent(value -> admin.setId(optionalAdmin.get().getId()));

            this.adminRepository.save(admin);
        });
    }

    private Optional<String> getAdminEmail(Jwt token) {
        Map<String, Object> attributes = token.getClaims();
        if (attributes.containsKey("email")) {
            return Optional.of(attributes.get("email").toString());
        }
        return Optional.empty();
    }
} */
