package com.techcompany.fastporte.trips.domain.model.aggregates.entities;

import com.techcompany.fastporte.trips.domain.model.aggregates.enums.RequestStatusType;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.Client;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Table(name = "request")
@AllArgsConstructor
@NoArgsConstructor
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

//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false, name = "status")
//    private RequestStatusType status;

    @ManyToOne
    @JoinColumn(name = "status", nullable = false)
    private RequestStatus status;

    @OneToOne(mappedBy = "request")
    private Trip trip;

    @OneToOne(mappedBy = "request")
    private Contract contract;

    @OneToMany(mappedBy = "request")
    private List<Application> applications;

}