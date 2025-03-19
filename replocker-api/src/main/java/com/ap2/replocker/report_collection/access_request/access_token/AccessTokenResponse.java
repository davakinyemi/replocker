package com.ap2.replocker.report_collection.access_request.access_token;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Dave AKN
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccessTokenResponse {
    private UUID id;
    private String tokenValue;
    private LocalDateTime expiresAt;
    private UUID reportCollectionId;
    private UUID accessRequestId;
    private boolean isActive;
}
