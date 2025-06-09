package com.techcompany.fastporte.trips.domain.model.aggregates.entities;

import com.techcompany.fastporte.trips.domain.model.aggregates.enums.RequestStatusType;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.Client;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Data
@Table(name = "request")
@AllArgsConstructor
public class Request implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @ManyToOne
    @JoinColumn(name = "status", nullable = false)
    private RequestStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "request", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Trip trip;

    @OneToOne(mappedBy = "request", cascade = CascadeType.REMOVE)
    private Contract contract;

    @OneToMany(mappedBy = "request", cascade = CascadeType.REMOVE)
    private List<Application> applications;

    public Request() {
        this.createdAt = LocalDateTime.now().withNano(0);
    }

}