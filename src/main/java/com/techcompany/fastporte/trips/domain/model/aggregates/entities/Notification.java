package com.techcompany.fastporte.trips.domain.model.aggregates.entities;

import com.techcompany.fastporte.trips.application.dtos.NotificationDto;
import com.techcompany.fastporte.trips.domain.model.aggregates.enums.NotificationType;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Entity
@Data
@Table(name = "notification")
@AllArgsConstructor
@NoArgsConstructor
public class Notification implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "type")
    private NotificationType type;

    @Column(name = "reference_id")
    private Long referenceId;

    @Column(nullable = false, name = "seen")
    private boolean seen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "trip_id")
//    private Trip trip;

    public NotificationDto toDto() {
        return new NotificationDto(
                id,
                createdAt,
                type,
                referenceId,
                seen,
                user.getId()
        );
    }

}
