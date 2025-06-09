package com.techcompany.fastporte.trips.domain.model.aggregates.entities;

import com.techcompany.fastporte.users.domain.model.aggregates.entities.Driver;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
@Table(name = "application")
@AllArgsConstructor
@NoArgsConstructor
public class Application implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message")
    private String message;

    @Column(name = "proposed_amount")
    private Double proposedAmount;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private Request request;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Application(String message, Double proposedAmount, Request request, Driver driver) {
        this.message = message;
        this.proposedAmount = proposedAmount;
        this.request = request;
        this.driver = driver;
        this.createdAt = LocalDateTime.now().withNano(0);
    }
}
