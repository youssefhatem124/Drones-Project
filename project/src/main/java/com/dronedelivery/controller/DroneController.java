package com.dronedelivery.controller;

import com.dronedelivery.model.Drone;
import com.dronedelivery.model.Medication;
import com.dronedelivery.service.DroneService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drones")
public class DroneController {
    private final DroneService droneService;

    public DroneController(DroneService droneService) {
        this.droneService = droneService;
    }

    @PostMapping
    public ResponseEntity<Drone> registerDrone(@Valid @RequestBody Drone drone) {
        return ResponseEntity.ok(droneService.registerDrone(drone));
    }

    @PostMapping("/{droneId}/load")
    public ResponseEntity<Drone> loadDrone(
            @PathVariable Long droneId,
            @Valid @RequestBody List<Medication> medications) {
        return ResponseEntity.ok(droneService.loadDrone(droneId, medications));
    }

    @GetMapping("/{droneId}/medications")
    public ResponseEntity<List<Medication>> getLoadedMedications(@PathVariable Long droneId) {
        return ResponseEntity.ok(droneService.getLoadedMedications(droneId));
    }

    @GetMapping("/available")
    public ResponseEntity<List<Drone>> getAvailableDrones() {
        return ResponseEntity.ok(droneService.getAvailableDrones());
    }

    @GetMapping("/{droneId}/battery")
    public ResponseEntity<Integer> getDroneBatteryLevel(@PathVariable Long droneId) {
        return ResponseEntity.ok(droneService.getDroneBatteryLevel(droneId));
    }
}