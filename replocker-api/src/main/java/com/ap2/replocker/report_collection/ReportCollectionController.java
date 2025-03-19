package com.ap2.replocker.report_collection;

import com.ap2.replocker.common.PageResponse;
import com.ap2.replocker.report_collection.report.ReportResponse;
import com.ap2.replocker.report_collection.report.ReportService;
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
@RequestMapping("/collection")
@SecurityRequirement(name = "keycloak")
@RequiredArgsConstructor
public class ReportCollectionController {
    private final ReportCollectionService reportCollectionService;
    private final ReportService reportService;

    @Operation(summary = "Create report collection (Admin only")
    @PostMapping("/create")
    @PreAuthorize("hasRole('REPORT_ADMIN')")
    public ResponseEntity<ReportCollectionResponse> createCollection(
            @Valid @RequestBody ReportCollectionRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID adminId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        this.reportCollectionService.createCollection(request, adminId)
                );
    }

    /* @Operation(summary = "List public collections")
    @GetMapping("/public")
    public ResponseEntity<PageResponse<ReportCollectionResponse>> getPublicCollections(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(this.reportCollectionService.getPublicCollections(page, size));
    } */

    /* @Operation(summary = "List reports in collection")
    @GetMapping
    public PageResponse<ReportResponse> getReports(
            @RequestParam UUID collectionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

    } */

}
