package com.ap2.replocker.common;

import com.ap2.replocker.admin.notification.NotificationRepository;
import com.ap2.replocker.email.EmailService;
import com.ap2.replocker.report_collection.ReportCollection;
import com.ap2.replocker.report_collection.access_request.AccessRequest;
import com.ap2.replocker.report_collection.access_request.AccessRequestRepository;
import com.ap2.replocker.report_collection.access_request.RequestStatus;
import com.ap2.replocker.report_collection.access_request.access_token.AccessToken;
import com.ap2.replocker.report_collection.access_request.access_token.AccessTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Dave AKN
 * @version 1.0
 */
@Slf4j
@ExtendWith(MockitoExtension.class)
public class ScheduledTasksTest {
    @Mock
    AccessRequestRepository accessRequestRepository;

    @Mock
    AccessTokenRepository accessTokenRepository;

    @Mock
    NotificationRepository notificationRepository;

    @Mock
    EmailService emailService;

    @InjectMocks
    ScheduledTasks scheduledTasks;

    @Test
    void testPurgeRejectedAccessRequests_HappyPath() {
        AccessRequest mockRequest = mock(AccessRequest.class);
        when(mockRequest.getId()).thenReturn(UUID.randomUUID());

        when(accessRequestRepository.findByStatusAndCreatedDateBefore(
                eq(RequestStatus.REJECTED), any(LocalDateTime.class))
        ).thenReturn(List.of(mockRequest));

        this.scheduledTasks.purgeRejectedAccessRequests();

        verify(this.notificationRepository).deleteByAccessRequestId(mockRequest.getId());
        verify(this.accessRequestRepository).delete(mockRequest);
    }

    @Test
    void testHandleTokenExpiry_ValidTokens() {
        LocalDateTime mockNow = LocalDateTime.of(2025, 4, 4, 3, 0);
        try (MockedStatic<LocalDateTime> mockedDateTime = mockStatic(LocalDateTime.class);
             MockedStatic<LocalTime> mockedTime = mockStatic(LocalTime.class)) {

            mockedDateTime.when(LocalDateTime::now).thenReturn(mockNow);
            mockedTime.when(LocalTime::now).thenReturn(mockNow.toLocalTime());

            AccessToken expiringToken = this.createMockToken(mockNow.plusHours(24));
            AccessToken expiredToken = this.createMockToken(mockNow.minusHours(1));

            when(this.accessTokenRepository.findByExpiresAtBetween(any(), any()))
                    .thenReturn(List.of(expiringToken));
            when(this.accessTokenRepository.findByExpiresAtBefore(any()))
                    .thenReturn(List.of(expiredToken));

            this.scheduledTasks.handleTokenExpiry();

            verify(this.emailService).sendTokenExpiryWarning(
                    "user@company.com",
                    mockNow.plusHours(24),
                    "Sales Reports",
                    "123456"
            );

            verify(this.emailService).sendTokenExpiredNotification(
                    "user@company.com",
                    "Sales Reports",
                    "123456"
            );

            verify(this.notificationRepository).deleteByAccessRequestId(expiredToken.getAccessRequest().getId());
            verify(this.accessRequestRepository).delete(expiredToken.getAccessRequest());
            verify(this.accessTokenRepository).delete(expiredToken);
        }

    }

    private AccessToken createMockToken(LocalDateTime expiry) {
        AccessToken accessToken = mock(AccessToken.class);
        AccessRequest accessRequest = mock(AccessRequest.class);
        ReportCollection reportCollection = mock(ReportCollection.class);

        lenient().when(accessToken.getExpiresAt()).thenReturn(expiry);
        lenient().when(accessRequest.getId()).thenReturn(UUID.randomUUID());

        when(accessToken.getAccessRequest()).thenReturn(accessRequest);
        when(accessToken.getReportCollection()).thenReturn(reportCollection);
        when(accessRequest.getEmail()).thenReturn("user@company.com");
        when(reportCollection.getName()).thenReturn("Sales Reports");
        when(accessToken.getTokenValue()).thenReturn("123456");

        return accessToken;
    }

}
