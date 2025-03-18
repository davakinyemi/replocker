package com.ap2.replocker.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author Dave AKN
 * @version 1.0
 */
@RestController
@RequestMapping("/user")
@SecurityRequirement(name = "keycloak")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "Request access to locked collection")
    @PostMapping("/request-access") // http://localhost:8088/api/users/request-access?collectionId=...
    public ResponseEntity<UserResponse> requestAccess(
            @Valid @RequestBody UserRequest userRequest,
            @RequestParam UUID collectionId
    ) {
        UserResponse userResponse = this.userService.createOrGetUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }
}
