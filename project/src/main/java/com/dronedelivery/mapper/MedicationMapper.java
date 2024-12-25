package com.dronedelivery.mapper;

import com.dronedelivery.dto.MedicationDTO;
import com.dronedelivery.model.Medication;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MedicationMapper {

    MedicationMapper INSTANCE = Mappers.getMapper(MedicationMapper.class);

    MedicationDTO toDTO(Medication medication);

    Medication toEntity(MedicationDTO dto);
}