package com.ap2.replocker.admin.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Dave AKN
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class NotificationMapper {
    public NotificationResponse toNotificationResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .isRead(notification.isRead())
                .createdDate(notification.getCreatedDate())
                .accessRequestId(notification.getAccessRequest().getId())
                .reportCollectionName(notification.getAccessRequest().getReportCollection().getName())
                .build();
    }
}
