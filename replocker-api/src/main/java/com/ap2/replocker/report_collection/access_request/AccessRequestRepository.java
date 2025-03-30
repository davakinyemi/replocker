package com.ap2.replocker.report_collection.access_request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Dave AKN
 * @version 1.0
 */
public interface AccessRequestRepository extends JpaRepository<AccessRequest, UUID> {
    Page<AccessRequest> findByReportCollectionId(UUID collectionId, Pageable pageable);
    Page<AccessRequest> findByReportCollectionIdAndStatus(UUID collectionId, RequestStatus status, Pageable pageable);
    Optional<AccessRequest> findByEmailAndReportCollectionId(String email, UUID collectionId);

    boolean existsByEmailAndReportCollectionId(@NotBlank(message = "Email cannot be blank") @Email(message = "Invalid email format") String email, UUID collectionId);
}
