package com.ap2.replocker.admin.notification;

import com.ap2.replocker.admin.AdminRepository;
import com.ap2.replocker.common.PageResponse;
import com.ap2.replocker.report_collection.access_request.AccessRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
public class NotificationService {
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final AdminRepository adminRepository;

    public PageResponse<NotificationResponse> getUnreadNotifications(UUID adminId, int page, int size) {
        Page<Notification> notifications = this.notificationRepository.findByAdminIdAndReadFalse(
                adminId,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"))
        );
        return PageResponse.fromPage(notifications.map(this.notificationMapper::toNotificationResponse));
    }

    public void createReportCollectionRequestAccessNotification(AccessRequest request) {
        Notification notification = Notification.builder()
                .message("New access request for report collection - " + request.getReportCollection().getName() + ": " + request.getMessage())
                .admin(request.getReportCollection().getAdmin())
                .accessRequest(request)
                .build();
        this.notificationRepository.save(notification);
    }

    public void notifyAdmin(UUID adminId, Notification notification) {
        this.messagingTemplate.convertAndSendToUser(
                adminId.toString(),
                "/queue/notifications",
                notification
        );
    }

    public void markNotificationAsRead(UUID notificationId) {
        this.notificationRepository.markAsRead(notificationId);
    }
}
