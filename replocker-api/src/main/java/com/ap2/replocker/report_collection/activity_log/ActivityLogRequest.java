package com.ap2.replocker.report_collection.activity_log;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ActivityLogRequest(
        @NotNull ActivityType activityType,
        @NotBlank String ipAddress,
        UUID userId,
        UUID reportId,
        UUID reportCollectionID
) {
}
