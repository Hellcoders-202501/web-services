package com.techcompany.fastporte.trips.domain.model.aggregates.entities;

import com.techcompany.fastporte.users.domain.model.aggregates.entities.Driver;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@Table(name = "contract")
@AllArgsConstructor
@NoArgsConstructor
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

    @OneToOne(mappedBy = "contract")
    //@JoinColumn(name = "payment_id")
    private Payment payment;

}
