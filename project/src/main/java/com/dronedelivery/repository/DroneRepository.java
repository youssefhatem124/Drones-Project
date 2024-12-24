package com.dronedelivery.repository;

import com.dronedelivery.model.Drone;
import com.dronedelivery.model.DroneState;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DroneRepository extends JpaRepository<Drone, Long> {
    List<Drone> findByState(DroneState state);
    List<Drone> findByBatteryCapacityGreaterThanAndState(Integer batteryLevel, DroneState state);
}