package com.ap2.replocker.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.*;
import io.swagger.v3.oas.annotations.servers.Server;

import static io.swagger.v3.oas.annotations.enums.SecuritySchemeIn.HEADER;
import static io.swagger.v3.oas.annotations.enums.SecuritySchemeType.OAUTH2;

/**
 * @author Dave AKN
 * @version 1.0
 */
@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Dave AKN",
                        email = "dav.akinyemi@gmail.com",
                        url = "https://github.com/davakinyemi"
                ),
                description = "OpenApi documentation for RepLocker API",
                title = "OpenApi specification - RepLocker",
                version = "1.0",
                license = @License(
                        name = "Trial",
                        url = "https://some-url.com"
                ),
                termsOfService = "Terms of service"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8088/api/v1"
                ),
                @Server(
                        description = "PROD ENV",
                        url = "https://another-url.com/v1"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = OAUTH2,
        flows = @OAuthFlows(
                clientCredentials =
                @OAuthFlow(
                        // authorizationUrl = "http://localhost:9090/realms/replocker/protocol/openid-connect/auth"
                        tokenUrl = "http://localhost:9090/realms/replocker/protocol/openid-connect/token",
                        scopes = @OAuthScope(name = "openid", description = "OpenID scope")
                )
        ),
        bearerFormat = "JWT",
        in = HEADER
)
public class OpenApiConfig {
}
