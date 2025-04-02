package com.ap2.replocker.common.audit_log;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author Dave AKN
 * @version 1.0
 */
public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
    @Query("SELECT a FROM AuditLog a WHERE a.entityName = :entityName")
    Page<AuditLog> findByEntityName(@Param("entityName") String entityName, Pageable pageable);

    @Query("SELECT a FROM AuditLog a WHERE a.createdDate < :date")
    List<AuditLog> findByCreatedDateBefore(@Param("date") LocalDateTime date);
}
