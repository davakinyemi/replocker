package com.ap2.replocker.report_collection;

import com.ap2.replocker.admin.Admin;
import com.ap2.replocker.admin.AdminMapper;
import com.ap2.replocker.report_collection.report.ReportMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportCollectionMapper {

    private final AdminMapper adminMapper;
    private final ReportMapper reportMapper;

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
                .admin(this.adminMapper.toAdminResponse(reportCollection.getAdmin()))
                .reports(reportCollection.getReports().stream()
                        .map(this.reportMapper::toReportResponse)
                        .toList())
                .createdDate(reportCollection.getCreatedDate())
                .build();
    }
}
