package com.ap2.replocker.report_collection.action;

import com.ap2.replocker.report_collection.ReportCollection;
import com.ap2.replocker.report_collection.report.Report;
import com.ap2.replocker.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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
@Table(name = "activity_log")
public class ActivityLog {
    @Id
    @GeneratedValue(strategy = UUID)
    private String id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Report report;

    @ManyToOne
    private ReportCollection reportCollection;

    @Enumerated(EnumType.STRING)
    private ActionType actionType;

    private String ipAddress;
    private LocalDateTime createdAt;
}
