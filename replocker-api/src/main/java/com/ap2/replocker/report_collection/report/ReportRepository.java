package com.ap2.replocker.report_collection.report;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ReportRepository extends JpaRepository<Report, UUID> {
    boolean existsByNameAndReportCollectionId(String name, UUID collectionId);
    Page<Report> findByReportCollectionId(UUID collectionId, Pageable pageable);
    List<Report> findByCreatedDateBefore(LocalDateTime threshold);
}
