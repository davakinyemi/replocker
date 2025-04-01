package com.ap2.replocker.report_collection;

import com.ap2.replocker.admin.AdminService;
import com.ap2.replocker.common.PageResponse;
import com.ap2.replocker.exception.custom.BusinessRuleException;
import com.ap2.replocker.file.FileUtils;
import com.ap2.replocker.report_collection.access_request.AccessRequestDTO;
import com.ap2.replocker.report_collection.access_request.AccessRequestResponse;
import com.ap2.replocker.report_collection.access_request.AccessRequestService;
import com.ap2.replocker.report_collection.access_request.access_token.AccessTokenResponse;
import com.ap2.replocker.report_collection.access_request.access_token.AccessTokenService;
import com.ap2.replocker.report_collection.report.ReportRequest;
import com.ap2.replocker.report_collection.report.ReportResponse;
import com.ap2.replocker.report_collection.report.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/report-collections")
@SecurityRequirement(name = "keycloak")
@RequiredArgsConstructor
public class ReportCollectionController {
    private final ReportCollectionService reportCollectionService;
    private final ReportService reportService;
    private final AccessRequestService accessRequestService;
    private final AccessTokenService accessTokenService;
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

    @Operation(summary = "Get reports in collection")
    @GetMapping("/my/{collectionId}/reports")
    @PreAuthorize("hasAnyRole('REPLOCKER_ADMIN')")
    public ResponseEntity<PageResponse<ReportResponse>> getReportsAsAdmin(
            @PathVariable UUID collectionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID adminId = this.adminService.getAdminId(jwt);
        return ResponseEntity.ok(this.reportService.getReportsByCollectionAndAdmin(collectionId, adminId, page, size));
    }

    @Operation(summary = "Upload report to collection (RepLocker Admin)")
    @PostMapping(
            value = "/my/{collectionId}/reports/upload-report",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @PreAuthorize("hasAnyRole('REPLOCKER_ADMIN')")
    public ResponseEntity<ReportResponse> uploadReport(
            @Valid ReportRequest reportRequest,
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID collectionId
    ) throws IOException {
        UUID adminId = adminService.getAdminId(jwt);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.reportService.createReport(reportRequest, adminId));
    }

    @Operation(summary = "Update report (RepLocker Admin)")
    @PutMapping(
            value = "/my/{collectionId}/reports/{reportId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @PreAuthorize("hasAnyRole('REPLOCKER_ADMIN')")
    public ResponseEntity<ReportResponse> updateReport(
            @PathVariable UUID collectionId,
            @PathVariable UUID reportId,
            @Valid @RequestPart("request") ReportRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @AuthenticationPrincipal Jwt jwt
    ) throws IOException {
        UUID adminId = adminService.getAdminId(jwt);
        return ResponseEntity.ok(this.reportService.updateReport(collectionId, reportId, request, file, adminId));
    }

    /* @Operation(summary = "Update report (RepLocker Admin)")
    @PutMapping("/my/{collectionId}/{reportId}")
    @PreAuthorize("hasAnyRole('REPLOCKER_ADMIN')")
    public ResponseEntity<ReportResponse> updateReport(
        @PathVariable UUID collectionId,
        @PathVariable UUID reportId,
        @Valid @RequestBody ReportRequest request,
        @AuthenticationPrincipal Jwt jwt
    ) {
        UUID adminId = adminService.getAdminId(jwt);
        return ResponseEntity.ok(this.reportService.updateReport(collectionId, reportId, request, adminId));
    } */

    @Operation(summary = "Delete report (RepLocker Admin)")
    @DeleteMapping("/my/{collectionId}/reports/{reportId}/delete")
    @PreAuthorize("hasAnyRole('REPLOCKER_ADMIN')")
    public ResponseEntity<Void> deleteReport(
        @PathVariable UUID collectionId,
        @PathVariable UUID reportId,
        @AuthenticationPrincipal Jwt jwt
    ) {
        UUID adminId = adminService.getAdminId(jwt);
        this.reportService.deleteReport(collectionId, reportId, adminId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "List access tokens for locked report collection")
    @GetMapping("/my/{collectionId}/access-tokens")
    @PreAuthorize("hasAnyRole('REPLOCKER_ADMIN')")
    public ResponseEntity<PageResponse<AccessTokenResponse>> getAccessTokens(
            @PathVariable UUID collectionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(this.accessTokenService.getTokensByCollectionId(collectionId, page, size));
    }

    @Operation(summary = "Revoke access token (RepLocker Admin)")
    @DeleteMapping("/my/{collectionId}/access-tokens/{tokenId}/revoke")
    @PreAuthorize("hasAnyRole('REPLOCKER_ADMIN')")
    public ResponseEntity<Void> revokeAccessToken(
        @PathVariable UUID collectionId,
        @PathVariable UUID tokenId,
        @AuthenticationPrincipal Jwt jwt
    ) {
        UUID adminId = adminService.getAdminId(jwt);
        this.accessTokenService.revokeToken(collectionId, adminId, tokenId);
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

    @Operation(summary = "Get reports in collection")
    @GetMapping("/public/{collectionId}/reports")
    public ResponseEntity<PageResponse<ReportResponse>> getReports(
        @PathVariable UUID collectionId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestHeader(value = "accessToken", required = false) String accessToken
    ) {
        return ResponseEntity.ok(this.reportService.getReportsByCollection(collectionId, page, size, accessToken));
    }

    @Operation(summary = "Download report file")
    @GetMapping("/public/{collectionId}/{reportId}/download")
    public ResponseEntity<Resource> downloadReport(
        @PathVariable UUID collectionId,
        @PathVariable UUID reportId,
        @RequestHeader(value = "accessToken", required = false) String accessToken
    ) {
        ReportResponse report = this.reportService.getReportByCollection(collectionId, reportId, accessToken);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + report.getName() + "\"")
                .body(
                        new ByteArrayResource(FileUtils.readFileFromLocation(report.getFilePath()))
                );
    }


}
