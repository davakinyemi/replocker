package com.ap2.replocker.report_collection.report;

import com.ap2.replocker.report_collection.ReportCollection;
import com.ap2.replocker.report_collection.ReportCollectionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportMapper {

    public Report toReport(ReportRequest request, ReportCollection collection) {
        return Report.builder()
                .name(request.name())
                .reportCollection(collection)
                .build();
    }

    public ReportResponse toReportResponse(Report report) {
        return ReportResponse.builder()
                .id(report.getId())
                .name(report.getName())
                .filePath(report.getFilePath())
                .sizeBytes(report.getSizeBytes())
                .type(report.getType())
                .createdDate(report.getCreatedDate())
                .reportCollectionId(report.getReportCollection().getId())
                .build();
    }
}