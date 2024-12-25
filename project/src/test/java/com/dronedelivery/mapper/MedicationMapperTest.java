package com.dronedelivery.mapper;
import static org.junit.jupiter.api.Assertions.*;

import com.dronedelivery.dto.MedicationDTO;
import com.dronedelivery.model.Medication;
import com.dronedelivery.utils.TestUtil;
import org.junit.jupiter.api.Test;

class MedicationMapperTest {

    private final MedicationMapperImpl medicationMapper = new MedicationMapperImpl();

    @Test
    void toDTO_ShouldMapEntityToDTO() {
        // Arrange
        Medication medication = TestUtil.getMedication();
        // Act
        MedicationDTO medicationDTO = medicationMapper.toDTO(medication);
        // Assert
        assertNotNull(medicationDTO);
        assertEquals(medication.getId(), medicationDTO.getId());
        assertEquals(medication.getName(), medicationDTO.getName());
        assertEquals(medication.getWeight(), medicationDTO.getWeight());
        assertEquals(medication.getCode(), medicationDTO.getCode());
        assertEquals(medication.getImage(), medicationDTO.getImage());
    }

    @Test
    void toDTO_ShouldReturnNull_WhenEntityIsNull() {
        // Act
        MedicationDTO medicationDTO = medicationMapper.toDTO(null);
        // Assert
        assertNull(medicationDTO);
    }

    @Test
    void toEntity_ShouldMapDTOToEntity() {
        // Arrange
        MedicationDTO medicationDTO = TestUtil.getMedicationDto();
        // Act
        Medication medication = medicationMapper.toEntity(medicationDTO);

        // Assert
        assertNotNull(medication);
        assertEquals(medicationDTO.getId(), medication.getId());
        assertEquals(medicationDTO.getName(), medication.getName());
        assertEquals(medicationDTO.getWeight(), medication.getWeight());
        assertEquals(medicationDTO.getCode(), medication.getCode());
        assertEquals(medicationDTO.getImage(), medication.getImage());
    }

    @Test
    void toEntity_ShouldReturnNull_WhenDTOIsNull() {
        // Act
        Medication medication = medicationMapper.toEntity(null);

        // Assert
        assertNull(medication);
    }
}
