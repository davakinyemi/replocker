package com.ap2.replocker.report_collection.access_request;

import com.ap2.replocker.exception.custom.CollectionNotFoundException;
import com.ap2.replocker.report_collection.ReportCollectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Dave AKN
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class AccessRequestMapper {
    private final ReportCollectionRepository reportCollectionRepository;

    public AccessRequest toAccessRequest(AccessRequestDTO accessRequest) {
        return AccessRequest.builder()
                .name(accessRequest.name())
                .email(accessRequest.email())
                .message(accessRequest.message())
                .reportCollection(this.reportCollectionRepository.findById(accessRequest.reportCollectionId())
                        .orElseThrow(() -> new CollectionNotFoundException("", accessRequest.reportCollectionId())))
                .build();
    }

    public AccessRequestResponse toAccessRequestResponse(AccessRequest accessRequest) {
        return AccessRequestResponse.builder()
                .id(accessRequest.getId())
                .name(accessRequest.getName())
                .email(accessRequest.getEmail())
                .message(accessRequest.getMessage())
                .reportCollectionId(accessRequest.getReportCollection().getId())
                .status(accessRequest.getStatus())
                .adminComment(accessRequest.getAdminComment())
                .createdDate(accessRequest.getCreatedDate())
                .build();
    }
}
