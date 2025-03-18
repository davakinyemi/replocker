package com.ap2.replocker.admin.allowed_domain;

import com.ap2.replocker.admin.Admin;
import com.ap2.replocker.admin.AdminRepository;
import com.ap2.replocker.exception.custom.AdminNotFoundException;
import com.ap2.replocker.exception.custom.DuplicateDomainException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author Dave AKN
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AllowedDomainService {

    private final AllowedDomainRepository domainRepository;
    private final AdminRepository adminRepository;
    private final AllowedDomainMapper domainMapper;

    public AllowedDomainResponse addDomain(AllowedDomainRequest request, UUID adminId) {
        Admin admin = this.adminRepository.findById(adminId)
                .orElseThrow(() -> new AdminNotFoundException(adminId));

        if (this.domainRepository.existsByAdminIdAndDomainNameIgnoreCase(adminId, request.domainName())) {
            throw new DuplicateDomainException(request.domainName());
        }

        AllowedDomain domain = this.domainMapper.toAllowedDomain(request, admin);
        return this.domainMapper.toAllowedDomainResponse(domainRepository.save(domain));
    }

    public List<AllowedDomainResponse> getDomainsByAdminId(UUID adminId) {
        return this.domainRepository.findByAdminId(adminId).stream()
                .map(this.domainMapper::toAllowedDomainResponse)
                .toList();
    }
}
