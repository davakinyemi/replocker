package com.ap2.replocker.common.audit_log;

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
public class AuditLogResponse {
    private UUID id;
    private ActionType actionType;
    private String entityName;
    private String entityId;
    private String performedBy;
    private LocalDateTime createdDate;
    private String details;
}
