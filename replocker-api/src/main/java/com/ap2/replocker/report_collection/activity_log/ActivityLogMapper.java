package com.ap2.replocker.report_collection.activity_log;

import com.ap2.replocker.report_collection.ReportCollection;
import com.ap2.replocker.report_collection.ReportCollectionMapper;
import com.ap2.replocker.report_collection.report.Report;
import com.ap2.replocker.report_collection.report.ReportMapper;
import com.ap2.replocker.user.User;
import com.ap2.replocker.user.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Dave AKN
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class ActivityLogMapper {
    private final UserMapper userMapper;
    private final ReportMapper reportMapper;
    private final ReportCollectionMapper reportCollectionMapper;

    public ActivityLog toActivityLog(ActivityLogRequest request, User user,  Report report, ReportCollection reportCollection) {
        return ActivityLog.builder()
                .activityType(request.activityType())
                .ipAddress(request.ipAddress())
                .user(user)
                .report(report)
                .reportCollection(reportCollection)
                .build();
    }

    public ActivityLogResponse toActivityLogResponse(ActivityLog log) {
        return ActivityLogResponse.builder()
                .id(log.getId())
                .activityType(log.getActivityType())
                .ipAddress(log.getIpAddress())
                .user(this.userMapper.toUserResponse(log.getUser()))
                .report(this.reportMapper.toReportResponse(log.getReport()))
                .reportCollection(this.reportCollectionMapper.toReportCollectionResponse(log.getReportCollection()))
                .createdDate(log.getCreatedDate())
                .build();
    }
}
