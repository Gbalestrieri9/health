package com.notificationservice.service;

import com.notificationservice.dto.AppointmentEvent;

public interface NotificationService {
    void sendReminder(AppointmentEvent event);
}
