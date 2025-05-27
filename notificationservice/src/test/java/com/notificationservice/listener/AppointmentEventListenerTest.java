package com.notificationservice.listener;

import com.notificationservice.dto.AppointmentEvent;
import com.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class AppointmentEventListenerTest {

    @Mock
    private NotificationService notificationService;

    private AppointmentEventListener listener;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        listener = new AppointmentEventListener(notificationService);
    }

    @Test
    void handleAppointmentCreatedShouldInvokeService() {
        AppointmentEvent event = new AppointmentEvent();
        event.setId(5L);
        event.setPatientId(10L);
        event.setDoctorId(20L);
        listener.handleAppointmentCreated(event);

        verify(notificationService, times(1)).sendReminder(event);
    }
}
