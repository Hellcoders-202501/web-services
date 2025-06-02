package com.techcompany.fastporte.trips.domain.model.commands;

import java.time.LocalDate;

public record CreateTripCommand(
    Long driverId,
    Long clientId,
    String origin,
    String destination,
    String type,
    Double amount,
    String weight,
    LocalDate date,
    String startTime,
    String endTime,
    String subject,
    String description
) {
}
