package com.ap2.replocker.admin.notification;

import com.ap2.replocker.admin.AdminRepository;
import com.ap2.replocker.common.PageResponse;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
public class NotificationService {
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final AdminRepository adminRepository;

    public PageResponse<NotificationResponse> filterNotifications(
            UUID adminId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            int page,
            int size
    ) {
        Specification<Notification> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("admin").get("id"), adminId));
            if (startDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), startDate));
            }
            if (endDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"), endDate));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<Notification> notifications = this.notificationRepository.findAll(
                specification,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate")));

        return PageResponse.fromPage(notifications.map(this.notificationMapper::toNotificationResponse));

    }

    public PageResponse<NotificationResponse> getUnreadNotifications(UUID adminId, int page, int size) {
        Page<Notification> notifications = this.notificationRepository.findByAdminIdAndIsReadFalse(
                adminId,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"))
        );
        return PageResponse.fromPage(notifications.map(this.notificationMapper::toNotificationResponse));
    }

    public void markNotificationAsRead(UUID notificationId) {
        this.notificationRepository.markAsRead(notificationId);
    }

    /* public void createReportCollectionRequestAccessNotification(AccessRequest request) {
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
    } */

}
