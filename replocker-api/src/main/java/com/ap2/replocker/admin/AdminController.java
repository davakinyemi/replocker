package com.ap2.replocker.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin")
@SecurityRequirement(name = "keycloak")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @Operation(summary = "Get current admin profile")
    @GetMapping("/me") // http://localhost:8088/api/admins/me
    @PreAuthorize("hasRole('REPORT_ADMIN')")
    public ResponseEntity<AdminResponse> getCurrentAdmin(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(adminService.syncWithKeycloak(jwt));
    }
}
