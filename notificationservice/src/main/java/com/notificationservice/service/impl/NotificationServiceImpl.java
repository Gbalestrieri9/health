package com.notificationservice.service.impl;

import com.notificationservice.dto.AppointmentEvent;
import com.notificationservice.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);
    private final JavaMailSender mailSender;

    public NotificationServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendReminder(AppointmentEvent event) {
        logger.info("Preparing to send reminder for appointment: {}", event.getId());

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("patient+" + event.getPatientId() + "@example.com");
        msg.setSubject("Appointment Reminder");
        msg.setText("Dear Patient,\nYour appointment (#" + event.getId() + ") is scheduled for "
                + event.getDateTime() + ". Status: " + event.getStatus() + ".");

        mailSender.send(msg);
        logger.info("Reminder sent for appointment id: {}", event.getId());
    }
}

