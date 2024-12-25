package com.dronedelivery.model;

import com.dronedelivery.common.enums.DroneModel;
import com.dronedelivery.common.enums.DroneState;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Drone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    private DroneModel model;

    private Double weightLimit;

    private Integer batteryCapacity;

    @Enumerated(EnumType.STRING)
    private DroneState state = DroneState.IDLE;
}
