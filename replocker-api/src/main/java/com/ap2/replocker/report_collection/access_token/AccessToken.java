package com.ap2.replocker.report_collection.access_token;

import com.ap2.replocker.report_collection.ReportCollection;
import com.ap2.replocker.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

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
@Table(name = "access_token")
public class AccessToken {
    @Id
    @GeneratedValue(strategy = UUID)
    private String id;

    @Column(unique = true, nullable = false)
    private String tokenValue;

    @ManyToOne
    @JoinColumn(name = "collection_id", nullable = false)
    private ReportCollection collection;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;
    private boolean isActive;
    private boolean isRevoked;
}
