package com.appointmentservice.service.impl;

import com.appointmentservice.domain.Appointment;
import com.appointmentservice.dto.AppointmentEvent;
import com.appointmentservice.exception.ResourceNotFoundException;
import com.appointmentservice.repository.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository repository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    private AppointmentServiceImpl service;

    private final String exchange = "consultas.exchange";
    private final String routingKey = "consulta.criada";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new AppointmentServiceImpl(repository, rabbitTemplate, exchange, routingKey);
    }

    @Test
    void createShouldSaveAndPublishEvent() {
        // Arrange
        Appointment input = new Appointment();
        input.setPatientId(1L);
        input.setDoctorId(2L);
        input.setDateTime(LocalDateTime.of(2025, 6, 1, 10, 0));
        input.setStatus("SCHEDULED");

        Appointment saved = new Appointment();
        saved.setId(100L);
        saved.setPatientId(1L);
        saved.setDoctorId(2L);
        saved.setDateTime(input.getDateTime());
        saved.setStatus("SCHEDULED");

        when(repository.save(input)).thenReturn(saved);

        // Act
        Appointment result = service.create(input);

        // Assert
        assertEquals(100L, result.getId());
        ArgumentCaptor<AppointmentEvent> captor = ArgumentCaptor.forClass(AppointmentEvent.class);
        verify(rabbitTemplate, times(1))
                .convertAndSend(eq(exchange), eq(routingKey), captor.capture());

        AppointmentEvent event = captor.getValue();
        assertEquals(100L, event.getId());
        assertEquals("SCHEDULED", event.getStatus());
    }

    @Test
    void getByIdWhenFound() {
        Appointment appt = new Appointment();
        appt.setId(50L);
        when(repository.findById(50L)).thenReturn(Optional.of(appt));

        Appointment result = service.getById(50L);
        assertNotNull(result);
        assertEquals(50L, result.getId());
    }

    @Test
    void getByIdWhenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));
    }

    @Test
    void getAllShouldReturnList() {
        Appointment a1 = new Appointment(); a1.setId(1L);
        Appointment a2 = new Appointment(); a2.setId(2L);
        when(repository.findAll()).thenReturn(Arrays.asList(a1, a2));

        List<Appointment> list = service.getAll();
        assertEquals(2, list.size());
    }

    @Test
    void updateShouldModifyAndPublishEvent() {
        // Arrange existing
        Appointment existing = new Appointment();
        existing.setId(10L);
        existing.setPatientId(5L);
        existing.setDoctorId(7L);
        existing.setDateTime(LocalDateTime.of(2025, 6, 1, 11, 0));
        existing.setStatus("SCHEDULED");

        Appointment updateInfo = new Appointment();
        updateInfo.setPatientId(5L);
        updateInfo.setDoctorId(8L);
        updateInfo.setDateTime(LocalDateTime.of(2025, 6, 1, 12, 30));
        updateInfo.setStatus("RESCHEDULED");

        when(repository.findById(10L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        // Act
        Appointment updated = service.update(10L, updateInfo);

        // Assert
        assertEquals(8L, updated.getDoctorId());
        assertEquals("RESCHEDULED", updated.getStatus());

        ArgumentCaptor<AppointmentEvent> captor = ArgumentCaptor.forClass(AppointmentEvent.class);
        verify(rabbitTemplate, times(1))
                .convertAndSend(eq(exchange), eq(routingKey), captor.capture());

        AppointmentEvent event = captor.getValue();
        assertEquals(10L, event.getId());
        assertEquals("RESCHEDULED", event.getStatus());
    }

    @Test
    void deleteShouldCallRepository() {
        doNothing().when(repository).deleteById(123L);
        service.delete(123L);
        verify(repository, times(1)).deleteById(123L);
    }

    @Test
    void getByPatientShouldReturnFilteredList() {
        Appointment appt = new Appointment(); appt.setPatientId(20L);
        when(repository.findByPatientId(20L)).thenReturn(Collections.singletonList(appt));

        List<Appointment> list = service.getByPatient(20L);
        assertEquals(1, list.size());
        assertEquals(20L, list.get(0).getPatientId());
    }

    @Test
    void getByDoctorShouldReturnFilteredList() {
        Appointment appt = new Appointment(); appt.setDoctorId(30L);
        when(repository.findByDoctorId(30L)).thenReturn(Collections.singletonList(appt));

        List<Appointment> list = service.getByDoctor(30L);
        assertEquals(1, list.size());
        assertEquals(30L, list.get(0).getDoctorId());
    }
}