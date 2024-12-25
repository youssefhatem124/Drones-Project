package com.dronedelivery.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Double weight;

    private String code;

    @Column(columnDefinition = "TEXT")
    private String image;

    @ManyToOne
    private Drone drone;

}
