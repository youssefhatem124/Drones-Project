package com.dronedelivery.repository;

import com.dronedelivery.model.BatteryLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatteryLogRepository extends JpaRepository<BatteryLog, Long> {
}