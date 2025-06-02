package com.techcompany.fastporte.trips.domain.model.aggregates.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "payment")
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount")
    private Double amount;

    @OneToOne
    @JoinColumn(name = "contrat_id")
    private Contract contract;

    @ManyToOne
    @JoinColumn(name = "payment_status_id")
    private PaymentStatus status;

}
