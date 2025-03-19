package com.ap2.replocker.admin.notification;

import com.ap2.replocker.admin.Admin;
import com.ap2.replocker.common.BaseAuditingEntity;
import com.ap2.replocker.report_collection.access_request.AccessRequest;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

/**
 * @author Dave AKN
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
@Table(name = "notification")
public class Notification extends BaseAuditingEntity {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(name = "message", nullable = false)
    private String message;

    @Builder.Default
    @Column(name = "is_read")
    private boolean isRead = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "access_request_id", nullable = false)
    private AccessRequest accessRequest;
}
