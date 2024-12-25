package com.dronedelivery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class MedicationDTO {

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


}