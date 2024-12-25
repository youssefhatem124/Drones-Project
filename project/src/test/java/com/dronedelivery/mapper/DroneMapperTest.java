package com.dronedelivery.mapper;

import com.dronedelivery.dto.DroneDTO;
import com.dronedelivery.model.Drone;
import com.dronedelivery.utils.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DroneMapperTest {

    private final DroneMapperImpl droneMapper = new DroneMapperImpl();

    @Test
    void entityToDTO_ShouldMapEntityToDTO() {
        Drone drone = TestUtil.getDrone();
        // Act
        DroneDTO droneDTO = droneMapper.entityToDTO(drone);
        // Assert
        assertNotNull(droneDTO);
        assertEquals(drone.getId(), droneDTO.getId());
        assertEquals(drone.getSerialNumber(), droneDTO.getSerialNumber());
        assertEquals(drone.getModel(), droneDTO.getModel());
        assertEquals(drone.getWeightLimit(), droneDTO.getWeightLimit());
        assertEquals(drone.getBatteryCapacity(), droneDTO.getBatteryCapacity());
        assertEquals(drone.getState(), droneDTO.getState());
    }

    @Test
    void entityToDTO_ShouldReturnNull_WhenEntityIsNull() {
        // Act
        DroneDTO droneDTO = droneMapper.entityToDTO(null);
        // Assert
        assertNull(droneDTO);
    }

    @Test
    void toEntity_ShouldMapDTOToEntity() {
        DroneDTO droneDTO = TestUtil.getDroneDto();
        // Act
        Drone drone = droneMapper.toEntity(droneDTO);
        // Assert
        assertNotNull(drone);
        assertEquals(droneDTO.getId(), drone.getId());
        assertEquals(droneDTO.getSerialNumber(), drone.getSerialNumber());
        assertEquals(droneDTO.getModel(), drone.getModel());
        assertEquals(droneDTO.getWeightLimit(), drone.getWeightLimit());
        assertEquals(droneDTO.getBatteryCapacity(), drone.getBatteryCapacity());
        assertEquals(droneDTO.getState(), drone.getState());
    }

    @Test
    void toEntity_ShouldReturnNull_WhenDTOIsNull() {
        // Act
        Drone drone = droneMapper.toEntity(null);
        // Assert
        assertNull(drone);
    }
}
