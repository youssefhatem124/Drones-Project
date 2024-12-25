package com.dronedelivery.service;

import com.dronedelivery.exception.DroneNotFoundException;
import com.dronedelivery.exception.ValidationException;
import com.dronedelivery.dto.DroneDTO;
import com.dronedelivery.dto.MedicationDTO;
import com.dronedelivery.mapper.DroneMapper;
import com.dronedelivery.mapper.MedicationMapper;
import com.dronedelivery.model.Drone;
import com.dronedelivery.model.Medication;
import com.dronedelivery.repository.DroneRepository;
import com.dronedelivery.repository.MedicationRepository;
import com.dronedelivery.common.enums.DroneState;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DroneService {
    private final DroneRepository droneRepository;
    private final MedicationRepository medicationRepository;

    public DroneService(DroneRepository droneRepository, MedicationRepository medicationRepository) {
        this.droneRepository = droneRepository;
        this.medicationRepository = medicationRepository;
    }

    public DroneDTO registerDrone(DroneDTO inputDrone) {
        // Convert DTO to entity, save it, and convert the saved entity back to DTO
        Drone drone = DroneMapper.INSTANCE.toEntity(inputDrone);
        Drone savedDrone = droneRepository.save(drone);
        return DroneMapper.INSTANCE.entityToDTO(savedDrone);
    }

    @Transactional
    public DroneDTO loadDrone(Long droneId, List<MedicationDTO> newMedications) {
        // Retrieve the drone entity or throw an exception if not found
        Drone drone = droneRepository.findById(droneId)
                .orElseThrow(() -> new DroneNotFoundException("Drone not found"));
        // Check if the drone's battery capacity is above the threshold
        if (drone.getBatteryCapacity() < 25) {
            throw new ValidationException("Cannot load drone: battery level below 25%");
        }
        if (drone.getState() != DroneState.IDLE) {
            throw new ValidationException("Drone is not available for loading");
        }

        // Retrieve existing medications already loaded on the drone

        // Calculate the total weight of existing and new medications
        double newWeight = newMedications.stream()
                .mapToDouble(MedicationDTO::getWeight)
                .sum();

        if ( newWeight > drone.getWeightLimit()) {
            throw new ValidationException("Total weight exceeds drone capacity");
        }

        // Update drone state to LOADING
        drone.setState(DroneState.LOADING);

        // Convert new medications from DTO to entity, set their drone, and save them
        List<Medication> medicationsToSave = newMedications.stream()
                .map(MedicationMapper.INSTANCE::toEntity)
                .peek(medication -> medication.setDrone(drone))
                .collect(Collectors.toList());
        medicationRepository.saveAll(medicationsToSave);

        // Update drone state to LOADED and save it
        drone.setState(DroneState.LOADED);
        Drone updatedDrone = droneRepository.save(drone);
        return DroneMapper.INSTANCE.entityToDTO(updatedDrone);
    }

    public List<MedicationDTO> getLoadedMedications(Long droneId) {
        // Retrieve medications by drone ID, convert them to DTOs, and return
        return medicationRepository.findByDroneId(droneId).stream()
                .map(MedicationMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    public List<DroneDTO> getAvailableDrones() {
        // Retrieve drones that meet the criteria, convert them to DTOs, and return
        return droneRepository.findByBatteryCapacityGreaterThanAndState(25, DroneState.IDLE).stream()
                .map(DroneMapper.INSTANCE::entityToDTO)
                .collect(Collectors.toList());
    }

    public Integer getDroneBatteryLevel(Long droneId) {
        // Retrieve the drone by ID and return its battery capacity
        return droneRepository.findById(droneId)
                .map(Drone::getBatteryCapacity)
                .orElseThrow(() -> new DroneNotFoundException("Drone not found"));
    }
}
