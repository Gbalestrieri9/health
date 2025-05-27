package com.appointmentservice.controller;

import com.appointmentservice.domain.Appointment;
import com.appointmentservice.service.AppointmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentController.class)
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentService service;

    @Autowired
    private ObjectMapper objectMapper;

    private Appointment sample;

    @BeforeEach
    void setUp() {
        sample = new Appointment();
        sample.setId(1L);
        sample.setPatientId(1L);
        sample.setDoctorId(2L);
        sample.setDateTime(LocalDateTime.of(2025, 6, 1, 10, 0));
        sample.setStatus("SCHEDULED");
    }

    @Test
    void createShouldReturnCreatedAppointment() throws Exception {
        when(service.create(any(Appointment.class))).thenReturn(sample);

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sample)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(service).create(any(Appointment.class));
    }

    @Test
    void getByIdShouldReturnAppointment() throws Exception {
        when(service.getById(1L)).thenReturn(sample);

        mockMvc.perform(get("/api/appointments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(service).getById(1L);
    }

    @Test
    void getAllShouldReturnList() throws Exception {
        when(service.getAll()).thenReturn(Arrays.asList(sample));

        mockMvc.perform(get("/api/appointments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));

        verify(service).getAll();
    }

    @Test
    void updateShouldReturnUpdated() throws Exception {
        Appointment updated = new Appointment();
        updated.setId(1L);
        updated.setPatientId(1L);
        updated.setDoctorId(3L);
        updated.setDateTime(LocalDateTime.of(2025, 6, 1, 11, 0));
        updated.setStatus("RESCHEDULED");

        when(service.update(eq(1L), any(Appointment.class))).thenReturn(updated);

        mockMvc.perform(put("/api/appointments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doctorId").value(3));

        verify(service).update(eq(1L), any(Appointment.class));
    }

    @Test
    void deleteShouldReturnNoContent() throws Exception {
        doNothing().when(service).delete(1L);

        mockMvc.perform(delete("/api/appointments/1"))
                .andExpect(status().isNoContent());

        verify(service).delete(1L);
    }

    @Test
    void byPatientShouldReturnList() throws Exception {
        when(service.getByPatient(1L)).thenReturn(Collections.singletonList(sample));

        mockMvc.perform(get("/api/appointments/patient/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].patientId").value(1L));

        verify(service).getByPatient(1L);
    }

    @Test
    void byDoctorShouldReturnList() throws Exception {
        when(service.getByDoctor(2L)).thenReturn(Collections.singletonList(sample));

        mockMvc.perform(get("/api/appointments/doctor/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].doctorId").value(2L));

        verify(service).getByDoctor(2L);
    }
}