package com.ap2.replocker.admin.allowed_domain;

import com.ap2.replocker.admin.Admin;
import com.ap2.replocker.admin.AdminRepository;
import com.ap2.replocker.common.PageResponse;
import com.ap2.replocker.exception.custom.AdminNotFoundException;
import com.ap2.replocker.exception.custom.DomainNotFoundException;
import com.ap2.replocker.exception.custom.DuplicateDomainException;
import com.ap2.replocker.exception.custom.OperationNotPermittedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .orElseThrow(() -> new AdminNotFoundException("Admin not found", adminId));

        if (this.domainRepository.existsByAdminIdAndDomainNameIgnoreCase(adminId, request.domainName())) {
            throw new DuplicateDomainException("Duplicate domain name", request.domainName());
        }

        return this.domainMapper.toAllowedDomainResponse(
                this.domainRepository.save(this.domainMapper.toAllowedDomain(request, admin))
        );
    }

    public PageResponse<AllowedDomainResponse> getDomainsByAdmin(UUID adminId, int page, int size) {
        Page<AllowedDomain> domains = this.domainRepository.findByAdminId(
                adminId, PageRequest.of(page, size, Sort.by("createdDate").descending())
        );

        return PageResponse.fromPage(domains.map(this.domainMapper::toAllowedDomainResponse));
    }

    public void deleteDomain(UUID domainId, UUID adminId) {
        AllowedDomain domain = this.domainRepository.findById(domainId)
                .orElseThrow(() -> new DomainNotFoundException("Domain not found: ", domainId));

        if (!domain.getAdmin().getId().equals(adminId)) {
            throw new OperationNotPermittedException("Domain ownership violation");
        }
        this.domainRepository.delete(domain);
    }
}
