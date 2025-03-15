package com.ap2.replocker.report_collection;

import org.springframework.stereotype.Service;

@Service
public class ReportCollectionMapper {
    public ReportCollectionResponse toReportResponse(ReportCollection reportCollection) {
        return ReportCollectionResponse.builder()
                .build();
    }
}
