package com.ap2.replocker.report_collection.access_request;

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
    Page<AccessRequest> findByReportCollectionIdAndStatus(UUID collectionId, RequestStatus status, Pageable pageable);
    Optional<AccessRequest> findByEmailAndReportCollectionId(String email, UUID collectionId);
}
