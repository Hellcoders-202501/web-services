package com.techcompany.fastporte.trips.domain.model.aggregates.entities;

import com.techcompany.fastporte.trips.domain.model.commands.CreateTripCommand;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@Table(name = "trip")
@AllArgsConstructor
@NoArgsConstructor
public class Trip implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "origin")
    private String origin;

    @Column(name = "destination")
    private String destination;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_time")
    private String endTime;

    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "driver_id")
    //private Driver driver;

    @Column(name = "driver_id")
    private Long driverId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private TripStatus status;

    public Trip(CreateTripCommand command) {
        this.origin = command.origin();
        this.destination = command.destination();
        this.startTime = command.startTime();
        this.endTime = command.endTime();
        this.driverId = command.driverId();
    }
}
