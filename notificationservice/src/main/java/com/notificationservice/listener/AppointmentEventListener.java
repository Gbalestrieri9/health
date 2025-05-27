package com.notificationservice.listener;

import com.notificationservice.dto.AppointmentEvent;
import com.notificationservice.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AppointmentEventListener {
    private static final Logger logger = LoggerFactory.getLogger(AppointmentEventListener.class);
    private final NotificationService notificationService;

    public AppointmentEventListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = "${notification.queue}")
    public void handleAppointmentCreated(AppointmentEvent event) {
        logger.info("Received appointment event: {}", event);
        try {
            notificationService.sendReminder(event);
        } catch (Exception e) {
            logger.error("Error sending reminder for appointment {}: {}", event.getId(), e.getMessage(), e);
        }
    }
}
