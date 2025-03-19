/*
package com.ap2.replocker.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final SimpMessagingTemplate messagingTemplate;

    public void sendNotification(String adminId, Notification notification) {
        log.info("Sending WebSocket notification to {} with payload {}", adminId, notification);
        this.messagingTemplate.convertAndSendToUser(
                adminId,
                "/request",
                notification
        );
    }
}
*/
