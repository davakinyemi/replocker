package com.ap2.replocker.user.access_token;

import com.ap2.replocker.exception.custom.InvalidTokenException;
import com.ap2.replocker.admin.allowed_domain.AllowedDomainRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

   public void validateToken(String tokenValue, UUID collectionId) {
       AccessToken token = this.accessTokenRepository.findByTokenValue(tokenValue)
               .orElseThrow(() -> new InvalidTokenException("Token not found"));

       if (token.isRevoked() || !token.isActive()) {
           throw new InvalidTokenException("Token revoked or no longer active");
       }

       if (LocalDateTime.now().isAfter(token.getExpiresAt())) {
           throw new InvalidTokenException("Token expired");
       }

       if (!token.getCollection().getId().equals(collectionId)) {
           throw new InvalidTokenException("Token not valid for this collection");
       }
   }

   @Scheduled(cron = "0 0 3 * * *")
    public void purgeExpiredTokens() {
       this.accessTokenRepository.deleteByExpiresAtBefore(LocalDateTime.now());
   }
}
