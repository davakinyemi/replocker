package com.ap2.replocker.admin.allowed_domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Dave AKN
 * @version 1.0
 */
public interface AllowedDomainRepository extends JpaRepository<AllowedDomain, UUID> {
    Page<AllowedDomain> findByAdminId(UUID adminId, Pageable pageable);
    boolean existsByAdminIdAndDomainNameIgnoreCase(UUID adminId, String domainName);

    @Query("SELECT d FROM AllowedDomain d WHERE d.admin.id = :adminId AND LOWER(d.domainName) = LOWER(:domainName)")
    Optional<AllowedDomain> findByAdminAndDomainCaseInsensitive(@Param("adminId") UUID adminId, @Param("domainName") String domainName);

    AllowedDomain findByDomainNameIgnoreCase(String domainName);
}