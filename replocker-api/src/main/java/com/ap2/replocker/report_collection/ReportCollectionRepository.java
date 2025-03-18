package com.ap2.replocker.report_collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;
import java.util.UUID;

public interface ReportCollectionRepository extends JpaRepository<ReportCollection, UUID> {
    boolean existsByNameIgnoreCase(String name);
    Page<ReportCollection> findByIsPublished(Pageable pageable);
    Optional<ReportCollection> findByAdminId(UUID adminId);
    @NonNull Optional<ReportCollection> findById(@NonNull UUID collectionId);
    Optional<ReportCollection> findByIdAndAdminId(UUID collectionId, UUID adminId);
    Page<ReportCollection> findByIsPublishedTrueAndIsLockedFalse(Pageable pageable);
}
