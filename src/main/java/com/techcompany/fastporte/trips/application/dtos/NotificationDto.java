package com.techcompany.fastporte.trips.application.dtos;

import com.techcompany.fastporte.trips.domain.model.aggregates.enums.NotificationType;

import java.time.LocalDateTime;

public record NotificationDto(
        Long id,
        LocalDateTime timestamp,
        NotificationType type,
        boolean seen,
        Long userId,
        Long tripId
) {
}
