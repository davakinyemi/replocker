package com.ap2.replocker.user;

import com.ap2.replocker.common.BaseAuditingEntity;
import com.ap2.replocker.report_collection.access_token.AccessToken;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static jakarta.persistence.GenerationType.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_user")
public class User extends BaseAuditingEntity {
    @Id
    @GeneratedValue(strategy = UUID)
    private String id;

    private String hashedEmail; // SHA-256 hashed

    private boolean isActive = true;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<AccessToken> tokens;
}
