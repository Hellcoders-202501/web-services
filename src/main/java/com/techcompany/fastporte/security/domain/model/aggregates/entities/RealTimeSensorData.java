package com.techcompany.fastporte.security.domain.model.aggregates.entities;

import com.techcompany.fastporte.security.domain.model.aggregates.enums.Type;
import com.techcompany.fastporte.shared.dtos.RealTimeSensorDataDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Builder
@Entity
@Data
@Table(name = "real_time_sensor_data")
@AllArgsConstructor
@NoArgsConstructor
public class RealTimeSensorData implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trip_id")
    Long tripId;

    @Column(name = "temperature_value")
    Float temperatureValue;

    @Column(name = "humidity_value")
    Float humidityValue;

    @Column(name = "pressure_value")
    Float pressureValue;

    @Column(name = "gas_value")
    Float gasValue;

    @Column(name = "timestamp")
    LocalDateTime timestamp;

    public RealTimeSensorData(RealTimeSensorDataDto dto) {

        // Obtén la zona horaria de Perú
        ZoneId peruZone = ZoneId.of("America/Lima");

        // Obtén la hora actual en Perú
        ZonedDateTime peruTime = ZonedDateTime.now(peruZone);

        this.tripId = dto.tripId();
        this.temperatureValue = dto.temperatureValue();
        this.humidityValue = dto.humidityValue();
        this.pressureValue = dto.pressureValue();
        this.gasValue = dto.gasValue();
        this.timestamp = peruTime.toLocalDateTime();
    }
}
