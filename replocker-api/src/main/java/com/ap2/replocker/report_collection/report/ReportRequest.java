package com.ap2.replocker.report_collection.report;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record ReportRequest(
        @NotBlank(message = "Name cannot be blank")
        @Size(max = 255, message = "Name must be less than 256 characters long")
        String name,

        @NotNull(message = "File cannot be null")
        MultipartFile file,

        @NotNull(message = "Collection ID cannot be null")
        UUID collectionId
) {
}
