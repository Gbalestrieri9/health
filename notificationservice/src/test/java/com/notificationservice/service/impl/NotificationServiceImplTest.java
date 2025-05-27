package com.notificationservice.service.impl;

import com.notificationservice.dto.AppointmentEvent;
import com.notificationservice.service.impl.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    private NotificationServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new NotificationServiceImpl(mailSender);
    }

    @Test
    void sendReminderShouldSendEmail() {
        AppointmentEvent event = new AppointmentEvent();
        event.setId(1L);
        event.setPatientId(2L);
        event.setDoctorId(3L);
        event.setDateTime(LocalDateTime.of(2025,6,1,10,0));
        event.setStatus("SCHEDULED");

        service.sendReminder(event);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(captor.capture());

        SimpleMailMessage msg = captor.getValue();
        assertNotNull(msg);
        assertTrue(msg.getTo()[0].contains("patient+2@"));
        assertTrue(msg.getText().contains("#1"));
        assertTrue(msg.getText().contains("SCHEDULED"));
    }
}
