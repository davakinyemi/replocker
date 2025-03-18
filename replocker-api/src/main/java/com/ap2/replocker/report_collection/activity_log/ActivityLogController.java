package com.ap2.replocker.report_collection.activity_log;

import com.ap2.replocker.common.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author Dave AKN
 * @version 1.0
 */
@RestController
@RequestMapping("/activity-log")
@SecurityRequirement(name = "keycloak")
@RequiredArgsConstructor
public class ActivityLogController {
    private final ActivityLogService activityLogService;

    /*@Operation(summary = "Get activity logs for collection")
    @GetMapping("/collection/{collectionId}")
    @PreAuthorize("hasRole('REPORT_ADMIN')")
    public PageResponse<ActivityLogResponse> getLogsByCollection(
            @PathVariable UUID collectionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        return PageResponse.fromPage(
                this.activityLogService.getLogsByCollection(collectionId, page, size)
                        .map(this.activityLogService.getMapper()::toResponse)
        );
    } */
}
