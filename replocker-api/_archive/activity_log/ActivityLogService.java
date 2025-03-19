/*
package com.ap2.replocker.report_collection.activity_log;

import com.ap2.replocker.exception.custom.CollectionNotFoundException;
import com.ap2.replocker.exception.custom.UserNotFoundException;
import com.ap2.replocker.report_collection.ReportCollection;
import com.ap2.replocker.report_collection.ReportCollectionRepository;
import com.ap2.replocker.report_collection.report.Report;
import com.ap2.replocker.report_collection.report.ReportRepository;
import com.ap2.replocker.user.User;
import com.ap2.replocker.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

*/
/**
 * @author Dave AKN
 * @version 1.0
 *//*

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ActivityLogService {
    private final ActivityLogRepository activityLogRepository;
    private final UserRepository userRepository;
    private final ReportRepository reportRepository;
    private final ReportCollectionRepository reportCollectionRepository;
    private final ActivityLogMapper activityLogMapper;

    public void logActivity(ActivityLogRequest request) {
        User user = this.userRepository.findById(request.userId())
                .orElseThrow(() -> new UserNotFoundException(request.userId()));

        Report report = request.reportId() != null ?
            this.reportRepository.findById(request.reportId()).orElse(null) : null;

        ReportCollection collection = this.reportCollectionRepository.findById(request.reportCollectionID())
                .orElseThrow(() -> new CollectionNotFoundException(request.reportCollectionID()));

        ActivityLog log = this.activityLogMapper.toActivityLog(request, user, report, collection);
        this.activityLogRepository.save(log);
    }

    @Scheduled(cron = "0 0 3 * * *")
    public void purgeOldLogs() {
        this.activityLogRepository.deleteAll(this.activityLogRepository.findByCreatedDateBefore(LocalDateTime.now().minusMonths(6)));
    }

}
*/
