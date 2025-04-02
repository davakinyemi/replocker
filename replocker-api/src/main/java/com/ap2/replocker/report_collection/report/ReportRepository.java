package com.ap2.replocker.report_collection.report;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReportRepository extends JpaRepository<Report, UUID> {
    boolean existsByNameAndReportCollectionId(String name, UUID collectionId);
    Page<Report> findByReportCollectionId(UUID collectionId, Pageable pageable);
    List<Report> findByCreatedDateBefore(LocalDateTime threshold);
    void deleteByCreatedDateBefore(LocalDateTime threshold);
    Optional<Report> findByIdAndReportCollectionAdminId(UUID reportId, UUID adminId);
    @Query("SELECT r FROM Report r WHERE r.id = :reportId AND r.reportCollection.id = :reportCollectionId")
    Optional<Report> findByIdAndReportCollectionId(
            @Param("reportId") UUID reportId,
            @Param("reportCollectionId") UUID reportCollectionId
    );

    // @Query("SELECT COUNT(r) > 0 FROM Report r WHERE r.name = :name AND r.reportCollection.id = :reportCollectionId AND r.id <> :excludeId")
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
            "FROM Report r WHERE r.name = :name AND r.reportCollection.id = :reportCollectionId " +
            "AND r.id <> :excludeId")
    boolean existsByNameAndCollectionIdExcludingId(
        @Param("name") String name,
        @Param("reportCollectionId") UUID reportCollectionId,
        @Param("excludeId") UUID excludeId
    );
}
