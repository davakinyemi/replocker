package com.ap2.replocker.report_collection.report;

import org.springframework.stereotype.Service;

@Service
public class ReportMapper {
    public ReportResponse toReportResponse(Report report) {
        return ReportResponse.builder()
                .build();
    }
}
