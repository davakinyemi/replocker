package com.ap2.replocker.report_collection.report;

import com.ap2.replocker.report_collection.ReportCollection;
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
@Table(name = "report")
public class Report {
    @Id
    @GeneratedValue(strategy = UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    private String filePath;
    private long sizeBytes;

    @Enumerated(EnumType.STRING)
    private ReportType type;

    @ManyToOne
    @JoinColumn(name = "collection_id", nullable = false)
    private ReportCollection collection;
}
