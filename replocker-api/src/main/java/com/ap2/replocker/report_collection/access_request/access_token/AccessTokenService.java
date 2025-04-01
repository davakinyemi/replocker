package com.ap2.replocker.report_collection.access_request.access_token;

import com.ap2.replocker.common.PageResponse;
import com.ap2.replocker.email.EmailService;
import com.ap2.replocker.exception.custom.AccessRequestNotFoundException;
import com.ap2.replocker.exception.custom.AccessTokenNotFoundException;
import com.ap2.replocker.exception.custom.InvalidTokenException;
import com.ap2.replocker.exception.custom.TokenGenerationException;
import com.ap2.replocker.report_collection.access_request.AccessRequest;
import com.ap2.replocker.report_collection.access_request.AccessRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Dave AKN
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AccessTokenService {
    private final AccessTokenRepository accessTokenRepository;
    private final AccessTokenMapper accessTokenMapper;
    private final AccessRequestRepository accessRequestRepository;
    private final EmailService emailService;
    private static final int MAX_GENERATION_ATTEMPTS = 10;
    private static final int TOKEN_VALUE_LENGTH = 6;
    private static final int MAX_VALIDITY_DAYS = 7;

   public void validateToken(String tokenValue, UUID collectionId) {
       if (tokenValue == null || !tokenValue.matches("\\d{6}")) {
           throw new InvalidTokenException("Invalid 6-digit format", tokenValue);
       }

       AccessToken token = this.accessTokenRepository.findByTokenValueAndReportCollectionId(tokenValue, collectionId)
               .orElseThrow(() -> new InvalidTokenException("Token not found, or invalid for this collection", tokenValue));

       /* if (!token.getReportCollection().getId().equals(collectionId)) {
           throw new InvalidTokenException("Token not valid for this collection", tokenValue);
       } */

       if (LocalDateTime.now().isAfter(token.getExpiresAt())) {
           throw new InvalidTokenException("Token expired", tokenValue);
       }

       if (token.isRevoked() || !token.isActive()) {
           throw new InvalidTokenException("Token revoked/inactive", tokenValue);
       }

   }

   public AccessTokenResponse createAndSaveAccessToken(UUID accessRequestId) {
       AccessRequest request = this.accessRequestRepository.findById(accessRequestId)
               .orElseThrow(() -> new AccessRequestNotFoundException(accessRequestId));

       String tokenValue = this.generateUniqueToken();

       AccessToken token = AccessToken.builder()
               .tokenValue(tokenValue)
               .reportCollection(request.getReportCollection())
               .accessRequest(request)
               .expiresAt(LocalDateTime.now().plusDays(MAX_VALIDITY_DAYS))
               .build();

       this.accessTokenRepository.save(token);

       return this.accessTokenMapper.toAccessTokenResponse(token);
   }

   private String generateUniqueToken() {
        String token;
        int attempts = 0;
        do {
            token = this.generateTokenValue();
            attempts++;
        } while (this.accessTokenRepository.existsByTokenValue(token) && attempts < MAX_GENERATION_ATTEMPTS);

        if (attempts >= MAX_GENERATION_ATTEMPTS) {
            throw new TokenGenerationException("Failed to generate unique token");
        }
        return token;
   }

    /* @Scheduled(cron = "0 0 3 * * *")
    public void purgeExpiredTokens() {
       this.accessTokenRepository.deleteByExpiresAtBefore(LocalDateTime.now());
    } */

   private String generateTokenValue() {
       SecureRandom random = new SecureRandom();
       StringBuilder tokenValue = new StringBuilder();
       for (int i = 0; i < AccessTokenService.TOKEN_VALUE_LENGTH; i++) {
           tokenValue.append(random.nextInt(10));
       }
       return tokenValue.toString();
   }

    public void revokeToken(UUID collectionId, UUID adminId, UUID tokenId) {
       AccessToken token = this.accessTokenRepository.findByIdAndReportCollectionId(tokenId, collectionId)
               .orElseThrow(() -> new AccessTokenNotFoundException(tokenId));

       /* if (!token.getReportCollection().getAdmin().getId().equals(adminId)) {
           throw new UnauthorizedException("Admin does not own this collection");
       } */

        this.emailService.sendTokenRevokedNotification(token.getAccessRequest().getEmail(), token.getReportCollection().getName(), token.getTokenValue());

        this.accessRequestRepository.delete(token.getAccessRequest());
    }

    public PageResponse<AccessTokenResponse> getTokensByCollectionId(UUID collectionId, int page, int size) {
        Page<AccessToken> tokens = this.accessTokenRepository.findByReportCollectionId(
                collectionId,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"))
        );
        return PageResponse.fromPage(tokens.map(this.accessTokenMapper::toAccessTokenResponse));
    }
}
