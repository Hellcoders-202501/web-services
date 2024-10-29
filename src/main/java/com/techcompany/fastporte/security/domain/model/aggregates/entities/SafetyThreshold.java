package com.techcompany.fastporte.security.domain.model.aggregates.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Entity
@Data
@Table(name = "safety_threshold")
@AllArgsConstructor
@NoArgsConstructor
public class SafetyThreshold  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sensor_type_id")
    private SensorType sensorType;

    private Double maxThreshold;

    private Double minThreshold;

    private Long tripId;

    private Long supervisorId;
}