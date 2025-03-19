package com.ap2.replocker.report_collection.access_request;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Dave AKN
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccessRequestResponse {
    private UUID id;
    private String name;
    private String email;
    private String message;
    private RequestStatus status;
    private UUID reportCollectionId;
    private String adminComment;
    private LocalDateTime createdDate;
}
