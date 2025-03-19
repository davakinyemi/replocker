package com.ap2.replocker.admin.allowed_domain;

import com.ap2.replocker.admin.Admin;
import com.ap2.replocker.common.BaseAuditingEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

import static jakarta.persistence.GenerationType.UUID;

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
        name = "allowed_domain",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uc_domain_admin",
                        columnNames = {
                                "admin_id",
                                "domain_name"
                        }
                )
        }
)
public class AllowedDomain extends BaseAuditingEntity {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(name = "domain_name", nullable = true)
    private String domainName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;
}
