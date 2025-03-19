package com.ap2.replocker.admin.allowed_domain;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Dave AKN
 * @version 1.0
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllowedDomainResponse {
    private UUID id;
    private String domainName;
    private UUID adminId;
    private LocalDateTime createdDate;
}
