package com.techcompany.fastporte.security.interfaces.rest;

import com.techcompany.fastporte.security.domain.model.aggregates.entities.RealTimeSensorData;
import com.techcompany.fastporte.security.domain.model.commands.SaveRealTimeSensorDataCommand;
import com.techcompany.fastporte.security.domain.model.queries.GetRealTimeSensorDataQuery;
import com.techcompany.fastporte.security.domain.services.RealTimeSensorDataCommandService;
import com.techcompany.fastporte.security.domain.services.RealTimeSensorDataQueryService;
import com.techcompany.fastporte.security.interfaces.rest.transform.fromEntity.RealTimeSensorDataDtoFromEntityAssembler;
import com.techcompany.fastporte.shared.dtos.RealTimeSensorDataDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/sensor-data")
@Tag(name = "Sensor Data Management", description = "Operations for managing sensor data, including retrieval by trip")
public class RealTimeSensorDataController {

    private final RealTimeSensorDataCommandService realTimeSensorDataCommandService;
    private final RealTimeSensorDataQueryService realTimeSensorDataQueryService;

    public RealTimeSensorDataController(RealTimeSensorDataCommandService realTimeSensorDataCommandService, RealTimeSensorDataQueryService realTimeSensorDataQueryService) {
        this.realTimeSensorDataCommandService = realTimeSensorDataCommandService;
        this.realTimeSensorDataQueryService = realTimeSensorDataQueryService;
    }

    /*
    @GetMapping("/trip/{tripId}")
    public ResponseEntity<List<RealTimeSensorDataDto>> getSensorDataByTripId(@PathVariable Long tripId) {
        try {
            List<RealTimeSensorData> sensorData = realTimeSensorDataQueryService.handle(new GetRealTimeSensorDataQuery(tripId));

            if (sensorData.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            } else {

                var sensorDataResource = sensorData.stream()
                        .map(RealTimeSensorDataDtoFromEntityAssembler::toDtoFromEntity)
                        .toList();

                return ResponseEntity.status(HttpStatus.OK).body(sensorDataResource);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveSensorData(@RequestBody List<RealTimeSensorDataDto> sensorData) {
        try {
            realTimeSensorDataCommandService.handle(new SaveRealTimeSensorDataCommand(sensorData));
            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    */
}
