package com.techcompany.fastporte.security.domain.model.commands;

public record SaveSensorDataCommand(
        String sensorType,
        Double value,
        String timestamp,
        Long tripId
) {
}
