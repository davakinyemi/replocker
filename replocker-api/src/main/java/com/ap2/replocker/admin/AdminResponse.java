package com.ap2.replocker.admin;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminResponse {
    private UUID id;
    private String username;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private List<UUID> reportCollectionIds;
    private List<String> allowedDomains;
}
