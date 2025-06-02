package com.techcompany.fastporte.trips.domain.model.aggregates.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "comment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "rating")
    private Integer rating;

    @OneToOne
    @JoinColumn(name = "trip_id", unique = true, nullable = false)
    private Trip trip;

}