package com.dronedelivery.service;

import com.dronedelivery.dto.BatteryLog;
import com.dronedelivery.repository.BatteryLogRepository;
import com.dronedelivery.repository.DroneRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BatteryCheckService {
    private final DroneRepository droneRepository;
    private final BatteryLogRepository batteryLogRepository;

    public BatteryCheckService(DroneRepository droneRepository, BatteryLogRepository batteryLogRepository) {
        this.droneRepository = droneRepository;
        this.batteryLogRepository = batteryLogRepository;
    }

    @Scheduled(fixedRateString = "${drone.battery.check.rate}")
    public void checkBatteryLevels() {
        droneRepository.findAll().forEach(drone -> {
            BatteryLog log = new BatteryLog();
            log.setDrone(drone);
            log.setBatteryLevel(drone.getBatteryCapacity());
            log.setTimestamp(LocalDateTime.now());
            batteryLogRepository.save(log);
        });
    }
}