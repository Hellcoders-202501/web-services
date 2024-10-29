package com.techcompany.fastporte.security.application.internal.commandservices;

import com.techcompany.fastporte.security.domain.model.aggregates.entities.Alert;
import com.techcompany.fastporte.security.domain.model.aggregates.entities.SensorData;
import com.techcompany.fastporte.security.domain.model.aggregates.entities.SensorType;
import com.techcompany.fastporte.security.domain.model.aggregates.enums.AlertLevel;
import com.techcompany.fastporte.security.domain.model.aggregates.enums.Type;
import com.techcompany.fastporte.security.domain.model.commands.CreateAlertCommand;
import com.techcompany.fastporte.security.domain.services.AlertCommandService;
import com.techcompany.fastporte.security.infrastructure.persistence.jpa.AlertRepository;
import com.techcompany.fastporte.security.infrastructure.persistence.jpa.SensorDataRepository;
import com.techcompany.fastporte.security.infrastructure.persistence.jpa.SensorTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = false)
public class AlertCommandServiceImp implements AlertCommandService {

    private final AlertRepository alertRepository;
    private final SensorDataRepository sensorDataRepository;
    private final SensorTypeRepository sensorTypeRepository;

    public AlertCommandServiceImp(AlertRepository alertRepository, SensorDataRepository sensorDataRepository, SensorTypeRepository sensorTypeRepository) {
        this.alertRepository = alertRepository;
        this.sensorDataRepository = sensorDataRepository;
        this.sensorTypeRepository = sensorTypeRepository;
    }

    @Override
    public Optional<Alert> handle(CreateAlertCommand command) {

        Type type = Type.valueOf(command.sensorType());
        Optional<SensorType> sensorType = sensorTypeRepository.findByType(type);

        if (sensorType.isEmpty()) {
            System.out.println("Sensor type not found");
            return Optional.empty();
        }

        SensorData sensorData = SensorData.builder()
                .sensorType(sensorType.get())
                .value(command.value())
                .timestamp(command.timestamp())
                .tripId(command.tripId())
                .build();

        sensorData = sensorDataRepository.save(sensorData);

        //sensorData = sensorDataRepository.findByTripIdAndSensorType_Id(command.tripId(), sensorType.get().getId()).orElse(null);

        if (sensorData == null) {
            System.out.println("Sensor data not found");
            return Optional.empty();
        }

        String message = "Alert: " + sensorType.get().getType().name() + " value is " + command.value() + " at " + command.timestamp();

        Alert alert = Alert.builder()
                .alertLevel(AlertLevel.CRITICAL)
                .message(message)
                .sensorData(sensorData)
                .build();

        return Optional.of(alertRepository.save(alert));
    }
}
