package com.ap2.replocker.report_collection.report;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponse {
    private UUID id;
    private String name;
    private String filePath;
    private long sizeBytes;
    private ReportType type;
    private LocalDateTime createdDate;
    private UUID reportCollectionId;
}
