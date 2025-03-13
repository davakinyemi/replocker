package com.ap2.replocker.admin;


import com.ap2.replocker.common.BaseAuditingEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.GenerationType.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "admin")
@NamedQuery(
        name = AdminConstants.FIND_ADMIN_BY_EMAIL,
        query = "SELECT a FROM Admin a WHERE a.email = :email"
)
@NamedQuery(
        name = AdminConstants.FIND_ADMIN_BY_PUBLIC_ID,
        query = "SELECT a FROM Admin a WHERE a.id = :publicId"
)
public class Admin extends BaseAuditingEntity {
    @Id
    @GeneratedValue(strategy = UUID)
    private String id;
    private String username;
    private String email;
}
