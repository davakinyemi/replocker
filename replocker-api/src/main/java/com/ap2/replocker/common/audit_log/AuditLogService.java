package com.ap2.replocker.common.audit_log;

import com.ap2.replocker.common.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author Dave AKN
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuditLogService {
    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;

    public PageResponse<AuditLogResponse> getLogsByEntityName(String entityName, int page, int size) {
        Page<AuditLog> logs = this.auditLogRepository.findByEntityName(entityName, PageRequest.of(page, size));
        return PageResponse.fromPage(logs.map(this.auditLogMapper::toResponse));
    }

    public PageResponse<AuditLogResponse> getLogs(int page, int size) {
        Page<AuditLog> logs = this.auditLogRepository.findAll(PageRequest.of(page, size));
        return PageResponse.fromPage(logs.map(this.auditLogMapper::toResponse));
    }

    @Transactional
    public void logAction(ActionType actionType, String entityName, String entityId, String details) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        this.auditLogRepository.save(AuditLog.builder()
                .actionType(actionType)
                .entityName(entityName)
                .entityId(entityId)
                .performedBy(username)
                .details(details)
                .build());
    }

    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void purgeOldLogs() {
        this.auditLogRepository.deleteAll(
                this.auditLogRepository.findByCreatedDateBefore(LocalDateTime.now().minusYears(1))
        );
    }
}
