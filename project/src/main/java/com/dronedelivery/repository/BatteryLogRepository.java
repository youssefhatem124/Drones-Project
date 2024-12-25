package com.dronedelivery.repository;

import com.dronedelivery.dto.BatteryLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatteryLogRepository extends JpaRepository<BatteryLog, Long> {
}