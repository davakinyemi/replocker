package com.ap2.replocker.report_collection.access_request;

import com.ap2.replocker.common.PageResponse;
import com.ap2.replocker.email.EmailService;
import com.ap2.replocker.exception.custom.AccessRequestNotFoundException;
import com.ap2.replocker.report_collection.ReportCollection;
import com.ap2.replocker.report_collection.access_request.access_token.AccessTokenResponse;
import com.ap2.replocker.report_collection.access_request.access_token.AccessTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author Dave AKN
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AccessRequestService {
    private final AccessRequestRepository accessRequestRepository;
    private final AccessRequestMapper accessRequestMapper;
    private final AccessTokenService accessTokenService;
    private final EmailService emailService;

    public PageResponse<AccessRequestResponse> getRequestsByCollection(
        UUID collectionId,
        int page,
        int size
    ) {
        Page<AccessRequest> requests = this.accessRequestRepository.findByReportCollectionId(
                collectionId,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"))
        );
        return PageResponse.fromPage(requests.map(this.accessRequestMapper::toAccessRequestResponse));
    }

    public void processAccessRequest(UUID accessRequestId, AccessRequestUpdateDTO update) {
        AccessRequest request = this.accessRequestRepository.findById(accessRequestId)
                .orElseThrow(() -> new AccessRequestNotFoundException(accessRequestId));

        ReportCollection reportCollection = request.getReportCollection();

        if (update.status() == RequestStatus.ACCEPTED) {
            AccessTokenResponse accessToken = this.accessTokenService.createAccessToken(accessRequestId);
            String token = accessToken.getTokenValue();
            this.emailService.sendRequestAccepted(
                request.getEmail(),
                token,
                request.getName(),
                accessToken.getExpiresAt()
            );
        } else {
            this.emailService.sendRequestRejected(
                    request.getEmail(),
                    update.adminComment(),
                    reportCollection.getName()
            );
        }
    }
}
