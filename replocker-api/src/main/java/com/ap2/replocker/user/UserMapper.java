package com.ap2.replocker.user;

import com.ap2.replocker.user.access_token.AccessToken;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMapper {

    public User toUser(UserRequest userRequest) {
        return User.builder()
                .hashedEmail(this.hashEmail(userRequest.email()))
                .build();
    }

    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .createdDate(user.getCreatedDate())
                .tokenIds(user.getTokens().stream()
                        .map(AccessToken::getId)
                        .toList())
                .isActive(user.isActive())
                .build();
    }

    protected String hashEmail(@NotBlank(message = "Email cannot be blank") @Email(message = "Invalid email format") String email) {
        return DigestUtils.sha256Hex(email.toLowerCase().trim());
    }
}
