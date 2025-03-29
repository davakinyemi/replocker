package com.ap2.replocker.report_collection;

import com.ap2.replocker.admin.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReportCollectionRepository extends JpaRepository<ReportCollection, UUID> {
    boolean existsByNameIgnoreCase(String name);
    @NonNull Optional<ReportCollection> findById(@NonNull UUID collectionId);
    Page<ReportCollection> findByPublishedTrueAndLockedFalse(Pageable pageable);
    Page<ReportCollection> findByPublishedTrue(Pageable pageable);

    Page<ReportCollection> findByAdminId(UUID adminId, Pageable pageable);

    List<ReportCollection> admin(Admin admin);
    Optional<ReportCollection> findByIdAndAdminId(UUID id, UUID adminId);

    boolean existsByNameAndAdminId(String name, UUID adminId);
}
