package com.techcompany.fastporte.users.domain.model.aggregates.entities;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Service;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "vehicle")
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToMany
    @JoinTable(
            name = "vehicle_service",
            joinColumns = @JoinColumn(name = "vehicle_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private List<Service> services = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    public Vehicle(String brand, String imageUrl, Driver driver) {
        this.brand = brand;
        this.imageUrl = imageUrl;
        this.driver = driver;
    }

    public Vehicle(String brand, String imageUrl) {
        this.brand = brand;
        this.imageUrl = imageUrl;
    }
}
