package com.techcompany.fastporte.trips.domain.model.aggregates.entities;

import com.techcompany.fastporte.trips.domain.model.commands.CreateTripCommand;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

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

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_time")
    private String endTime;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "subject")
    private String subject;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private TripStatus status;

    @OneToOne
    @JoinColumn(name = "request_id")
    private Request request;

    @OneToOne(mappedBy = "trip", cascade = CascadeType.ALL)
    private Comment comment;

    public Trip(CreateTripCommand command) {
        this.origin = command.origin();
        this.destination = command.destination();
        this.date = command.date();
        this.startTime = command.startTime();
        this.endTime = command.endTime();
        this.amount = command.amount();
        this.subject = command.subject();
        this.description = command.description();
    }
}
