package com.ap2.replocker.report_collection.access_request.access_token;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Dave AKN
 * @version 1.0
 */
public interface AccessTokenRepository extends JpaRepository<AccessToken, UUID> {
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END " +
            "FROM AccessToken t WHERE t.tokenValue = :tokenValue")
    boolean existsByTokenValue(@Param("tokenValue") String tokenValue);

    // Optional<AccessToken> findByTokenValue(String tokenValue);

    /* boolean existsByAccessRequestAndReportCollectionAndExpiresAtAfter(
            AccessRequest accessRequest,
            ReportCollection reportCollection,
            LocalDateTime expiresAt
    ); */

    @Query("SELECT t FROM AccessToken t WHERE t.expiresAt < :date")
    List<AccessToken> findByExpiresAtBefore(@Param("date") LocalDateTime date);

    // void deleteByExpiresAtBefore(LocalDateTime expiresAtBefore);

    @Query("SELECT t FROM AccessToken t WHERE t.tokenValue = :tokenValue AND t.reportCollection.id = :collectionId")
    Optional<AccessToken> findByTokenValueAndReportCollectionId(
        @Param("tokenValue") String tokenValue,
        @Param("collectionId") UUID collectionId
    );

    @Query("SELECT t FROM AccessToken t WHERE t.expiresAt BETWEEN :start AND :end")
    List<AccessToken> findByExpiresAtBetween(
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );

    @Query("SELECT t FROM AccessToken t WHERE t.id = :tokenId AND t.reportCollection.id = :collectionId")
    Optional<AccessToken> findByIdAndReportCollectionId(
        @Param("tokenId") UUID tokenId,
        @Param("collectionId") UUID collectionId
    );

    @Query("SELECT t FROM AccessToken t WHERE t.reportCollection.id = :collectionId")
    Page<AccessToken> findByReportCollectionId(@Param("collectionId") UUID collectionId, Pageable pageable);
}
