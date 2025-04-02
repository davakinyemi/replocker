package com.ap2.replocker.report_collection.access_request;

import com.ap2.replocker.admin.Admin;
import com.ap2.replocker.admin.allowed_domain.AllowedDomainRepository;
import com.ap2.replocker.admin.allowed_domain.AllowedDomainService;
import com.ap2.replocker.admin.notification.Notification;
import com.ap2.replocker.admin.notification.NotificationRepository;
import com.ap2.replocker.admin.notification.NotificationService;
import com.ap2.replocker.common.PageResponse;
import com.ap2.replocker.email.EmailService;
import com.ap2.replocker.exception.custom.*;
import com.ap2.replocker.report_collection.ReportCollection;
import com.ap2.replocker.report_collection.ReportCollectionRepository;
import com.ap2.replocker.report_collection.access_request.access_token.AccessTokenResponse;
import com.ap2.replocker.report_collection.access_request.access_token.AccessTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    private final ReportCollectionRepository reportCollectionRepository;
    private final AllowedDomainRepository allowedDomainRepository;
    private final AllowedDomainService allowedDomainService;
    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    public PageResponse<AccessRequestResponse> getRequestsByCollectionId(
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

    public AccessRequest getRequestById(UUID requestId) {
        return this.accessRequestRepository.findById(requestId)
                .orElseThrow(() -> new AccessRequestNotFoundException(requestId));
        /* return this.accessRequestMapper.toAccessRequestResponse(
                this.accessRequestRepository.findById(requestId)
                .orElseThrow(() -> new AccessRequestNotFoundException(requestId))
        ); */
    }

    public AccessRequestResponse processAccessRequest(UUID adminId, UUID accessRequestId, AccessRequestUpdateDTO update) {
        AccessRequest request = this.accessRequestRepository.findByIdAndReportCollectionAdminId(accessRequestId, adminId)
                .orElseThrow(() -> new AccessRequestNotFoundException(accessRequestId));

        List<Notification> notifications = this.notificationRepository.findByAccessRequestId(accessRequestId)
                .orElseThrow(() -> new NotificationNotFoundException(accessRequestId));

        request.setStatus(update.status());
        request.setAdminComment(update.adminComment());

        notifications.forEach(notification -> {
            notification.setRead(true);
            this.notificationRepository.save(notification);
        });

        AccessRequest updatedRequest = this.accessRequestRepository.save(request);

        ReportCollection reportCollection = request.getReportCollection();

        if (update.status() == RequestStatus.ACCEPTED) {
            AccessTokenResponse accessToken = this.accessTokenService.createAndSaveAccessToken(accessRequestId);
            String token = accessToken.getTokenValue();
            this.emailService.sendRequestAccepted(
                updatedRequest.getEmail(),
                token,
                reportCollection.getName(),
                accessToken.getExpiresAt()
            );
        } else {
            this.emailService.sendRequestRejected(
                    updatedRequest.getEmail(),
                    updatedRequest.getAdminComment(),
                    reportCollection.getName()
            );
        }

        this.notificationService.createAccessRequestNotification(
                updatedRequest,
                "Request updated: " + updatedRequest.getStatus()
        );

        return this.accessRequestMapper.toAccessRequestResponse(updatedRequest);
    }

    public AccessRequestResponse createAccessRequest(UUID collectionId, @Valid AccessRequestDTO requestDTO) throws BusinessRuleException {
        ReportCollection collection = this.reportCollectionRepository.findById(collectionId)
                .orElseThrow(() -> new CollectionNotFoundException(collectionId));

        Admin admin = collection.getAdmin();
        String domain = this.allowedDomainService.extractDomain(requestDTO.email());

        if (!this.allowedDomainRepository.existsByAdminIdAndDomainNameIgnoreCase(admin.getId(), domain)) {
            throw new DomainNotAllowedException(domain);
        }

        if (!collection.isLocked()) {
            throw new BusinessRuleException("Access requests only allowed for locked collections");
        }

        if (this.accessRequestRepository.existsByEmailAndReportCollectionId(requestDTO.email(), collectionId)) {
            throw new DuplicateRequestException(requestDTO.email(), collection.getName());
        }

        AccessRequest accessRequest = this.accessRequestMapper.toAccessRequest(requestDTO);
        accessRequest.setReportCollection(collection);

        AccessRequest savedAccessRequest = this.accessRequestRepository.save(accessRequest);

        this.emailService.sendRequestPending(
                savedAccessRequest.getEmail(),
                savedAccessRequest.getId(),
                collection.getName()
        );

        this.notificationService.createAccessRequestNotification(
                savedAccessRequest,
                "New access request for " + savedAccessRequest.getReportCollection().getName() + ": " + savedAccessRequest.getMessage()
        );

        return this.accessRequestMapper.toAccessRequestResponse(savedAccessRequest);
    }
}
