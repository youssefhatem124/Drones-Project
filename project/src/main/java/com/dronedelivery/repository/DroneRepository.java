package com.dronedelivery.repository;

import com.dronedelivery.dto.DroneDTO;
import com.dronedelivery.common.enums.DroneState;
import com.dronedelivery.model.Drone;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DroneRepository extends JpaRepository<Drone, Long> {
    List<Drone> findByBatteryCapacityGreaterThanAndState(Integer batteryLevel, DroneState state);
}