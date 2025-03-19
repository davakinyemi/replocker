package com.ap2.replocker.report_collection;

import com.ap2.replocker.admin.Admin;
import com.ap2.replocker.common.BaseAuditingEntity;
import com.ap2.replocker.report_collection.report.Report;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "report_collection",
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_report_collection_name", columnNames = "name")
        }
)
public class ReportCollection extends BaseAuditingEntity {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "is_locked")
    @Builder.Default
    private boolean isLocked = false;

    @Column(name = "is_published")
    @Builder.Default
    private boolean isPublished = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    @OneToMany(mappedBy = "reportCollection", cascade = CascadeType.ALL)
    private List<Report> reports;
}
