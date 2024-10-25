package com.techcompany.fastporte.trips.domain.model.commands;

public record CreateTripCommand(
    Long driverId,
    Long supervisorId,
    String origin,
    String destination,
    String startTime,
    String endTime
) {
}
