package com.dronedelivery.utils;

import com.dronedelivery.common.enums.DroneModel;
import com.dronedelivery.common.enums.DroneState;
import com.dronedelivery.dto.DroneDTO;
import com.dronedelivery.dto.MedicationDTO;
import com.dronedelivery.model.Drone;
import com.dronedelivery.model.Medication;

public class TestUtil {
    public static Drone getDrone() {
        Drone drone = new Drone();
        drone.setSerialNumber("DRONE001");
        drone.setModel(DroneModel.LIGHTWEIGHT);
        drone.setBatteryCapacity(100);
        drone.setWeightLimit(500.0);
        drone.setState(DroneState.IDLE);
        return drone;
    }

    public static DroneDTO getDroneDto() {

        DroneDTO droneDTO = new DroneDTO();
        droneDTO.setSerialNumber("DRONE001");
        droneDTO.setModel(DroneModel.LIGHTWEIGHT);
        droneDTO.setBatteryCapacity(100);
        droneDTO.setWeightLimit(500.0);
        droneDTO.setState(DroneState.IDLE);
        return droneDTO;
    }

    public static Medication getMedication() {
        Medication medication = new Medication();
        medication.setId(1L);
        medication.setName("Medication1");
        medication.setWeight(100.0);
        medication.setCode("MED001");
        return medication;
    }

    public static MedicationDTO getMedicationDto() {
        MedicationDTO medication = new MedicationDTO();
        medication.setName("TEST");
        medication.setCode("CODE1");
        medication.setImage("Image");
        medication.setWeight(100.0);
        return medication;
    }
}
