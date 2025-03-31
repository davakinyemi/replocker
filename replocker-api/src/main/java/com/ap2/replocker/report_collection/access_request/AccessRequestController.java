package com.ap2.replocker.report_collection.access_request;

import com.ap2.replocker.admin.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
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
@RequestMapping("/access-requests")
@SecurityRequirement(name = "keycloak")
@RequiredArgsConstructor
public class AccessRequestController {
    private final AccessRequestService accessRequestService;
    private final AccessRequestMapper accessRequestMapper;
    private final AdminService adminService;

    @Operation(summary = "Get access request details")
    @GetMapping("/{requestId}")
    @PreAuthorize("hasAnyRole('REPLOCKER_ADMIN')")
    public ResponseEntity<AccessRequestResponse> getRequestDetails(
            @PathVariable UUID requestId
    ) {
        return ResponseEntity.ok(
                // this.accessRequestService.getRequestById(requestId)
                this.accessRequestMapper.toAccessRequestResponse(
                        this.accessRequestService.getRequestById(requestId)
                )
        );
    }

    @Operation(summary = "Process access request")
    @PatchMapping("/{requestId}")
    @PreAuthorize("hasAnyRole('REPLOCKER_ADMIN')")
    public ResponseEntity<AccessRequestResponse> processRequest(
        @PathVariable UUID requestId,
        @Valid @RequestBody AccessRequestUpdateDTO updateDTO,
        @AuthenticationPrincipal Jwt jwt
    ) {
        UUID adminId = this.adminService.getAdminId(jwt);
        AccessRequestResponse response = this.accessRequestService.processAccessRequest(adminId, requestId, updateDTO);
        return ResponseEntity.ok(response);
        /* this.notificationService.createAccessRequestNotification(
                this.accessRequestService.getRequestById(requestId),
                "Request updated: " + updateDTO.status()
        ); */
    }
}
