package com.ap2.replocker.common;

import com.ap2.replocker.admin.notification.NotificationRepository;
import com.ap2.replocker.email.EmailService;
import com.ap2.replocker.report_collection.access_request.AccessRequest;
import com.ap2.replocker.report_collection.access_request.AccessRequestRepository;
import com.ap2.replocker.report_collection.access_request.RequestStatus;
import com.ap2.replocker.report_collection.access_request.access_token.AccessToken;
import com.ap2.replocker.report_collection.access_request.access_token.AccessTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * @author Dave AKN
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledTasks {
    private final AccessRequestRepository accessRequestRepository;
    private final AccessTokenRepository accessTokenRepository;
    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    @Scheduled(cron = "0 */30 * * * *")
    @Transactional
    public void purgeRejectedAccessRequests() {
        LocalDateTime threshold = LocalDateTime.now().minusHours(24);
        List<AccessRequest> rejectedRequests = this.accessRequestRepository
                .findByStatusAndCreatedDateBefore(RequestStatus.REJECTED, threshold);

        rejectedRequests.forEach(accessRequest -> {
            this.notificationRepository.deleteByAccessRequestId(accessRequest.getId());
            this.accessRequestRepository.delete(accessRequest);
        });
    }

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void handleTokenExpiry() {
        LocalDateTime warningThreshold = LocalDateTime.now().plusHours(24);
        LocalDateTime expiryThreshold = LocalDateTime.now();

        this.accessTokenRepository.findByExpiresAtBetween(expiryThreshold, warningThreshold)
                .forEach(accessToken -> this.emailService.sendTokenExpiryWarning(
                        accessToken.getAccessRequest().getEmail(),
                        accessToken.getExpiresAt(),
                        accessToken.getReportCollection().getName(),
                        accessToken.getTokenValue()
                ));

        if (LocalTime.now().getHour() == 3) {
            List<AccessToken> expiredTokens = this.accessTokenRepository.findByExpiresAtBefore(expiryThreshold);
            expiredTokens.forEach(accessToken -> {
                this.emailService.sendTokenExpiredNotification(
                        accessToken.getAccessRequest().getEmail(),
                        accessToken.getReportCollection().getName(),
                        accessToken.getTokenValue()
                );
                this.notificationRepository.deleteByAccessRequestId(accessToken.getAccessRequest().getId());
                this.accessRequestRepository.delete(accessToken.getAccessRequest());
                this.accessTokenRepository.delete(accessToken);
            });
        }
    }
}
