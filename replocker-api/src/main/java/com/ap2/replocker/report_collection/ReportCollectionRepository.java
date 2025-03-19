package com.ap2.replocker.report_collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;
import java.util.UUID;

public interface ReportCollectionRepository extends JpaRepository<ReportCollection, UUID> {
    boolean existsByNameIgnoreCase(String name);
    @NonNull Optional<ReportCollection> findById(@NonNull UUID collectionId);
    Page<ReportCollection> findByPublishedTrueAndLockedFalse(Pageable pageable);
}
