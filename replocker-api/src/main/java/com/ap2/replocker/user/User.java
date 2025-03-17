package com.ap2.replocker.user;

import com.ap2.replocker.common.BaseAuditingEntity;
import com.ap2.replocker.report_collection.access_token.AccessToken;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

import static jakarta.persistence.GenerationType.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "_user",
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_user_hashed_email", columnNames = "hashed_email")
        }
)
public class User extends BaseAuditingEntity {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(name = "hashed_email", nullable = false)
    private String hashedEmail; // SHA-256 hashed

    @Builder.Default
    private boolean isActive = true;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<AccessToken> tokens;
}
