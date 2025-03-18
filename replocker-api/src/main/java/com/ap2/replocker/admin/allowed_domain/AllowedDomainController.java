package com.ap2.replocker.admin.allowed_domain;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * @author Dave AKN
 * @version 1.0
 */
@RestController
@RequestMapping("/admin/domain")
@SecurityRequirement(name = "keycloak")
@RequiredArgsConstructor
public class AllowedDomainController {
    private final AllowedDomainService domainService;

    @Operation(summary = "Add allowed domain")
    @PostMapping
    @PreAuthorize("hasRole('REPORT_ADMIN')")
    public ResponseEntity<AllowedDomainResponse> addDomain(
            @Valid @RequestBody AllowedDomainRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID adminId = UUID.fromString(jwt.getSubject());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.domainService.addDomain(request, adminId));
    }

    @Operation(summary = "Get domains by admin")
    @GetMapping
    @PreAuthorize("hasRole('REPORT_ADMIN')")
    public ResponseEntity<List<AllowedDomainResponse>> getDomains(@AuthenticationPrincipal Jwt jwt) {
        UUID adminId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(this.domainService.getDomainsByAdminId(adminId));
    }
}
