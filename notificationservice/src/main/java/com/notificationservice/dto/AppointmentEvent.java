package com.notificationservice.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AppointmentEvent {
    private Long id;
    private Long patientId;
    private Long doctorId;
    private LocalDateTime dateTime;
    private String status;
}
