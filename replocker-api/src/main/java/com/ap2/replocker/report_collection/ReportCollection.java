package com.ap2.replocker.report_collection;

import com.ap2.replocker.admin.Admin;
import com.ap2.replocker.common.BaseAuditingEntity;
import com.ap2.replocker.report_collection.report.Report;
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
@Table(name = "report_collection")
public class ReportCollection extends BaseAuditingEntity {
    @Id
    @GeneratedValue(strategy = UUID)
    private String id;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;
    private boolean isLocked;
    private boolean isPublished;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    @OneToMany(mappedBy = "collection", cascade = CascadeType.ALL)
    private List<Report> reports;
}
