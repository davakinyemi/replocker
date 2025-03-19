package com.ap2.replocker.report_collection;

import com.ap2.replocker.admin.Admin;
import com.ap2.replocker.admin.AdminMapper;
import com.ap2.replocker.report_collection.report.Report;
import com.ap2.replocker.report_collection.report.ReportMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportCollectionMapper {

    private final AdminMapper adminMapper;

    public ReportCollection toReportCollection(ReportCollectionRequest request, Admin admin) {
        return ReportCollection.builder()
                .name(request.name())
                .description(request.description())
                .isLocked(request.isLocked())
                .isPublished(request.isPublished())
                .admin(admin)
                .build();
    }

    public ReportCollectionResponse toReportCollectionResponse(ReportCollection reportCollection) {
        return ReportCollectionResponse.builder()
                .id(reportCollection.getId())
                .name(reportCollection.getName())
                .description(reportCollection.getDescription())
                .isLocked(reportCollection.isLocked())
                .isPublished(reportCollection.isPublished())
                .adminId(reportCollection.getAdmin().getId())
                .reportIds(reportCollection.getReports().stream()
                        .map(Report::getId)
                        .toList())
                .createdDate(reportCollection.getCreatedDate())
                .build();
    }
}
