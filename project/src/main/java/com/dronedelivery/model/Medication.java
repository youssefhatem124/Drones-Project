package com.dronedelivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9-_]+$")
    private String name;

    @NotNull
    @Positive
    private Double weight;

    @NotBlank
    @Pattern(regexp = "^[A-Z0-9_]+$")
    private String code;

    private String image;

    @ManyToOne
    @JsonIgnore
    private Drone drone;
}