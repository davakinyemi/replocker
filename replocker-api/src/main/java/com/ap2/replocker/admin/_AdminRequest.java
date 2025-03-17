/*package com.ap2.replocker.admin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record _AdminRequest(
        @NotBlank(message = "Username cannot be blank")
        @Size(min = 5, max = 255, message = "Username must be more than 5 characters and less than 256 characters")
        String username,

        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email format")
        String email
) {}*/
