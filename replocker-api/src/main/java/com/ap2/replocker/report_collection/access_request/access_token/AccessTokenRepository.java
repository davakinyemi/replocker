package com.ap2.replocker.report_collection.access_request.access_token;

import com.ap2.replocker.report_collection.ReportCollection;
import com.ap2.replocker.report_collection.access_request.AccessRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    boolean existsByTokenValue(String tokenValue);
    Optional<AccessToken> findByTokenValue(String tokenValue);
    boolean existsByAccessRequestAndReportCollectionAndExpiresAtAfter(
            AccessRequest accessRequest,
            ReportCollection reportCollection,
            LocalDateTime expiresAt
    );
    List<AccessToken> findByExpiresAtBefore(LocalDateTime date);

    void deleteByExpiresAtBefore(LocalDateTime expiresAtBefore);

    Optional<AccessToken> findByTokenValueAndReportCollectionId(String tokenValue, UUID collectionId);

    List<AccessToken> findByExpiresAtBetween(LocalDateTime start, LocalDateTime end);

    Optional<AccessToken> findByIdAndReportCollectionId(UUID tokenId, UUID collectionId);

    Page<AccessToken> findByReportCollectionId(UUID collectionId, Pageable pageable);
}
