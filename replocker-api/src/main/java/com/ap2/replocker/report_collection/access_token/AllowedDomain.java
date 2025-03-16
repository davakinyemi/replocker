package com.ap2.replocker.report_collection.access_token;

import com.ap2.replocker.admin.Admin;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.GenerationType.UUID;

/**
 * @author Dave AKN
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "allowed_domain")
public class AllowedDomain {
    @Id
    @GeneratedValue(strategy = UUID)
    private String id;

    @Column(unique = true, nullable = true)
    private String domainName;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;
}
