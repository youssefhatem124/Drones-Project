package com.dronedelivery.dto;

import com.dronedelivery.model.Drone;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class BatteryLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Drone drone;

    private Integer batteryLevel;
    private LocalDateTime timestamp;
}