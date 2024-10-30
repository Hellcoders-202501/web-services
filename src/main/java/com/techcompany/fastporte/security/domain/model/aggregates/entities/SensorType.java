package com.techcompany.fastporte.security.domain.model.aggregates.entities;

import com.techcompany.fastporte.security.domain.model.aggregates.enums.Type;
import com.techcompany.fastporte.trips.domain.model.aggregates.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@Table(name = "sensor_type")
@AllArgsConstructor
@NoArgsConstructor
public class SensorType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", unique = true, nullable = false)
    private Type type;
}
