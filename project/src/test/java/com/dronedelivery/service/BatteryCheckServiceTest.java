package com.dronedelivery.service;

import com.dronedelivery.dto.BatteryLog;
import com.dronedelivery.model.Drone;
import com.dronedelivery.repository.BatteryLogRepository;
import com.dronedelivery.repository.DroneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BatteryCheckServiceTest {

    @Mock
    private DroneRepository droneRepository;

    @Mock
    private BatteryLogRepository batteryLogRepository;

    @InjectMocks
    private BatteryCheckService batteryCheckService;

    @Test
    void checkBatteryLevels_savesLogsForAllDrones() {
        Drone drone1 = new Drone();
        drone1.setId(1L);
        drone1.setBatteryCapacity(75);
        Drone drone2 = new Drone();
        drone2.setId(2L);
        drone2.setBatteryCapacity(50);
        List<Drone> drones = Arrays.asList(drone1, drone2);
        when(droneRepository.findAll()).thenReturn(drones);

        batteryCheckService.checkBatteryLevels();

        ArgumentCaptor<BatteryLog> captor = ArgumentCaptor.forClass(BatteryLog.class);
        verify(batteryLogRepository, times(2)).save(captor.capture());
        List<BatteryLog> savedLogs = captor.getAllValues();
        assertEquals(2, savedLogs.size());
        BatteryLog log1 = savedLogs.get(0);
        assertEquals(drone1, log1.getDrone());
        assertEquals(75, log1.getBatteryLevel());
        assertEquals(LocalDateTime.now().getDayOfMonth(), log1.getTimestamp().getDayOfMonth());
        BatteryLog log2 = savedLogs.get(1);
        assertEquals(drone2, log2.getDrone());
        assertEquals(50, log2.getBatteryLevel());
        assertEquals(LocalDateTime.now().getDayOfMonth(), log2.getTimestamp().getDayOfMonth());
    }

    @Test
    void checkBatteryLevels_noDronesFound_doesNotSaveAnyLogs() {
        when(droneRepository.findAll()).thenReturn(List.of());

        batteryCheckService.checkBatteryLevels();

        verify(batteryLogRepository, never()).save(any(BatteryLog.class));
    }
}

