package com.techcompany.fastporte.shared.dtos;

public record RealTimeSensorDataDto(
        Long tripId,
        Float temperatureValue,
        Float humidityValue,
        Float pressureValue,
        Float gasValue,
        String timestamp
) {
}
