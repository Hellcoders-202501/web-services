package com.techcompany.fastporte.users.domain.model.aggregates.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Table(name = "supervisor")
@AllArgsConstructor
@NoArgsConstructor
public class Supervisor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "department", length = 50)
    private String department;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "supervisor", fetch = FetchType.LAZY)
    private List<Driver> drivers;
}
