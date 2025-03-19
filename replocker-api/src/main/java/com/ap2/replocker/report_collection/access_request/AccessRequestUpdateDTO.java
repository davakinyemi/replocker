package com.ap2.replocker.report_collection.access_request;

public record AccessRequestUpdateDTO(
        RequestStatus status,
        String adminComment
) {
}
