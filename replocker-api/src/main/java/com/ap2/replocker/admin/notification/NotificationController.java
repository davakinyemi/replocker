package com.ap2.replocker.admin.notification;

import com.ap2.replocker.common.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author Dave AKN
 * @version 1.0
 */
@RestController
@RequestMapping("/admin/notifications")
@SecurityRequirement(name = "keycloak")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @Operation(summary = "Get unread notifications")
    @GetMapping
    @PreAuthorize("hasRole('REPORT_ADMIN')")
    public PageResponse<NotificationResponse> getNotifications(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        UUID adminId = UUID.fromString(jwt.getSubject());
        return this.notificationService.getUnreadNotifications(adminId, page, size);
    }

    @Operation(summary = "Mark notification as read")
    @PatchMapping("/{notificationId}/read")
    @PreAuthorize("hasRole('REPORT_ADMIN')")
    public ResponseEntity<Void> markAsRead(@PathVariable UUID notificationId) {
        this.notificationService.markNotificationAsRead(notificationId);
        return ResponseEntity.ok().build();
    }
}
