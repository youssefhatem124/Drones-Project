package com.dronedelivery.config;

import com.dronedelivery.dto.DroneDTO;
import com.dronedelivery.common.enums.DroneModel;
import com.dronedelivery.common.enums.DroneState;
import com.dronedelivery.model.Drone;
import com.dronedelivery.repository.DroneRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {
    @Bean
    CommandLineRunner initDatabase(DroneRepository repository) {
        return args -> {
            for (int i = 1; i <= 10; i++) {
                Drone drone = new Drone();
                drone.setSerialNumber("DRONE" + String.format("%03d", i));
                drone.setModel(DroneModel.values()[i % 4]);
                drone.setWeightLimit(500.0);
                drone.setBatteryCapacity(100);
                drone.setState(DroneState.IDLE);
                repository.save(drone);
            }
        };
    }
}