package com.techcompany.fastporte.users.domain.model.aggregates.entities;

import com.techcompany.fastporte.users.domain.model.commands.driver.AddDriverExperienceCommand;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "experience")
@AllArgsConstructor
@NoArgsConstructor
public class Experience implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job", nullable = false)
    private String job;

    @Column(name = "duration", precision = 3, scale = 1, nullable = false)
    private BigDecimal duration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    public Experience(String job, BigDecimal duration, Driver driver) {
        this.job = job;
        this.duration = duration;
        this.driver = driver;
    }
}
