package com.techcompany.fastporte.trips.domain.model.aggregates.entities;

import com.techcompany.fastporte.trips.domain.model.aggregates.enums.ServiceType;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.Vehicle;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "service")
@AllArgsConstructor
@NoArgsConstructor
public class Service implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", unique = true, nullable = false)
    private ServiceType type;

    @Column(name = "name_spanish")
    private String nameSpanish;

    @ManyToMany(mappedBy = "services")
    private List<Vehicle> vehicles = new ArrayList<>();
}