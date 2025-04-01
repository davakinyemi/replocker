package com.ap2.replocker.report_collection;

import com.ap2.replocker.admin.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReportCollectionRepository extends JpaRepository<ReportCollection, UUID> {
    /* boolean existsByNameIgnoreCase(String name);
    @NonNull Optional<ReportCollection> findById(@NonNull UUID collectionId); */

    /* @Query("SELECT rc FROM ReportCollection rc WHERE rc.isPublished = true AND rc.isLocked = false")
    Page<ReportCollection> findByIsPublishedTrueAndLockedFalse(Pageable pageable); */

    @Query("SELECT rc FROM ReportCollection rc WHERE rc.isPublished = true")
    Page<ReportCollection> findByIsPublishedTrue(Pageable pageable);

    @Query("SELECT c FROM ReportCollection c WHERE c.admin.id = :adminId")
    Page<ReportCollection> findByAdminId(@Param("adminId") UUID adminId, Pageable pageable);

    // List<ReportCollection> admin(Admin admin);

    @Query("SELECT c FROM ReportCollection c WHERE c.id = :id AND c.admin.id = :adminId")
    Optional<ReportCollection> findByIdAndAdminId(
        @Param("id") UUID id,
        @Param("adminId") UUID adminId
    );

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM ReportCollection c WHERE c.name = :name AND c.admin.id = :adminId")
    boolean existsByNameAndAdminId(
        @Param("name") String name,
        @Param("adminId") UUID adminId
    );
}
