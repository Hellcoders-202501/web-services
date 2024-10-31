package com.techcompany.fastporte.security.application.internal.commandservices;

import com.techcompany.fastporte.security.domain.model.aggregates.entities.SensorData;
import com.techcompany.fastporte.security.domain.model.aggregates.entities.SensorType;
import com.techcompany.fastporte.security.domain.model.aggregates.enums.Type;
import com.techcompany.fastporte.security.domain.model.commands.SaveSensorDataCommand;
import com.techcompany.fastporte.security.domain.services.SensorDataCommandService;
import com.techcompany.fastporte.security.infrastructure.persistence.jpa.AlertRepository;
import com.techcompany.fastporte.security.infrastructure.persistence.jpa.SafetyThresholdRepository;
import com.techcompany.fastporte.security.infrastructure.persistence.jpa.SensorDataRepository;
import com.techcompany.fastporte.security.infrastructure.persistence.jpa.SensorTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional(readOnly = false)
public class SensorDataCommandServiceImp implements SensorDataCommandService {

    private final SensorDataRepository sensorDataRepository;
    private final SensorTypeRepository sensorTypeRepository;

    public SensorDataCommandServiceImp(SensorDataRepository sensorDataRepository, SensorTypeRepository sensorTypeRepository, SafetyThresholdRepository safetyThresholdRepository, AlertRepository alertRepository) {
        this.sensorDataRepository = sensorDataRepository;
        this.sensorTypeRepository = sensorTypeRepository;
    }

    @Override
    public void handle(SaveSensorDataCommand command) {

        Type type = Type.valueOf(command.sensorType());
        Optional<SensorType> sensorType = sensorTypeRepository.findByType(type);

        if (sensorType.isEmpty()) {
            System.out.println("Sensor type not found");
            return;
        }

        SensorData sensorData = SensorData.builder()
                .sensorType(sensorType.get())
                .value(command.value())
                .timestamp(LocalDateTime.parse(command.timestamp()))
                .tripId(command.tripId())
                .build();

        sensorDataRepository.save(sensorData);
        System.out.println("Sensor data saved");
    }
}
