package com.dronedelivery.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
public class Drone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(unique = true)
    private String serialNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    private DroneModel model;

    @NotNull
    @Max(500)
    @Positive
    private Double weightLimit;

    @NotNull
    @Min(0)
    @Max(100)
    private Integer batteryCapacity;

    @NotNull
    @Enumerated(EnumType.STRING)
    private DroneState state = DroneState.IDLE;
}