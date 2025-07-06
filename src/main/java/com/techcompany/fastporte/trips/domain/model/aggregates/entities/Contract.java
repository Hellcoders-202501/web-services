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
@Table(name = "contract")
@AllArgsConstructor
public class Contract implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "request_id")
    private Request request;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "contract", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Payment payment;

    public Contract() {
        this.createdAt = LocalDateTime.now().withNano(0);
    }

}
