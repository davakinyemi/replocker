package com.ap2.replocker.report_collection;

import com.ap2.replocker.admin.AdminService;
import com.ap2.replocker.common.PageResponse;
import com.ap2.replocker.exception.custom.BusinessRuleException;
import com.ap2.replocker.report_collection.access_request.AccessRequestDTO;
import com.ap2.replocker.report_collection.access_request.AccessRequestResponse;
import com.ap2.replocker.report_collection.access_request.AccessRequestService;
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

import java.util.UUID;

@RestController
@RequestMapping("/report-collections")
@SecurityRequirement(name = "keycloak")
@RequiredArgsConstructor
public class ReportCollectionController {
    private final ReportCollectionService reportCollectionService;
    private final AccessRequestService accessRequestService;
    private final AdminService adminService;

    @Operation(summary = "List report collections by RepLocker admin")
    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('REPLOCKER_ADMIN')")
    public ResponseEntity<PageResponse<ReportCollectionResponse>> getMyCollections(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        UUID adminId = this.adminService.getAdminId(jwt);
        return ResponseEntity.ok(this.reportCollectionService.getCollectionsByAdmin(adminId, page, size));
    }

    @Operation(summary = "Get collection details for RepLocker admin")
    @GetMapping("/my/{collectionId}")
    @PreAuthorize("hasAnyRole('REPLOCKER_ADMIN')")
    public ResponseEntity<ReportCollectionResponse> getCollectionDetails(
        @PathVariable UUID collectionId,
        @AuthenticationPrincipal Jwt jwt
    ) {
        UUID adminId = this.adminService.getAdminId(jwt);
        return ResponseEntity.ok(this.reportCollectionService.getCollectionByIdAndAdmin(collectionId, adminId));
    }

    @Operation(summary = "Create new report collection as RepLocker admin")
    @PostMapping("/my/create")
    @PreAuthorize("hasAnyRole('REPLOCKER_ADMIN')")
    public ResponseEntity<ReportCollectionResponse> createCollection(
        @Valid @RequestBody ReportCollectionRequest request,
        @AuthenticationPrincipal Jwt jwt
    ) {
        UUID adminId = this.adminService.getAdminId(jwt);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.reportCollectionService.createCollection(request, adminId));
    }

    @Operation(summary = "List access request for locked report collection")
    @GetMapping("/my/{collectionId}/access-requests")
    @PreAuthorize("hasAnyRole('REPLOCKER_ADMIN')")
    public ResponseEntity<PageResponse<AccessRequestResponse>> getAccessRequests(
        @PathVariable UUID collectionId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(this.accessRequestService.getRequestsByCollectionId(collectionId, page, size));
    }

    @Operation(summary = "Update report collection metadata")
    @PatchMapping("/my/{collectionId}/edit")
    @PreAuthorize("hasAnyRole('REPLOCKER_ADMIN')")
    public ResponseEntity<ReportCollectionResponse> updateCollection(
        @PathVariable UUID collectionId,
        @Valid @RequestBody ReportCollectionRequest request,
        @AuthenticationPrincipal Jwt jwt
    ) {
        UUID adminId = this.adminService.getAdminId(jwt);
        return ResponseEntity.ok(this.reportCollectionService.updateCollection(collectionId, request, adminId));
    }

    @Operation(summary = "Delete report collection cascade")
    @DeleteMapping("/my/{collectionId}/delete")
    @PreAuthorize("hasAnyRole('REPLOCKER_ADMIN')")
    public ResponseEntity<Void> deleteCollection(
        @PathVariable UUID collectionId,
        @AuthenticationPrincipal Jwt jwt
    ) {
        UUID adminId = this.adminService.getAdminId(jwt);
        this.reportCollectionService.deleteCollection(collectionId, adminId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "List report collections (public)")
    @GetMapping("/public")
    public ResponseEntity<PageResponse<ReportCollectionResponse>> getPublishedCollections(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(this.reportCollectionService.getCollectionsPublic(page, size));
    }

    @Operation(summary = "Get report collection details (public)")
    @GetMapping("/public/{collectionId}")
    public ResponseEntity<ReportCollectionResponse> getPublishedCollection(
        @PathVariable UUID collectionId,
        @RequestHeader(value = "accessToken", required = false) String accessToken
    ) {
        return ResponseEntity.ok(this.reportCollectionService.getCollectionWithAccessCheck(collectionId, accessToken));
    }

    @Operation(summary = "Submit access request for locked collection")
    @PostMapping("/public/{collectionId}/request-access")
    public ResponseEntity<AccessRequestResponse> createAccessRequest(
        @PathVariable UUID collectionId,
        @Valid @RequestBody AccessRequestDTO requestDTO
    ) throws BusinessRuleException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.accessRequestService.createAccessRequest(collectionId, requestDTO));
    }

}
