package com.techcompany.fastporte.security.domain.model.commands;

import com.techcompany.fastporte.security.domain.model.aggregates.entities.SensorData;
import com.techcompany.fastporte.security.domain.model.aggregates.entities.SensorType;
import com.techcompany.fastporte.security.domain.model.aggregates.enums.AlertLevel;

import java.time.LocalDateTime;

public record CreateAlertCommand(
//        AlertLevel alertLevel,
//        String message,
//        SensorData sensorData
//        Long sensorDataId
        String sensorType,
        Double value,
        LocalDateTime timestamp,
        Long tripId
) {
}