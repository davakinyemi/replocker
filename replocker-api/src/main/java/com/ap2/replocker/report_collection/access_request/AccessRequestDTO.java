package com.ap2.replocker.report_collection.access_request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AccessRequestDTO(
        @NotBlank String name,
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email format")
        String email,
        String message,
        @NotNull UUID reportCollectionId
) {
}
