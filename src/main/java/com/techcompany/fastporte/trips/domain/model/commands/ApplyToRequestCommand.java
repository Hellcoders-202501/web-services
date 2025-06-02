package com.techcompany.fastporte.trips.domain.model.commands;

public record ApplyToRequestCommand(
        String message,
        Double proposedAmount,
        Long requestId,
        Long driverId
) {
}
