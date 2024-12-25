package com.dronedelivery.service;

import com.dronedelivery.common.enums.DroneState;
import com.dronedelivery.dto.DroneDTO;
import com.dronedelivery.dto.MedicationDTO;
import com.dronedelivery.exception.DroneNotFoundException;
import com.dronedelivery.exception.ValidationException;
import com.dronedelivery.model.Drone;
import com.dronedelivery.model.Medication;
import com.dronedelivery.repository.DroneRepository;
import com.dronedelivery.repository.MedicationRepository;
import com.dronedelivery.utils.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DroneServiceTest {

    @Mock
    private DroneRepository droneRepository;

    @Mock
    private MedicationRepository medicationRepository;
    @InjectMocks
    private DroneService droneService;
    @Test
    void registerDrone_ShouldSaveAndReturnDrone() {
        DroneDTO droneDTO = TestUtil.getDroneDto();
        Drone drone = TestUtil.getDrone();
        when(droneRepository.save(drone)).thenReturn(drone);

        DroneDTO result = droneService.registerDrone(droneDTO);

        assertNotNull(result);
        assertEquals(droneDTO.getModel(), result.getModel());
        assertEquals(droneDTO.getWeightLimit(), result.getWeightLimit());
    }

    @Test
    void loadDrone_ShouldLoadDroneSuccessfully() {
        Drone drone = TestUtil.getDrone();
        MedicationDTO medicationDTO = TestUtil.getMedicationDto();
        when(droneRepository.findById(anyLong())).thenReturn(Optional.of(drone));
        when(droneRepository.save(any(Drone.class))).thenReturn(drone);

        DroneDTO loadedDrone = droneService.loadDrone(1L, List.of(medicationDTO));

        assertEquals(DroneState.LOADED, loadedDrone.getState());
        verify(droneRepository, times(1)).findById(1L);
        verify(droneRepository, times(1)).save(drone);
    }

    @Test
    void loadDrone_ShouldThrowDroneNotFoundException_WhenDroneDoesNotExist() {
        MedicationDTO medicationDTO = TestUtil.getMedicationDto();
        when(droneRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(DroneNotFoundException.class, () ->
                droneService.loadDrone(1L, List.of(medicationDTO)));

        assertEquals("Drone not found", exception.getMessage());
        verify(droneRepository, times(1)).findById(1L);
    }

    @Test
    void loadDrone_ShouldThrowValidationException_WhenBatteryIsLow() {
        Drone drone = TestUtil.getDrone();
        MedicationDTO medicationDTO =TestUtil.getMedicationDto();
        drone.setBatteryCapacity(20);
        when(droneRepository.findById(anyLong())).thenReturn(Optional.of(drone));

        Exception exception = assertThrows(ValidationException.class, () ->
                droneService.loadDrone(1L, List.of(medicationDTO)));

        assertEquals("Cannot load drone: battery level below 25%", exception.getMessage());
    }

    @Test
    void getLoadedMedications_ShouldReturnMedications() {
        Medication medication = TestUtil.getMedication();
        when(medicationRepository.findByDroneId(anyLong())).thenReturn(List.of(medication));

        List<MedicationDTO> medications = droneService.getLoadedMedications(1L);

        assertEquals(1, medications.size());
        verify(medicationRepository, times(1)).findByDroneId(1L);
    }

    @Test
    void getAvailableDrones_ShouldReturnAvailableDrones() {
        Drone drone = TestUtil.getDrone();
        when(droneRepository.findByBatteryCapacityGreaterThanAndState(anyInt(), any(DroneState.class)))
                .thenReturn(List.of(drone));

        List<DroneDTO> availableDrones = droneService.getAvailableDrones();

        assertEquals(1, availableDrones.size());
        verify(droneRepository, times(1))
                .findByBatteryCapacityGreaterThanAndState(25, DroneState.IDLE);}

    @Test
    void getDroneBatteryLevel_ShouldReturnBatteryLevel() {
        Drone drone  = TestUtil.getDrone();
        when(droneRepository.findById(1L)).thenReturn(Optional.of(drone));

        Integer batteryLevel = droneService.getDroneBatteryLevel(1L);

        assertEquals(100, batteryLevel);
        verify(droneRepository,times(1)).findById(1L);
    }

    @Test
    void getDroneBatteryLevel_ShouldThrowDroneNotFoundException() {
        when(droneRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(DroneNotFoundException.class, () ->
                droneService.getDroneBatteryLevel(1L));

        assertEquals("Drone not found", exception.getMessage());
        verify(droneRepository, times(1)).findById(1L);
    }
}
