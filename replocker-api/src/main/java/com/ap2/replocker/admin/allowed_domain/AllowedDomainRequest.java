package com.ap2.replocker.admin.allowed_domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AllowedDomainRequest(
        @NotBlank(message = "Domain cannot be blank")
        @Pattern(
                regexp = AllowedDomainRequest.ALLOWED_DOMAIN_PATTERN,
                message = "Invalid domain format (e.g., domain.xyz, 24x7care.com, domain2024.xyz)",
                flags = Pattern.Flag.CASE_INSENSITIVE
        )
        String domainName
) {
        private static final String ALLOWED_DOMAIN_PATTERN =
                "^[a-zA-Z0-9_](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)+$";
                // "^@[a-zA-Z0-9_](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)+$";
}
