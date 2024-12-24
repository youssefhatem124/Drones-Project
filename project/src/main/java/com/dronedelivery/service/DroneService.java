package com.dronedelivery.service;

import com.dronedelivery.model.Drone;
import com.dronedelivery.model.DroneState;
import com.dronedelivery.model.Medication;
import com.dronedelivery.repository.DroneRepository;
import com.dronedelivery.repository.MedicationRepository;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DroneService {
    private final DroneRepository droneRepository;
    private final MedicationRepository medicationRepository;

    public DroneService(DroneRepository droneRepository, MedicationRepository medicationRepository) {
        this.droneRepository = droneRepository;
        this.medicationRepository = medicationRepository;
    }

    public Drone registerDrone(Drone drone) {
        return droneRepository.save(drone);
    }

    @Transactional
    public Drone loadDrone(Long droneId, List<Medication> newMedications) {
        // Retrieve the drone by ID or throw an exception if not found
        Drone drone = droneRepository.findById(droneId)
                .orElseThrow(() -> new RuntimeException("Drone not found"));

        // Check if the drone's battery capacity is above the threshold
        if (drone.getBatteryCapacity() < 25) {
            throw new ValidationException("Cannot load drone: battery level below 25%");
        }

        // Retrieve the medications already loaded on the drone
        List<Medication> existingMedications = medicationRepository.findByDroneId(droneId);

        // Calculate the total weight of already loaded medications
        double existingWeight = existingMedications.stream()
                .mapToDouble(Medication::getWeight)
                .sum();

        // Calculate the total weight of new medications
        double newWeight = newMedications.stream()
                .mapToDouble(Medication::getWeight)
                .sum();

        // Ensure the total weight (existing + new) does not exceed the drone's weight limit
        if (existingWeight + newWeight > drone.getWeightLimit()) {
            throw new ValidationException("Total weight exceeds drone capacity");
        }

        // Set the drone's state to LOADING
        drone.setState(DroneState.LOADING);

        // Associate new medications with the drone and save them
        newMedications.forEach(med -> med.setDrone(drone));
        medicationRepository.saveAll(newMedications);

        // Update the drone's state to LOADED and save the drone
        drone.setState(DroneState.LOADED);
        return droneRepository.save(drone);
    }

    public List<Medication> getLoadedMedications(Long droneId) {
        return medicationRepository.findByDroneId(droneId);
    }

    public List<Drone> getAvailableDrones() {
        return droneRepository.findByBatteryCapacityGreaterThanAndState(25, DroneState.IDLE);
    }

    public Integer getDroneBatteryLevel(Long droneId) {
        return droneRepository.findById(droneId)
                .map(Drone::getBatteryCapacity)
                .orElseThrow(() -> new RuntimeException("Drone not found"));
    }
}