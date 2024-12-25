package com.dronedelivery.mapper;

import com.dronedelivery.dto.DroneDTO;
import com.dronedelivery.model.Drone;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DroneMapper {

    DroneMapper INSTANCE = Mappers.getMapper(DroneMapper.class);

    DroneDTO entityToDTO(Drone drone);

    Drone toEntity(DroneDTO dto);
}
