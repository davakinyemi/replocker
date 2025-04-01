package com.ap2.replocker.common.audit_log;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Dave AKN
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class AuditLogMapper {
    public AuditLogResponse toResponse(AuditLog log) {
        return AuditLogResponse.builder()
                .id(log.getId())
                .actionType(log.getActionType())
                .entityName(log.getEntityName())
                .entityId(log.getEntityId())
                .performedBy(log.getPerformedBy())
                .createdDate(log.getCreatedDate())
                .details(log.getDetails())
                .build();
    }

}
