package com.ap2.replocker.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class ApplicationAuditAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() { // ToDo: Not annotated method overrides method annotated with @NonNullApi
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(
                authentication == null ||
                        !authentication.isAuthenticated() ||
                        authentication instanceof AnonymousAuthenticationToken

        ) {
            return Optional.empty();
        }
        return Optional.ofNullable(authentication.getName());
    }
}
