package com.techcompany.fastporte.security.application.internal.commandservices;

import com.techcompany.fastporte.security.domain.model.aggregates.entities.SafetyThreshold;
import com.techcompany.fastporte.security.domain.model.aggregates.entities.SensorType;
import com.techcompany.fastporte.security.domain.model.aggregates.enums.Type;
import com.techcompany.fastporte.security.domain.model.commands.CreateSafetyThresholdCommand;
import com.techcompany.fastporte.security.domain.model.commands.UpdateSafetyThresholdCommand;
import com.techcompany.fastporte.security.domain.services.SafetyThresholdCommandService;
import com.techcompany.fastporte.security.infrastructure.persistence.jpa.SafetyThresholdRepository;
import com.techcompany.fastporte.security.infrastructure.persistence.jpa.SensorTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = false)
public class SafetyThresholdCommandServiceImp implements SafetyThresholdCommandService {

    private final SafetyThresholdRepository safetyThresholdRepository;
    private final SensorTypeRepository sensorTypeRepository;

    public SafetyThresholdCommandServiceImp(SafetyThresholdRepository safetyThresholdRepository, SensorTypeRepository sensorTypeRepository) {
        this.safetyThresholdRepository = safetyThresholdRepository;
        this.sensorTypeRepository = sensorTypeRepository;
    }

    @Override
    public Optional<SafetyThreshold> handle(CreateSafetyThresholdCommand command) {

        Optional<SensorType> sensorType = sensorTypeRepository.findById(command.sensorTypeId());

        if (sensorType.isEmpty()) {
            return Optional.empty();
        }

        Double minThreshold;
        Double maxThreshold;

        /*switch (sensorType.get().getType()){
            case SENSOR_GAS -> {
                minThreshold = 0.0;
                maxThreshold = 100.0;
            }
            case SENSOR_TEMPERATURE -> {
                minThreshold = -20.0;
                maxThreshold = 50.0;
            }
            case SENSOR_HUMIDITY -> {
                minThreshold = 0.0;
                maxThreshold = 100.0;
            }
            case SENSOR_PRESSURE -> {
                minThreshold = 0.0;
                maxThreshold = 1000.0;
            }
            default -> {
                minThreshold = 0.0;
                maxThreshold = 100.0;
            }
        }*/

        SafetyThreshold safetyThreshold = SafetyThreshold.builder()
                .sensorType(sensorType.get())
                //.maxThreshold(maxThreshold)
                .maxThreshold(command.maxThreshold())
                //.minThreshold(minThreshold)
                .minThreshold(command.minThreshold())
                .tripId(command.tripId())
                .supervisorId(command.supervisorId())
                .build();

        return Optional.of(safetyThresholdRepository.save(safetyThreshold));
    }

    @Override
    public Optional<SafetyThreshold> handle(UpdateSafetyThresholdCommand command) {
        Optional<SafetyThreshold> safetyThreshold = safetyThresholdRepository.findById(command.safetyThresholdId());

        if (safetyThreshold.isEmpty()) {
            return Optional.empty();
        }

        safetyThreshold.get().setMaxThreshold(command.maxThreshold());
        safetyThreshold.get().setMinThreshold(command.minThreshold());

        return Optional.of(safetyThresholdRepository.save(safetyThreshold.get()));
    }
}
