package com.ap2.replocker.admin;

import com.ap2.replocker.admin.allowed_domain.AllowedDomainRequest;
import com.ap2.replocker.admin.allowed_domain.AllowedDomainResponse;
import com.ap2.replocker.admin.allowed_domain.AllowedDomainService;
import com.ap2.replocker.admin.notification.NotificationResponse;
import com.ap2.replocker.admin.notification.NotificationService;
import com.ap2.replocker.common.PageResponse;
import com.ap2.replocker.common.audit_log.AuditLogMapper;
import com.ap2.replocker.common.audit_log.AuditLogResponse;
import com.ap2.replocker.common.audit_log.AuditLogService;
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

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/admins")
@SecurityRequirement(name = "keycloak")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final AllowedDomainService allowedDomainService;
    private final NotificationService notificationService;
    private final AuditLogService auditLogService;
    private final AuditLogMapper auditLogMapper;

    @Operation(summary = "Get current admin profile")
    @GetMapping("/me") // http://localhost:8088/api/admins/me
    @PreAuthorize("hasAnyRole('REPLOCKER_ADMIN')")
    public ResponseEntity<AdminResponse> getCurrentAdmin(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(this.adminService.getAdminProfile(jwt));
    }

    @Operation(summary = "Add allowed domain")
    @PostMapping("/me/allowed-domain")
    @PreAuthorize("hasAnyRole('REPLOCKER_ADMIN')")
    public ResponseEntity<AllowedDomainResponse> addDomain(
            @Valid @RequestBody AllowedDomainRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID adminId = this.adminService.getAdminId(jwt);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.allowedDomainService.addDomain(request, adminId));
                // .body(this.allowedDomainService.addDomain(request, UUID.fromString(jwt.getSubject())));
    }

    @Operation(summary = "List allowed domains")
    @GetMapping("/me/allowed-domain")
    @PreAuthorize("hasAnyRole('REPLOCKER_ADMIN')")
    public ResponseEntity<PageResponse<AllowedDomainResponse>> getDomains(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        UUID adminId = this.adminService.getAdminId(jwt);
        return ResponseEntity.ok(
                this.allowedDomainService.getDomainsByAdmin(adminId, page, size)
                // this.allowedDomainService.getDomainsByAdmin(UUID.fromString(jwt.getSubject()), page, size)
        );
    }

    @Operation(summary = "Delete allowed domain")
    @DeleteMapping("/me/allowed-domain/{id}")
    @PreAuthorize("hasAnyRole('REPLOCKER_ADMIN')")
    public ResponseEntity<Void> deleteDomain(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID adminId = this.adminService.getAdminId(jwt);
        this.allowedDomainService.deleteDomain(id, adminId);
        // this.allowedDomainService.deleteDomain(id, UUID.fromString(jwt.getSubject()));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Filter notifications")
    @GetMapping("/me/notification")
    @PreAuthorize("hasAnyRole('REPLOCKER_ADMIN')")
    public ResponseEntity<PageResponse<NotificationResponse>> filterNotifications(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        UUID adminId = this.adminService.getAdminId(jwt);
        return ResponseEntity.ok(this.notificationService.filterNotifications(
                adminId, startDate, endDate, page, size
                // UUID.fromString(jwt.getSubject()), startDate, endDate, page, size
        ));
    }

    @Operation(summary = "Get unread notifications")
    @GetMapping("/me/notification/unread")
    @PreAuthorize("hasAnyRole('REPLOCKER_ADMIN')")
    public ResponseEntity<PageResponse<NotificationResponse>> getUnreadNotifications(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        UUID adminId = this.adminService.getAdminId(jwt);
        return ResponseEntity.ok(this.notificationService.getUnreadNotifications(
                adminId, page, size
                // UUID.fromString(jwt.getSubject()), page, size
        ));
    }

    @Operation(summary = "Get audit logs")
    @GetMapping("/my/audit-logs")
    @PreAuthorize("hasAnyRole('REPLOCKER_ADMIN')")
    public ResponseEntity<PageResponse<AuditLogResponse>> getLogs(
        @RequestParam(required = false) String entityName,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "50") int size
    ) {
        return ResponseEntity.ok(
            entityName != null
                ? this.auditLogService.getLogsByEntityName(entityName, page, size)
                    : this.auditLogService.getLogs(page, size)
        );
    }
}
