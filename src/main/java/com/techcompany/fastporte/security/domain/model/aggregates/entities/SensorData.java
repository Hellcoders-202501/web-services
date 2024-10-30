package com.techcompany.fastporte.security.domain.model.aggregates.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Entity
@Data
@Table(name = "sensor_data")
@AllArgsConstructor
@NoArgsConstructor
public class SensorData implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sensor_type_id")
    private SensorType sensorType;

    private Double value;

    private LocalDateTime timestamp;

    private Long tripId;
}