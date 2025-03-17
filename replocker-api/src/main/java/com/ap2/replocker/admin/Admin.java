package com.ap2.replocker.admin;

import com.ap2.replocker.common.BaseAuditingEntity;
import com.ap2.replocker.report_collection.ReportCollection;
import com.ap2.replocker.report_collection.access_token.allowed_domain.AllowedDomain;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
@Table(
        name = "admin",
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_admin_username", columnNames = "username"),
                @UniqueConstraint(name = "uc_admin_hashed_email", columnNames = "hashed_email"),
                @UniqueConstraint(name = "uc_admin_keycloak_id", columnNames = "keycloak_user_id")
        }
)
public class Admin extends BaseAuditingEntity {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "hashed_email", nullable = false)
    private String hashedEmail; //SHA-256

    @Column(name = "keycloak_user_id", nullable = false)
    private UUID keycloakUserId;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
    private List<ReportCollection> collections;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
    private List<AllowedDomain> allowedDomains;
}