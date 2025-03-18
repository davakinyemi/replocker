package com.ap2.replocker.user.access_token;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Dave AKN
 * @version 1.0
 */
@RestController
@RequestMapping("access-tokens")
@SecurityRequirement(name = "keycloak")
@RequiredArgsConstructor
public class AccessTokenController {
    private final AccessTokenService accessTokenService;


}
