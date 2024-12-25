package com.dronedelivery.dto;

import jakarta.validation.constraints.*;
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