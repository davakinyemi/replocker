package com.ap2.replocker.report_collection;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReportCollectionRequest(
        @NotBlank(message = "Name cannot be blank")
        @Size(max = 255, message = "Name must be less than 256 characters")
        String name,

        String description,

        @NotNull(message = "Lock status is required")
        Boolean isLocked,

        @NotNull(message = "Publish status is required")
        Boolean isPublished
) {}