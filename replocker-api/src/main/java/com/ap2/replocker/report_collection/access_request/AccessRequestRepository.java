package com.ap2.replocker.report_collection.access_request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public interface AccessRequestRepository extends JpaRepository<AccessRequest, UUID> {
    @Query("SELECT r FROM AccessRequest r WHERE r.reportCollection.id = :collectionId")
    Page<AccessRequest> findByReportCollectionId(@Param("collectionId") UUID collectionId, Pageable pageable);

    // Page<AccessRequest> findByReportCollectionIdAndStatus(UUID collectionId, RequestStatus status, Pageable pageable);
    // Optional<AccessRequest> findByEmailAndReportCollectionId(String email, UUID collectionId);

    @Query("SELECT CASE WHEN COUNT (r) > 0 THEN true ELSE false END " +
            "FROM AccessRequest r WHERE r.email = :email AND r.reportCollection.id = :collectionId")
    boolean existsByEmailAndReportCollectionId(
        @Param("email")
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email format")
        String email,
        @Param("collectionId") UUID collectionId
    );

    @Query("SELECT r FROM AccessRequest r WHERE r.id = :accessRequestId " +
            "AND r.reportCollection.id IN (SELECT c.id FROM ReportCollection c where c.admin.id = :adminId)")
    Optional<AccessRequest> findByIdAndReportCollectionAdminId(
        @Param("accessRequestId") UUID accessRequestId,
        @Param("adminId") UUID adminId
    );

    @Query("SELECT r FROM AccessRequest r WHERE r.status = :status AND r.createdDate < :date")
    List<AccessRequest> findByStatusAndCreatedDateBefore(
        @Param("status") RequestStatus status,
        @Param("data") LocalDateTime date
    );
}
