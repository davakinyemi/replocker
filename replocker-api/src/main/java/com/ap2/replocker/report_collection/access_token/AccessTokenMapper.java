package com.ap2.replocker.report_collection.access_token;

import com.ap2.replocker.exception.custom.CollectionNotFoundException;
import com.ap2.replocker.report_collection.ReportCollectionRepository;
import com.ap2.replocker.user.User;
import com.ap2.replocker.user.UserRepository;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Dave AKN
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class AccessTokenMapper {
    private final UserRepository userRepository;
    private final ReportCollectionRepository reportCollectionRepository;

    public AccessToken toAccessToken(AccessTokenRequest request) {
        return AccessToken.builder()
                .tokenValue(UUID.randomUUID().toString())
                .expiresAt(LocalDateTime.now().plusDays(7))
                .collection(this.reportCollectionRepository.findById(request.collectionId())
                        .orElseThrow(() -> new CollectionNotFoundException(request.collectionId())))
                .user(this.userRepository.findByHashedEmail(this.hashEmail(request.email()))
                        .orElseGet(() -> this.createUser(request.email())))
                .build();
    }

    public AccessTokenResponse toAccessTokenResponse(AccessToken token) {
        return AccessTokenResponse.builder()
                .id(token.getId())
                .tokenValue(token.getTokenValue())
                .expiresAt(token.getExpiresAt())
                .collectionId(token.getCollection().getId())
                .userId(token.getUser().getId())
                .isActive(token.isActive())
                .build();
    }

    private User createUser(@NotBlank(message = "Email cannot be blank") @Email(message = "Invalid email format") String email) {
        return this.userRepository.save(User.builder()
                .hashedEmail(this.hashEmail(email))
                .build());
    }

    protected String hashEmail(@NotBlank(message = "Email cannot be blank") @Email(message = "Invalid email format") String email) {
        return DigestUtils.sha256Hex(email.toLowerCase().trim());
    }
}
