package com.ap2.replocker.admin;

import com.ap2.replocker.report_collection.ReportCollection;
import com.ap2.replocker.user.access_token.allowed_domain.AllowedDomain;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Dave AKN
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class AdminMapper {
    public Admin fromKeycloakToken(Jwt token) {
        return Admin.builder()
                .username(token.getClaimAsString("preferred_username"))
                .hashedEmail(this.hashEmail(token.getClaimAsString("email")))
                .keycloakUserId(UUID.fromString(token.getSubject()))
                .build();
    }

    public AdminResponse toAdminResponse(Admin admin) {
        return AdminResponse.builder()
                .id(admin.getId())
                .username(admin.getUsername())
                .createdDate(admin.getCreatedDate())
                .lastModifiedDate(admin.getLastModifiedDate())
                .collectionIds(admin.getCollections().stream()
                        .map(ReportCollection::getId)
                        .toList())
                .allowedDomains(admin.getAllowedDomains().stream()
                        .map(AllowedDomain::getDomainName)
                        .toList())
                .build();
    }

    private String hashEmail(String email) {
        return DigestUtils.sha256Hex(email.toLowerCase().trim());
    }
}
