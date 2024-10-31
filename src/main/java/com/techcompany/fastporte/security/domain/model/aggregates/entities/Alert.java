package com.techcompany.fastporte.security.domain.model.aggregates.entities;

import com.techcompany.fastporte.security.domain.model.aggregates.enums.AlertLevel;
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
@Table(name = "alert")
@AllArgsConstructor
@NoArgsConstructor
public class Alert implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private AlertLevel alertLevel;

    private String message;

    @OneToOne(fetch = FetchType.LAZY)
    private SensorData sensorData;

//    private Long tripId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "sensor_type_id")
//    private SensorType sensorType;

//    private Double value;
//
//    private LocalDateTime timestamp;

}
