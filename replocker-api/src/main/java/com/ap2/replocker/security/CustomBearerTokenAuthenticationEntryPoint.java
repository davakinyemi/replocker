package com.ap2.replocker.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Dave AKN
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CustomBearerTokenAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException authException
    ) throws IOException {
        if (!this.isPublicEndpoint(request)) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), authException.getMessage());
        }
    }

    private boolean isPublicEndpoint(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/report-collections/public/");
    }
}
