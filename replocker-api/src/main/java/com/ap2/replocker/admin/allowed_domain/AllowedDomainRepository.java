package com.ap2.replocker.admin.allowed_domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author Dave AKN
 * @version 1.0
 */
public interface AllowedDomainRepository extends JpaRepository<AllowedDomain, UUID> {
    boolean existsByAdminIdAndDomainNameIgnoreCase(UUID adminId, String domainName);
    List<AllowedDomain> findByAdminId(UUID adminId);
    AllowedDomain findByDomainNameIgnoreCase(String domainName);
}