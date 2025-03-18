package com.ap2.replocker.user.access_token;

import com.ap2.replocker.report_collection.ReportCollection;
import com.ap2.replocker.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Dave AKN
 * @version 1.0
 */
public interface AccessTokenRepository extends JpaRepository<AccessToken, UUID> {
    Optional<AccessToken> findByTokenValue(String tokenValue);
    boolean existsByUserAndCollectionAndExpiresAtAfter(
            User user,
            ReportCollection reportCollection,
            LocalDateTime expiresAt
    );
    List<AccessToken> findByExpiresAtBefore(LocalDateTime threshold);

    void deleteByExpiresAtBefore(LocalDateTime expiresAtBefore);
}
