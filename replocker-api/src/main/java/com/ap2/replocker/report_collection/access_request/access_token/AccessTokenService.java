package com.ap2.replocker.report_collection.access_request.access_token;

import com.ap2.replocker.admin.allowed_domain.AllowedDomainRepository;
import com.ap2.replocker.exception.custom.AccessRequestNotFoundException;
import com.ap2.replocker.exception.custom.InvalidTokenException;
import com.ap2.replocker.exception.custom.TokenGenerationException;
import com.ap2.replocker.report_collection.access_request.AccessRequest;
import com.ap2.replocker.report_collection.access_request.AccessRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
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
    private final AllowedDomainRepository allowedDomainRepository;
    private final AccessRequestRepository accessRequestRepository;
    private static final int MAX_GENERATION_ATTEMPTS = 10;
    private static final int TOKEN_VALUE_LENGTH = 6;
    private static final int MAX_VALIDITY_DAYS = 7;

   public void validateToken(String tokenValue, UUID collectionId) {
       AccessToken token = this.accessTokenRepository.findByTokenValue(tokenValue)
               .orElseThrow(() -> new InvalidTokenException("Token not found", tokenValue));

       if (token.isRevoked() || !token.isActive()) {
           throw new InvalidTokenException("Token revoked or no longer active", tokenValue);
       }

       if (LocalDateTime.now().isAfter(token.getExpiresAt())) {
           throw new InvalidTokenException("Token expired", tokenValue);
       }

       if (!token.getReportCollection().getId().equals(collectionId)) {
           throw new InvalidTokenException("Token not valid for this collection", tokenValue);
       }
   }

   public AccessTokenResponse createAccessToken(UUID accessRequestId) {
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
            token = this.generateTokenValue(TOKEN_VALUE_LENGTH);
            attempts++;
        } while (this.accessTokenRepository.existsByTokenValue(token) && attempts < MAX_GENERATION_ATTEMPTS);

        if (attempts >= MAX_GENERATION_ATTEMPTS) {
            throw new TokenGenerationException("Failed to generate unique token");
        }
        return token;
   }

   @Scheduled(cron = "0 0 3 * * *")
    public void purgeExpiredTokens() {
       this.accessTokenRepository.deleteByExpiresAtBefore(LocalDateTime.now());
   }

   private String generateTokenValue(int length) {
       SecureRandom random = new SecureRandom();
       StringBuilder tokenValue = new StringBuilder();
       for (int i = 0; i < length; i++) {
           tokenValue.append(random.nextInt(10));
       }
       return tokenValue.toString();
   }
}
