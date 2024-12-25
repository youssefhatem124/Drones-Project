package com.dronedelivery.dto;

import com.dronedelivery.common.enums.DroneModel;
import com.dronedelivery.common.enums.DroneState;
import jakarta.validation.constraints.*;
import lombok.Data;
@Data
public class DroneDTO {

    private Long id;
    @NotBlank
    @Size(max = 100)
    private String serialNumber;

    @NotNull
    private DroneModel model; // Same type as in Drone entity

    @NotNull
    @Max(500)
    @Positive
    private Double weightLimit;

    @NotNull
    @Min(0)
    @Max(100)
    private Integer batteryCapacity;

    @NotNull
    private DroneState state; // Same type as in Drone entity
}