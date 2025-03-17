package com.ap2.replocker.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Dave AKN
 * @version 1.0
 */
public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private static final String CLIENT_ID = "replocker-app";
    private static final String ROLES_CLAIM = "roles";

    private final JwtGrantedAuthoritiesConverter defaultConverter = new JwtGrantedAuthoritiesConverter();

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        return new JwtAuthenticationToken(
                jwt,
                Stream.concat(
                        this.defaultConverter.convert(jwt).stream(),
                        this.extractClientRoles(jwt).stream()
                ).collect(Collectors.toSet())
        );
    }

    private Collection<? extends GrantedAuthority> extractClientRoles(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess == null || !resourceAccess.containsKey(CLIENT_ID)) return Collections.emptyList();

        Object clientResource = resourceAccess.get(CLIENT_ID);
        if (!(clientResource instanceof Map<?, ?> clientRolesMap)) return Collections.emptyList();

        Object rolesObject = clientRolesMap.get(ROLES_CLAIM);
        if (!(rolesObject instanceof List<?> rolesNames)) return Collections.emptyList();

        return rolesNames.stream()
                .filter(String.class::isInstance)
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .toList();
    }
}
