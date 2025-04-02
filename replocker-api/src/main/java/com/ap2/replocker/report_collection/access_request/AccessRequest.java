package com.ap2.replocker.report_collection.access_request;

import com.ap2.replocker.admin.notification.Notification;
import com.ap2.replocker.common.BaseAuditingEntity;
import com.ap2.replocker.report_collection.ReportCollection;
import com.ap2.replocker.report_collection.access_request.access_token.AccessToken;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

/**
 * @author Dave AKN
 * @version 1.0
 */
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "access_request",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uc_request_email_report_collection",
                        columnNames = {
                                "email",
                                "report_collection_id"
                        }
                )
        }
)
public class AccessRequest extends BaseAuditingEntity {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "message")
    private String message;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RequestStatus status = RequestStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_collection_id", nullable = false)
    private ReportCollection reportCollection;

    @Column(name = "admin_comment", length = 1000)
    private String adminComment;

    @OneToOne(
            mappedBy = "accessRequest",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private AccessToken accessToken;

    @OneToMany(
            mappedBy = "accessRequest",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Notification> notification;
}
