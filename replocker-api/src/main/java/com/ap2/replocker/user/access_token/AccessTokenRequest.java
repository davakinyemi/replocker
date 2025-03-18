package com.ap2.replocker.user.access_token;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AccessTokenRequest(
        @NotBlank
        @Email
        String email,
        @NotNull
        UUID collectionId
) {
}
