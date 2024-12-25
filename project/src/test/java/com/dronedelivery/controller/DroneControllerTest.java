package com.dronedelivery.controller;

import com.dronedelivery.dto.DroneDTO;
import com.dronedelivery.dto.MedicationDTO;
import com.dronedelivery.exception.DroneNotFoundException;
import com.dronedelivery.exception.ValidationException;
import com.dronedelivery.service.DroneService;
import com.dronedelivery.utils.TestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DroneController.class)
class DroneControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DroneService droneService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerDrone_ShouldReturnCreatedDrone() throws Exception {
        DroneDTO drone = TestUtil.getDroneDto();
        when(droneService.registerDrone(any(DroneDTO.class))).thenReturn(drone);

        mockMvc.perform(post("/api/drones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(drone)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(drone.getId()))
                .andExpect(jsonPath("$.batteryCapacity").value(drone.getBatteryCapacity()))
                .andExpect(jsonPath("$.weightLimit").value(drone.getWeightLimit()));
    }

    @Test
    void registerDrone_WithInvalidDroneModel_ReturnsError() throws Exception {
        // Arrange
        String invalidPayload = """
                {
                    "batteryCapacity": 100,
                    "serialNumber":"SERIAL1",
                    "state": "IDLE",
                    "weightLimit": "500",
                    "model": "INVALID_MODEL"
                }
                """;

        // Act
        MvcResult result = mockMvc.perform(post("/api/drones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPayload))
                .andExpect(status().isBadRequest())
                .andReturn();
        // Assert
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains("Invalid value provided for DroneModel"));
    }

    @Test
    void loadDrone_ShouldReturnLoadedDrone() throws Exception {
        MedicationDTO medication = TestUtil.getMedicationDto();
        DroneDTO drone = TestUtil.getDroneDto();
        when(droneService.loadDrone(1L, List.of(medication))).thenReturn(drone);

        mockMvc.perform(post("/api/drones/1/load")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(medication))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.batteryCapacity").value(drone.getBatteryCapacity()))
                .andExpect(jsonPath("$.weightLimit").value(drone.getWeightLimit()));
    }

    @Test
    void getLoadedMedications_ShouldReturnMedicationList() throws Exception {
        MedicationDTO medication = TestUtil.getMedicationDto();
        when(droneService.getLoadedMedications(anyLong())).thenReturn(List.of(medication));

        mockMvc.perform(get("/api/drones/1/medications")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("TEST"))
                .andExpect(jsonPath("$[0].weight").value(100.0));
    }

    @Test
    void getAvailableDrones_ShouldReturnDroneList() throws Exception {
        DroneDTO drone = TestUtil.getDroneDto();
        when(droneService.getAvailableDrones()).thenReturn(List.of(drone));

        mockMvc.perform(get("/api/drones/available")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(drone.getId()))
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void getDroneBatteryLevel_ShouldReturnBatteryLevel() throws Exception {
        when(droneService.getDroneBatteryLevel(anyLong())).thenReturn(100);

        mockMvc.perform(get("/api/drones/1/battery")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("100"));
    }

    @Test
    void getDroneBatteryLevel_ShouldReturnNotFound_WhenDroneNotFound() throws Exception {
        when(droneService.getDroneBatteryLevel(anyLong()))
                .thenThrow(new DroneNotFoundException("Drone not found"));

        mockMvc.perform(get("/api/drones/1/battery")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void loadDrone_ShouldReturnBadRequest_WhenValidationFails() throws Exception {
        MedicationDTO medication = TestUtil.getMedicationDto();
        medication.setCode("code1");
        mockMvc.perform(post("/api/drones/1/load")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(medication))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void loadDrone_ShouldReturnBadRequest_WhenBatteryIsLow() throws Exception {
        MedicationDTO medication = TestUtil.getMedicationDto();
        when(droneService.loadDrone(anyLong(), any(List.class)))
                .thenThrow(new ValidationException("Battery too low"));

        mockMvc.perform(post("/api/drones/1/load")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(medication))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getDroneBatteryLevel_ShouldReturnInternalServerError_WhenUnexpectedErrorOccurs() throws Exception {
        when(droneService.getDroneBatteryLevel(anyLong()))
                .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/api/drones/1/battery")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}
