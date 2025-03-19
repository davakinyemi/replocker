package com.ap2.replocker.report_collection.access_request.access_token;

import com.ap2.replocker.exception.custom.AccessRequestNotFoundException;
import com.ap2.replocker.report_collection.access_request.AccessRequest;
import com.ap2.replocker.report_collection.access_request.AccessRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Dave AKN
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class AccessTokenMapper {
    private final AccessRequestRepository accessRequestRepository;

    public AccessToken toAccessToken(UUID accessRequestId) {
        AccessRequest accessRequest = this.accessRequestRepository.findById(accessRequestId)
                .orElseThrow(() -> new AccessRequestNotFoundException("", accessRequestId));

        return AccessToken.builder()
                .tokenValue(UUID.randomUUID().toString())
                .expiresAt(LocalDateTime.now().plusDays(7))
                .reportCollection(accessRequest.getReportCollection())
                .accessRequest(accessRequest)
                .isActive(true)
                .build();
    }

    public AccessTokenResponse toAccessTokenResponse(AccessToken token) {
        return AccessTokenResponse.builder()
                .id(token.getId())
                .tokenValue(token.getTokenValue())
                .expiresAt(token.getExpiresAt())
                .reportCollectionId(token.getReportCollection().getId())
                .accessRequestId(token.getAccessRequest().getId())
                .isActive(token.isActive())
                .build();
    }
}
