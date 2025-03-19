package com.ap2.replocker.admin.notification;

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
public class NotificationResponse {
    private UUID id;
    private String message;
    private boolean isRead;
    private LocalDateTime createdDate;
    private UUID accessRequestId;
    private String reportCollectionName;
}
