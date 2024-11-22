package com.techcompany.fastporte.trips.interfaces.rest.resources;

import com.techcompany.fastporte.trips.domain.model.aggregates.enums.NotificationType;

import java.time.LocalDateTime;

public record NotificationInformationResource(
    Long id,
    LocalDateTime timestamp,
    NotificationType type,
    boolean seen,
    Long userId,
    Long tripId
) {
}
