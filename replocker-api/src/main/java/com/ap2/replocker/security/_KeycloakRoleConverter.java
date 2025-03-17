/* package com.ap2.replocker.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;
import java.util.stream.Collectors; */

/**
 * @author Dave AKN
 * @version 1.0
 */
/* public class _KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    @SuppressWarnings("unchecked")
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");

        if (resourceAccess == null) {
            return Collections.emptyList();
        }

        Object clientAccess = resourceAccess.get("replocker_backend");
        if (!(clientAccess instanceof Map)) {
            return Collections.emptyList();
        }

        Map<String, Object> clientRoles = (Map<String, Object>) clientAccess;
        Object rolesClaim = clientRoles.get("roles");

        if (!(rolesClaim instanceof List)) {
            return Collections.emptyList();
        }

        List<String> roles = (List<String>) rolesClaim;

        return roles.stream()
                .filter(Objects::nonNull)
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }
} */
