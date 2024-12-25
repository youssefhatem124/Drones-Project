package com.dronedelivery.repository;

import com.dronedelivery.dto.MedicationDTO;
import com.dronedelivery.model.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MedicationRepository extends JpaRepository<Medication, Long> {
    List<Medication> findByDroneId(Long droneId);
}