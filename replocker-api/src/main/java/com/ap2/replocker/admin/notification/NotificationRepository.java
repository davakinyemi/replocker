package com.ap2.replocker.admin.notification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

/**
 * @author Dave AKN
 * @version 1.0
 */
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    Page<Notification> findByAdminIdAndReadFalse(UUID adminId, Pageable pageable);

    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.id = :id")
    void markAsRead(UUID id);
}
