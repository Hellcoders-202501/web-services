package com.techcompany.fastporte.security.interfaces.rest.transform.fromEntity;


import com.techcompany.fastporte.security.domain.model.aggregates.entities.RealTimeSensorData;
import com.techcompany.fastporte.shared.dtos.RealTimeSensorDataDto;

public class RealTimeSensorDataDtoFromEntityAssembler {

    public static RealTimeSensorDataDto toDtoFromEntity(RealTimeSensorData realTimeSensorData) {
        return new RealTimeSensorDataDto(
                realTimeSensorData.getTripId(),
                realTimeSensorData.getTemperatureValue(),
                realTimeSensorData.getHumidityValue(),
                realTimeSensorData.getPressureValue(),
                realTimeSensorData.getGasValue(),
                realTimeSensorData.getTimestamp().toString()
        );
    }

}
