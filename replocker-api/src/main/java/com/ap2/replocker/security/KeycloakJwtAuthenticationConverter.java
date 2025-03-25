package com.ap2.replocker.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Dave AKN
 * @version 1.0
 */
public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    @Value("${keycloak.replocker.client-id}")
    private String clientId;

    private static final String ROLES_CLAIM = "roles";

    private final JwtGrantedAuthoritiesConverter defaultConverter = new JwtGrantedAuthoritiesConverter();

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        return new JwtAuthenticationToken(
                jwt,
                Stream.concat(
                        Stream.concat(
                                defaultConverter.convert(jwt).stream(),
                                extractRealmRoles(jwt).stream()
                        ),
                        extractClientRoles(jwt).stream()
                ).collect(Collectors.toSet())
        );
    }

    private Collection<? extends GrantedAuthority> extractRealmRoles(Jwt jwt) {
        return Optional.ofNullable(jwt.getClaimAsStringList("realm_roles"))
                .orElse(Collections.emptyList())
                .stream()
                .map(role -> "ROLE_" + role)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    private Collection<? extends GrantedAuthority> extractClientRoles(Jwt jwt) {
        return Optional.ofNullable(jwt.getClaimAsMap("resource_access"))
                .map(resAccess -> (Map<?, ?>) resAccess.get(clientId))
                .map(clientRoles -> (List<?>) clientRoles.get("roles"))
                .orElse(Collections.emptyList())
                .stream()
                .map(String::valueOf)
                .map(role -> "ROLE_" + role)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
}
