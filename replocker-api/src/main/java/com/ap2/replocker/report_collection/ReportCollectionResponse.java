package com.ap2.replocker.report_collection;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportCollectionResponse {
    private UUID id;
    private String name;
    private String description;
    private boolean isLocked;
    private boolean isPublished;
    private UUID adminId;
    private List<UUID> reportIds;
    private LocalDateTime createdDate;
}
