package com.ap2.replocker.admin.allowed_domain;

import com.ap2.replocker.admin.Admin;
import com.ap2.replocker.admin.AdminMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Dave AKN
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class AllowedDomainMapper {
    private final AdminMapper adminMapper;

    public AllowedDomain toAllowedDomain(AllowedDomainRequest request, Admin admin) {
        return AllowedDomain.builder()
                .domainName(request.domainName().toLowerCase().trim())
                .admin(admin)
                .build();
    }

    public AllowedDomainResponse toAllowedDomainResponse(AllowedDomain domain) {
        return AllowedDomainResponse.builder()
                .id(domain.getId())
                .domainName(domain.getDomainName())
                .adminResponse(adminMapper.toAdminResponse(domain.getAdmin()))
                .createdDate(domain.getCreatedDate())
                .build();
    }
}
